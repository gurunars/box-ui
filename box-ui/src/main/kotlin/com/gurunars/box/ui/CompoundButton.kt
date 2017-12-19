package com.gurunars.box.ui

import android.widget.CompoundButton
import com.gurunars.box.Box
import com.gurunars.box.BoxContext

fun BoxContext<CompoundButton>.isChecked(field: Box<Boolean>) {
    context.setOnCheckedChangeListener { _, isChecked -> field.set(isChecked) }
    field.onChange(listener = context::setChecked)
}
