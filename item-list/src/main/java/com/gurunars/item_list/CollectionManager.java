package com.gurunars.item_list;

import com.esotericsoftware.kryo.Kryo;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java8.util.function.Consumer;


class CollectionManager<PayloadType extends Payload> implements Serializable {

    private Kryo kryo = new Kryo();

    private List<Item<PayloadType>> items = new ArrayList<>();
    private Set<Item<PayloadType>> selectedItems = new HashSet<>();

    private final Consumer<List<Item<SelectablePayload<PayloadType>>>> stateChangeHandler;
    private final Runnable selectionChangeListener;

    CollectionManager(
            Consumer<List<Item<SelectablePayload<PayloadType>>>> stateChangeHandler,
            Runnable selectionChangeListener ) {
        this.stateChangeHandler = stateChangeHandler;
        this.selectionChangeListener = selectionChangeListener;
        this.kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    }

    private void changed(List<Item<PayloadType>> newItems, Set<Item<PayloadType>> newSelection) {
        this.items = newItems;
        Set<Item<PayloadType>> filteredSelection = new HashSet<>(newSelection);
        for (Item<PayloadType> cursor: filteredSelection) {
            newSelection.remove(cursor);
            if (items.contains(cursor)) {
                // This is to replace the item's payload with a new one
                newSelection.add(items.get(items.indexOf(cursor)));
            }
        }
        boolean selectionChanged = !selectedItems.equals(filteredSelection);
        selectedItems = newSelection;
        List<Item<SelectablePayload<PayloadType>>> selectableItems = new ArrayList<>();
        for (Item<PayloadType> item: items) {
            selectableItems.add(new Item<>(item.getId(),
                    new SelectablePayload<>(item.getPayload(), selectedItems.contains(item))));
        }
        stateChangeHandler.accept(selectableItems);
        if (selectionChanged) {
            selectionChangeListener.run();
        }
    }

    private Item<PayloadType> getItem(Item<SelectablePayload<PayloadType>> item) {
        return new Item<>(item.getId(), item.getPayload().getPayload());
    }

    void itemClick(Item<SelectablePayload<PayloadType>> selectableItem) {
        if (selectedItems.size() == 0) {
            return;
        }

        Item<PayloadType> item = getItem(selectableItem);

        Set<Item<PayloadType>> newSelectedItems = new HashSet<>(selectedItems);

        if (newSelectedItems.contains(item)) {
            newSelectedItems.remove(item);
        } else {
            newSelectedItems.add(item);
        }
        changed(items, newSelectedItems);
    }

    void itemLongClick(Item<SelectablePayload<PayloadType>>selectableItem) {
        if (selectedItems.size() != 0) {
            return;
        }
        Set<Item<PayloadType>> newSelectedItems = new HashSet<>(selectedItems);
        newSelectedItems.add(getItem(selectableItem));
        changed(items, newSelectedItems);
    }

    void setItems(List<Item<PayloadType>> items) {
        changed(kryo.copy(new ArrayList<>(items)), kryo.copy(new HashSet<>(selectedItems)));
    }

    void setSelectedItems(Set<Item<PayloadType>> selectedItems) {
        changed(items, kryo.copy(new HashSet<>(selectedItems)));
    }

    Set<Item<PayloadType>> getSelectedItems() {
        return kryo.copy(selectedItems);
    }

}
