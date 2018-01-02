package com.gurunars.box.ui

import android.os.Handler
import android.os.Looper

/* Allows to buffer runnable execution to prevent spamming events of the same type. */
class UiThrottleBuffer(
    private val timeout: Long = 500L
) {
    private val handler = Handler(Looper.getMainLooper())
    private var currentCallback: (() -> Unit)? = null

    fun cancel() {
        handler.removeCallbacksAndMessages(null)
    }

    fun shutdown() {
        cancel()
        currentCallback?.invoke()
    }

    fun call(runnable: () -> Unit) {
        cancel()
        currentCallback = {
            runnable()
            currentCallback = null
        }
        handler.postDelayed(currentCallback, timeout)
    }
}