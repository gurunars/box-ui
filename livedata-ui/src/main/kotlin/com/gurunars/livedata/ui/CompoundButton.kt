package com.gurunars.livedata.ui

import android.widget.CompoundButton
import com.gurunars.livedata.Box
import com.gurunars.livedata.BoxContext

fun BoxContext<CompoundButton>.isChecked(field: Box<Boolean>) {
    context.setOnCheckedChangeListener { _, isChecked -> field.set(isChecked) }
    field.onChange(listener = context::setChecked)
}
