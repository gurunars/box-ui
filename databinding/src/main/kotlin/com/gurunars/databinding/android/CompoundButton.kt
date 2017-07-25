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
 * @see CompoundButton.bind
 */
fun BindableField<Boolean>.bind(compoundButton: CompoundButton) {
    compoundButton.bind(this)
}
