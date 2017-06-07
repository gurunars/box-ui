package com.gurunars.databinding.views

import android.widget.CompoundButton
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.RecieverField
import com.gurunars.databinding.SenderField


inline fun CompoundButton.bind(field: BindableField<Boolean>) {
    field.bind { isChecked=it }
    setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            field.set(isChecked)
        }
    })
}

inline fun BindableField<Boolean>.bind(compoundButton: CompoundButton) {
    compoundButton.bind(this)
}


fun RecieverField<Boolean>.receiveFrom(compoundButton: CompoundButton) {
    val field = this
    compoundButton.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            field.set(isChecked)
        }
    })
}


inline fun SenderField<Boolean>.sendTo(compoundButton: CompoundButton) {
    bind { set(compoundButton.isChecked) }
}