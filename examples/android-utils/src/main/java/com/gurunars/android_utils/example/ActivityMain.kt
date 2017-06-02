package com.gurunars.android_utils.example

import android.graphics.Color
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.TextView
import com.gurunars.android_utils.AutoBg
import com.gurunars.android_utils.ColoredShapeDrawable
import com.gurunars.shortcuts.color
import com.gurunars.shortcuts.fullSize
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*


class ActivityMain : AppCompatActivity() {

    private val storage = PersistentStorage(this, "main")

    private val title = storage.storageField("title", "Empty")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        relativeLayout {
            gravity=Gravity.CENTER
            fullSize()
            textView {
                id=R.id.payloadView
                padding=dip(15)
                gravity=Gravity.CENTER
                backgroundColor=Color.parseColor("#FFFFAA")
                title.bind { text = it }
            }.lparams {
                width=matchParent
                margin=dip(10)
            }

            linearLayout {
                gravity=Gravity.CENTER

                textView {
                    isEnabled=false
                    backgroundColor=color(android.R.color.holo_blue_light)
                    text=getString(R.string.disabled)
                }.lparams()

                textView {
                    background=ColoredShapeDrawable(OvalShape(), Color.YELLOW)
                    text=getString(R.string.set)
                    setOnClickListener { title.set("Configured") }
                }.lparams()

                textView {
                    backgroundColor=color(android.R.color.holo_green_light)
                    text=getString(R.string.clear)
                    setOnClickListener { title.set("Empty") }
                }.lparams()

            }.lparams {
                width=matchParent
                below(R.id.payloadView)
            }.applyRecursively { view -> when(view) {
                is TextView -> { view.apply {
                    isClickable=true
                    isFocusable=true
                    padding=dip(20)
                    AutoBg.apply(this, 6)
                } }
            } }
        }

        storage.load()

    }

}