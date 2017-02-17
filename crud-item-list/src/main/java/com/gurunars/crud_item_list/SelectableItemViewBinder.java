package com.gurunars.crud_item_list;

import com.gurunars.item_list.Item;
import com.gurunars.item_list.ItemViewBinder;

public interface SelectableItemViewBinder<ItemType extends Item> extends ItemViewBinder<SelectableItem<ItemType>> {}
