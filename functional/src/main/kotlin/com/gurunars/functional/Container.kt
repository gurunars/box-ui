package com.gurunars.functional

import android.view.View as AndroidView
import android.view.ViewGroup

interface Container {
    val children: List<View>
}

fun sameClass(old: View?, new: View?) =
    old?.child?.javaClass != new?.child?.javaClass

fun List<View>.group() =
    mapIndexed { index, it ->
        Pair(
            it.id.takeIf { it != AndroidView.NO_ID } ?: index,
            it
        )
    }.toMap()

fun getCollectionDiff(
    old: List<View>,
    new: List<View>
): List<Mutation> {
    val gOld = old.group()
    val gNew = new.group()

    val retained = gOld.keys.intersect(gNew.keys)

    val typeChanged = retained.filterNot { sameClass(gNew[it], gOld[it]) }
    val updated = retained.filterNot { gNew[it] == gOld[it] } - typeChanged

    val added = gNew.keys - gOld.keys + typeChanged
    val removed = gOld.keys - gNew.keys + typeChanged

    // The cache is to prevent wasting time with findViewById calls
    val viewCache = mutableMapOf<Int, AndroidView>()

    return (
        removed.map { { view: Any ->
            (view as ViewGroup).removeView(view.findViewById(it))
        } } +
        added.flatMap { uid ->
            val newItem = gNew[uid]
            val component = Registry.getElement(newItem)
            listOf(
                { view: Any ->
                    component.getEmptyTarget(
                        (view as AndroidView).context
                    ).apply {
                        id = uid
                    }.let { child ->
                        (view as ViewGroup).addView(child)
                        viewCache[uid] = child
                    }
                }
            ) + component.diff(component.empty, newItem as Any).map { item ->
                { _: Any -> item.invoke(viewCache[uid]!!) }
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