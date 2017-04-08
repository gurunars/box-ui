package com.gurunars.crud_item_list;

import com.esotericsoftware.kryo.Kryo;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import java8.util.function.Consumer;

final class ActionEdit<ItemType> implements Action<ItemType> {

    private Kryo kryo = new Kryo();
    private Consumer<ItemType> itemConsumer;

    ActionEdit(Consumer<ItemType> itemConsumer) {
        this.kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        this.itemConsumer = itemConsumer;
    }

    @Override
    public boolean perform(List<ItemType> all, Set<ItemType> selectedItems) {
        Iterator<ItemType> iterator = selectedItems.iterator();
        if (iterator.hasNext()) {
            itemConsumer.accept(kryo.copy(iterator.next()));
        }
        return false;
    }

    @Override
    public boolean canPerform(List<ItemType> all, Set<ItemType> selectedItems) {
        return selectedItems.size() == 1;
    }

}
