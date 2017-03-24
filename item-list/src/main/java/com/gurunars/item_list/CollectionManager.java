package com.gurunars.item_list;

import com.esotericsoftware.kryo.Kryo;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java8.util.function.Consumer;


class CollectionManager<ItemType extends Item> implements Serializable {

    private Kryo kryo = new Kryo();

    private List<ItemHolder<ItemType>> items = new ArrayList<>();
    private Set<ItemHolder<ItemType>> selectedItems = new HashSet<>();

    private final Consumer<List<SelectableItem<ItemType>>> stateChangeHandler;
    private final Runnable selectionChangeListener;

    CollectionManager(
            Consumer<List<SelectableItem<ItemType>>> stateChangeHandler,
            Runnable selectionChangeListener ) {
        this.stateChangeHandler = stateChangeHandler;
        this.selectionChangeListener = selectionChangeListener;
        this.kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    }

    private void changed(List<ItemHolder<ItemType>> newItems, Set<ItemHolder<ItemType>> newSelection) {
        this.items = newItems;
        Set<ItemHolder<ItemType>> filteredSelection = new HashSet<>(newSelection);
        for (ItemHolder<ItemType> cursor: filteredSelection) {
            newSelection.remove(cursor);
            if (items.contains(cursor)) {
                // This is to replace the item's payload with a new one
                newSelection.add(items.get(items.indexOf(cursor)));
            }
        }
        boolean selectionChanged = !selectedItems.equals(filteredSelection);
        selectedItems = newSelection;
        List<SelectableItem<ItemType>> selectableItems = new ArrayList<>();
        for (ItemHolder<ItemType> item: items) {
            selectableItems.add(new SelectableItem<>(item.getRaw(), selectedItems.contains(item)));
        }
        stateChangeHandler.accept(selectableItems);
        if (selectionChanged) {
            selectionChangeListener.run();
        }
    }

    void itemClick(SelectableItem<ItemType> selectableItem) {
        if (selectedItems.size() == 0) {
            return;
        }

        ItemHolder<ItemType> item = new ItemHolder<>(selectableItem.getItem());

        Set<ItemHolder<ItemType>> newSelectedItems = new HashSet<>(selectedItems);

        if (newSelectedItems.contains(item)) {
            newSelectedItems.remove(item);
        } else {
            newSelectedItems.add(item);
        }
        changed(items, newSelectedItems);
    }

    void itemLongClick(SelectableItem<ItemType> selectableItem) {
        if (selectedItems.size() != 0) {
            return;
        }
        Set<ItemHolder<ItemType>> newSelectedItems = new HashSet<>(selectedItems);
        newSelectedItems.add(new ItemHolder<>(selectableItem.getItem()));
        changed(items, newSelectedItems);
    }

    void setItems(List<ItemType> items) {
        changed(ItemHolder.wrap(kryo.copy(new ArrayList<>(items))), kryo.copy(new HashSet<>(selectedItems)));
    }

    void setSelectedItems(Set<ItemType> selectedItems) {
        changed(items, ItemHolder.wrap(kryo.copy(new HashSet<>(selectedItems))));
    }

    Set<ItemType> getSelectedItems() {
        return ItemHolder.unwrap(kryo.copy(selectedItems));
    }

}
