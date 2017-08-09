package com.gurunars.databinding.android

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.gurunars.databinding.BindableField

/**
 * Ensure that a string field is changed whenever TextView's value changes and vice versa.
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
 * Same as regular bind but with two way value transformation.
 *
 * @param From - type of payload to be transformed into a string and to be generated out of a string
 */
fun<From> TextView.bind(
    field: BindableField<From>,
    valueTransformer: BindableField.ValueTransformer<From, String>
) {
    field.onChange { setText(valueTransformer.forward(it)) }
    addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            field.set(valueTransformer.backward(s.toString()))
        }
    })
}


/**
 * @see TextView.bind
 */
fun BindableField<String>.bind(textView: TextView) {
    textView.bind(this)
}

/**
 * @see TextView.bind with value transformation
 */
fun<From> BindableField<From>.bind(
    textView: TextView,
    valueTransformer: BindableField.ValueTransformer<From, String>
) {
    textView.bind(this, valueTransformer)
}