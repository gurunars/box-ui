package com.gurunars.livedata.ui

import android.widget.CompoundButton
import com.gurunars.livedata.Box
import com.gurunars.livedata.LifecycleContext

fun CompoundButton.isChecked(field: Box<Boolean>) {
    setOnCheckedChangeListener { _, isChecked -> field.set(isChecked) }
    field.onChange(listener = this::setChecked)
}
