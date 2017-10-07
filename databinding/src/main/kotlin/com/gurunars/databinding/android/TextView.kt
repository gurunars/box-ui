package com.gurunars.databinding.android

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.gurunars.databinding.BindableField

/**
 * Ensure that a string field is changed whenever TextView's value changes and vice versa.
 */
fun TextView.bind(field: BindableField<String>) {
    field.onChange {
        if (text.toString() != it) {
            text = it
        }
    }
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            field.set(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

/**
 * Same as regular bind but with two way value transformation.
 *
 * @param From - type of payload to be transformed into a string and to be generated out of a string
 */
fun <From> TextView.bind(
    field: BindableField<From>,
    forward: String.() -> From,
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
            field.set(forward(s.toString()))
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun BindableField<String>.bind(textView: TextView) {
    textView.bind(this)
}

fun <From> BindableField<From>.bind(
    textView: TextView,
    forward: From.() -> String,
    backward: String.() -> From
) {
    textView.bind(this, backward, forward)
}