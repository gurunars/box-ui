package com.gurunars.crud_item_list;


import com.gurunars.item_list.Item;

class AnimalItem implements Item {

    enum Type {
        MONKEY, TIGER, WOLF, LION
    }

    private Type type;
    private long id;

    AnimalItem(long id, Type type) {
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
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof AnimalItem)) {
            return false;
        }
        AnimalItem other = (AnimalItem) obj;

        return id == other.id && type == other.type;
    }

    @Override
    public String toString() {
        return "" + id + " [" + type.name().toLowerCase() + "]";
    }

}