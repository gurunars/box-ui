package com.gurunars.android_utils


class BindableField<Type>(private var value: Type) {

    interface Transformer<TypeOne, TypeTwo> {
        fun oneToTwo(one: TypeOne): TypeTwo
        fun twoToOne(two: TypeTwo): TypeOne
    }

    val listeners: MutableList<(value: Type) -> Unit> = mutableListOf()

    fun bind(listener: (value: Type) -> Unit) {
        listeners.add(listener)
        listener(this.value)
    }

    fun <TypeTwo> bind(field: BindableField<TypeTwo>, transformer: Transformer<Type, TypeTwo>, dual:Boolean=true) {
        listeners.add({
            field.set(transformer.oneToTwo(value))
        })
        if (dual) {
            field.bind(this, object: Transformer<TypeTwo, Type>{
                override fun oneToTwo(one: TypeTwo): Type {
                    return transformer.twoToOne(one)
                }

                override fun twoToOne(two: Type): TypeTwo {
                    return transformer.oneToTwo(two)
                }
            }, false)
        }
    }

    fun bind(field: BindableField<Type>, dual:Boolean=true) {
        bind({ field.set(it) })
        if(dual) {
            field.bind(this, false)
        }
    }

    fun set(value: Type) {
        val previousValue = this.value
        this.value = value
        if (this.value != previousValue) {
            listeners.forEach { it(value) }
        }
    }

    fun get() : Type {
        return this.value
    }

}