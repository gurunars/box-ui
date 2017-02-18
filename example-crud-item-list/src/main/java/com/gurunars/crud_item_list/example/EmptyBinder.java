package com.gurunars.crud_item_list.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.gurunars.item_list.EmptyViewBinder;

class EmptyBinder implements EmptyViewBinder {

    @Override
    public View getView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.completely_empty_list, null);
    }

}
