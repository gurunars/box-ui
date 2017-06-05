package com.gurunars.databinding

class BindingRegistryService : BindingRegistry {

    override fun forEach(predicate: (field: BindableField<*>) -> Unit) {
        return fields.forEach(predicate)
    }

    private val fields = mutableListOf<BindableField<*>>()

    override fun add(field: BindableField<*>) {
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