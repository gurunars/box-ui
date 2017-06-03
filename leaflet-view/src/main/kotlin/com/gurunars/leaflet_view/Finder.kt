package com.gurunars.leaflet_view

internal object Finder {

    interface Equator<T> {
        fun equal(one: T, two: T): Boolean
    }

    fun <T> contains(list: List<T>, item: T, equator: Equator<T>): Boolean {
        return indexOf(list, item, equator) > 0
    }

    fun <T> indexOf(list: List<T>, item: T, equator: Equator<T>): Int {
        for (i in list.indices) {
            val cursor = list[i]
            if (equator.equal(cursor, item)) {
                return i
            }
        }
        return -1
    }

}
