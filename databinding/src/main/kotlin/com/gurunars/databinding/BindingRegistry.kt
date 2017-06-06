package com.gurunars.databinding

interface BindingRegistry {
    fun add(field: BindableField<*>)
    fun forEach(predicate: (field: BindableField<*>) -> Unit)
}