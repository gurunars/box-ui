package com.gurunars.box.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.gurunars.box.IBox
import com.gurunars.box.IRoBox

fun TextView.txt(field: IRoBox<String>) {
    field.onChange { txt ->
        if (text.toString() != txt) {
            text = txt
        }
    }
}

fun EditText.txt(field: IBox<String>) {
    field.onChange { txt ->
        if (text.toString() != txt) {
            setText(txt)
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
