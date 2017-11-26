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
import com.gurunars.databinding.android.txt
import com.gurunars.databinding.box
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*


class ActivityMain : Activity() {

    private val storage = PersistentStorage(this, "main")

    private val title = storage.storageField("title", "Empty")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        storage.load()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        storage.unbindAll()
    }

}