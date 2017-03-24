package com.gurunars.item_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.esotericsoftware.kryo.Kryo;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.ArrayList;
import java.util.List;


class ItemAdapter<ItemType extends Item> extends RecyclerView.Adapter<BindableViewHolder<? extends View, ItemType>> {

    private Kryo kryo = new Kryo();
    private List<ItemType> items = new ArrayList<>();
    private List<ItemType> previousList = new ArrayList<>();

    private Differ<ItemType> differ = new Differ<>();

    private Scroller scroller;
    private EmptyViewBinder emptyViewBinder;

    ItemAdapter(Scroller scroller) {
        this.scroller = scroller;
        this.kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        setEmptyViewBinder(new ItemViewBinderEmpty());
    }

    private ItemViewBinder<? extends View, ItemType> defaultViewBinder = new ItemViewBinderString<>();

    private SparseArray<ItemViewBinder<? extends View, ItemType>> itemViewBinderMap = new SparseArray<>();

    void setEmptyViewBinder(@NonNull EmptyViewBinder emptyViewBinder) {
        this.emptyViewBinder = emptyViewBinder;
    }

    void registerItemViewBinder(@NonNull Enum anEnum,
                                @NonNull ItemViewBinder<? extends View, ItemType> itemViewBinder) {
        itemViewBinderMap.put(anEnum.ordinal(), itemViewBinder);
    }

    void setItems(@NonNull List<ItemType> newItems) {
        // make sure that item lists are passed by value
        previousList = kryo.copy(items);
        newItems = kryo.copy(newItems);

        if (items.isEmpty()) {
            this.items = newItems;
            notifyDataSetChanged();
        } else {
            int position = -1;

            for (Change<ItemType> change: differ.apply(items, newItems)){
                position = change.apply(this, scroller, items, position);
            }

            if (position >= 0) {
                scroller.scrollToPosition(position);
            }
        }
    }

    @Override
    public BindableViewHolder<? extends View, ItemType> onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        if (viewType == ItemViewBinderEmpty.EMPTY_TYPE) {
            return new BindableViewHolder<>(parent, emptyViewBinder);
        } else {
            ItemViewBinder<? extends View, ItemType> itemViewBinder = this.itemViewBinderMap.get(viewType);
            return new BindableViewHolder<>(parent, itemViewBinder == null ? defaultViewBinder :
                    itemViewBinder);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BindableViewHolder<? extends View, ItemType> holder, int position) {
        if (position == items.size()) {
            return;  // nothing to bind
        }

        ItemType item = items.get(position);

        int previousIndex = previousList.indexOf(item);

        ItemType previousItem = previousIndex >= 0 ? previousList.get(previousIndex) : null;

        holder.bind(item, previousItem);

    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return ItemViewBinderEmpty.EMPTY_TYPE;
        }
        return items.get(position).getType().ordinal();
    }

    @Override
    public int getItemCount() {
        return Math.max(1, items.size());
    }

}
