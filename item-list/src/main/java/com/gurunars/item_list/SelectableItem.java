package com.gurunars.item_list;

/**
 * Wrapper around item item with an "isSelected" flag.
 *
 * @param <ItemType> type of the actual item.
 */
public final class SelectableItem<ItemType extends Item> implements Item {

    private final ItemType item;
    private boolean selected;

    SelectableItem(ItemType item, boolean selected) {
        this.item = item;
        this.selected = selected;
    }

    /**
     * @return True if the item is selected
     */
    public final boolean isSelected() {
        return selected;
    }

    /**
     * @return actual item
     */
    public ItemType getItem() {
        return item;
    }

    void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int hashCode() {
        return item.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SelectableItem) {
            SelectableItem other = (SelectableItem) obj;
            return item.equals(other.item) && selected == other.selected;
        }
        return false;
    }

    @Override
    public long getId() {
        return item.getId();
    }

    @Override
    public Enum getType() {
        return item.getType();
    }

    @Override
    public String toString() {
        return item + "|" + selected;
    }
}
