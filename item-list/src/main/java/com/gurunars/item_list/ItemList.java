package com.gurunars.item_list;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.ButterKnife;

/**
 *
 * @param <ItemType> subclass of the Item instances of which are used to populate the views
 */
public class ItemList<ItemType extends Item> extends FrameLayout {

    private ItemAdapter<ItemType> itemAdapter;
    private LinearLayoutManager layoutManager;

    public ItemList(Context context) {
        this(context, null);
    }

    public ItemList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.item_list_view, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemList);

        Drawable divider = a.getDrawable(R.styleable.ItemList_itemDivider);

        a.recycle();

        RecyclerView recyclerView = ButterKnife.findById(this, R.id.recyclerView);
        layoutManager = new LinearLayoutManager(context);
        itemAdapter = new ItemAdapter<>(new Scroller() {
            @Override
            public void scrollToPosition(int position) {
                layoutManager.scrollToPosition(position);
            }

            @Override
            public int findFirstVisibleItemPosition() {
                return layoutManager.findFirstVisibleItemPosition();
            }

            @Override
            public int findLastVisibleItemPosition() {
                return layoutManager.findLastVisibleItemPosition();
            }
        });

        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), divider));
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

    }

    /**
     * Set a collection of items to be shown.
     *
     * If the collection of items is different - the diff shall be animated.
     *
     * @param items a new collection to be shown
     */
    public void setItems(List<ItemType> items) {
        itemAdapter.setItems(items);
    }

    /**
     * Map item type to view binder responsible for rending items of this type.
     *
     * @param itemType type of the Item
     * @param itemViewBinder renderer for the items of a given type
     */
    public void registerItemViewBinder(Enum itemType, ItemViewBinder<ItemType> itemViewBinder) {
        itemAdapter.registerItemViewBinder(itemType, itemViewBinder);
    }

    /**
     * Set the renderer to be employed when the list contains no items.
     *
     * @param emptyViewBinder renderer for the empty list
     */
    public void setEmptyViewBinder(EmptyViewBinder emptyViewBinder) {
        itemAdapter.setEmptyViewBinder(emptyViewBinder);
    }

}
