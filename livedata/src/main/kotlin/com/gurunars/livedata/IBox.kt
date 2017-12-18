package com.gurunars.databinding

/**
 * Mutable IRoBox.
 */
interface IBox<Type> : IRoBox<Type> {
    /**
     * Change fields content to a new value. The change is made only if current and new values
     * actually differ content-wise.
     *
     * @param value payload to set the value to
     * @param force if true - the change is made even if current and new values are the same
     * @param true if the value was different from the current one and thus had to be changed
     */
    fun set(value: Type, force: Boolean = false): Boolean
}