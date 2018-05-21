package com.gurunars.box.ui

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

var TextView.textColor by PlainConsumerField<TextView, Int> { setTextColor(it)  }
var TextView.textColor_ by ConsumerField<TextView, Int> { textColor = it  }
var TextView.typeface_ by ConsumerField<TextView, Typeface> { typeface = it  }
var TextView.textSize_ by ConsumerField<TextView, Float> { textSize = it }
var TextView.text_ by SyncFieldField<TextView, String>(
    initializeEmitter = {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                it.set(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    },
    onChange = {
        if (text.toString() != it) {
            text = it
        }
    }
)
fun Context.textView(init: TextView.() -> Unit = {}) = TextView(this).apply { init() }