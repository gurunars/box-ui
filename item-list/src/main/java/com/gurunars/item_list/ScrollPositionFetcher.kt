package com.gurunars.item_list

internal class ScrollPositionFetcher {

    fun getScrollPosition(position: Int, scroller: Scroller, items: List<*>): Int {
        var newPosition = position
        val upperVisibilityThreshold = scroller.findFirstVisibleItemPosition() + 3
        val lowerVisibilityThreshold = scroller.findLastVisibleItemPosition() - 3

        if (upperVisibilityThreshold < newPosition && newPosition < lowerVisibilityThreshold) {
            return -1  // No change
        }

        newPosition += if (newPosition <= upperVisibilityThreshold) -3 else +3
        return Math.min(Math.max(newPosition, 0), items.size - 1)
    }

}
