package com.gurunars.box

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView

class SampleActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(TextView(this).apply {
            text = "FOO BAR"
            gravity = Gravity.CENTER
            textSize = 25f
        }, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
    }

}