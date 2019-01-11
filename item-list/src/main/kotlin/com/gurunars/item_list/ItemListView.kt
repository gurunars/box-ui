package com.gurunars.item_list

import android.content.Context
import android.graphics.Color
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gurunars.box.Box
import com.gurunars.box.IRoBox
import com.gurunars.box.ui.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.ComputationScheduler

/***/
typealias RenderItem<ItemType> = (field: IRoBox<ItemType>) -> View

/***/
fun<T> Context.defaultItemViewBinder(field: IRoBox<T>) = TextView(this).apply {
    setBackgroundColor(Color.YELLOW)
    setTextColor(Color.RED)
    field.onChange { value ->
        text = with(value.toString()) {
            substring(0, minOf(42, length))
        }
        asRow()
    }
}

class RenderType<ItemType>(
    val type: Any,
    val render: RenderItem<ItemType>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RenderType<*>) return false
        if (type != other.type) return false
        return true
    }

    override fun hashCode(): Int = type.hashCode()

    @Suppress("UNCHECKED_CAST")
    internal fun<T> renderGeneric(item: IRoBox<T>) =
        this.render(item as IRoBox<ItemType>)
}

class Renderer<T: Any>(
    internal vararg val functionMap: RenderType<T>,
    internal val getType: (item: T) -> Any = { it::class }
)

fun <ItemType> renderWith(target: Any, render: RenderItem<ItemType>): RenderType<ItemType> =
    RenderType(target, render)

inline fun <reified ItemType> renderWith(noinline render: RenderItem<ItemType>): RenderType<ItemType> =
    RenderType(ItemType::class, render)

/** View binder for the case when there are no item in the list. */
typealias EmptyViewBinder = () -> View

/***/
fun Context.defaultEmptyViewBinder() = TextView(this).apply {
    id = R.id.noItemsLabel
    fullSize()
    text = getString(R.string.empty)
    gravity = Gravity.CENTER
}

private class ItemAdapter<T: Any>(
    private val items: IRoBox<List<T>>,
    private val emptyViewBinder: EmptyViewBinder,
    private val renderer: Renderer<T>,
    private val getKey: KeyGetter<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var previousList: List<T> = listOf()
    private var recyclerView: RecyclerView? = null

    private val renderMap = renderer.functionMap.map { it.type to it }.toMap()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    private class ItemCallback<T>(
        val getKey: KeyGetter<T>,
        val previousList: List<T>,
        val currentList: List<T>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = Math.max(1, previousList.size)

        override fun getNewListSize() = Math.max(1, currentList.size)

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            previousList.getOrNull(oldItemPosition) == currentList.getOrNull(newItemPosition)

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            previousList.getOrNull(oldItemPosition)?.let { getKey(it) } == currentList.getOrNull(newItemPosition)?.let { getKey(it) }
    }

    init {
        items.onChange { list ->
            if (previousList.isEmpty() || list.isEmpty()) {
                notifyDataSetChanged()
                previousList = list
                return@onChange
            }
            Single.fromCallable {
                DiffUtil.calculateDiff(
                    ItemCallback(getKey, previousList, list)
                )
            }
                .subscribeOn(ComputationScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { it: DiffUtil.DiffResult ->
                    it.dispatchUpdatesTo(this)
                }
                .subscribe({
                    previousList = list
                }, { exe ->
                    // In case of a drastic failure - just reset the adapter
                    if (exe is IndexOutOfBoundsException) {
                        val recycler = recyclerView ?: return@subscribe
                        recycler.recycledViewPool.clear()
                        recycler.swapAdapter(
                            ItemAdapter(items, emptyViewBinder, renderer, getKey),
                            false
                        )
                    }
                    previousList = list
                })
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
            val binder: RenderType<*> = renderMap[renderer.getType(initialPayload)]
                ?: RenderType<Any>(Any::class) { parent.context.defaultItemViewBinder(it) }
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
        val field = holder.itemView.getTag(R.id.payloadTag) as Box<Any>
        field.set(items.get()[position])
    }

    override fun getItemId(position: Int) =
        if (!hasStableIds()) {
            RecyclerView.NO_ID
        } else {
            val itemList = items.get()
            if (itemList.isNotEmpty()) {
                getKey(items.get()[position])
            } else {
                RecyclerView.NO_ID
            }
        }

    private fun getItemTypeInt(item: T) = renderMap.keys.indexOf(item::class)

    override fun getItemViewType(position: Int) =
        if (items.get().isEmpty())
            EMPTY_TYPE
        else getItemTypeInt(items.get()[position])

    override fun getItemCount() = Math.max(1, items.get().size)

    companion object {
        val EMPTY_TYPE = -404
    }
}

/**
 * @param items A collection of items shown in the list
 * @param getKey a function returning a unique key for the item
 * @param renderer a mapping between item types and view functionMap meant to render the respective items
 */
fun<T: Any> Context.itemListView(
    items: IRoBox<List<T>>,
    renderer: Renderer<T>,
    getKey: KeyGetter<T> = { it.hashCode().toLong() },
    emptyRenderer: EmptyViewBinder = this::defaultEmptyViewBinder
): View = RecyclerView(this).apply {
    id = R.id.recyclerView
    fullSize()
    clipToPadding = false
    padding = Bounds(bottom=dip(60))
    isSaveEnabled = false
    adapter = ItemAdapter(
        items,
        emptyRenderer,
        renderer,
        getKey
    )
    layoutManager = LinearLayoutManager(context).apply {
        orientation = LinearLayoutManager.VERTICAL
    }
}
