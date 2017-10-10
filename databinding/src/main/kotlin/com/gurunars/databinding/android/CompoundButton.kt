package com.gurunars.databinding.android

import android.widget.CompoundButton
import com.gurunars.databinding.BindableField

/**
 * Same as regular bind but with two way value transformation.
 *
 * @param From - type of payload to be transformed into a boolean and to be generated out of a boolean
 */
fun CompoundButton.isChecked(field: BindableField<Boolean>) {
    setOnCheckedChangeListener { _, isChecked -> field.set(isChecked) }
    field.onChange { isChecked = it }
}

