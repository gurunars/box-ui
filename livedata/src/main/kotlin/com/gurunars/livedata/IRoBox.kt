package com.gurunars.livedata

import android.arch.lifecycle.LifecycleOwner

/**
 * Listener aware of the previous and the current state of the observable.
 *
 * Useful in the cases when an action must be performed based on a diff between
 * a previous an a new state.
 */
typealias Listener<Type> = (prevValue: Type, value: Type) -> Unit

/**
 * Entity meant to hold the value and notify the observers about its
 * changes.
 */
interface IRoBox<Type> {
    /**
     * Entity that manages the lifecycle
     */
    val owner: LifecycleOwner
    /**
     * Fetches the current value.
     */
    fun get(): Type
    /**
     * Subscribes to changes of the value. Aware of the previous state.
     *
     * @param listener a function called with a new value after the change takes place
     * @param hot if true, immediately executes the listener with the current value
     *            otherwise just adds it to the collection of subscribers
     */
    fun LifecycleOwner.onChange(hot: Boolean = true, listener: Listener<Type>)
}