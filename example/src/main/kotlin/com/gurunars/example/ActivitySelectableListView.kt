package com.gurunars.example

import android.graphics.Color
import android.os.Bundle
import com.gurunars.box.core.ObservableValue
import com.gurunars.box.core.bind
import com.gurunars.box.ui.*
import com.gurunars.box.ui.components.listview.Wrapped
import com.gurunars.box.ui.components.listview.itemView
import com.gurunars.box.ui.components.listview.selectableListView

class ActivitySelectableListView : BaseDeclarativeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val items = ObservableValue(
            listOf(
                "ONE", "TWO", "THREE", "FOUR", "FIVE"
            ).mapIndexed { index, s -> Wrapped(index, s) }
        )

        val selected = ObservableValue(setOf<Any>())

        selectableListView(
            items = items,
            selectedItems = selected
        ) {
            itemView {
                textView {
                    padding = Bounds(dip(10))
                    textSize = 18f
                    text_ = it.bind { item.payload }
                    it.bind { isSelected }.onChange {
                        background = if (it) {
                            Color.RED
                        } else {
                            Color.TRANSPARENT
                        }.drawable
                        setBorders(Bounds(bottom = 2))
                    }
                }
            }
        }.layoutParams {
            matchParent()
        }
    }

}