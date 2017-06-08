package com.gurunars.databinding.views

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.gurunars.databinding.BindableField

inline fun EditText.bind(field: BindableField<String>) {
    field.onChange { setText(it) }
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
