package com.gurunars.box

/**
 * A decorator for the box that transforms the value before the actual
 * payload is updated.
 */
class TransformerBox<Type>(
    private val source: IBox<Type>,
    private val transform: Type.() -> Type
): IBox<Type> {
    override fun set(value: Type, force: Boolean): Boolean {
        val transformed = transform(value)
        if (transformed != value) {
            return source.set(transform(value), force)
        }
        return false
    }

    override fun get() = transform(source.get())

    override fun onChange(hot: Boolean, listener: (value: Type) -> Unit): Bond {
        return source.onChange(hot) {
            listener(transform(it))
        }
    }
}