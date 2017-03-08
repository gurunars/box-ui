package com.gurunars.crud_item_list.example;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gurunars.item_list.Item;

import java.util.ArrayList;
import java.util.List;

class Model {

    private final Activity activity;

    private List<Item<AnimalPayload>> items = new ArrayList<>();
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
                new TypeToken<ArrayList<Item<AnimalPayload>>>(){}.getType());
    }

    List<Item<AnimalPayload>> getItems() {
        return items;
    }

    private void save() {
        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString("data", new Gson().toJson(items));
        editor.putInt("maxItemId", maxItemId);
        editor.putBoolean("wasInited", true);
        editor.apply();
    }

    void clear() {
        maxItemId = 0;
        items.clear();
    }

    void updateItem(Item<AnimalPayload> item) {
        for (int i = 0; i < items.size(); i++) {
            Item<AnimalPayload> cursor = items.get(i);
            if (cursor.getId() == item.getId()) {
                items.set(i, item);
                save();
                return;
            }
        }
    }

    void createItem(AnimalPayload payload) {
        maxItemId++;
        items.add(new Item<>(maxItemId, payload));
        save();
    }
}
