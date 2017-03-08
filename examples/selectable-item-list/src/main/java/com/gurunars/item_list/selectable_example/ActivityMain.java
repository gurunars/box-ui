package com.gurunars.item_list.selectable_example;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gurunars.item_list.EmptyViewBinder;
import com.gurunars.item_list.Item;
import com.gurunars.item_list.ItemViewBinder;
import com.gurunars.item_list.SelectableItemList;
import com.gurunars.item_list.SelectablePayload;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;


public class ActivityMain extends AppCompatActivity {

    static class AnimalBinder implements ItemViewBinder<SelectablePayload<AnimalPayload>> {

        @Override
        public View getView(Context context) {
            TextView text = new TextView(context);
            int padding = context.getResources().getDimensionPixelOffset(R.dimen.padding);
            text.setPadding(padding, padding, padding, padding);
            return text;
        }

        @Override
        public void bind(View itemView, Item<SelectablePayload<AnimalPayload>> item, @Nullable Item<SelectablePayload<AnimalPayload>> previousItem) {
            ((TextView) itemView).setText("" + item);
            itemView.setBackgroundColor(item.getPayload().isSelected() ? Color.RED : Color.WHITE);
        }

    }

    static class EmptyBinder implements EmptyViewBinder {

        @Override
        public View getView(Context context) {
            TextView view = new TextView(context);
            view.setGravity(Gravity.CENTER);
            view.setText(R.string.empty);
            return view;
        }

    }

    private SelectableItemList<AnimalPayload> itemList;
    private List<Item<AnimalPayload>> items = new ArrayList<>();
    private int count = 0;

    private void add(AnimalPayload.Type type) {
        items.add(new Item<>(count, new AnimalPayload(0, type)));
        count++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = ButterKnife.findById(this, R.id.selectableItemList);

        for (AnimalPayload.Type type: AnimalPayload.Type.values()) {
            itemList.registerItemViewBinder(type, new AnimalBinder());
        }

        itemList.setEmptyViewBinder(new EmptyBinder());

        reset();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_selection:
                return showToast(clearSelection());
            case R.id.create:
                return showToast(create());
            case R.id.delete_selected:
                return showToast(deleteSelected());
            case R.id.update_selected:
                return showToast(updateSelected());
            case R.id.reset:
                return showToast(reset());
        }
        return super.onOptionsItemSelected(item);
    }

    private int updateSelected() {
        for (Item<AnimalPayload> item: itemList.getSelectedItems()) {
            item.getPayload().update();
            items.set(items.indexOf(item), item);
        }
        itemList.setItems(items);
        return R.string.did_update_selected;
    }

    private int deleteSelected() {
        items.removeAll(itemList.getSelectedItems());
        itemList.setItems(items);
        return R.string.did_delete_selected;
    }

    private boolean showToast(@StringRes int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        return true;
    }

    private @StringRes int clearSelection() {
        itemList.setSelectedItems(new HashSet<Item<AnimalPayload>>());
        return R.string.did_clear_selection;
    }

    private @StringRes int create() {
        add(AnimalPayload.Type.TIGER);
        add(AnimalPayload.Type.WOLF);
        add(AnimalPayload.Type.MONKEY);
        add(AnimalPayload.Type.LION);
        itemList.setItems(items);
        return R.string.did_create;
    }

    private @StringRes int reset() {
        count = 0;
        items.clear();
        create();
        return R.string.did_reset;
    }

}