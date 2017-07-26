package com.gurunars.databinding.android.thin

import android.content.Context
import android.view.View

interface Component {
    fun render(context: Context) : View
}