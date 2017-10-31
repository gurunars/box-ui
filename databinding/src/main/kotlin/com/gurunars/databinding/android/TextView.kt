package com.gurunars.databinding.android

import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.gurunars.databinding.BindableField

/**
 * Same as regular txt but with two way value transformation.
 *
 * @param From - type of payload to be transformed into a string and to be generated out of a string
 */
fun TextView.txt(field: BindableField<String>) {
    field.onChange {
        val trans = it
        if (text.toString() != trans) {
            setText(trans)
        }
    }
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            field.set(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}
