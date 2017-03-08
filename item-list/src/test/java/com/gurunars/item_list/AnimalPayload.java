package com.gurunars.item_list;


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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AnimalPayload) {
            AnimalPayload other = (AnimalPayload) obj;
            return version == other.version && type == other.type;
        }
        return false;
    }
}
