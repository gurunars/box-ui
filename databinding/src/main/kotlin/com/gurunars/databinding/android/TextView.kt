package com.gurunars.databinding.android

import android.widget.TextView
import com.gurunars.databinding.BindableField


/**
 * Ensure that a change in a String field triggers change in TextView's payload.
 */
fun BindableField<String>.bind(textView: TextView) {
    onChange { textView.text = it }
}