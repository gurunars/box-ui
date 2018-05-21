package com.gurunars.animal_item

import android.content.Context
import android.view.Gravity
import android.view.View
import com.gurunars.box.core.IRoBox
import com.gurunars.box.core.bind
import com.gurunars.box.ui.Bounds
import com.gurunars.box.ui.asRow
import com.gurunars.box.ui.resource
import com.gurunars.box.ui.setBorders
import com.gurunars.box.ui.text
import org.jetbrains.anko.*

fun Context.bindAnimal(field: IRoBox<AnimalItem>): View = linearLayout {
    gravity = Gravity.CENTER_VERTICAL
    imageView().apply {
        resource(field.bind { type.resourceId })
        padding = dip(5)
    }.lparams {
        width = dip(25)
        height = dip(25)
    }
    textView {
        id=R.id.title
        asRow()
        padding = dip(5)
        text(field.bind { toString() })
    }
    setBorders(Bounds(bottom=1))
}