package com.gurunars.crud_item_list.example;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.gurunars.crud_item_list.CrudItemList;
import com.gurunars.crud_item_list.ItemEditListener;
import com.gurunars.crud_item_list.ListChangeListener;
import com.gurunars.item_list.Item;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class ActivityMain extends AppCompatActivity {

    private CrudItemList<AnimalPayload> crudItemList;
    private Model model;
    private View creationMenu;

    private void initData(boolean force) {
        if (model.wasInited() && !force) {
            crudItemList.setItems(model.getItems());
            return;
        }
        model.clear();
        for (int i=0; i < 1; i++) {
            model.createItem(new AnimalPayload(AnimalPayload.Type.LION));
            model.createItem(new AnimalPayload(AnimalPayload.Type.TIGER));
            model.createItem(new AnimalPayload(AnimalPayload.Type.MONKEY));
            model.createItem(new AnimalPayload(AnimalPayload.Type.WOLF));
        }

        crudItemList.setItems(model.getItems());
    }

    private void confItemType(@IdRes int id, final AnimalPayload.Type type) {
        creationMenu.findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.createItem(new AnimalPayload(type));
                crudItemList.setItems(model.getItems());
            }
        });
        crudItemList.registerItemType(type, new AnimalRowBinder());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new Model(this);
        setContentView(R.layout.crud_list);

        creationMenu = View.inflate(this, R.layout.create_layout, null);

        crudItemList = ButterKnife.findById(this, R.id.customView);

        crudItemList.setListChangeListener(new ListChangeListener<AnimalPayload>() {
            @Override
            public void onChange(List<Item<AnimalPayload>> items) {
                for (Item<AnimalPayload> item: items) {
                    model.updateItem(item);
                }
            }

        });
        crudItemList.setEmptyViewBinder(new EmptyBinder());
        crudItemList.setCreationMenu(creationMenu);
        crudItemList.setItemEditListener(new ItemEditListener<AnimalPayload>() {
            @Override
            public void onEdit(Item<AnimalPayload> editableItem, boolean isNew) {
                editableItem.getPayload().update();
                model.updateItem(editableItem);
                crudItemList.setItems(model.getItems());
            }

        });

        confItemType(R.id.lion, AnimalPayload.Type.LION);
        confItemType(R.id.tiger, AnimalPayload.Type.TIGER);
        confItemType(R.id.monkey, AnimalPayload.Type.MONKEY);
        confItemType(R.id.wolf, AnimalPayload.Type.WOLF);

        initData(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.leftHanded:
                crudItemList.setLeftHanded(true);
                break;
            case R.id.rightHanded:
                crudItemList.setLeftHanded(false);
                break;
            case R.id.reset:
                initData(true);
                break;
            case R.id.lock:
                setTitle(R.string.unsortable);
                crudItemList.setSortable(false);
                break;
            case R.id.unlock:
                setTitle(R.string.sortable);
                crudItemList.setSortable(true);
                break;
            case R.id.addMany:
                addMany();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addMany() {
        model.clear();
        for (int i=0; i < 20; i++) {
            model.createItem(new AnimalPayload(AnimalPayload.Type.LION));
            model.createItem(new AnimalPayload(AnimalPayload.Type.TIGER));
            model.createItem(new AnimalPayload(AnimalPayload.Type.MONKEY));
            model.createItem(new AnimalPayload(AnimalPayload.Type.WOLF));
        }
        crudItemList.setItems(model.getItems());
    }

}