package com.gurunars.databinding.android

import android.widget.TextView
import com.gurunars.databinding.BindableField


inline fun BindableField<String>.bind(textView: TextView) {
    onChange { textView.setText(it) }
}