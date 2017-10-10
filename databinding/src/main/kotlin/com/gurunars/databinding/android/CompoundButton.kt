package com.gurunars.databinding.android

import android.widget.CompoundButton
import com.gurunars.databinding.BindableField

/**
 * Ensure that a boolean field is changed whenever CompoundButton's value changes and vice versa.
 */
fun CompoundButton.isChecked(field: BindableField<Boolean>) = isChecked(field, { it }, { this })

/**
 * Same as regular bind but with two way value transformation.
 *
 * @param From - type of payload to be transformed into a boolean and to be generated out of a boolean
 */
fun <From> CompoundButton.isChecked(
    field: BindableField<From>,
    forward: From.(value: Boolean) -> From,
    backword: From.() -> Boolean
) {
    setOnCheckedChangeListener { _, isChecked -> field.set(field.get().forward(isChecked)) }
    field.onChange { isChecked = backword(it) }
}

