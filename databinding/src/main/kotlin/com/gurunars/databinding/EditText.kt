package com.gurunars.databinding

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

inline fun EditText.bind(field: BindableField<String>) {
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