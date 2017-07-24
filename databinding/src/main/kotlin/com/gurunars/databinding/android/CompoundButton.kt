package com.gurunars.databinding.android

import android.widget.CompoundButton
import com.gurunars.databinding.BindableField


inline fun CompoundButton.bind(field: BindableField<Boolean>) {
    field.onChange { isChecked=it }
    setOnCheckedChangeListener { _, isChecked -> field.set(isChecked) }
}

inline fun BindableField<Boolean>.bind(compoundButton: CompoundButton) {
    compoundButton.bind(this)
}
