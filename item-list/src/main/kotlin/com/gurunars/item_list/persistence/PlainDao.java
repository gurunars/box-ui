package com.gurunars.item_list.persistence;


import com.gurunars.item_list.Item;

import java.util.List;

// NOTE: if written in Kotlin - does not work with Room annotations
public interface PlainDao<ItemType extends Item> {
    List<ItemType> all();
    void update(List<ItemType> items);
    void insert(List<ItemType> items);
    void delete(List<ItemType> items);
}
