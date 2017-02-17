package com.gurunars.item_list;

import java.util.List;

class ScrollPositionFetcher {

    int getScrollPosition(int position, Scroller scroller, List items) {
        int upperVisibilityThreshold = scroller.findFirstVisibleItemPosition() + 3;
        int lowerVisibilityThreshold = scroller.findLastVisibleItemPosition() - 3;

        if (upperVisibilityThreshold < position && position < lowerVisibilityThreshold) {
            return -1;  // No change
        }

        position += position <= upperVisibilityThreshold ? -3 : +3;
        return Math.min(Math.max(position, 0), items.size()-1);
    }

}
