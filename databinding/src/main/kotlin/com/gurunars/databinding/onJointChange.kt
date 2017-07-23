package com.gurunars.databinding

fun List<BindableField<*>>.onChange(listener: () -> Unit) {
    forEach { it.onChange { listener() } }
}
