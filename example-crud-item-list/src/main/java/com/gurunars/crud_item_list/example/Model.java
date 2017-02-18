package com.gurunars.crud_item_list.example;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

class Model {

    private final Activity activity;

    private List<AnimalItem> items = new ArrayList<>();
    private int maxItemId = 0;
    private boolean wasInited = false;

    Model(Activity activity) {
        this.activity = activity;
        load();
    }

    boolean wasInited() {
        return wasInited;
    }

    private void load() {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        wasInited = preferences.getBoolean("wasInited", false);
        maxItemId = preferences.getInt("maxItemId", 0);
        items = new Gson().fromJson(preferences.getString("data", "[]"),
                new TypeToken<ArrayList<AnimalItem>>(){}.getType());
    }

    List<AnimalItem> getItems() {
        return items;
    }

    void setItems(List<AnimalItem> items) {
        this.items = items;
        for (AnimalItem item: items) {
            if (item.getId() == 0) {
                maxItemId++;
                item.setId(maxItemId);
            } else {
                maxItemId = Math.max(maxItemId, (int) item.getId());
            }
            // At least version 1
            if (item.getVersion() == 0) {
                item.setVersion(1);
            }
        }
        save();
    }

    private void save() {
        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString("data", new Gson().toJson(items));
        editor.putInt("maxItemId", maxItemId);
        editor.putBoolean("wasInited", true);
        editor.apply();
    }

    int getMaxItemId() {
        return maxItemId;
    }

    void clear() {
        maxItemId = 0;
        items.clear();
    }
}
