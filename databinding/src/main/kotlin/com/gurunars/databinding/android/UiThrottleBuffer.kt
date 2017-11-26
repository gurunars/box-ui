package com.gurunars.databinding.android


import android.os.Handler
import android.os.Looper

typealias Run = () -> Unit

/* Allows to buffer runnable execution to prevent spamming events of the same type. */
class UiThrottleBuffer(
    private val timeout: Long = 500L
) {
    private val handler = Handler(Looper.getMainLooper())
    private var currentCallback: Run? = null

    fun cancel() {
        handler.removeCallbacksAndMessages(null)
    }

    fun shutdown() {
        cancel()
        currentCallback?.invoke()
    }

    fun call(runnable: Run) {
        cancel()
        currentCallback = {
            runnable()
            currentCallback = null
        }
        handler.postDelayed(currentCallback, timeout)
    }
}