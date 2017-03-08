package com.gurunars.crud_item_list;

import android.view.Menu;
import android.view.MenuItem;

import com.gurunars.item_list.Item;

import java.util.List;
import java.util.Set;

final class ActionDelete<ItemType extends Item> implements Action<ItemType> {

    @Override
    public MenuItem configureMenuItem(Menu menu) {
        return null;
    }

    @Override
    public void perform(List<ItemType> all, Set<ItemType> selectedItems) {
        all.removeAll(selectedItems);
    }

    @Override
    public boolean canPerform(List<ItemType> all, Set<ItemType> selectedItems) {
        return selectedItems.size() > 0;
    }

}
