package com.gurunars.databinding

typealias Listener<Type> = (prevValue: Type, value: Type) -> Unit
typealias SimpleListener<Type> = (value: Type) -> Unit

interface Observable<out Type> {
    fun get(): Type
    fun onChange(listener: SimpleListener<Type>)
    fun onChange(listener: Listener<Type>)
}