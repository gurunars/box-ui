package com.gurunars.box.ui

import android.widget.CompoundButton
import com.gurunars.box.IBox

fun CompoundButton.isChecked(field: IBox<Boolean>) {
    setOnCheckedChangeListener { _, isChecked -> field.set(isChecked) }
    field.onChange(listener = this::setChecked)
}
