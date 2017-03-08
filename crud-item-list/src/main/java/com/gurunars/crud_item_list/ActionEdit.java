package com.gurunars.crud_item_list;

import android.view.Menu;
import android.view.MenuItem;

import com.esotericsoftware.kryo.Kryo;
import com.gurunars.item_list.Item;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import java8.util.function.Consumer;

final class ActionEdit<ItemType extends Item> implements Action<ItemType> {

    private Kryo kryo = new Kryo();
    private Consumer<ItemType> itemConsumer;

    public ActionEdit(Consumer<ItemType> itemConsumer) {
        this.itemConsumer = itemConsumer;
    }

    @Override
    public MenuItem configureMenuItem(Menu menu) {
        return null;
    }

    @Override
    public void perform(List<ItemType> all, Set<ItemType> selectedItems) {
        Iterator<ItemType> iterator = selectedItems.iterator();
        if (iterator.hasNext()) {
            itemConsumer.accept(kryo.copy(iterator.next()));
        }
    }

    @Override
    public boolean canPerform(List<ItemType> all, Set<ItemType> selectedItems) {
        return selectedItems.size() == 1;
    }

}
