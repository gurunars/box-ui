package com.gurunars.item_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

class ItemViewBinderEmpty implements EmptyViewBinder {

    static final int EMPTY_TYPE = -404;

    @Override
    public View getView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.empty, null);
    }

}
