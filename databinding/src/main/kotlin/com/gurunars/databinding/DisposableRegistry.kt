package com.gurunars.databinding

interface DisposableRegistry {
    fun add(field: BindableField<*>)
    fun forEach(predicate: (field: BindableField<*>) -> Unit)
}