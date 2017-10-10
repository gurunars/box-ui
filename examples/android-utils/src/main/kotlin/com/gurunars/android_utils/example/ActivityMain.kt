package com.gurunars.android_utils.example

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import com.gurunars.android_utils.ColoredShapeDrawable
import com.gurunars.android_utils.setAutoBg
import com.gurunars.databinding.android.text
import com.gurunars.shortcuts.color
import com.gurunars.shortcuts.fullSize
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
                text(title)
            }.lparams {
                width = matchParent
                margin = dip(10)
            }

            linearLayout {
                gravity = Gravity.CENTER

                textView {
                    id = R.id.disabled
                    isEnabled = false
                    backgroundColor = color(android.R.color.holo_blue_light)
                    text = getString(R.string.disabled)
                }.lparams()

                textView {
                    id = R.id.set
                    background = ColoredShapeDrawable(OvalShape(), Color.YELLOW)
                    text = getString(R.string.set)
                    setOnClickListener { title.set("Configured") }
                }.lparams()

                textView {
                    id = R.id.clear
                    backgroundColor = color(android.R.color.holo_green_light)
                    text = getString(R.string.clear)
                    setOnClickListener { title.set("Empty") }
                }.lparams()

            }.lparams {
                width = matchParent
                below(R.id.payloadView)
            }.applyRecursively { view ->
                when (view) {
                    is TextView -> {
                        view.apply {
                            isClickable = true
                            isFocusable = true
                            padding = dip(20)
                            setAutoBg(6)
                        }
                    }
                }
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