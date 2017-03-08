package com.gurunars.item_list;

import java.io.Serializable;

/**
 * Abstraction of the entity that can be shown in the ItemListView.
 *
 * "equals" method MUST be implemented to compare items subclasses by value.
 */
public final class Item<PayloadType extends Serializable> implements Serializable {

    private final long id;
    private final PayloadType payload;

    public Item(long id, PayloadType payload) {
        this.id = id;
        this.payload = payload;
    }

    /**
     * @return value to differentiate one item from another within a RecyclerView
     */
    public final long getId() {
        return id;
    }

    public final PayloadType getPayload() {
        return payload;
    }

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

    @Override
    public final String toString() {
        return "#" + id + "{" + payload.toString() + "}";
    }
}
