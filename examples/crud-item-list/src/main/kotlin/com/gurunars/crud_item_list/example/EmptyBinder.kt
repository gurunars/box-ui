package com.gurunars.crud_item_list.example

import android.content.Context
import android.view.LayoutInflater
import android.view.View

import com.gurunars.item_list.EmptyViewBinder

internal class EmptyBinder : EmptyViewBinder {

    override fun getView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.completely_empty_list, null)
    }

}
