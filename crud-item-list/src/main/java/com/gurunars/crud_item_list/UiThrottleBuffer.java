package com.gurunars.crud_item_list;

import android.os.Handler;

/* Allows to buffer runnable execution to prevent spamming events of the same type. */
class UiThrottleBuffer {

    private static final int TIMEOUT = 500;
    private final Handler handler = new Handler();
    private Runnable currentCallback;

    private void cancel() {
        handler.removeCallbacks(currentCallback);
    }

    void shutdown() {
        cancel();
        if (currentCallback != null) {
            currentCallback.run();
        }
    }

    void call(final Runnable runnable) {
        cancel();
        currentCallback = new Runnable() {
            @Override
            public void run() {
                runnable.run();
                currentCallback = null;
            }
        };
        handler.postDelayed(currentCallback, TIMEOUT);
    }

}
