package com.gurunars.crud_item_list.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.gurunars.crud_item_list.CrudItemList;
import com.gurunars.crud_item_list.ListChangeListener;
import com.gurunars.crud_item_list.NewItemSupplier;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class ActivityMain extends AppCompatActivity {

    private CrudItemList<AnimalItem> crudItemList;
    private Model model;

    private void initData(boolean force) {
        if (model.wasInited() && !force) {
            crudItemList.setItems(model.getItems());
            return;
        }
        List<AnimalItem> items = new ArrayList<>();
        for (int i=0; i < 1; i++) {
            items.add(new AnimalItem((AnimalItem.Type.LION)));
            items.add(new AnimalItem((AnimalItem.Type.TIGER)));
            items.add(new AnimalItem((AnimalItem.Type.MONKEY)));
            items.add(new AnimalItem((AnimalItem.Type.WOLF)));
        }
        model.clear();
        model.setItems(items);
        crudItemList.setItems(model.getItems());
    }

    private void confItemType(int id, final AnimalItem.Type type) {
        crudItemList.registerItemType(type,
                new AnimalRowBinder(),
                id,
                new AnimalFormSupplier(type),
                new NewItemSupplier<AnimalItem>() {
                    @Override
                    public AnimalItem supply() {
                        return new AnimalItem(model.getMaxItemId()+1, type);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new Model(this);
        setContentView(R.layout.crud_list);

        crudItemList = ButterKnife.findById(this, R.id.customView);

        crudItemList.setListChangeListener(new ListChangeListener<AnimalItem>() {
            @Override
            public void onChange(List<AnimalItem> items) {
                model.setItems(items);
            }
        });
        crudItemList.setEmptyViewBinder(new EmptyBinder());
        crudItemList.setCreationMenu(View.inflate(this, R.layout.create_layout, null));

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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        switch (i) {
            case R.id.leftHanded:
                crudItemList.setLeftHanded(true);
                break;
            case R.id.rightHanded:
                crudItemList.setLeftHanded(false);
                break;
            case R.id.toTop:
                lm.scrollToPositionWithOffset(0, 0);
                break;
            case R.id.toBottom:
                lm.scrollToPositionWithOffset(79, 0);
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
        List<AnimalItem> items = new ArrayList<>();
        for (int i=0; i < 20; i++) {
            items.add(new AnimalItem((AnimalItem.Type.LION)));
            items.add(new AnimalItem((AnimalItem.Type.TIGER)));
            items.add(new AnimalItem((AnimalItem.Type.MONKEY)));
            items.add(new AnimalItem((AnimalItem.Type.WOLF)));
        }
        model.clear();
        model.setItems(items);
        crudItemList.setItems(model.getItems());
    }

}