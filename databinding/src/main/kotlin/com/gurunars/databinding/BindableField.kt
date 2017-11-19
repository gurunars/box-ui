package com.gurunars.databinding


/**
 * An observable field capable to emit changes and listen to change events
 *
 * @param Type type of the value the field is meant to hold
 * @param value initial value of the field
 */
class BindableField<Type>(private var value: Type): Observable<Type> {

    private val listeners: MutableList<Listener<Type>> = mutableListOf()
    private var prevValue: Type = value

    override fun onChange(
        listener: SimpleListener<Type>
    ) = onChange({ _, value -> listener(value) })

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

    /**
     * Change fields content to a new value. The change is made only if current and new values
     * actually differ content-wise.
     *
     * @param value payload to set the value to
     * @param force if true - the change is made even if current and new values are the same
     */
    fun set(value: Type, force: Boolean = false) {
        if (force || !equal(this.value, value)) {
            this.prevValue = this.value
            this.value = value
            notifyListeners()
        }
    }

    override fun get(): Type = this.value

}