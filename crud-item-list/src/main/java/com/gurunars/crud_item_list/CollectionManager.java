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

    private List<ItemType> items = new ArrayList<>();
    private Set<ItemType> selectedItems = new HashSet<>();

    private final MoverUp<ItemType> moverUp = new MoverUp<>();
    private final MoverDown<ItemType> moverDown = new MoverDown<>();
    private final Deleter<ItemType> deleter = new Deleter<>();

    private final CheckerMoveDown<ItemType> checkerMoveDown = new CheckerMoveDown<>();
    private final CheckerMoveUp<ItemType> checkerMoveUp = new CheckerMoveUp<>();
    private final CheckerSelectAll<ItemType> checkerSelectAll = new CheckerSelectAll<>();
    private final CheckerDelete<ItemType> checkerDelete = new CheckerDelete<>();
    private final CheckerEdit<ItemType> checkerEdit = new CheckerEdit<>();

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
        for (ItemType item: items) {
            rval.add(new ConcreteSelectableItem<>(item, selectedItems.contains(item)));
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
        Set<ItemType> newSelection = new HashSet<>(selectedItems);
        for (ItemType cursor: selectedItems) {
            if (!items.contains(cursor)) {
                newSelection.remove(cursor);
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
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        changed();
    }

    void itemLongClick(ItemType item) {
        if (selectedItems.size() == 0) {
            selectedItems.add(item);
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
        this.items = kryo.copy(new ArrayList<>(items));
        changed();
    }

    private void changeDataSet(BiFunction<List<ItemType>, Set<ItemType>, List<ItemType>> changer) {
        items = changer.apply(items, selectedItems);
        changed();
        collectionChangeHandler.accept(items);
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

        Iterator<ItemType> iterator = selectedItems.iterator();
        if (iterator.hasNext()) {
            itemConsumer.accept(iterator.next());
        }
    }

    Serializable saveState() {
        return new HashSet<>(selectedItems);
    }

    void loadState(Serializable state) {
        selectedItems = (HashSet<ItemType>) state;
        changed();
    }

    private void addOrUpdate(ItemType item) {
        for (int i=0; i < items.size(); i++) {
            ItemType cursor = items.get(i);
            if (cursor.getId() == item.getId()) {
                items.set(i, item);
                return;
            }
        }
        items.add(item);
    }

    void setItem(ItemType item) {
        addOrUpdate(item);
        selectedItems.clear();
        changed();
        collectionChangeHandler.accept(items);
    }

}
