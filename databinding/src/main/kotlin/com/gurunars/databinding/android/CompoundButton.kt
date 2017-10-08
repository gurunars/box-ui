package com.gurunars.databinding.android

import android.widget.CompoundButton
import com.gurunars.databinding.BindableField

/**
 * Ensure that a boolean field is changed whenever CompoundButton's value changes and vice versa.
 */
fun CompoundButton.bind(field: BindableField<Boolean>) {
    field.onChange { isChecked = it }
    setOnCheckedChangeListener { _, isChecked -> field.set(isChecked) }
}

/**
 * Same as regular bind but with two way value transformation.
 *
 * @param From - type of payload to be transformed into a boolean and to be generated out of a boolean
 */
fun <From> CompoundButton.bind(
    field: BindableField<From>,
    forward: From.(value: Boolean) -> From,
    backword: From.() -> Boolean
) {
    setOnCheckedChangeListener { _, isChecked -> field.set(field.get().forward(isChecked)) }
    field.onChange { isChecked = backword(it) }
}

fun BindableField<Boolean>.bind(compoundButton: CompoundButton) {
    compoundButton.bind(this)
}

fun <From> BindableField<From>.bind(
    compoundButton: CompoundButton,
    forward: From.() -> Boolean,
    backward: From.(value: Boolean) -> From
) {
    compoundButton.bind(this, backward, forward)
}
