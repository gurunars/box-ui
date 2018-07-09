package com.gurunars.box.ui.component.text

import com.gurunars.box.ui.component.Color
import com.gurunars.box.ui.component.Size
import com.gurunars.box.ui.component.Gravity
import com.gurunars.box.ui.component.sp

data class TextAppearence(
    val textStyle: Set<TextStyle> = setOf(),
    val color: Color = Color("#000000"),
    val size: Size = 12.sp,
    val gravity: Gravity = Gravity.LEFT
) {
    enum class TextStyle {
        BOLD, ITALIC, UNDERLINE, STRIKE_THROUGH
    }
}