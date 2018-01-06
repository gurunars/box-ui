package com.gurunars.file_picker

import android.content.Context
import android.view.Gravity
import org.jetbrains.anko.dip
import org.jetbrains.anko.imageView
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

fun Context.fileList(

) = verticalLayout {
    linearLayout {
        gravity= Gravity.CENTER_VERTICAL
        padding=dip(3)

        val parentIcon = imageView(R.drawable.ic_parent) {
            id=R.id.parentIcon


        }.lparams {
            width=dip(24)
            height=dip(24)
        }

        val currentPathText=textView {
            id=R.id.currentPathText
        }.lparams {
            width= matchParent
            leftMargin=dip(10)
        }
    }.lparams {
        width= matchParent
    }

}
