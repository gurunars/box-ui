package com.gurunars.databinding.android

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Context.closeKeyboard() {
    if (this is Activity) {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}