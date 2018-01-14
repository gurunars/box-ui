package com.gurunars.animal_item

import android.content.Context
import android.view.View
import android.widget.TextView
import com.gurunars.box.IRoBox
import com.gurunars.box.oneWayBranch
import com.gurunars.box.ui.asRow
import com.gurunars.box.ui.text
import org.jetbrains.anko.dip
import org.jetbrains.anko.padding

fun Context.bindAnimal(field: IRoBox<AnimalItem>): View = TextView(this).apply {
    asRow()
    padding = context.dip(5)
    text(field.oneWayBranch { toString() })
}