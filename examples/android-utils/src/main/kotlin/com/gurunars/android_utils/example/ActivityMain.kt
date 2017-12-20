package com.gurunars.android_utils.example

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.graphics.Color
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import com.gurunars.android_utils.autoButton
import com.gurunars.box.Box
import com.gurunars.box.ui.BoxActivity
import com.gurunars.box.ui.onClick
import com.gurunars.box.inContext
import com.gurunars.databinding.android.add
import com.gurunars.databinding.android.fullSize
import com.gurunars.databinding.android.setAsOne
import com.gurunars.databinding.android.statefulView
import com.gurunars.databinding.android.txt
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.below
import org.jetbrains.anko.dip
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.margin
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.textView

class ActivityMain : BoxActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inContext {
            statefulView(R.id.main) {
                val title = "".box
                retain(title)
                relativeLayout {
                    gravity = Gravity.CENTER
                    fullSize()
                    textView {
                        id = R.id.payloadView
                        padding = dip(15)
                        gravity = Gravity.CENTER
                        backgroundColor = Color.parseColor("#FFFFAA")
                        inContext(this@textView) {
                            txt(title)
                        }
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
                            shape = Box<Shape>(OvalShape()),
                            bgColor = Color.YELLOW.box
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
                }
            }.setAsOne(this@ActivityMain)
        }
    }

}