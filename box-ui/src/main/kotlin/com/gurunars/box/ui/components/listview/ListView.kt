package com.gurunars.box.ui.components.listview

import android.content.Context
import android.view.View
import com.gurunars.box.core.IReadOnlyObservableValue
import com.gurunars.box.core.ObservableValue
import com.gurunars.box.core.bind

interface Renderer<T> {
    fun <ItemType : T> itemView(type: Any, render: RenderItem<ItemType>)
}

internal class ItemRenderer<T> : Renderer<T> {
    internal var binders: MutableMap<Any, RenderItem<T>> = mutableMapOf()
    override fun <ItemType : T> itemView(type: Any, render: RenderItem<ItemType>) {
        @Suppress("UNCHECKED_CAST")
        binders[type] = render as RenderItem<T>
    }
}

inline fun <T : Any, reified TType : T> Renderer<T>.itemView(noinline render: RenderItem<TType>) =
    itemView(TType::class, render)

/**
 * List view to show entries that do NOT have stable ids. E.g. strings or date time objects.
 *
 * @param items A collection of items shown in the list
 * @param itemRenderers a mapping between item types and view binders meant to render the respective items
 */
fun <T : WithId> Context.listView(
    items: IReadOnlyObservableValue<List<T>>,
    itemRenderers: Renderer<T>.() -> Unit = {}
) = baseListView(
    ItemAdapter(items, binders = ItemRenderer<T>().apply {
        itemRenderers()
    }.binders)
)

fun <T : Any> Context.simpleListView(
    items: IReadOnlyObservableValue<List<T>>,
    itemRenderers: Renderer<T>.() -> Unit = {}
): View {
    val realItems = items.bind { mapIndexed { index, t -> Wrapped(index, t) } }
    val binders: Map<Any, RenderItem<T>> = ItemRenderer<T>().apply { itemRenderers() }.binders
    return listView(realItems) {
        binders.forEach { pair ->
            itemView<Wrapped<T>>(Wrapped::class) { item -> pair.value.invoke(item.bind { payload })  }
        }
    }
}

fun <T : Any> Context.staticListView(
    items: List<T>,
    itemRenderers: Renderer<T>.() -> Unit = {}
): View =
    simpleListView(items = ObservableValue(items), itemRenderers = itemRenderers)
