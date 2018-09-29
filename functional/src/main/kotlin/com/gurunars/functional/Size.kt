package com.gurunars.functional

import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT

sealed class Size
object MatchParent : Size()
object WrapContent : Size()
data class dp(val size: Int) : Size()
data class sp(val size: Int) : Size()
data class px(val size: Int) : Size()

val Int.dp
    get() = dp(this)

val Int.sp
    get() = sp(this)

val Int.px
    get() = px(this)

fun Context.toInt(size: Size): Int =
    when (size) {
        is MatchParent -> MATCH_PARENT
        is WrapContent -> WRAP_CONTENT
        is dp -> (size.size * resources.displayMetrics.density).toInt()
        is sp -> (size.size * resources.displayMetrics.scaledDensity).toInt()
        is px -> size.size
    }

