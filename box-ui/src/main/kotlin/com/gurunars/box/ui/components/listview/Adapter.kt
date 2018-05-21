package com.gurunars.box.ui.components.listview

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gurunars.box.core.*
import com.gurunars.box.ui.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

typealias KeyGetter<T> = (item: T) -> Any

typealias RenderItem<ItemType> = (field: IReadOnlyObservableValue<ItemType>) -> View

private fun Context.defaultItemViewBinder(field: IReadOnlyObservableValue<Any>) = TextView(this).apply {
    setBackgroundColor(Color.YELLOW)
    setTextColor(Color.RED)
    field.onChange { value ->
        text = with(value.toString()) {
            substring(0, minOf(42, length))
        }
        asRow()
    }
}

private sealed class ListChange<T>(val items: List<T>) {
    class FullChange<T>(items: List<T>) : ListChange<T>(items)
    class Perform<T>(val diff: DiffUtil.DiffResult, items: List<T>) : ListChange<T>(items)
}

internal class ItemViewHolder<T> internal constructor(view: View, val field: IObservableValue<T>) :
    RecyclerView.ViewHolder(view)

internal class ItemAdapter<T: WithId>(
    private val items: IReadOnlyObservableValue<List<T>>,
    private val binders: Map<Any, RenderItem<T>> = mapOf()
) : RecyclerView.Adapter<ItemViewHolder<T>>() {

    private val disposable = CompositeDisposable()
    private var previousList: List<T> = listOf()
    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    private class ItemCallback<T: WithId>(
        val previousList: List<T>,
        val currentList: List<T>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = previousList.size

        override fun getNewListSize() = currentList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            previousList.getOrNull(oldItemPosition) == currentList.getOrNull(newItemPosition)

        private fun getId(item: T) = item.id

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            previousList.getOrNull(oldItemPosition)?.let(::getId) == currentList.getOrNull(
                newItemPosition
            )?.let(::getId)
    }

    init {
        setHasStableIds(false)

        items.toObservable().subscribeOn(Schedulers.computation()).map { listWrapper: Wrapper<List<T>> ->
            val list = listWrapper.payload
            if (previousList.isEmpty() || list.isEmpty()) {
                ListChange.FullChange(list)
            } else {
                ListChange.Perform(
                    DiffUtil.calculateDiff(
                        ItemCallback(previousList, list)
                    ),
                    list
                )
            }
        }.observeOn(AndroidSchedulers.mainThread()).subscribe {
            when (it) {
                is ListChange.FullChange -> notifyDataSetChanged()
                is ListChange.Perform -> it.diff.dispatchUpdatesTo(this)
            }
            previousList = it.items
        }.let { disposable.add(it) }
    }

    private fun getType(item: T) = if (item is WithType) item.type else item::class

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        disposable.clear()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<T> {
        // If enums are from different classes - they have same ordinals
        val initialPayload = items.get().first { getItemTypeInt(it) == viewType }
        val field = ObservableValue(initialPayload)
        @Suppress("UNCHECKED_CAST")
        return binders[getType(initialPayload)]?.let { binder ->
            ItemViewHolder(
                binder(field).apply { asRow() },
                field
            )
        } ?: ItemViewHolder(
            parent.context.defaultItemViewBinder(field as IReadOnlyObservableValue<Any>),
            field
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder<T>, position: Int) {
        holder.field.set(items.get()[position])
    }

    private fun getItemTypeInt(item: T) =
        binders.keys.indexOf(getType(item))

    override fun getItemViewType(position: Int) =
        getItemTypeInt(items.get()[position])

    override fun getItemCount() =
        items.get().size

}

internal fun <T : WithId> Context.baseListView(
    itemAdapter: ItemAdapter<T>
) = RecyclerView(this).apply {
    clipToPadding = false
    padding = Bounds(bottom = dip(60))
    isSaveEnabled = false
    adapter = itemAdapter
    itemAnimator = DefaultItemAnimator().apply {
        supportsChangeAnimations = false
    }
    layoutManager = LinearLayoutManager(context).apply {
        orientation = RecyclerView.VERTICAL
    }
}

