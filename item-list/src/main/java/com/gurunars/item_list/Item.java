package com.gurunars.item_list;

import java.io.Serializable;

/**
 * Abstraction of the entity that can be shown in the ItemListView.
 *
 * "equals" method MUST be implemented to compare items subclasses by value.
 */
public interface Item extends Serializable {

    /**
     * @return a unique ID of a persisted item
     */
    long getId();

    /**
     * @return item type
     */
    Enum getType();

}
