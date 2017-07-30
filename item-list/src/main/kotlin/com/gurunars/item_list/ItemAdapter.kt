package com.gurunars.item_list

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.bindableField
import com.gurunars.shortcuts.asRow
import com.gurunars.shortcuts.fullSize
import org.objenesis.strategy.StdInstantiatorStrategy


/**
 * @param context Android context
 * @param itemType type of the item for which the view is supposed to be created
 * @param field field representing a field
 * @return a view bound to a field holding the item
 */
typealias ItemViewBinder<ItemType> = (
    context: Context,
    itemType: Enum<*>,
    field: BindableField<Pair<ItemType, ItemType?>>
) -> View

internal class ItemAdapter<ItemType : Item>(
        private val items: BindableField<List<ItemType>>,
        private val emptyViewBinder: EmptyViewBinder,
        private val itemViewBinder: ItemViewBinder<ItemType>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var previousList: List<ItemType> = ArrayList()

    private class ItemCallback<out ItemType: Item>(
            val previousList: List<ItemType>,
            val currentList: List<ItemType>): DiffUtil.Callback() {

        override fun getOldListSize() = Math.max(1, previousList.size)

        override fun getNewListSize() = Math.max(1, currentList.size)

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            currentList.isNotEmpty() && previousList.isNotEmpty() &&
            previousList[oldItemPosition] == currentList[newItemPosition]

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            currentList.isNotEmpty() && previousList.isNotEmpty() &&
            previousList[oldItemPosition].id == currentList[newItemPosition].id

    }

    private val kryo = Kryo().apply {
        instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }


    init {
        items.onChange({
            previousList = kryo.copy(ArrayList(it))
        }) {
            DiffUtil.calculateDiff(ItemCallback(previousList, it)).dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == EMPTY_TYPE) {
            return object : RecyclerView.ViewHolder(emptyViewBinder(parent.context).apply {
                fullSize()
            }) {}
        } else {
            val initialPayload = items.get().first { it.type.ordinal == viewType }
            val field = parent.bindableField(Pair<ItemType, ItemType?>(initialPayload, null))
            return object : RecyclerView.ViewHolder(
                itemViewBinder(parent.context, initialPayload.type, field).apply {
                    asRow()
                    setTag(R.id.payloadTag, field)
            }) {}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == items.get().size) {
            return   // nothing to bind
        }

        val item = items.get()[position]
        val previousIndex = previousList.indexOfFirst { it.id == item.id }
        val previousItem = if (previousIndex >= 0) previousList[previousIndex] else null
        val field = holder.itemView.getTag(R.id.payloadTag) as BindableField<Pair<ItemType?, ItemType?>>

        // we know for sure because of DiffUtil that the pair was updated
        field.set(Pair(item, previousItem), true)
    }

    override fun getItemViewType(position: Int) =
        if (items.get().isEmpty())
            EMPTY_TYPE
        else
            items.get()[position].type.ordinal

    override fun getItemCount() = Math.max(1, items.get().size)

    companion object {
        val EMPTY_TYPE = -404
    }
}
