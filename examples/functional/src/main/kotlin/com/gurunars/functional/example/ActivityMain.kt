package com.gurunars.functional.example

import android.app.Activity
import android.content.Context
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


// This hack with dummy layout is needed because Yoga does not allow
// to create the layouts from code directly
fun Context.yogaLayout(config: YogaLayout.() -> Unit) =
    (View.inflate(this, R.layout.yoga_layout, null) as YogaLayout).apply {
        config()
    }

class ActivityMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoLoader.init(this, false);

        yogaLayout {
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