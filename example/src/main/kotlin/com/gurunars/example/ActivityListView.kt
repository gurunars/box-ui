package com.gurunars.example

import android.os.Bundle
import com.gurunars.box.core.ObservableValue
import com.gurunars.box.ui.*
import com.gurunars.box.ui.components.listview.itemView
import com.gurunars.box.ui.components.listview.listView
import com.gurunars.box.ui.components.listview.simpleListView

class ActivityListView : BaseDeclarativeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val items = ObservableValue(
            listOf(
                "ONE", "TWO", "THREE", "FOUR", "FIVE"
            )
        )

        simpleListView(items) {
            itemView {
                textView {
                    padding = Bounds(dip(10))
                    textSize = 18f
                    text_ = it
                    setBorders(Bounds(bottom = 2))
                }
            }
        }.layoutParams {
            matchParent()
        }
    }

}