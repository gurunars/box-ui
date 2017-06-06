package com.gurunars.item_list

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.esotericsoftware.kryo.Kryo
import org.objenesis.strategy.StdInstantiatorStrategy

import android.support.v7.util.DiffUtil

internal class ItemAdapter<ItemType : Item> : RecyclerView.Adapter<BindableViewHolder<out View, ItemType>>() {

    private val kryo = Kryo()
    private var items: List<ItemType> = ArrayList()
    private var previousList: List<ItemType> = ArrayList()

    private lateinit var emptyViewBinder: EmptyViewBinder

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
        this.kryo.instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
        setEmptyViewBinder(ItemViewBinderEmpty())
    }

    private var defaultViewBinder: ItemViewBinder<out View, ItemType> = ItemViewBinderString()

    private val itemViewBinderMap = SparseArray<ItemViewBinder<out View, ItemType>>()

    fun setEmptyViewBinder(emptyViewBinder: EmptyViewBinder) {
        this.emptyViewBinder = emptyViewBinder
    }

    fun setDefaultViewBinder(defaultViewBinder: ItemViewBinder<out View, ItemType>) {
        this.defaultViewBinder = defaultViewBinder
    }

    fun registerItemViewBinder(anEnum: Enum<*>,
                               itemViewBinder: ItemViewBinder<out View, ItemType>) {
        itemViewBinderMap.put(anEnum.ordinal, itemViewBinder)
    }

    fun setItems(newItems: List<ItemType>) {
        // make sure that item lists are passed by value
        previousList = kryo.copy(items)
        this.items = kryo.copy(newItems)
        DiffUtil.calculateDiff(ItemCallback(previousList, this.items)).dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BindableViewHolder<out View, ItemType> {
        return if (viewType == ItemViewBinderEmpty.EMPTY_TYPE)
            BindableViewHolder(parent, emptyViewBinder)
        else
            BindableViewHolder(parent, this.itemViewBinderMap.get(viewType) ?: defaultViewBinder)
    }

    override fun onBindViewHolder(holder: BindableViewHolder<out View, ItemType>, position: Int) {
        if (position == items.size) {
            return   // nothing to bind
        }

        val item = items[position]
        val previousIndex = previousList.indexOf(item)
        val previousItem = if (previousIndex >= 0) previousList[previousIndex] else null
        holder.bind(item, previousItem)
    }

    override fun getItemViewType(position: Int): Int =
        if (items.isEmpty()) ItemViewBinderEmpty.EMPTY_TYPE else items[position].getType().ordinal

    override fun getItemCount(): Int = Math.max(1, items.size)

}
