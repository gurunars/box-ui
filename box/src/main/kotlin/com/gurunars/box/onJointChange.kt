package com.gurunars.box

/** Apply a common change listener to a list of fields */
fun List<IRoBox<*>>.onChange(hot: Boolean = true, listener: () -> Unit) {
    forEach { it.onChange(hot) { _ -> listener() } }
}
