package com.gurunars.item_list;

import android.support.annotation.NonNull;

abstract class ChangeOfPart<ItemType extends Item> implements Change<ItemType> {

    int sourcePosition = -1;
    int targetPosition = -1;
    ItemType item;

    ChangeOfPart(ItemType item, int sourcePosition, int targetPosition) {
        this.item = item;
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
    }

    @Override
    public int hashCode() {
        int hash = getClass().getName().hashCode();
        return hash * sourcePosition +
                sourcePosition * targetPosition +
                hash * targetPosition;
    }

    @NonNull
    @Override
    public String toString() {
        return "new " + getClass().getName() + "<>(" + item.toString() + ", " + sourcePosition + ", " + targetPosition + ")";
    }

    private boolean samePositions(ChangeOfPart change) {
        return sourcePosition == change.sourcePosition && targetPosition == change.targetPosition;
    }

    @Override
    public boolean equals(Object object) {
        return object.getClass().equals(getClass()) && samePositions((ChangeOfPart) object);
    }

}

