package com.gurunars.item_list;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class ItemViewBinderEmpty implements EmptyViewBinder {

    static final int EMPTY_TYPE = -404;

    @Override
    public View getView(Context context) {
        TextView view = new TextView(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        view.setText(R.string.empty);
        view.setGravity(Gravity.CENTER);
        return view;
    }

}
