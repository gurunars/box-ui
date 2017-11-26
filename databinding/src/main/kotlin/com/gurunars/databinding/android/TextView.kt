package com.gurunars.databinding.android

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.gurunars.databinding.Box

fun TextView.txt(field: Box<String>) {
    field.onChange { txt ->
        if (text.toString() != txt) {
            text = txt
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
