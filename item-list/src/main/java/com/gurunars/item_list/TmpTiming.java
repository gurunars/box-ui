package com.gurunars.item_list;

import android.util.Log;

public class TmpTiming {

    private long timing;

    public TmpTiming() {
        timing = System.nanoTime();
    }

    public void tick(String name) {
        long newTiming = System.nanoTime();
        Log.e("TIMING", name + " | " + (newTiming - timing));
        timing = System.nanoTime();
    }

}
