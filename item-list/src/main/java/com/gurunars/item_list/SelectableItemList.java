package com.gurunars.item_list;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import java8.util.function.Consumer;

/**
 * Item list that has selection enabled.
 *
 * Items can be selected via long clicking and consequentially clicking them.
 *
 * @param <ItemType> class describing item payload
 */
public class SelectableItemList<ItemType extends Item> extends FrameLayout {

    private ItemList<SelectableItem<ItemType>> itemList;
    private CollectionManager<ItemType> collectionManager;
    private Runnable selectionChangeListener;

    public SelectableItemList(Context context) {
        this(context, null);
    }

    public SelectableItemList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectableItemList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.selectable_item_list, this);
        itemList = ButterKnife.findById(this, R.id.itemList);

        collectionManager = new CollectionManager<>(new Consumer<List<SelectableItem<ItemType>>>() {
            @Override
            public void accept(List<SelectableItem<ItemType>> items) {
                itemList.setItems(items);
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (selectionChangeListener != null) {
                    selectionChangeListener.run();
                }
            }
        });

    }

    /**
     * Set a collection of items to be shown.
     *
     * If the collection of items is different - the diff shall be animated.
     *
     * @param items a new collection to be shown
     */
    public void setItems(List<ItemType> items) {
        collectionManager.setItems(items);
    }

    /**
     * Mark items as selected. If the items passed in are not in the actual list - they are filtered
     * out.
     *
     * @param selectedItems collection of items to be marked as selected
     */
    public void setSelectedItems(Set<ItemType> selectedItems) {
        collectionManager.setSelectedItems(selectedItems);
    }

    /**
     * Map item type to view binder responsible for rending items of this type.
     *
     * @param itemType type of the Item
     * @param itemViewBinder renderer for the items of a given type wrapped into a container with
     *                       an isSelected flag
     */
    public void registerItemViewBinder(Enum itemType,
                                       ItemViewBinder<SelectableItem<ItemType>> itemViewBinder) {
        itemList.registerItemViewBinder(itemType,
                new ClickableItemViewBinder<>(itemViewBinder, collectionManager));
    }

    /**
     * Set the renderer to be employed when the list contains no items.
     *
     * @param emptyViewBinder renderer for the empty list
     */
    public void setEmptyViewBinder(EmptyViewBinder emptyViewBinder) {
        itemList.setEmptyViewBinder(emptyViewBinder);
    }

    /**
     * @return a set of items selected ATM
     */
    public Set<ItemType> getSelectedItems() {
        return collectionManager.getSelectedItems();
    }

    /**
     * Configure a listener for the cases when the item collection within the view is updated. I.e.
     * if selection status or payload value gets changed.
     *
     * @param stateChangeListener callable to invoked whenever the changes occur
     */
    public void setSelectionChangeListener(Runnable stateChangeListener) {
        this.selectionChangeListener = stateChangeListener;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putSerializable("selectedItems", new HashSet<>(collectionManager.getSelectedItems()));
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle localState = (Bundle) state;
        super.onRestoreInstanceState(localState.getParcelable("superState"));
        collectionManager.setSelectedItems((HashSet<ItemType>) localState.getSerializable("selectedItems"));
    }
}
