package com.gurunars.item_list.selectable_example;


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

    void update() {
        this.version++;
    }

    AnimalPayload(int version, Type type) {
        this.version = version;
        this.type = type;
    }

    @Override
    public String toString() {
        return "" + type + " @ " + version;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AnimalPayload)) {
            return false;
        }
        AnimalPayload other = (AnimalPayload) obj;
        return version == other.version && type == other.type;
    }

}
