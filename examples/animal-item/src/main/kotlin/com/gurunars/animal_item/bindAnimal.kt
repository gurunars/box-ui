package com.gurunars.animal_item

import android.content.Context
import android.view.View
import android.widget.TextView
import com.gurunars.box.IBox
import com.gurunars.box.ui.asRow
import com.gurunars.box.ui.txt
import com.gurunars.box.branch
import org.jetbrains.anko.dip
import org.jetbrains.anko.padding

fun Context.bindAnimal(field: IBox<AnimalItem>): View = TextView(this).apply {
    asRow()
    padding = context.dip(5)
    txt(field.branch { toString() })
}