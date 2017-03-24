package com.gurunars.crud_item_list.example;

import com.gurunars.item_list.Item;

class AnimalItem extends Item<AnimalItem.Type> {

    private int version;

    @Override
    public boolean payloadsEqual(Item other) {
        return other instanceof AnimalItem && version == ((AnimalItem) other).version;
    }

    enum Type {
        MONKEY, TIGER, WOLF, LION
    }

    public int getVersion() {
        return version;
    }

    void update() {
        this.version++;
    }

    AnimalItem(long id, int version, Type type) {
        super(id, type);
        this.version = version;
    }

    AnimalItem(long id, int version) {
        this(id, version, Type.MONKEY);
    }

    AnimalItem(long id, Type type) {
        this(id, 0, type);
    }

    AnimalItem(Type type) {
        this(0, 0, type);
    }

    @Override
    public String toString() {
        return "#" + getId() + "{" + getType() + " @ " + version + "}";
    }

}
