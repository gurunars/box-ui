package com.gurunars.crud_item_list.example;

import android.content.Context;
import android.os.Parcelable;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gurunars.crud_item_list.ItemForm;

import icepick.Icepick;
import icepick.State;

class AnimalItemForm extends ItemForm<AnimalItem> {

    private TextView title, currentVersion, nextVersion;

    @State AnimalItem.Type itemType;
    @State AnimalItem animalItem;

    public AnimalItemForm(Context context) {
        super(context);
        setBackgroundColor(context.getResources().getColor(R.color.White));
    }

    @Override
    protected AnimalItem getItem() {
        AnimalItem item = new AnimalItem(animalItem.getId(), (AnimalItem.Type) animalItem.getType());
        item.setVersion(animalItem.getVersion() + 1);
        return item;
    }

    @Override
    protected void render(ViewGroup contentPoint) {
        inflate(getContext(), R.layout.animal_item_form, contentPoint);
        title = (TextView) findViewById(R.id.title);
        currentVersion = (TextView) findViewById(R.id.currentVersion);
        nextVersion = (TextView) findViewById(R.id.nextVersion);
    }

    public void setItemType(AnimalItem.Type itemType) {
        this.itemType = itemType;
    }

    @Override
    public void populate(AnimalItem item) {
        animalItem = item;
        title.setText(getContext().getString(R.string.id) + ":" + item.getId());
        currentVersion.setText(getContext().getString(R.string.current_version)
                + ":" + item.getVersion());
        nextVersion.setText(getContext().getString(R.string.next_version)
                + ":" + (item.getVersion()+1));
    }

    @Override public Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
        populate(animalItem);
    }

}
