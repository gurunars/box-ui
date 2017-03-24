package com.gurunars.item_list;

/**
 * Wrapper around item item with an "isSelected" flag.
 *
 * @param <ItemType> type of the actual item.
 */
public final class SelectableItem<ItemType extends Item> extends Item {

    private final ItemType item;
    private boolean selected;

    SelectableItem(ItemType item, boolean selected) {
        super(item.getId(), item.getType());
        this.item = item;
        this.selected = selected;
    }

    /**
     * @return True if the item is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @return actual item
     */
    public ItemType getItem() {
        return item;
    }

    @Override
    public boolean payloadsEqual(Item obj) {
        if (obj instanceof SelectableItem) {
            SelectableItem other = (SelectableItem) obj;
            return item.payloadsEqual(other.item) && selected == other.selected;
        }
        return false;
    }

    @Override
    public String toString() {
        return item + "|" + selected;
    }
}
