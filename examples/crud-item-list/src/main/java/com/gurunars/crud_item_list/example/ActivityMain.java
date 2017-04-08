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

import java.util.List;

import butterknife.ButterKnife;


public class ActivityMain extends AppCompatActivity {

    private CrudItemList<AnimalItem> crudItemList;
    private Model model;
    private View creationMenu;

    private void initData(boolean force) {
        if (model.wasInited() && !force) {
            crudItemList.setItems(model.getItems());
            return;
        }
        model.clear();
        for (int i=0; i < 1; i++) {
            model.createItem(new AnimalItem(AnimalItem.Type.LION));
            model.createItem(new AnimalItem(AnimalItem.Type.TIGER));
            model.createItem(new AnimalItem(AnimalItem.Type.MONKEY));
            model.createItem(new AnimalItem(AnimalItem.Type.WOLF));
        }

        crudItemList.setItems(model.getItems());
    }

    private void confItemType(@IdRes int id, final AnimalItem.Type type) {
        creationMenu.findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.createItem(new AnimalItem(type));
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

        crudItemList.setListChangeListener(new ListChangeListener<AnimalItem>() {
            @Override
            public void onChange(List<AnimalItem> items) {
                for (AnimalItem item: items) {
                    model.updateItem(item);
                }
            }

        });
        crudItemList.setEmptyViewBinder(new EmptyBinder());
        crudItemList.setCreationMenu(creationMenu);
        crudItemList.setItemEditListener(new ItemEditListener<AnimalItem>() {
            @Override
            public void onEdit(AnimalItem editableItem) {
                editableItem.update();
                model.updateItem(editableItem);
                crudItemList.setItems(model.getItems());
            }

        });

        confItemType(R.id.lion, AnimalItem.Type.LION);
        confItemType(R.id.tiger, AnimalItem.Type.TIGER);
        confItemType(R.id.monkey, AnimalItem.Type.MONKEY);
        confItemType(R.id.wolf, AnimalItem.Type.WOLF);

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
            model.createItem(new AnimalItem(AnimalItem.Type.LION));
            model.createItem(new AnimalItem(AnimalItem.Type.TIGER));
            model.createItem(new AnimalItem(AnimalItem.Type.MONKEY));
            model.createItem(new AnimalItem(AnimalItem.Type.WOLF));
        }
        crudItemList.setItems(model.getItems());
    }

}