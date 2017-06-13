package com.gurunars.item_list

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gurunars.databinding.BindableField
import com.gurunars.shortcuts.l

internal class ItemAdapter<ItemType : Item>(
        private val items: BindableField<List<ItemType>>,
        private val emptyViewBinder: BindableField<EmptyViewBinder>,
        private val itemViewBinders: BindableField<Map<Enum<*>, ItemViewBinder<*, ItemType>>>,
        private val defaultViewBinder: BindableField<ItemViewBinder<*, ItemType>>
) : RecyclerView.Adapter<BindableViewHolder<out View, ItemType>>() {
    private var previousList: List<ItemType> = ArrayList()

    private class ItemCallback<out ItemType: Item>(
            val previousList: List<ItemType>,
            val currentList: List<ItemType>): DiffUtil.Callback() {

        override fun getOldListSize() = previousList.size

        override fun getNewListSize() = currentList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            previousList[oldItemPosition].payloadsEqual(currentList.get(newItemPosition))

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            previousList[oldItemPosition].getId() == currentList.get(newItemPosition).getId()

    }

    init {
        items.onChange {
            DiffUtil.calculateDiff(ItemCallback(previousList, it)).dispatchUpdatesTo(this)
            previousList = it
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == ItemViewBinderEmpty.EMPTY_TYPE)
            BindableViewHolder(parent, emptyViewBinder.get())
        else
            BindableViewHolder(parent,
                itemViewBinders.get().entries.find { it.key.ordinal == viewType }?.value
                ?: defaultViewBinder.get()
            )

    override fun onBindViewHolder(holder: BindableViewHolder<out View, ItemType>, position: Int) {
        if (position == items.get().size) {
            return   // nothing to bind
        }

        val item = items.get()[position]
        val previousIndex = previousList.indexOf(item)
        val previousItem = if (previousIndex >= 0) previousList[previousIndex] else null
        holder.bind(item, previousItem)
    }

    override fun getItemViewType(position: Int) =
        if (items.get().isEmpty())
            ItemViewBinderEmpty.EMPTY_TYPE
        else
            items.get()[position].getType().ordinal

    override fun getItemCount() = Math.max(1, items.get().size)

}
