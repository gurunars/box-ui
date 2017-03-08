package com.gurunars.item_list.example;

import android.animation.ValueAnimator;
import android.content.Context;
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
import com.gurunars.item_list.ItemList;
import com.gurunars.item_list.ItemViewBinder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class ActivityMain extends AppCompatActivity {

    static class AnimalBinder implements ItemViewBinder<AnimalPayload> {

        @Override
        public View getView(Context context) {
            TextView text = new TextView(context);
            int padding = context.getResources().getDimensionPixelOffset(R.dimen.padding);
            text.setPadding(padding, padding, padding, padding);
            return text;
        }

        @Override
        public void bind(View itemView, Item<AnimalPayload> item, @Nullable Item<AnimalPayload> previousItem) {
            ((TextView) itemView).setText(item.toString());
            if (previousItem != null) {
                animateUpdate(itemView);
            }
        }

        private void animateUpdate(final View view) {
            view.clearAnimation();
            ValueAnimator anim = new ValueAnimator();
            anim.setFloatValues((float) 1.0, (float) 0.0, (float) 1.0);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.setAlpha((Float) animation.getAnimatedValue());
                }
            });
            anim.setDuration(1300);
            anim.start();
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

    private ItemList<AnimalPayload> itemList;
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

        itemList = ButterKnife.findById(this, R.id.itemList);

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
            case R.id.clear:
                return showToast(clear());
            case R.id.create:
                return showToast(create());
            case R.id.delete:
                return showToast(delete());
            case R.id.update:
                return showToast(update());
            case R.id.moveDown:
                return showToast(moveTopToBottom());
            case R.id.moveUp:
                return showToast(moveBottomToTop());
            case R.id.reset:
                return showToast(reset());
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean showToast(@StringRes int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        return true;
    }

    private @StringRes int clear() {
        items.clear();
        itemList.setItems(items);
        return R.string.did_clear;
    }

    private @StringRes int create() {
        add(AnimalPayload.Type.TIGER);
        add(AnimalPayload.Type.WOLF);
        add(AnimalPayload.Type.MONKEY);
        add(AnimalPayload.Type.LION);
        itemList.setItems(items);
        return R.string.did_create;
    }

    private @StringRes int delete() {
        for (int i=items.size()-1; i >= 0; i--) {
            if (i % 2 != 0) {
                items.remove(i);
            }
        }
        itemList.setItems(items);
        return R.string.did_delete;
    }

    private @StringRes int update() {
        for (int i=0; i < items.size(); i++) {
            if (i % 2 != 0) {
                items.get(i).getPayload().update();
            }
        }
        itemList.setItems(items);
        return R.string.did_update;
    }

    private @StringRes int moveBottomToTop() {
        if (items.size() <= 0) {
            return R.string.no_action;
        }
        Item<AnimalPayload> item = items.get(items.size()-1);
        items.remove(items.size()-1);
        items.add(0, item);
        itemList.setItems(items);
        return R.string.did_move_to_top;
    }

    private @StringRes int moveTopToBottom() {
        if (items.size() <= 0) {
            return R.string.no_action;
        }
        Item<AnimalPayload> item = items.get(0);
        items.remove(0);
        items.add(item);
        itemList.setItems(items);
        return R.string.did_move_to_bottom;
    }

    private @StringRes int reset() {
        count = 0;
        items.clear();
        create();
        return R.string.did_reset;
    }

}