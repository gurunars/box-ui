package com.gurunars.databinding.android

import android.widget.CompoundButton
import com.gurunars.databinding.Box
import com.gurunars.databinding.onChange

fun CompoundButton.isChecked(field: Box<Boolean>) {
    setOnCheckedChangeListener { _, isChecked -> field.set(isChecked) }
    field.onChange(this::setChecked)
}

