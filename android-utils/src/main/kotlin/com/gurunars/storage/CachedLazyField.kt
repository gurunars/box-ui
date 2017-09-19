package com.gurunars.storage

internal class CachedLazyField<Type>(private val init: () -> Type) {
    private var value: Type? = null

    fun get(): Type {
        if (value == null) {
            value = init()
        }
        return value!!
    }

}