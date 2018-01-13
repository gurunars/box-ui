package com.gurunars.storybook.example

import android.app.Activity
import android.os.Bundle

class ActivityMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(sampleComponent())

    }
}