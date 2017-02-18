package com.gurunars.android_utils.example;

import android.support.annotation.Keep;

import java.io.Serializable;

class TestPayload implements Serializable {
    String title;

    TestPayload(String title) {
        this.title = title;
    }
}

