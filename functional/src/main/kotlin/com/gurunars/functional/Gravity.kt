package com.gurunars.functional

import android.view.Gravity as AndroidGravity

enum class Gravity(
    val value: Int
) {
    LEFT(AndroidGravity.LEFT), RIGHT(AndroidGravity.RIGHT), CENTER(AndroidGravity.CENTER)
}