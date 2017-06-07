package com.gurunars.databinding.views

import android.widget.TextView
import com.gurunars.databinding.SenderField


fun SenderField<String>.sendTo(textView: TextView) {
    bind { textView.setText(it) }
}