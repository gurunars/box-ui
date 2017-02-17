package com.gurunars.crud_item_list;

import android.os.Handler;

import java.util.Random;

/* Executes the runnable every N seconds till it is stopped. */
class ScheduledRunner {

    private static final int INITIAL_PAUSE = 500;
    private static final int PAUSE = 200;
    private final Handler handler = new Handler();
    private Runnable currentCallback;
    private int pause=INITIAL_PAUSE;
    private final Random random = new Random();
    private int executionId;

    public void stop() {
        handler.removeCallbacks(currentCallback);
        pause = INITIAL_PAUSE;
        currentCallback = null;
    }

    private void runIteration(final int currentExecutionId) {
        if (currentCallback == null || currentExecutionId != executionId) {
            return;
        }
        currentCallback.run();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runIteration(currentExecutionId);
            }
        }, pause);
        pause = PAUSE;
    }

    public void start(Runnable runnable) {
        currentCallback = runnable;
        executionId = random.nextInt();
        runIteration(executionId);
    }

}
