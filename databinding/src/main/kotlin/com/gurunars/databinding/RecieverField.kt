package com.gurunars.databinding

class RecieverField<Type>(value: Type): ListenerField<Type>(value) {

    public override fun get() = super.get()

    fun receiveFrom(field: ListenerField<Type>): Binding {
        return field.bind { set(it) }
    }

    fun<To> receiveFrom(field: ListenerField<To>, transformer: (source: To) -> Type): Binding {
        return field.bind { set(transformer(it)) }
    }

}
