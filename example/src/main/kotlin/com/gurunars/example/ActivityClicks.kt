package com.gurunars.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import com.gurunars.box.ui.*

class ActivityClicks: BaseDeclarativeActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {

            verticalLayout {

                button {
                    text = "Click Me Single!"
                    onClick(ClickEvent.SINGLE) {
                        showToast("Single click")
                    }
                }.layoutParams {
                    wrapContent()
                }

                button {
                    text = "Click Me Double!"
                    onClick(ClickEvent.DOUBLE) {
                        showToast("Double click")
                    }

                    padding = Bounds(dip(40))
                }.layoutParams {
                    wrapContent()
                    margin = Bounds(top=dip(50))
                }
            }.layoutParams {
                wrapContent()
                gravity = Gravity.CENTER
            }

        }.layoutParams {
            matchParent()
        }

    }

}