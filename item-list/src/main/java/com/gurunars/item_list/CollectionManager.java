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

    private List<ItemType> items = new ArrayList<>();
    private Set<ItemType> selectedItems = new HashSet<>();

    private final Consumer<List<Item<SelectableItem<ItemType>>>> stateChangeHandler;
    private final Runnable selectionChangeListener;

    CollectionManager(
            Consumer<List<Item<SelectableItem<ItemType>>>> stateChangeHandler,
            Runnable selectionChangeListener ) {
        this.stateChangeHandler = stateChangeHandler;
        this.selectionChangeListener = selectionChangeListener;
        this.kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    }

    private void changed(List<ItemType> newItems, Set<ItemType> newSelection) {
        this.items = newItems;
        Set<ItemType> filteredSelection = new HashSet<>(newSelection);
        for (ItemType cursor: filteredSelection) {
            newSelection.remove(cursor);
            if (items.contains(cursor)) {
                // This is to replace the item's payload with a new one
                newSelection.add(items.get(items.indexOf(cursor)));
            }
        }
        boolean selectionChanged = !selectedItems.equals(filteredSelection);
        selectedItems = newSelection;
        List<Item<SelectableItem<ItemType>>> selectableItems = new ArrayList<>();
        for (ItemType item: items) {
            selectableItems.add(new Item<>(item.getId(),
                    new SelectableItem<>(item.getPayload(), selectedItems.contains(item))));
        }
        stateChangeHandler.accept(selectableItems);
        if (selectionChanged) {
            selectionChangeListener.run();
        }
    }

    private ItemType getItem(Item<SelectableItem<ItemType>> item) {
        return new Item<>(item.getId(), item.getPayload().getPayload());
    }

    void itemClick(Item<SelectableItem<ItemType>> selectableItem) {
        if (selectedItems.size() == 0) {
            return;
        }

        ItemType item = getItem(selectableItem);

        Set<ItemType> newSelectedItems = new HashSet<>(selectedItems);

        if (newSelectedItems.contains(item)) {
            newSelectedItems.remove(item);
        } else {
            newSelectedItems.add(item);
        }
        changed(items, newSelectedItems);
    }

    void itemLongClick(Item<SelectableItem<ItemType>>selectableItem) {
        if (selectedItems.size() != 0) {
            return;
        }
        Set<ItemType> newSelectedItems = new HashSet<>(selectedItems);
        newSelectedItems.add(getItem(selectableItem));
        changed(items, newSelectedItems);
    }

    void setItems(List<ItemType> items) {
        changed(kryo.copy(new ArrayList<>(items)), kryo.copy(new HashSet<>(selectedItems)));
    }

    void setSelectedItems(Set<ItemType> selectedItems) {
        changed(items, kryo.copy(new HashSet<>(selectedItems)));
    }

    Set<ItemType> getSelectedItems() {
        return kryo.copy(selectedItems);
    }

}
