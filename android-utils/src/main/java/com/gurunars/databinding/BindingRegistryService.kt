package com.gurunars.databinding

class BindingRegistryService(val fields: MutableList<BindableField<*>>) : BindingRegistry {

    override fun registerFieldForUnbinding(field: BindableField<*>) {
        fields.add(field)
    }

    private var unbinding = false

    fun unbindAll() {
        if (unbinding) return
        unbinding = true
        fields.forEach {
            it.unbindFromAll()
        }
        unbinding = false
    }

}