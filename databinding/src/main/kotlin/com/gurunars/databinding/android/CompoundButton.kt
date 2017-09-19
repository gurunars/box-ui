package com.gurunars.databinding.android

import android.widget.CompoundButton
import com.gurunars.databinding.BindableField

/**
 * Ensure that a boolean field is changed whenever CompoundButton's value changes and vice versa.
 */
fun CompoundButton.bind(field: BindableField<Boolean>) {
    field.onChange { isChecked=it }
    setOnCheckedChangeListener { _, isChecked -> field.set(isChecked) }
}

/**
 * Same as regular bind but with two way value transformation.
 *
 * @param From - type of payload to be transformed into a boolean and to be generated out of a boolean
 */
fun<From> CompoundButton.bind(
    field: BindableField<From>,
    valueTransformer: BindableField.ValueTransformer<From, Boolean>
) {
    field.onChange { isChecked=valueTransformer.forward(it) }
    setOnCheckedChangeListener { _, isChecked -> field.set(valueTransformer.backward(isChecked)) }
}

//TODO: CompoundButton.bind

/**
 * @see CompoundButton.bind
 */
fun BindableField<Boolean>.bind(compoundButton: CompoundButton) {
    compoundButton.bind(this)
}

/**
 * @see CompoundButton.bind with value transformation
 */
fun<From> BindableField<From>.bind(
    compoundButton: CompoundButton,
    valueTransformer: BindableField.ValueTransformer<From, Boolean>
) {
    compoundButton.bind(this, valueTransformer)
}
