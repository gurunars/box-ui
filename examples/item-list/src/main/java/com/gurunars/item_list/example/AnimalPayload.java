package com.gurunars.item_list.example;


import com.gurunars.item_list.Payload;

class AnimalPayload implements Payload {

    private int version;
    private Type type;

    @Override
    public Enum getType() {
        return type;
    }

    enum Type {
        MONKEY, TIGER, WOLF, LION
    }

    public void update() {
        this.version++;
    }

    public AnimalPayload(int version, Type type) {
        this.version = version;
        this.type = type;
    }

    @Override
    public String toString() {
        return "" + type + " @ " + version;
    }
}
