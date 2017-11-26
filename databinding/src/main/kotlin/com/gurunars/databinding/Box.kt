package com.gurunars.databinding


/**
 * An observable field capable to emit changes and listen to change events
 *
 * @param Type type of the value the field is meant to hold
 * @param value initial value of the field
 */
class Box<Type>(private var value: Type) : IBox<Type> {
    private val listeners: MutableList<Listener<Type>> = mutableListOf()
    private var prevValue: Type = value

    override fun onChange(
        listener: Listener<Type>
    ) {
        listeners.add(listener)
        listener(this.value, this.value)
    }

    private fun notifyListeners()
        = listeners.forEach {
        it.invoke(this.prevValue, this.value)
    }


    override fun set(value: Type, force: Boolean) {
        if (force || !equal(this.value, value)) {
            this.prevValue = this.value
            this.value = value
            notifyListeners()
        }
    }

    override fun get(): Type = this.value

}