package com.gurunars.crud_item_list.example

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.ArrayList

internal class Model(private val activity: Activity) {

    private var items: MutableList<AnimalItem> = ArrayList()
    private var maxItemId = 0
    private var wasInited = false

    init {
        load()
    }

    fun wasInited(): Boolean {
        return wasInited
    }

    private fun load() {
        val preferences = activity.getPreferences(Context.MODE_PRIVATE)
        wasInited = preferences.getBoolean("wasInited", false)
        maxItemId = preferences.getInt("maxItemId", 0)
        items = Gson().fromJson<List<AnimalItem>>(preferences.getString("data", "[]"),
                object : TypeToken<ArrayList<AnimalItem>>() {

                }.type)
    }

    fun getItems(): List<AnimalItem> {
        return items
    }

    private fun save() {
        val editor = activity.getPreferences(Context.MODE_PRIVATE).edit()
        editor.putString("data", Gson().toJson(items))
        editor.putInt("maxItemId", maxItemId)
        editor.putBoolean("wasInited", true)
        editor.apply()
    }

    fun clear() {
        maxItemId = 0
        items.clear()
    }

    fun updateItem(item: AnimalItem) {
        for (i in items.indices) {
            val cursor = items[i]
            if (cursor.getId() == item.getId()) {
                items[i] = item
                save()
                return
            }
        }
    }

    fun createItem(payload: AnimalItem) {
        maxItemId++
        items.add(AnimalItem(maxItemId.toLong(), payload.version, payload.getType()))
        save()
    }
}
