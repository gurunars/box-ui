package com.gurunars.functional.example

import android.app.Activity
import android.os.Bundle
import com.gurunars.functional.ui

class ActivityMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui()
    }
}