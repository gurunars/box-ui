package com.gurunars.databinding.android

import android.widget.CompoundButton
import com.gurunars.databinding.IBox
import com.gurunars.databinding.onChange

fun CompoundButton.isChecked(field: IBox<Boolean>) {
    setOnCheckedChangeListener { _, isChecked -> field.set(isChecked) }
    field.onChange(listener = this::setChecked)
}
