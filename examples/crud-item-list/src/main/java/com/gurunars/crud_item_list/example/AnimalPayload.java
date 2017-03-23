package com.gurunars.crud_item_list.example;


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

    AnimalPayload(Type type) {
        this.version = 1;
        this.type = type;
    }

    public int getVersion() {
        return version;
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
