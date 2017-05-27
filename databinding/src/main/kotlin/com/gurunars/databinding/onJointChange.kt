package com.gurunars.databinding

/**
 * Apply a common change listener to a list of fields
 */
fun List<BindableField<*>>.onChange(listener: () -> Unit) {
    forEach { it.onChange { listener() } }
}
