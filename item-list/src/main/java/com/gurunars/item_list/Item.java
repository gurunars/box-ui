package com.gurunars.item_list;

import java.io.Serializable;

/**
 * Abstraction of the entity that can be shown in the ItemListView.
 *
 * "equals" method MUST be implemented to compare items subclasses by value.
 */
public abstract class Item implements Serializable {


    private final long id;
    private final Enum type;

    protected Item(long id, Enum type) {
        this.id = id;
        this.type = type;
    }

    /**
     * @return a unique ID of a persisted item
     */
    public final long getId() {
        return id;
    }

    /**
     * @return item type
     */
    public final Enum getType() {
        return type;
    }

    /**
     * @param other another object to compare payload with
     * @return true if payloads are the same
     */
    public abstract boolean payloadsEqual(Item other);

    @Override
    public final boolean equals(Object other) {
        return
                other != null &&
                        this.getClass() == other.getClass() &&
                        getId() == ((Item) other).getId();
    }

    @Override
    public final int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

}
