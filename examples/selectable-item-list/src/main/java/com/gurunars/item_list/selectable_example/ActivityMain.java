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
import com.gurunars.item_list.ItemViewBinder;
import com.gurunars.item_list.SelectableItem;
import com.gurunars.item_list.SelectableItemList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;


public class ActivityMain extends AppCompatActivity {

    static class AnimalBinder implements ItemViewBinder<SelectableItem<AnimalItem>> {

        @Override
        public View getView(Context context) {
            TextView text = new TextView(context);
            int padding = context.getResources().getDimensionPixelOffset(R.dimen.padding);
            text.setPadding(padding, padding, padding, padding);
            return text;
        }

        @Override
        public void bind(View itemView, SelectableItem<AnimalItem> item, @Nullable SelectableItem<AnimalItem> previousItem) {
            ((TextView) itemView).setText("" + item);
            itemView.setBackgroundColor(item.isSelected() ? Color.RED : Color.WHITE);
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

    private SelectableItemList<AnimalItem> itemList;
    private List<AnimalItem> items = new ArrayList<>();
    private int count = 0;

    private void add(AnimalItem.Type type) {
        items.add(new AnimalItem(count, 0, type));
        count++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = ButterKnife.findById(this, R.id.selectableItemList);

        for (AnimalItem.Type type: AnimalItem.Type.values()) {
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
        for (AnimalItem item: itemList.getSelectedItems()) {
            item.update();
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
        itemList.setSelectedItems(new HashSet<AnimalItem>());
        return R.string.did_clear_selection;
    }

    private @StringRes int create() {
        add(AnimalItem.Type.TIGER);
        add(AnimalItem.Type.WOLF);
        add(AnimalItem.Type.MONKEY);
        add(AnimalItem.Type.LION);
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