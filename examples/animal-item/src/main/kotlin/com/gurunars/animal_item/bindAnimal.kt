package com.gurunars.animal_item

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gurunars.box.IRoBox
import com.gurunars.box.oneWayBranch
import com.gurunars.box.ui.*

fun Context.bindAnimal(field: IRoBox<AnimalItem>): View = with<LinearLayout> {
    gravity = Gravity.CENTER_VERTICAL
    with<ImageView> {
        resource(field.oneWayBranch { type.resourceId })
        padding = OldBounds(dip(5))
    }.layout(this) {
        width = dip(25)
        height = dip(25)
    }
    with<TextView> {
        id=R.id.title
        asRow()
        padding = OldBounds(dip(5))
        text(field.oneWayBranch { toString() })
    }.layout(this)
    setBorders(OldBounds(bottom=1))
}