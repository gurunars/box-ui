package com.gurunars.item_list

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gurunars.box.Box
import com.gurunars.box.IRoBox
import com.gurunars.box.ui.asRow
import com.gurunars.box.ui.fullSize

internal class ItemAdapter(
    private val items: IRoBox<List<Item>>,
    private val emptyViewBinder: EmptyViewBinder,
    itemViewBinders: Set<ItemBinding<*>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var previousList: List<Pair<Long, Int>> = listOf()
    private var recyclerView: RecyclerView? = null

    private val binders = itemViewBinders.map { it.type to it }.toMap()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    private class ItemCallback(
            val previousList: List<Pair<Long, Int>>,
            val currentList: List<Pair<Long, Int>>) : DiffUtil.Callback() {

        override fun getOldListSize() = Math.max(1, previousList.size)

        override fun getNewListSize() = Math.max(1, currentList.size)

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                previousList.getOrNull(oldItemPosition)?.second == currentList.getOrNull(newItemPosition)?.second

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                previousList.getOrNull(oldItemPosition)?.first == currentList.getOrNull(newItemPosition)?.first
    }

    init {
        items.onChange { list ->
            if (previousList.isEmpty() || list.isEmpty()) {
                notifyDataSetChanged()
            } else try {
                DiffUtil.calculateDiff(
                    ItemCallback(previousList, list.shrink())
                ).dispatchUpdatesTo(this)
            } catch (exe: IndexOutOfBoundsException) {
                // In case of a drastic failure - just reset the adapter
                val recycler = recyclerView ?: return@onChange
                recycler.recycledViewPool.clear()
                recycler.swapAdapter(
                    ItemAdapter(items, emptyViewBinder, itemViewBinders),
                    false
                )
            }
            previousList = list.shrink()
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
            val binder = binders[initialPayload::class] ?: ItemBinding(Item::class) {
                parent.context.defaultItemViewBinder(it)
            }
            return object : RecyclerView.ViewHolder(
                binder.renderGeneric(field).apply {
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
        val field = holder.itemView.getTag(R.id.payloadTag) as Box<Item>
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

    private fun getItemTypeInt(item: Item) = binders.keys.indexOf(item::class)

    override fun getItemViewType(position: Int) =
            if (items.get().isEmpty())
                EMPTY_TYPE
            else getItemTypeInt(items.get()[position])

    override fun getItemCount() = Math.max(1, items.get().size)

    companion object {
        val EMPTY_TYPE = -404
    }
}
