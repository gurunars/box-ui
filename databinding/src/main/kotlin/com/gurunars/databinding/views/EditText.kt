package com.gurunars.databinding.views

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.RecieverField
import com.gurunars.databinding.SenderField

fun EditText.bind(field: BindableField<String>) {
    field.bind { setText(it) }
    addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            field.set(s.toString())
        }

    })
}

inline fun BindableField<String>.bind(editText: EditText) {
    editText.bind(this)
}


fun RecieverField<String>.receiveFrom(editText: EditText) {
    val field = this
    editText.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            field.set(s.toString())
        }

    })
}


inline fun SenderField<String>.sendTo(editText: EditText) {
    bind { editText.setText(it) }
}