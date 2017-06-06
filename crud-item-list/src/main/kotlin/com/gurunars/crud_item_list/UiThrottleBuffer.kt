package com.gurunars.crud_item_list

import android.os.Handler

/* Allows to buffer runnable execution to prevent spamming events of the same type. */
internal class UiThrottleBuffer {
    private val handler = Handler()
    private var currentCallback: Runnable? = null

    private fun cancel() {
        handler.removeCallbacks(currentCallback)
    }

    fun shutdown() {
        cancel()
        if (currentCallback != null) {
            currentCallback!!.run()
        }
    }

    fun call(runnable: Runnable) {
        cancel()
        currentCallback = Runnable {
            runnable.run()
            currentCallback = null
        }
        handler.postDelayed(currentCallback, TIMEOUT.toLong())
    }

    companion object {

        private val TIMEOUT = 500
    }

}
