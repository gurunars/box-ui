package com.gurunars.databinding

interface Field<Type> {
    /**
     * Change fields content to a new value. The change is made only if current and new values
     * actually differ content-wise.
     *
     * @param value payload to set the value to
     * @param force if true - the change is made even if current and new values are the same
     */
    fun set(value: Type, force:Boolean=false)

    /**
     * @return current value
     */
    fun get(): Type
}