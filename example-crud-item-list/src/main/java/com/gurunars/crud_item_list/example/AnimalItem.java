package com.gurunars.crud_item_list.example;

import com.gurunars.item_list.Item;


class AnimalItem implements Item {

    enum Type {
        MONKEY, TIGER, WOLF, LION
    }

    private final Type type;
    private long id;
    private int version;

    AnimalItem(Type type) {
        this(0, type);
    }

    AnimalItem(long id, Type type) {
        this.id = id;
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public Enum getType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
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
        return "" + id + " @ " + version  + " [" + type.name().toLowerCase() + "]";
    }
}