package com.gurunars.crud_item_list

import android.os.Handler

/* Allows to buffer runnable execution to prevent spamming events of the same type. */
internal class UiThrottleBuffer {
    private val handler = Handler()
    private var currentCallback = {}

    private fun cancel() {
        handler.removeCallbacks(currentCallback)
    }

    fun shutdown() {
        cancel()
        currentCallback()
    }

    fun call(runnable: () -> Unit) {
        cancel()
        currentCallback = {
            runnable()
            currentCallback = {}
        }
        handler.postDelayed(currentCallback, TIMEOUT.toLong())
    }

    companion object {
        private val TIMEOUT = 500
    }

}
