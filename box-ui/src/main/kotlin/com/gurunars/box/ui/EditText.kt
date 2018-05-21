package com.gurunars.box.ui

import android.content.Context
import android.widget.EditText
import com.gurunars.box.core.IObservableValue


fun Context.editText(init: EditText.() -> Unit) = EditText(this).apply { init() }

