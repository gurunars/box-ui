package com.gurunars.crud_item_list.example;

import android.content.Context;

import com.gurunars.crud_item_list.ItemForm;
import com.gurunars.crud_item_list.ItemFormSupplier;

class AnimalFormSupplier implements ItemFormSupplier<AnimalItem> {

    private AnimalItem.Type type;

    AnimalFormSupplier(AnimalItem.Type type) {
        this.type = type;
    }

    @Override
    public ItemForm<AnimalItem> supply(Context context) {
        AnimalItemForm form = new AnimalItemForm(context);
        form.setItemType(type);
        return form;
    }

}
