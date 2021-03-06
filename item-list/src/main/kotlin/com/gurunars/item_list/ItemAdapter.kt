package com.gurunars.item_list

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gurunars.box.Box
import com.gurunars.box.IRoBox
import com.gurunars.box.ui.asRow
import com.gurunars.box.ui.fullSize

internal class ItemAdapter<ItemType : Item>(
        private val items: IRoBox<List<ItemType>>,
        private val emptyViewBinder: EmptyViewBinder,
        private val itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var previousList: List<ItemType> = listOf()
    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

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

    private val kryo = getKryo()

    init {
        items.onChange { list ->
            if (previousList.isEmpty() || list.isEmpty()) {
                notifyDataSetChanged()
            } else try {
                DiffUtil.calculateDiff(ItemCallback(previousList, list)).dispatchUpdatesTo(this)
            } catch (exe: IndexOutOfBoundsException) {
                // In case of a drastic failure - just reset the adapter
                val recycler = recyclerView ?: return@onChange
                recycler.recycledViewPool.clear()
                recycler.swapAdapter(
                        ItemAdapter(items, emptyViewBinder, itemViewBinders),
                        false
                )
            }
            previousList = kryo.copy(ArrayList(list))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == EMPTY_TYPE) {
            return object : RecyclerView.ViewHolder(emptyViewBinder().apply {
                fullSize()
                addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                    override fun onViewAttachedToWindow(v: View) {
                    }

                    override fun onViewDetachedFromWindow(v: View) {
                        clearAnimation()
                        clearFocus()
                    }
                })
            }) {
            }
        } else {
            // If enums are from different classes - they have same ordinals
            val initialPayload = items.get().first { getItemTypeInt(it) == viewType }
            val field = Box(initialPayload)
            val binder = itemViewBinders[initialPayload.type] ?: parent.context::defaultItemViewBinder
            return object : RecyclerView.ViewHolder(
                    binder(field).apply {
                        asRow()
                        setTag(R.id.payloadTag, field)
                    }) {}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == items.get().size) {
            return // nothing to bind
        }
        @Suppress("UNCHECKED_CAST")
        val field = holder.itemView.getTag(R.id.payloadTag) as Box<ItemType>
        field.set(items.get()[position])
    }

    override fun getItemId(position: Int) =
            if (!hasStableIds()) {
                RecyclerView.NO_ID
            } else {
                val itemList = items.get()
                if (itemList.isNotEmpty()) {
                    items.get()[position].id
                } else {
                    RecyclerView.NO_ID
                }
            }

    private fun getItemTypeInt(item: ItemType) = itemViewBinders.keys.indexOf(item.type)

    override fun getItemViewType(position: Int) =
            if (items.get().isEmpty())
                EMPTY_TYPE
            else getItemTypeInt(items.get()[position])

    override fun getItemCount() = Math.max(1, items.get().size)

    companion object {
        val EMPTY_TYPE = -404
    }
}
