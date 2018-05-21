package com.gurunars.example

import android.content.Intent
import android.os.Bundle
import com.gurunars.box.core.*
import com.gurunars.box.ui.*
import com.gurunars.box.ui.components.listview.*


class ActivityMain : BaseDeclarativeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        staticListView(items = listOf(
            "Float Menu" to ActivityFloatMenu::class,
            "List View" to ActivityListView::class,
            "Selectable List View" to ActivitySelectableListView::class,
            "Edit Pin Code" to ActivityEditPinCode::class,
            "Paged View" to ActivityPagedView::class,
            "List Controller" to ActivityListController::class,
            "onClick" to ActivityClicks::class
        )) {
            itemView {
                textView {
                    padding = Bounds(dip(10))
                    textSize = 18f
                    text_ = it.bind { first }
                    onClick {
                        startActivity(Intent().apply {
                            setClass(this@ActivityMain, it.get().second.java)
                        })
                    }
                    setBorders(Bounds(bottom=1))
                }
            }
        }.layoutParams {
            matchParent()
        }
    }

}