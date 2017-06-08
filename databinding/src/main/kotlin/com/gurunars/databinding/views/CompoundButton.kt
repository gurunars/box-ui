package com.gurunars.databinding.views

import android.widget.CompoundButton
import com.gurunars.databinding.BindableField


inline fun CompoundButton.bind(field: BindableField<Boolean>) {
    field.onChange { isChecked=it }
    setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            field.set(isChecked)
        }
    })
}

inline fun BindableField<Boolean>.bind(compoundButton: CompoundButton) {
    compoundButton.bind(this)
}
