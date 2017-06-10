package com.gurunars.databinding

class DisposableRegistryService {

    private val fields = mutableListOf<Disposable>()

    fun add(field: Disposable) {
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