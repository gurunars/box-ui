package com.gurunars.functional.example

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.facebook.soloader.SoLoader
import com.facebook.yoga.android.YogaLayout
import com.gurunars.box.ui.*

fun View.layout(
    yogaLayout: YogaLayout,
    config: YogaLayout.LayoutParams.() -> Unit
) = layout(yogaLayout, config, YogaLayout.LayoutParams(
    ViewGroup.LayoutParams.WRAP_CONTENT,
    ViewGroup.LayoutParams.WRAP_CONTENT
))


class ActivityMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoLoader.init(this, false);

        with<YogaLayout> {
            (0..10).forEach {
                with<TextView> {
                    text="Foo $it"
                }.layout(this)
            }

            val parent = this
            with<Button> {
                text="Remove"
                onClick {
                    parent.removeView(parent.getChildAt(0))
                }
            }.layout(this)
        }.layoutAsOne(this)
    }
}