package com.gurunars.box.core

interface IObservableValue<Type> : IReadOnlyObservableValue<Type> {
    /**
     * Change fields content to a new value. The change is made only if current and new values
     * actually differ content-wise.
     *
     * @param value payload to set the value to
     * @return true if the value was different from the current one and thus had to be changed
     */
    fun set(value: Type): Boolean
}