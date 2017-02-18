package com.gurunars.item_list.example;


import com.gurunars.item_list.Item;

public class AnimalItem implements Item {

    enum Type {
        MONKEY, TIGER, WOLF, LION
    }

    private Type type;
    private long id;
    private int version;

    public void update() {
        this.version++;
    }

    public AnimalItem(long id, Type type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public Enum getType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof AnimalItem)) {
            return false;
        }
        AnimalItem other = (AnimalItem) obj;
        return id == other.id && type == other.type && version == other.version;
    }

    @Override
    public String toString() {
        return "" + id + " @ " + version;
    }
}
