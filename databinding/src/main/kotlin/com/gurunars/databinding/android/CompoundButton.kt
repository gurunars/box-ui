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
    forward: Boolean.() -> From,
    backword: From.() -> Boolean
) {
    setOnCheckedChangeListener { _, isChecked -> field.set(forward(isChecked)) }
    field.onChange { isChecked = backword(it) }
}

/**
 * @see CompoundButton.bind
 */
fun BindableField<Boolean>.bind(compoundButton: CompoundButton) {
    compoundButton.bind(this)
}

/**
 * @see CompoundButton.bind with value transformation
 */
fun <From> BindableField<From>.bind(
    compoundButton: CompoundButton,
    forward: From.() -> Boolean,
    backward: Boolean.() -> From
) {
    compoundButton.bind(this, backward, forward)
}
