package com.gurunars.databinding

interface BindingRegistry {
    fun registerFieldForUnbinding(field: BindableField<*>)
}