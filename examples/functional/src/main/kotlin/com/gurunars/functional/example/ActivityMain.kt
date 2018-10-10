package com.gurunars.functional.example

import android.app.Activity
import android.os.Bundle
import com.gurunars.box.Box
import com.gurunars.functional.ui

class ActivityMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val state = Box("one")

        ui(state) { this }
    }
}