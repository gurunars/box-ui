package com.gurunars.example

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import com.gurunars.box.core.ObservableValue
import com.gurunars.box.ui.BaseDeclarativeActivity
import com.gurunars.box.ui.components.MenuItem
import com.gurunars.box.ui.components.Page
import com.gurunars.box.ui.components.pagedViewWithNavigation
import com.gurunars.box.ui.matchParent
import com.gurunars.box.ui.textView

class ActivityPagedView: BaseDeclarativeActivity() {

    enum class Item(
        override val id: Int,
        override val title: Int,
        override val icon: Int
    ): MenuItem {
        ONE(
            id = R.id.one,
            title = R.string.one,
            icon = R.drawable.one
        ),
        TWO(
            id = R.id.two,
            title = R.string.two,
            icon = R.drawable.two
        )
    }

    private val one by lazy {
        textView {
            background =
                ColorDrawable(Color.CYAN)
            text = "ONE"
            gravity = Gravity.CENTER
        }
    }

    private val two by lazy {
        textView {
            background =
                ColorDrawable(Color.YELLOW)
            text = "TWO"
            gravity = Gravity.CENTER
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val page = ObservableValue(Item.ONE)

        pagedViewWithNavigation(
            currentPage = page,
            pages = listOf(
                Page(Item.ONE, one),
                Page(Item.TWO, two)
            )
        ).layoutParams {
            matchParent()
        }

    }

}