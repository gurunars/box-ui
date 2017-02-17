package com.gurunars.item_list;

class TestItem implements Item {

    private long id;
    private int version;

    TestItem(long id, int version) {
        this.id = id;
        this.version = version;
    }

    @Override
    public Enum getType() {
        return null;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "new TestItem(" + getId() + ", " + version + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || ! (obj instanceof TestItem)) {
            return false;
        }
        TestItem other = (TestItem) obj;
        return id == other.id && version == other.version;
    }
}
