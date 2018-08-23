package com.gurunars.functional.example

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.facebook.soloader.SoLoader
import com.facebook.yoga.YogaNode
import com.facebook.yoga.android.YogaLayout
import com.gurunars.box.ui.*

class ActivityMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoLoader.init(this, false);

        with<YogaLayout> {

            (0..10).forEach {
                with<TextView> {
                    text="Foo $it"
                }.let { addView(it, YogaNode().apply {
                    setWidth(100f)
                    flexGrow = 1f
                    setHeight(30f)
                }) }
            }

            val parent = this
            with<Button> {
                text="Remove"
                onClick {
                    parent.removeView(parent.getChildAt(0))
                }
            }.let { addView(it, YogaNode().apply {
                this.setWidthAuto()
                this.setHeightAuto()
            }) }
        }.layoutAsOne(this)
    }
}