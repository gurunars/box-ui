package com.gurunars.functional

import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT

sealed class Size
object MatchParent : Size()
object WrapContent : Size()
data class dp(val size: Int) : Size()
data class sp(val size: Int) : Size()
data class pt(val size: Int) : Size()
data class px(val size: Int) : Size()
data class mm(val size: Int) : Size()

val Int.dp
    get() = dp(this)

val Int.sp
    get() = sp(this)

val Int.pt
    get() = pt(this)

val Int.px
    get() = px(this)

val Int.mm
    get() = mm(this)


fun Context.toInt(size: Size): Int =
    when (size) {
        is MatchParent -> MATCH_PARENT
        is WrapContent -> WRAP_CONTENT
        is dp -> 0
        is sp -> 0
        is pt -> 0
        is px -> 0
        is mm -> 0
    }

