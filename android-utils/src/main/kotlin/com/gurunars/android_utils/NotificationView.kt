package com.gurunars.android_utils

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import com.gurunars.databinding.bindableField

/**
 * A text view which is visible only if it has a notification to show. The notification can be
 * cleared by long clicking the view.
 */
class NotificationView(context: Context): TextView(context) {

    val notification = bindableField("")

    init {
        isClickable = true
        notification.onChange {
            if (it.isEmpty()) {
                visibility=View.GONE
            } else {
                visibility=View.VISIBLE
                this.text = it
            }
        }
        setOnLongClickListener {
            notification.set("")
            true
        }
    }

    /**
     * @suppress
     */
    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putParcelable("superState", super.onSaveInstanceState())
            putString("notification", notification.get())
        }
    }

    /**
     * @suppress
     */
    override fun onRestoreInstanceState(state: Parcelable) {
        val localState = state as Bundle
        super.onRestoreInstanceState(localState.getParcelable("superState"))
        notification.set(localState.getString("notification"))
    }

}