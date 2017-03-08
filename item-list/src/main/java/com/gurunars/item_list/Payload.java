package com.gurunars.item_list;

import java.io.Serializable;

/**
 * Interface to be implemented by specific item payloads.
 */
public interface Payload extends Serializable {
    /**
     * @return value to be used when deciding which view to use to render the items in a
     *         RecyclerView
     */
    Enum getType();
}
