package com.gurunars.item_list

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup

import com.esotericsoftware.kryo.Kryo

import org.objenesis.strategy.StdInstantiatorStrategy

import java.util.ArrayList


internal class ItemAdapter<ItemType : Item>(private val scroller: Scroller) : RecyclerView.Adapter<BindableViewHolder<out View, ItemType>>() {

    private val kryo = Kryo()
    private var items: MutableList<ItemType> = ArrayList()
    private var previousList: List<ItemType> = ArrayList()

    private val differ = Differ<ItemType>()
    private lateinit var emptyViewBinder: EmptyViewBinder

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
        val newItemsCopy = kryo.copy(newItems)

        if (items.isEmpty()) {
            this.previousList = newItemsCopy
            this.items = newItemsCopy.toMutableList()
            notifyDataSetChanged()
        } else {
            var position = -1

            for (change in differ.apply(items, newItemsCopy)) {
                position = change.apply(this, scroller, items, position)
            }

            if (position >= 0) {
                scroller.scrollToPosition(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BindableViewHolder<out View, ItemType> {
        if (viewType == ItemViewBinderEmpty.EMPTY_TYPE) {
            return BindableViewHolder(parent, emptyViewBinder)
        } else {
            val itemViewBinder = this.itemViewBinderMap.get(viewType)
            return BindableViewHolder(parent, itemViewBinder ?: defaultViewBinder)
        }
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

    override fun getItemViewType(position: Int): Int {
        if (items.isEmpty()) {
            return ItemViewBinderEmpty.EMPTY_TYPE
        }
        return items[position].type.ordinal
    }

    override fun getItemCount(): Int {
        return Math.max(1, items.size)
    }

}
