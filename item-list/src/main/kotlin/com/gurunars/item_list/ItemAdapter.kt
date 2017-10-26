package com.gurunars.item_list

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.BindableField
import com.gurunars.shortcuts.asRow
import com.gurunars.shortcuts.fullSize
import org.objenesis.strategy.StdInstantiatorStrategy

internal class ItemAdapter<ItemType : Item>(
    private val items: BindableField<List<ItemType>>,
    private val emptyViewBinder: EmptyViewBinder,
    private val itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var previousList: List<ItemType> = ArrayList()

    private class ItemCallback<out ItemType : Item>(
        val previousList: List<ItemType>,
        val currentList: List<ItemType>) : DiffUtil.Callback() {

        override fun getOldListSize() = Math.max(1, previousList.size)

        override fun getNewListSize() = Math.max(1, currentList.size)

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            previousList.getOrNull(oldItemPosition) == currentList.getOrNull(newItemPosition)

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            previousList.getOrNull(oldItemPosition)?.id == currentList.getOrNull(newItemPosition)?.id

    }

    private val kryo = Kryo().apply {
        instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    init {
        items.onChange({
            previousList = kryo.copy(ArrayList(it))
        }) {
            if (previousList.isEmpty() || it.isEmpty()) {
                notifyDataSetChanged()
            } else {
                DiffUtil.calculateDiff(ItemCallback(previousList, it)).dispatchUpdatesTo(this)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == EMPTY_TYPE) {
            return object : RecyclerView.ViewHolder(emptyViewBinder.bind().apply {
                fullSize()
                addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                    override fun onViewAttachedToWindow(v: View) {

                    }

                    override fun onViewDetachedFromWindow(v: View) {
                        clearAnimation()
                        clearFocus()
                    }
                })
            }) {}
        } else {
            // If enums are from different classes - they have same ordinals
            val initialPayload = items.get().first { getItemTypeInt(it) == viewType }
            val field = BindableField(initialPayload)
            val binder = itemViewBinders[initialPayload.type] ?: DefaultItemViewBinder(parent.context)
            return object : RecyclerView.ViewHolder(
                binder.bind(field).apply {
                    asRow()
                    setTag(R.id.payloadTag, field)
                }) {}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == items.get().size) {
            return   // nothing to bind
        }
        @Suppress("UNCHECKED_CAST")
        val field = holder.itemView.getTag(R.id.payloadTag) as BindableField<ItemType>
        field.set(items.get()[position], true)
    }

    override fun getItemId(position: Int) =
        if (hasStableIds())
            RecyclerView.NO_ID
        else
            items.get()[position].id

    private fun getItemTypeInt(item: ItemType) = itemViewBinders.keys.indexOf(item.type)

    override fun getItemViewType(position: Int) =
        if (items.get().isEmpty())
            EMPTY_TYPE
        else
            getItemTypeInt(items.get()[position])

    override fun getItemCount() = Math.max(1, items.get().size)

    companion object {
        val EMPTY_TYPE = -404
    }
}
