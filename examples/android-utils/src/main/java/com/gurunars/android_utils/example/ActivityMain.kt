package com.gurunars.android_utils.example

import android.graphics.Color
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.gurunars.android_utils.AutoBg
import com.gurunars.android_utils.ColoredShapeDrawable
import com.gurunars.test_utils.storage.Assignable
import com.gurunars.test_utils.storage.PersistentStorage
import org.jetbrains.anko.*


class ActivityMain : AppCompatActivity() {

    class State : Assignable<State>() {
        var title="Empty"
    }

    private lateinit var payloadView: TextView
    private lateinit var storage: PersistentStorage<State>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        relativeLayout {
            gravity=Gravity.CENTER
            layoutParams=ViewGroup.LayoutParams(matchParent, matchParent)
            payloadView=textView {
                id=R.id.payloadView
                padding=dip(15)
                gravity=Gravity.CENTER
                backgroundColor=Color.parseColor("#FFFFAA")
            }.lparams {
                width=matchParent
                margin=dip(10)
            }

            linearLayout {
                gravity=Gravity.CENTER

                textView {
                    isEnabled=false
                    backgroundColor=ContextCompat.getColor(
                            this@ActivityMain,
                            android.R.color.holo_blue_light)
                    text=getString(R.string.disabled)
                }.lparams()

                textView {
                    background=ColoredShapeDrawable(OvalShape(), Color.YELLOW)
                    text=getString(R.string.set)
                    setOnClickListener { storage.patch { title="Configured" } }
                }.lparams()

                textView {
                    backgroundColor=ContextCompat.getColor(
                            this@ActivityMain,
                            android.R.color.holo_green_light)
                    text=getString(R.string.clear)
                    setOnClickListener { storage.clear() }
                }.lparams()

            }.lparams {
                width=matchParent
                below(payloadView)
            }.applyRecursively { view -> when(view) {
                is TextView -> { view.apply {
                    isClickable=true
                    isFocusable=true
                    padding=dip(20)
                    AutoBg.apply(this, 6)
                } }
            } }
        }

        storage = PersistentStorage(this, "payloadAlias", State(), { payloadView.text = it.title })

    }

}