package com.gurunars.item_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

class ItemViewBinderFooter<ItemType extends Item> implements ItemViewBinder<ItemType> {

    static final int FOOTER_TYPE = -42;

    //http://stackoverflow.com/questions/30866106/android-add-spacing-below-last-element-in-recyclerview-with-gridlayoutmanager

    @Override
    public View getView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.footer, null);
        view.setTag(FOOTER_TYPE, FOOTER_TYPE);
        return view;
    }

    @Override
    public void bind(View itemView, ItemType item, ItemType previousItem) {

    }
}
