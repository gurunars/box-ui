package com.gurunars.item_list

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.BindableField
import com.gurunars.shortcuts.asRow
import com.gurunars.shortcuts.fullSize
import org.objenesis.strategy.StdInstantiatorStrategy

/**
 * @param itemType type of the item for which the view is supposed to be created
 * @param field field representing item's payload
 * @return a view bound to a field holding the item
 */
typealias ItemViewBinder<ItemType> = Context.(
    itemType: Enum<*>,
    field: BindableField<ItemType>
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
            val initialPayload = items.get().first { it.type.ordinal == viewType }
            val field = BindableField(initialPayload)
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

        val field = holder.itemView.getTag(R.id.payloadTag) as BindableField<ItemType>
        field.set(items.get()[position], true)
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
