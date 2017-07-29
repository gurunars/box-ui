package com.gurunars.databinding.android

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.gurunars.databinding.BindableField

/**
 * Ensure that a string field is changed whenever CompoundButton's value changes and vice versa.
 */
fun TextView.bind(field: BindableField<String>) {
    field.onChange { setText(it) }
    addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            field.set(s.toString())
        }

    })
}

/**
 * @see TextView.bind
 */
fun BindableField<String>.bind(textView: TextView) {
    textView.bind(this)
}
