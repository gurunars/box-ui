package com.gurunars.databinding

interface Bindable<Type> {

    fun bind(listener: (value: Type) -> Unit): Binding
    fun bind(field: BindableField<Type>, transformer: ValueProcessor<Type>?=null): Binding
    fun set(value: Type, force:Boolean=false)
    fun get() : Type
    fun unbindFromAll()

}