package com.gurunars.functional.example

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.facebook.yoga.android.YogaLayout
import com.gurunars.box.ui.*
import com.gurunars.box.ui.decorators.statefulLayer

fun View.layout(
    relativeLayout: YogaLayout,
    config: YogaLayout.LayoutParams.() -> Unit
) = layout(relativeLayout, config, YogaLayout.LayoutParams(
    ViewGroup.LayoutParams.WRAP_CONTENT,
    ViewGroup.LayoutParams.WRAP_CONTENT
))

class ActivityMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        statefulLayer(R.id.main) {
            with<YogaLayout> {
                fullSize()

                (0..10).forEach {
                    with<TextView> {
                        text="Foo $it"
                    }
                }

                val parent = this
                with<Button> {
                    text="Remove"
                    onClick {
                        parent.removeView(parent.getChildAt(0))
                    }
                }.layout(this)

            }
        }.layoutAsOne(this)
    }
}