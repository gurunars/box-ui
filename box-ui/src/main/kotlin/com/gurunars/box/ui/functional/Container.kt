package com.gurunars.box.ui.functional

import android.view.View
import android.view.ViewGroup

interface Container {
    val children: List<Slot>
}

/**
 * A decorator for the component with layout parameters for a specific layout types
 */
interface Slot {
    val key: Int?
    val child: Any
}

fun sameClass(old: Slot?, new: Slot?) =
    old?.child?.javaClass != new?.child?.javaClass

fun List<Slot>.group() =
    mapIndexed { index, it ->
        Pair(
            it.key ?: index,
            it
        )
    }.toMap()

fun getCollectionDiff(
    old: List<Slot>,
    new: List<Slot>
): List<Mutation> {
    val olds = old.group()
    val news = new.group()

    val retained = olds.keys.intersect(news.keys)

    val typeChanged = retained.filterNot { sameClass(news[it], olds[it]) }
    val updated = retained.filterNot { news[it] == olds[it] } - typeChanged

    val added = news.keys - olds.keys + typeChanged
    val removed = olds.keys - news.keys + typeChanged

    // The cache is to prevent wasting time with findViewById calls
    val viewCache = mutableMapOf<Int, View>()

    return (
        removed.map { { view: View ->
            (view as ViewGroup).removeView(view.findViewById(it))
        } } +
        added.flatMap { uid ->
            val newItem = news[uid]
            val component = Registry.getElement(newItem)
            listOf(
                { view: View ->
                    component.getEmptyView(
                        view.context
                    ).apply {
                        id = uid
                    }.let { child ->
                        (view as ViewGroup).addView(child)
                        viewCache[uid] = child
                    }
                }
            ) + component.diff(component.empty, newItem as Any).map { item ->
                { _: View -> item.invoke(viewCache[uid]!!) }
            }
        } +
        updated.flatMap {
            val newItem = news[it]
            val oldItem = olds[it]
            val component = Registry.getElement(newItem)
            component.diff(
                oldItem as Any, newItem as Any
            )
        }
    )
}