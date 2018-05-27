package com.gurunars.android_utils.example

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.gurunars.android_utils.autoButton
import com.gurunars.box.box
import com.gurunars.box.ui.*

class ActivityMain : Activity() {

    private val title = "Empty".box

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        statefulView(R.id.main) {
            retain(title)
            with<RelativeLayout> {
                gravity = Gravity.CENTER
                fullSize()
                with<TextView> {
                    id = R.id.payloadView
                    padding = Bounds(dip(15))
                    gravity = Gravity.CENTER
                    setBackgroundColor(Color.parseColor("#FFFFAA"))
                    text(title)
                }.layout(this) {
                    asRow()
                    margin = Bounds(dip(10))
                }

                with<LinearLayout> {
                    gravity = Gravity.CENTER

                    autoButton(
                        text = getString(R.string.disabled).box,
                        bgColor = getColor(android.R.color.holo_blue_light).box
                    ).apply {
                        id = R.id.disabled
                        isEnabled = false
                    }.layout(this)

                    autoButton(
                        text = getString(R.string.set).box,
                        bgColor = Color.YELLOW.box,
                        shape = OvalShape().box
                    ).apply {
                        id = R.id.set
                        onClick { title.set("Configured") }
                    }.layout(this)

                    autoButton(
                        text = getString(R.string.clear).box,
                        bgColor = getColor(android.R.color.holo_green_light).box
                    ).apply {
                        id = R.id.clear
                        onClick { title.set("Empty") }
                    }.layout(this)
                }.layout(this) {
                    asRow()
                    alignWithRespectTo(R.id.payloadView, verticalPosition = VerticalPosition.BELOW)
                }
            }.layoutAsOne(this)
        }.layoutAsOne(this)
    }
}