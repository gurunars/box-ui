package com.gurunars.livedata

/**
 * Apply a common change listener to a list of fields
 */
fun List<IBox<*>>.onChange(listener: () -> Unit) {
    forEach { it.onChange { _ -> listener() } }
}
