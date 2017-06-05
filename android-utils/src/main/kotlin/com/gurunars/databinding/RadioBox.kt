package com.gurunars.databinding

import android.widget.CompoundButton

inline fun CompoundButton.bind(field: BindableField<Boolean>) {
    field.bind { isChecked=it }
    setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            field.set(isChecked)
        }
    })
}

inline fun BindableField<Boolean>.bind(compoundButton: CompoundButton) {
    compoundButton.bind(this)
}