package com.gurunars.item_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.esotericsoftware.kryo.Kryo;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.ArrayList;
import java.util.List;


class ItemAdapter<PayloadType extends Payload> extends RecyclerView.Adapter<BindableViewHolder<PayloadType>> {

    private Kryo kryo = new Kryo();
    private List<Item<PayloadType>> items = new ArrayList<>();
    private List<Item<PayloadType>> previousList = new ArrayList<>();

    private Differ<Item<PayloadType>> differ = new Differ<>();

    private Scroller scroller;
    private EmptyViewBinder emptyViewBinder;

    ItemAdapter(Scroller scroller) {
        this.scroller = scroller;
        this.kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        setEmptyViewBinder(new ItemViewBinderEmpty());
    }

    private ItemViewBinder<PayloadType> defaultViewBinder = new ItemViewBinderString<>();
    private ItemViewBinder<PayloadType> footer = new ItemViewBinderFooter<>();

    private SparseArray<ItemViewBinder<PayloadType>> itemViewBinderMap =
            new SparseArray<ItemViewBinder<PayloadType>>() {{
        put(ItemViewBinderFooter.FOOTER_TYPE, footer);
    }};

    void setEmptyViewBinder(@NonNull EmptyViewBinder emptyViewBinder) {
        this.emptyViewBinder = emptyViewBinder;
    }

    void registerItemViewBinder(@NonNull Enum anEnum,
                                @NonNull ItemViewBinder<PayloadType> itemViewBinder) {
        itemViewBinderMap.put(anEnum.ordinal(), itemViewBinder);
    }

    void setItems(@NonNull List<Item<PayloadType>> newItems) {
        // make sure that item lists are passed by value
        previousList = kryo.copy(items);
        newItems = kryo.copy(newItems);

        if (items.isEmpty()) {
            this.items = newItems;
            notifyDataSetChanged();
        } else {
            int position = -1;

            for (Change<Item<PayloadType>> change: differ.apply(items, newItems)){
                position = change.apply(this, scroller, items, position);
            }

            if (position >= 0) {
                scroller.scrollToPosition(position);
            }
        }
    }

    @Override
    public BindableViewHolder<PayloadType> onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        if (viewType == ItemViewBinderEmpty.EMPTY_TYPE) {
            return new BindableViewHolder<>(parent, emptyViewBinder);
        } else {
            ItemViewBinder<PayloadType> itemViewBinder = this.itemViewBinderMap.get(viewType);
            return new BindableViewHolder<>(parent, itemViewBinder == null ? defaultViewBinder :
                    itemViewBinder);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BindableViewHolder<PayloadType> holder, int position) {
        if (position == items.size()) {
            return;  // nothing to bind
        }

        Item<PayloadType> item = items.get(position);
        Item<PayloadType> previousItem = null;
        for (Item<PayloadType> cursor: previousList) {
            if (item.getId() == cursor.getId()) {
                previousItem = cursor;
                break;
            }
        }

        holder.bind(item, previousItem);

    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return ItemViewBinderEmpty.EMPTY_TYPE;
        }
        if (position == items.size()) {
            return ItemViewBinderFooter.FOOTER_TYPE;
        }
        return items.get(position).getPayload().getType().ordinal();
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

}
