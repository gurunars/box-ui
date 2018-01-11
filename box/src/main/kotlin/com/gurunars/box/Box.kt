package com.gurunars.box

import java.util.Objects

/**
 * An observable box capable to emit changes and listen to change events
 *
 * @param Type type of the value the box is meant to hold
 * @param value initial value of the box
 */
class Box<Type>(private var value: Type) : IBox<Type> {
    private val listeners: MutableList<(value: Type) -> Unit> = mutableListOf()

    override fun onChange(hot: Boolean, listener: (value: Type) -> Unit): Bond {
        listeners.add(listener)
        if (hot) listener(this.value)

        return object : Bond {
            override fun drop() {
                listeners.remove(listener)
            }
        }
    }

    private fun notifyListeners() = listeners.forEach {
        it.invoke(this.value)
    }

    override fun set(value: Type, force: Boolean): Boolean {
        if (force || !Objects.deepEquals(this.value, value)) {
            this.value = value
            notifyListeners()
            return true
        }
        return false
    }

    override fun get(): Type = this.value
}