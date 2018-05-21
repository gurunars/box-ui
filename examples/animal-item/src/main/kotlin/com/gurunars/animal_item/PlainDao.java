package com.gurunars.animal_item;


import java.util.List;

// NOTE: if written in Kotlin - does not work with Room annotations
// TODO: move to own package
public interface PlainDao<ItemType> {
    List<ItemType> all();
    void update(List<ItemType> items);
    void insert(List<ItemType> items);
    void delete(List<ItemType> items);
}
