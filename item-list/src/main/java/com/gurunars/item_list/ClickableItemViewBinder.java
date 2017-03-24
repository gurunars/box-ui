package com.gurunars.item_list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

class ClickableItemViewBinder<ItemType extends Item> implements ItemViewBinder<SelectableItem<ItemType>> {

    private final ItemViewBinder<SelectableItem<ItemType>> itemViewBinder;
    private final CollectionManager<ItemType> collectionManager;

    ClickableItemViewBinder(
            ItemViewBinder<SelectableItem<ItemType>> itemViewBinder,
            CollectionManager<ItemType> collectionManager) {
        this.itemViewBinder = itemViewBinder;
        this.collectionManager = collectionManager;
    }

    @Override
    public View getView(Context context) {
        return itemViewBinder.getView(context);
    }

    @Override
    public void bind(View itemView, final SelectableItem<ItemType> item, @Nullable SelectableItem<ItemType> previousItem) {
        itemViewBinder.bind(itemView, item, previousItem);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionManager.itemClick(item);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                collectionManager.itemLongClick(item);
                return true;
            }
        });
    }

}
