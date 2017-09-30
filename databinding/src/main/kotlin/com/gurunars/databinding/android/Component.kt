package com.gurunars.databinding.android

import android.content.Context
import android.os.Parcelable
import android.widget.FrameLayout

abstract class Component(context: Context): FrameLayout(context) {

    /**
     * @suppress
     */
    final override fun onSaveInstanceState() = super.onSaveInstanceState()

    /**
     * @suppress
     */
    final override fun onRestoreInstanceState(state: Parcelable) = super.onRestoreInstanceState(state)
}