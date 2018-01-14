package com.gurunars.box.ui

import android.widget.CompoundButton
import com.gurunars.box.IBox

/** compoundButton.setChecked(true) -> booleanBox.set(true) */
fun CompoundButton.isChecked(field: IBox<Boolean>) {
    setOnCheckedChangeListener { _, isChecked -> field.set(isChecked) }
    field.onChange(listener = this::setChecked)
}
