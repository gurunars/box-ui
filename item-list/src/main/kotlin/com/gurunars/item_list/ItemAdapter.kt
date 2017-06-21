package com.gurunars.item_list

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.asRow
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.dip
import org.jetbrains.anko.padding
import org.objenesis.strategy.StdInstantiatorStrategy

typealias EmptyViewBinder = (context: Context) -> View
typealias ItemViewBinder<ItemType> =
(context: Context, payload: BindableField<Pair<ItemType, ItemType?>>) -> View


private fun defaultItemViewBinder(context: Context, payload: BindableField<Pair<Any, Any?>>) : View {
    return TextView(context).apply {
        padding = context.dip(5)
        payload.onChange {
            text = it.first.toString()
        }
    }
}


internal class ItemAdapter<ItemType : Item>(
        private val items: BindableField<List<ItemType>>,
        private val emptyViewBinder: BindableField<EmptyViewBinder>,
        private val itemViewBinders: BindableField<Map<Enum<*>, Pair<ItemViewBinder<ItemType>, ItemType>>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var previousList: List<ItemType> = ArrayList()

    private class ItemCallback<out ItemType: Item>(
            val previousList: List<ItemType>,
            val currentList: List<ItemType>): DiffUtil.Callback() {

        override fun getOldListSize() = Math.max(1, previousList.size)

        override fun getNewListSize() = Math.max(1, currentList.size)

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            currentList.isNotEmpty() && previousList.isNotEmpty() &&
            previousList[oldItemPosition].payloadsEqual(currentList.get(newItemPosition))

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            currentList.isNotEmpty() && previousList.isNotEmpty() &&
            previousList[oldItemPosition].getId() == currentList.get(newItemPosition).getId()

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
            return object : RecyclerView.ViewHolder(emptyViewBinder.get()(parent.context).apply {
                fullSize()
            }) {}
        } else {
            val pair = itemViewBinders.get().entries.find { it.key.ordinal == viewType }?.value

            fun<SubType> getHolder(
                    viewBinder: ItemViewBinder<SubType>,
                    field: BindableField<Pair<SubType, SubType?>>): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(viewBinder(parent.context, field).apply {
                    asRow()
                    setTag(R.id.payloadTag, field)
                }) {}
            }

            if (pair == null) {
                return getHolder(
                    ::defaultItemViewBinder,
                    parent.bindableField(Pair<Any, Any?>("Nothing", null))
                )
            } else {
                return getHolder(
                    pair.first,
                    parent.bindableField(Pair<ItemType, ItemType?>(pair.second, null))
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == items.get().size) {
            return   // nothing to bind
        }

        val item = items.get()[position]
        val previousIndex = previousList.indexOfFirst { it.getId() == item.getId() }
        val previousItem = if (previousIndex >= 0) previousList[previousIndex] else null
        val field = holder.itemView.getTag(R.id.payloadTag) as BindableField<Pair<ItemType?, ItemType?>>

        // we know for sure because of DiffUtil that the pair was updated
        field.set(Pair(item, previousItem), true)
    }

    override fun getItemViewType(position: Int) =
        if (items.get().isEmpty())
            EMPTY_TYPE
        else
            items.get()[position].getType().ordinal

    override fun getItemCount() = Math.max(1, items.get().size)

    companion object {
        val EMPTY_TYPE = -404
    }
}
