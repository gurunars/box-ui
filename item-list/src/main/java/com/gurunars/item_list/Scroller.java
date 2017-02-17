package com.gurunars.item_list;

interface Scroller {
    void scrollToPosition(int position);
    int findFirstVisibleItemPosition();
    int findLastVisibleItemPosition();
}
