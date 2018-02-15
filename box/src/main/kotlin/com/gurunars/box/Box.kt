package com.gurunars.box

import java.util.*

/**
 * An observable box capable to emit changes and listen to change events
 *
 * @param Type type of the value the box is meant to hold
 * @param value initial value of the box
 */
class Box<Type>(private var value: Type) : IBox<Type> {
    private val listeners: MutableList<(value: Type) -> Unit> = mutableListOf()

    override fun onChange(hot: Boolean, listener: (value: Type) -> Unit): Bond {
        synchronized(this) {
            listeners.add(listener)
            if (hot) listener(this.value)

            return object : Bond {
                override fun drop() {
                    synchronized(this) {
                        listeners.remove(listener)
                    }
                }
            }
        }
    }

    override fun set(value: Type, force: Boolean): Boolean {
        synchronized(this) {
            if (force || !Objects.deepEquals(this.value, value)) {
                this.value = value
                listeners.forEach { it.invoke(value) }
                return true
            }
            return false
        }
    }

    override fun get(): Type = this.value
}