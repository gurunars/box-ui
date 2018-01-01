package com.gurunars.android_utils.example

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.Gravity
import com.gurunars.android_utils.autoButton
import com.gurunars.databinding.android.add
import com.gurunars.databinding.android.fullSize
import com.gurunars.databinding.android.onClick
import com.gurunars.databinding.android.setAsOne
import com.gurunars.databinding.android.statefulView
import com.gurunars.databinding.android.txt
import com.gurunars.databinding.box
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.below
import org.jetbrains.anko.dip
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.margin
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.textView

class ActivityMain : Activity() {

    private val title = "Empty".box

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        statefulView(R.id.main) {
            retain(title)
            relativeLayout {
                gravity = Gravity.CENTER
                fullSize()
                textView {
                    id = R.id.payloadView
                    padding = dip(15)
                    gravity = Gravity.CENTER
                    backgroundColor = Color.parseColor("#FFFFAA")
                    txt(title)
                }.lparams {
                    width = matchParent
                    margin = dip(10)
                }

                linearLayout {
                    gravity = Gravity.CENTER

                    autoButton(
                        text = getString(R.string.disabled).box,
                        bgColor = getColor(android.R.color.holo_blue_light).box
                    ).add(this) {
                        id = R.id.disabled
                        isEnabled = false
                    }

                    autoButton(
                        text = getString(R.string.set).box,
                        bgColor = Color.YELLOW.box,
                        shape = OvalShape().box
                    ).add(this) {
                        id = R.id.set
                        onClick { title.set("Configured") }
                    }

                    autoButton(
                        text = getString(R.string.clear).box,
                        bgColor = getColor(android.R.color.holo_green_light).box
                    ).add(this) {
                        id = R.id.clear
                        onClick { title.set("Empty") }
                    }
                }.lparams {
                    width = matchParent
                    below(R.id.payloadView)
                }
            }.setAsOne(this)
        }.setAsOne(this)
    }
}