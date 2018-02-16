package com.gurunars.box

import io.reactivex.disposables.Disposable

/**
 * Entity meant to hold the value and notify the observers about its
 * changes.
 */
interface IRoBox<Type> {
    /** Fetches the current value. */
    fun get(): Type
    /**
     * Subscribes to changes of the value. Aware of the previous state.
     *
     * @param listener a function called with a new value after the change takes place
     */
    fun onChange(listener: (value: Type) -> Unit): Disposable
}