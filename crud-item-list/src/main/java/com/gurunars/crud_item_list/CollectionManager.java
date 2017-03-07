package com.gurunars.crud_item_list;

import com.esotericsoftware.kryo.Kryo;
import com.gurunars.item_list.Item;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import java8.util.function.BiFunction;
import java8.util.function.Consumer;


class CollectionManager<ItemType extends Item> implements Serializable {

    private Kryo kryo = new Kryo();

    private List<ItemHolder<ItemType>> items = new ArrayList<>();
    private Set<ItemHolder<ItemType>> selectedItems = new HashSet<>();

    private final MoverUp<ItemHolder<ItemType>> moverUp = new MoverUp<>();
    private final MoverDown<ItemHolder<ItemType>> moverDown = new MoverDown<>();
    private final Deleter<ItemHolder<ItemType>> deleter = new Deleter<>();

    private final CheckerMoveDown<ItemHolder<ItemType>> checkerMoveDown = new CheckerMoveDown<>();
    private final CheckerMoveUp<ItemHolder<ItemType>> checkerMoveUp = new CheckerMoveUp<>();
    private final CheckerSelectAll<ItemHolder<ItemType>> checkerSelectAll = new CheckerSelectAll<>();
    private final CheckerDelete<ItemHolder<ItemType>> checkerDelete = new CheckerDelete<>();
    private final CheckerEdit<ItemHolder<ItemType>> checkerEdit = new CheckerEdit<>();

    private final Consumer<List<SelectableItem<ItemType>>> stateChangeHandler;
    private final Consumer<List<ItemType>> collectionChangeHandler;
    private Consumer<ItemType> itemConsumer;

    CollectionManager(
            Consumer<List<SelectableItem<ItemType>>> stateChangeHandler,
            Consumer<List<ItemType>> collectionChangeHandler) {
        this.stateChangeHandler = stateChangeHandler;
        this.collectionChangeHandler = collectionChangeHandler;
        this.kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    }

    private List<SelectableItem<ItemType>> getSelectableItems() {
        List<SelectableItem<ItemType>> rval = new ArrayList<>();
        for (ItemHolder<ItemType> item: items) {
            rval.add(new ConcreteSelectableItem<>(item.getRaw(), selectedItems.contains(item)));
        }
        return rval;
    }

    boolean hasSelection() {
        return !selectedItems.isEmpty();
    }

    boolean canEdit() {
        return checkerEdit.apply(items, selectedItems);
    }

    boolean canMoveUp() {
        return checkerMoveUp.apply(items, selectedItems);
    }

    boolean canMoveDown() {
        return checkerMoveDown.apply(items, selectedItems);
    }

    boolean canDelete() {
        return checkerDelete.apply(items, selectedItems);
    }

    boolean canSelectAll() {
        return checkerSelectAll.apply(items, selectedItems);
    }

    private void cleanSelection() {
        Set<ItemHolder<ItemType>> newSelection = new HashSet<>(selectedItems);
        for (ItemHolder<ItemType> cursor: selectedItems) {
            newSelection.remove(cursor);
            if (items.contains(cursor)) {
                // This is to replace the item's payload with a new one
                newSelection.add(items.get(items.indexOf(cursor)));
            }
        }
        selectedItems = newSelection;
    }

    private void changed() {
        cleanSelection();
        stateChangeHandler.accept(getSelectableItems());
    }

    void itemClick(ItemType item) {
        if (selectedItems.size() == 0) {
            return;
        }

        ItemHolder<ItemType> holder = new ItemHolder<>(item);

        if (selectedItems.contains(holder)) {
            selectedItems.remove(holder);
        } else {
            selectedItems.add(holder);
        }
        changed();
    }

    void itemLongClick(ItemType item) {
        if (selectedItems.size() == 0) {
            selectedItems.add(new ItemHolder<>(item));
        }
        changed();
    }

    void setItemConsumer(Consumer<ItemType> itemConsumer) {
        this.itemConsumer = itemConsumer;
    }

    void unselectAll() {
        this.selectedItems.clear();
        changed();
    }

    void setItems(List<ItemType> items) {
        this.items = kryo.copy(new ArrayList<>(ItemHolder.wrap(items)));
        changed();
    }

    private void changeDataSet(BiFunction<List<ItemHolder<ItemType>>, Set<ItemHolder<ItemType>>, List<ItemHolder<ItemType>>> changer) {
        items = changer.apply(items, selectedItems);
        changed();
        collectionChangeHandler.accept(ItemHolder.unwrap(items));
    }

    void deleteSelected() {
        changeDataSet(deleter);
    }

    void moveSelectionUp() {
        changeDataSet(moverUp);
    }

    void moveSelectionDown() {
        changeDataSet(moverDown);
    }

    void selectAll() {
        selectedItems.addAll(items);
        changed();
    }

    void triggerConsumption() {
        if (itemConsumer == null) {
            return;
        }

        Iterator<ItemHolder<ItemType>> iterator = selectedItems.iterator();
        if (iterator.hasNext()) {
            itemConsumer.accept(kryo.copy(iterator.next().getRaw()));
        }
    }

    Serializable saveState() {
        return new HashSet<>(selectedItems);
    }

    void loadState(Serializable state) {
        selectedItems = (HashSet<ItemHolder<ItemType>>) state;
        changed();
    }

}
