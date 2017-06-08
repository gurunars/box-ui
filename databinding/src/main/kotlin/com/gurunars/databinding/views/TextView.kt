package com.gurunars.databinding.views

import android.widget.TextView
import com.gurunars.databinding.BindableField


inline fun BindableField<String>.bind(textView: TextView) {
    onChange { textView.setText(it) }
}