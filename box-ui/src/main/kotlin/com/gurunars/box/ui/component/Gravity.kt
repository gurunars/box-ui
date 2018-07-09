package com.gurunars.box.ui.component

import android.view.Gravity as AndroidGravity

enum class Gravity(
    val value: Int
) {
    LEFT(AndroidGravity.LEFT), RIGHT(AndroidGravity.RIGHT), CENTER(AndroidGravity.CENTER)
}