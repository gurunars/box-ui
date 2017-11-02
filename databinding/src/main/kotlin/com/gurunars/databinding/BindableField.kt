package com.gurunars.databinding


/**
 * An observable field capable to emit changes and listen to change events
 *
 * @param Type type of the value the field is meant to hold
 * @param value initial value of the field
 */
class BindableField<Type>(private var value: Type) {

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
            }
        }
        listener(this.value)
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
        if (force || !equal(this.value, value)) {
            beforeChangeListeners.forEach { it.invoke(this.value) }
            this.value = value
            listeners.forEach { it.invoke(this.value) }
        }
    }

    /**
     * @return current value
     */
    fun get(): Type = this.value

    private fun join(
        forwardBinding: Binding,
        backwardBinding: Binding
    ) = object : Binding {
        override fun unbind() {
            forwardBinding.unbind()
            backwardBinding.unbind()
        }
    }

    /**
     * Create a two-way bond between this and another target. Whenever one changes its value another
     * get updated automatically.
     *
     * @param target field to bind to
     * @param forward function that transforms Type to To
     * @param backward function that transforms To to Type
     * @return a disposable bond
     */
    fun <To> bind(
        target: BindableField<To>,
        forward: (source: Type) -> To,
        backward: (source: To) -> Type
    ) = join(
        onChange { target.set(forward(it)) },
        target.onChange { this.set(backward(it)) }
    )

    /**
     * Create a two-way bond between this and another target. Whenever one changes its value another
     * get updated automatically.
     *
     * @param target target to bind to
     * @return a disposable bond
     */
    fun bind(target: BindableField<Type>) = join(
        onChange { target.set(it) },
        target.onChange { this.set(it) }
    )

}