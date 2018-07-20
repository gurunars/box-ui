package com.gurunars.functional

import android.view.View
import android.view.ViewGroup

interface Container<SlotType: Slot> {
    val children: List<SlotType>
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
    val gOld = old.group()
    val gNew = new.group()

    val retained = gOld.keys.intersect(gNew.keys)

    val typeChanged = retained.filterNot { sameClass(gNew[it], gOld[it]) }
    val updated = retained.filterNot { gNew[it] == gOld[it] } - typeChanged

    val added = gNew.keys - gOld.keys + typeChanged
    val removed = gOld.keys - gNew.keys + typeChanged

    // The cache is to prevent wasting time with findViewById calls
    val viewCache = mutableMapOf<Int, View>()

    return (
        removed.map { { view: View ->
            (view as ViewGroup).removeView(view.findViewById(it))
        } } +
        added.flatMap { uid ->
            val newItem = gNew[uid]
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
            val newItem = gNew[it]
            val oldItem = gOld[it]
            val component = Registry.getElement(newItem)
            component.diff(
                oldItem as Any, newItem as Any
            )
        }
    )
}