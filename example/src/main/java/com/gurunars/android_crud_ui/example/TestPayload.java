package com.gurunars.android_crud_ui.example;

import java.io.Serializable;

class TestPayload implements Serializable {
    String title;

    TestPayload(String title) {
        this.title = title;
    }
}

