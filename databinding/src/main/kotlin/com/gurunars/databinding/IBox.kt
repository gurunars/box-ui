package com.gurunars.databinding

/**
 * Listener aware of the previous and the current state of the observable.
 *
 * Useful in the cases when an action must be performed based on a diff between
 * a previous an a new state.
 */
typealias Listener<Type> = (prevValue: Type, value: Type) -> Unit

/**
 * Listener aware of the current state of the observable.
 */
typealias SimpleListener<Type> = (value: Type) -> Unit

/**
 * Entity meant to hold the value and notify the observers about its
 * changes.
 */
interface IBox<out Type> {
    /**
     * Fetches the current value.
     */
    fun get(): Type
    /**
     * Subscribes to changes of the value.
     *
     * @param listener a function called with a new value after the change takes place
     */
    fun onChange(listener: SimpleListener<Type>)
    /**
     * Subscribes to changes of the value. Aware of the previous state.
     *
     * @param listener a function called with a new value after the change takes place
     */
    fun onChange(listener: Listener<Type>)
}