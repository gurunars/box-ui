package com.gurunars.animal_item

import android.content.Context
import android.view.Gravity
import android.view.View
import com.gurunars.box.IRoBox
import com.gurunars.box.oneWayBranch
import com.gurunars.box.ui.*
import org.jetbrains.anko.*

fun Context.bindAnimal(field: IRoBox<AnimalItem>): View = linearLayout {
    gravity = Gravity.CENTER_VERTICAL
    imageView().apply {
        resource(field.oneWayBranch { type.resourceId })
        padding = dip(5)
    }.lparams {
        width = dip(25)
        height = dip(25)
    }
    textView {
        id=R.id.title
        asRow()
        padding = context.dip(5)
        text(field.oneWayBranch { toString() })
    }
    setBorders(Spec(bottom=1))
}