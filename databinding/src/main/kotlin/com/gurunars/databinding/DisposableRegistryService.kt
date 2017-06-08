package com.gurunars.databinding

class DisposableRegistryService : DisposableRegistry {

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
            it.disposeAll()
        }
        unbinding = false
    }

}