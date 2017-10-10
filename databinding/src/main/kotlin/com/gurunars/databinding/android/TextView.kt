package com.gurunars.databinding.android

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.gurunars.databinding.BindableField

/**
 * Same as regular text but with two way value transformation.
 *
 * @param From - type of payload to be transformed into a string and to be generated out of a string
 */
fun <From> TextView.text(
    field: BindableField<From>,
    forward: From.(value: String) -> From,
    backword: From.() -> String
) {
    field.onChange {
        val trans = backword(it)
        if (text.toString() != trans) {
            text = trans
        }
    }
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            field.set(field.get().forward(s.toString()))
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

/**
 * Ensure that a string field is changed whenever TextView's value changes and vice versa.
 */
fun TextView.text(field: BindableField<String>) = text(field, { it }, { this })
