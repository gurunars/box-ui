package com.gurunars.item_list;


class AnimalItem implements Item {

    private final long id;
    private int version;
    private Type type;

    @Override
    public long getId() {
        return id;
    }

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

    AnimalItem(long id, int version, Type type) {
        this.version = version;
        this.type = type;
        this.id = id;
    }

    AnimalItem(long id, int version) {
        this(id, version, Type.MONKEY);
    }

    AnimalItem(long id, Type type) {
        this(id, 0, type);
    }

    @Override
    public String toString() {
        return "#" + id + "{" + type + " @ " + version + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AnimalItem) {
            AnimalItem other = (AnimalItem) obj;
            return version == other.version && type == other.type;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }
}
