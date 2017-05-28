package com.gurunars.item_list

internal interface Scroller {
    fun scrollToPosition(position: Int)
    fun findFirstVisibleItemPosition(): Int
    fun findLastVisibleItemPosition(): Int
}
