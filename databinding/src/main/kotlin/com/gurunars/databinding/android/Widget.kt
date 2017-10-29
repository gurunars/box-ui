package com.gurunars.databinding.android

import android.content.Context
import android.os.Parcelable
import android.widget.FrameLayout

/**
 * A base component meant to develop custom stateless UI widgets using bindable fields
 */
abstract class Widget(context: Context): FrameLayout(context) {

    /**
     * @suppress
     */
    final override fun onSaveInstanceState(): Parcelable = super.onSaveInstanceState()

    /**
     * @suppress
     */
    final override fun onRestoreInstanceState(state: Parcelable) = super.onRestoreInstanceState(state)
}