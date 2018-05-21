package com.gurunars.box.ui.components.listview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import androidx.annotation.ColorInt
import com.gurunars.box.core.*
import com.gurunars.box.ui.*
import io.reactivex.rxjava3.schedulers.Schedulers


private fun <T : Any> Collection<T>.has(item: T) =
    find { item == it } != null

private fun <T : Any> Set<T>.exclude(item: T) =
    filterNot { item == it }.toSet()

private fun <T : Any> Set<T>.include(item: T) =
    this + item

/**
 * A decorator to add row coloring behavior to the list view items.
 *
 * @param isSelected indicates selection status
 * @param selectionColor color integer applied when the row is selected
 */
fun View.coloredRowSelectionDecorator(
    isSelected: IReadOnlyObservableValue<Boolean>,
    @ColorInt selectionColor: Int = context.themeColor(R.attr.colorAccent)!!
): View {
    asRow()
    val originalBackground: Drawable
    originalBackground = if (background is LayerDrawable) {
        val current = background as LayerDrawable
        LayerDrawable(
            (0 until current.numberOfLayers).map { current.getDrawable(it) }.toTypedArray()
        )
    } else {
        background.mutate()
    }
    isSelected.onChange { flag ->
        contentDescription = if (flag) "SELECTED" else null
        background = if (flag)
            LayerDrawable(listOf(ColorDrawable(selectionColor), background).toTypedArray())
        else
            originalBackground
    }
    return this
}

data class WithSelection<T: WithId>(
    val item: T,
    val isSelected: Boolean
): WithId, WithType {
    override val id: Any
        get() = item.id
    override val type: Any
        get() = item::class
}

private fun <T : WithId> withSelector(
    field: IReadOnlyObservableValue<WithSelection<T>>,
    selectedItems: IObservableValue<Set<Any>>,
    render: RenderItem<WithSelection<T>>
) = render(field).apply {
    isClickable = true
    onClick {
        val item = field.get()
        val id = item.id
        val sel = selectedItems.get()
        selectedItems.set(
            when {
                sel.isEmpty() -> setOf()
                selectedItems.get().has(id) -> sel.exclude(id)
                else -> sel.include(id)
            }
        )
    }
    onLongClick {
        val item = field.get()
        val sel = selectedItems.get()
        if (sel.isEmpty()) selectedItems.set(sel.include(item.id))
    }
}

interface SelectableRenderer<T: WithId> {
    fun <ItemType : T> itemView(type: Any, render: RenderItem<WithSelection<ItemType>>)
}

private class SelectableItemRenderer<T: WithId> : SelectableRenderer<T> {
    internal var binders: MutableMap<Any, RenderItem<WithSelection<T>>> = mutableMapOf()
    override fun <ItemType : T> itemView(type: Any, render: RenderItem<WithSelection<ItemType>>) {
        @Suppress("UNCHECKED_CAST")
        binders[type] = render as RenderItem<WithSelection<T>>
    }
}

inline fun <T : WithId, reified TType : T> SelectableRenderer<T>.itemView(noinline render: RenderItem<WithSelection<TType>>) =
    itemView(TType::class, render)

fun <T : WithId> Context.selectableListView(
    items: IReadOnlyObservableValue<List<T>>,
    selectedItems: IObservableValue<Set<Any>> = ObservableValue(setOf()),
    itemRenderers: SelectableRenderer<T>.() -> Unit = {}
): View = baseListView(
    ItemAdapter(
        items = merge(items, selectedItems).toObservable().observeOn(Schedulers.computation()).map {
            it.payload.first.map { item -> WithSelection(item, it.payload.second.contains(item.id)) }
        }.toIRoBox(listOf()),
        binders = SelectableItemRenderer<T>().apply {
            itemRenderers()
        }.binders.map { binder ->
            Pair<Any, RenderItem<WithSelection<T>>>(
                binder.key,
                { withSelector(it, selectedItems, binder.value) }
            )
        }.toMap()
    ).also {
        items.toObservable().observeOn(Schedulers.computation()).subscribe { wrapper ->
            val allIds = wrapper.payload.map { item -> item.id }
            selectedItems.patch { filter { allIds.contains(it) }.toSet() }
        }
    }
)
