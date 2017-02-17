package com.gurunars.item_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

class ChangeComplexPermutation<ItemType extends Item> implements Change<ItemType> {

    private int startPosition;
    private List<ItemType> mutatedSubList;

    // start is inclusive
    ChangeComplexPermutation(int startPosition, List<ItemType> mutatedSubList) {
        this.mutatedSubList = mutatedSubList;
        this.startPosition = startPosition;
    }

    @Override
    public int apply(RecyclerView.Adapter adapter, Scroller scroller, List<ItemType> items,
                     int currentPosition) {
        items.subList(startPosition, startPosition + mutatedSubList.size()).clear();
        items.addAll(startPosition, mutatedSubList);
        adapter.notifyItemRangeChanged(startPosition, mutatedSubList.size());
        return currentPosition;
    }

    @Override
    public int hashCode() {
        int hash = getClass().getName().hashCode();
        return hash * startPosition +
                startPosition * mutatedSubList.hashCode() +
                hash * mutatedSubList.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return "new " + getClass().getName() + "<>(" + startPosition + ", " + mutatedSubList.toString() + ")";
    }

    private boolean sameRanges(ChangeComplexPermutation change) {
        return startPosition == change.startPosition && mutatedSubList.equals(change.mutatedSubList);
    }

    @Override
    public boolean equals(Object object) {
        return object.getClass().equals(getClass()) && sameRanges((ChangeComplexPermutation) object);
    }
}
