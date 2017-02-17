package com.gurunars.item_list;

import java.util.ArrayList;
import java.util.List;

class ItemHolder<ItemType extends Item> implements Item {

    private ItemType item;

    static <ItemType extends Item> List<ItemHolder<ItemType>> wrap(List<ItemType> items) {
        List<ItemHolder<ItemType>> wrapped = new ArrayList<>();
        for (ItemType cursor: items) {
            wrapped.add(new ItemHolder<>(cursor));
        }
        return wrapped;
    }

    static <ItemType extends Item> List<ItemType> unwrap(List<ItemHolder<ItemType>> items) {
        List<ItemType> unwrapped = new ArrayList<>();
        for (ItemHolder<ItemType> cursor: items) {
            unwrapped.add(cursor.item);
        }
        return unwrapped;
    }

    private ItemHolder(ItemType item) {
        this.item = item;
    }

    @Override
    public Enum getType() {
        return item.getType();
    }

    @Override
    public long getId() {
        return item.getId();
    }

    @Override
    public boolean equals(Object other) {
        return
            other != null &&
            this.getClass() == other.getClass() &&
            getId() == ((Item) other).getId();
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    ItemType getRaw() {
        return item;
    }
}
