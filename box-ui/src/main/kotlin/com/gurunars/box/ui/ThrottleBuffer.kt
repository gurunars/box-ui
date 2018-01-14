package com.gurunars.box.ui

import android.os.Handler
import android.os.Looper

/**
 * Allows to buffer runnable execution to prevent spamming events of the same type.
 *
 * @param timeout delay between the last invocation of the callback and the actual execution
 *                of it if no new invocations follow
 */
class ThrottleBuffer(
    private val timeout: Long = 500L
) {
    private val handler = Handler(Looper.getMainLooper())
    private var currentCallback: (() -> Unit)? = null

    private fun cancel() {
        handler.removeCallbacksAndMessages(null)
    }

    /** Finalizes throttle buffer execution. */
    fun shutdown() {
        cancel()
        currentCallback?.invoke()
    }

    /** Schedules invocation of a function within a throttle buffer. */
    fun call(runnable: () -> Unit) {
        cancel()
        currentCallback = {
            runnable()
            currentCallback = null
        }
        handler.postDelayed(currentCallback, timeout)
    }
}