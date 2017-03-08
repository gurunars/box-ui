package com.gurunars.crud_item_list;

import android.view.Menu;
import android.view.MenuItem;

import com.gurunars.item_list.Item;

import java.util.List;
import java.util.Set;

public interface Action<ItemType extends Item> {

    MenuItem configureMenuItem(Menu menu);
    void perform(List<ItemType> all, Set<ItemType> selectedItems);
    boolean canPerform(List<ItemType> all, Set<ItemType> selectedItems);

}
