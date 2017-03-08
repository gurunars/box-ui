package com.gurunars.item_list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.gurunars.item_list.Item;
import com.gurunars.item_list.ItemViewBinder;
import com.gurunars.item_list.Payload;
import com.gurunars.item_list.SelectablePayload;

class ClickableItemViewBinder<PayloadType extends Payload> implements ItemViewBinder<SelectablePayload<PayloadType>> {

    private final ItemViewBinder<SelectablePayload<PayloadType>> itemViewBinder;
    private final CollectionManager<PayloadType> collectionManager;

    ClickableItemViewBinder(
            ItemViewBinder<SelectablePayload<PayloadType>> itemViewBinder,
            CollectionManager<PayloadType> collectionManager) {
        this.itemViewBinder = itemViewBinder;
        this.collectionManager = collectionManager;
    }

    @Override
    public View getView(Context context) {
        return itemViewBinder.getView(context);
    }

    @Override
    public void bind(View itemView, final Item<SelectablePayload<PayloadType>> item,
                     @Nullable Item<SelectablePayload<PayloadType>> previousItem) {
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
