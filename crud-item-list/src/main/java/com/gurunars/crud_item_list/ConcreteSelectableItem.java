package com.gurunars.crud_item_list;

import com.gurunars.item_list.Item;

class ConcreteSelectableItem<ItemType extends Item> implements SelectableItem<ItemType> {

    private final ItemType item;
    private final boolean isSelected;

    ConcreteSelectableItem(ItemType item, boolean isSelected) {
        this.item = item;
        this.isSelected = isSelected;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public ItemType getItem() {
        return item;
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
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConcreteSelectableItem)) {
            return false;
        }
        ConcreteSelectableItem<ItemType> other = (ConcreteSelectableItem<ItemType>) obj;

        return item.equals(other.item) && isSelected == other.isSelected;
    }

    @Override
    public String toString() {
        return item.toString() + " | " + isSelected;
    }
}
