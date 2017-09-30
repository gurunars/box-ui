package com.gurunars.databinding

import android.util.Log


/**
 * An observable field capable to emit changes and listen to change events
 *
 * @param Type type of the value the field is meant to hold
 * @param value initial value of the field
 * @param preset function to call on the value before storing it - a good place e.g. to perform
 *               a deep copy of the value
 */
class BindableField<Type>(
    private var value: Type,
    private val preset: (one: Type) -> Type = { item -> item }
) : Unbindable {

    private var isActive = true

    /**
     * Helper interface to ease field to field binding if the fields happen to have different value
     * types.
     *
     * @param From source data type
     * @param To target data type
     */
    interface ValueTransformer<From, To> {
        /**
         * Transform from source to target type.
         */
        fun forward(value: From): To

        /**
         * Transform from target to source type.
         */
        fun backward(value: To): From
    }

    /**
     * Representation of a bond between a field and a listener. The listener could be either
     * another field or a function.
     */
    interface Binding {
        /**
         * Dispose the binding.
         */
        fun unbind()
    }

    private val listeners: MutableList<(value: Type) -> Unit> = mutableListOf()
    private val beforeChangeListeners: MutableList<(value: Type) -> Unit> = mutableListOf()
    private val bindings: MutableList<Binding> = mutableListOf()

    /**
     * Subscribe to changes.
     *
     * @param beforeChange a function called with a current value before the change takes place
     * @param listener a function called with a new value after the change takes place
     */
    fun onChange(beforeChange: (value: Type) -> Unit = {}, listener: (value: Type) -> Unit): Binding {
        beforeChangeListeners.add(beforeChange)
        listeners.add(listener)
        val binding = object : Binding {
            override fun unbind() {
                beforeChangeListeners.remove(beforeChange)
                listeners.remove(listener)
                bindings.remove(this)
            }
        }
        bindings.add(binding)
        if (isActive) {
            listener(this.value)
        }
        return binding
    }

    /**
     * Change fields content to a new value. The change is made only if current and new values
     * actually differ content-wise.
     *
     * @param value payload to set the value to
     * @param force if true - the change is made even if current and new values are the same
     */
    fun set(value: Type, force: Boolean = false) {
        if (isActive && (force || !equal(this.value, value))) {
            beforeChangeListeners.forEach { it(this.value) }
            this.value = preset(value)
            listeners.forEach { it(this.value) }
        }
    }

    /**
     * @return current value
     */
    fun get(): Type = this.value

    private fun join(
        field: BindableField<*>,
        forwardBinding: Binding,
        backwardBinding: Binding
    ) = object : Binding {
        override fun unbind() {
            forwardBinding.unbind()
            backwardBinding.unbind()
            bindings.remove(this)
            field.bindings.remove(this)
        }
    }.apply {
        bindings.add(this)
        field.bindings.add(this)
    }

    /**
     * Create a two-way bond between this and another target. Whenever one changes its value another
     * get updated automatically.
     *
     * @param target field to bind to
     * @param transformer entity responsible for handling the transformation of values of two
     * different types from on type to another.
     * @return a disposable bond
     */
    fun <To> bind(
        target: BindableField<To>,
        transformer: ValueTransformer<Type, To>
    ) = join(target,
        onChange { target.set(transformer.forward(it)) },
        target.onChange { this.set(transformer.backward(it)) }
    )

    /**
     * Create a two-way bond between this and another target. Whenever one changes its value another
     * get updated automatically.
     *
     * @param target target to bind to
     * @return a disposable bond
     */
    fun bind(target: BindableField<Type>) = join(target,
        onChange { target.set(it) },
        target.onChange { this.set(it) }
    )

    /**
     * Dispose all bindings and listener.
     */
    override fun unbindAll() {
        bindings.toList().forEach { it.unbind() }
        listeners.clear()
        beforeChangeListeners.clear()
    }

    /**
     * Start responding to changes.
     */
    fun resume() {
        isActive = true
    }

    /**
     * Stop responding to changes.
     */
    fun pause() {
        isActive = false
    }

}