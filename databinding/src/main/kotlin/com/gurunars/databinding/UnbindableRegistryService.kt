package com.gurunars.databinding

/**
 * Helper register to hold the references to initialized field.
 *
 * Its main purpose is to simplify lifecycle management in the cases when it is not possible out
 * of the box.
 */
class UnbindableRegistryService {

    private val fields = mutableListOf<Unbindable>()

    /**
     * Register an externally created unbindable entity.
     */
    fun add(field: Unbindable) {
        fields.add(field)
    }

    private var unbinding = false

    /**
     * Unbind all the fields mentioned in the register.
     */
    fun unbindAll() {
        if (unbinding) return
        unbinding = true
        fields.forEach {
            it.unbindAll()
        }
        unbinding = false
    }

    /**
     * Create and store a reference of a bindable field
     *
     * @return created field
     */
    fun<Type> bindableField(
        value: Type,
        preset: (one: Type) -> Type = { item -> item }
    ) = BindableField(value, preset).apply {
        this@UnbindableRegistryService.add(this)
    }

}