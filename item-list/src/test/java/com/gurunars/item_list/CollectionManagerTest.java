package com.gurunars.item_list;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java8.util.function.Consumer;

import static junit.framework.Assert.assertEquals;

public class CollectionManagerTest {

    private List<SelectableItem<AnimalItem>> selectableItems;

    private CollectionManager<AnimalItem> manager = new CollectionManager<>(new Consumer<List<SelectableItem<AnimalItem>>>() {
        @Override
        public void accept(List<SelectableItem<AnimalItem>> selectableItems) {
            CollectionManagerTest.this.selectableItems = selectableItems;
        }
    }, new Runnable() {
        @Override
        public void run() {

        }
    });

    @Before
    public void before() {
        manager.setItems(Arrays.asList(
                new AnimalItem(0, AnimalItem.Type.LION),
                new AnimalItem(1, AnimalItem.Type.TIGER),
                new AnimalItem(2, AnimalItem.Type.WOLF),
                new AnimalItem(3, AnimalItem.Type.MONKEY),
                new AnimalItem(4, AnimalItem.Type.LION),
                new AnimalItem(5, AnimalItem.Type.TIGER),
                new AnimalItem(6, AnimalItem.Type.WOLF),
                new AnimalItem(7, AnimalItem.Type.MONKEY)
        ));
        selectableItems = new ArrayList<>();
    }

    @Test
    public void selectingItems_shouldGenerateProperListOfSelectableItems() {
        manager.setSelectedItems(Sets.newSet(
                new AnimalItem(0, AnimalItem.Type.LION),
                new AnimalItem(1, AnimalItem.Type.TIGER),
                new AnimalItem(2, AnimalItem.Type.WOLF)
        ));
        assertEquals(Arrays.asList(
                new SelectableItem<>(new AnimalItem(0, AnimalItem.Type.LION), true),
                new SelectableItem<>(new AnimalItem(1, AnimalItem.Type.TIGER), true),
                new SelectableItem<>(new AnimalItem(2, AnimalItem.Type.WOLF), true),
                new SelectableItem<>(new AnimalItem(3, AnimalItem.Type.MONKEY), false),
                new SelectableItem<>(new AnimalItem(4, AnimalItem.Type.LION), false),
                new SelectableItem<>(new AnimalItem(5, AnimalItem.Type.TIGER), false),
                new SelectableItem<>(new AnimalItem(6, AnimalItem.Type.WOLF), false),
                new SelectableItem<>(new AnimalItem(7, AnimalItem.Type.MONKEY), false)
        ), selectableItems);
    }

    @Test
    public void selectingNonexistentItems_shouldRemoveNonExsistentItemsFromSelection() {
        manager.setSelectedItems(Sets.newSet(
                new AnimalItem(0, AnimalItem.Type.LION),
                new AnimalItem(1, AnimalItem.Type.TIGER),
                new AnimalItem(2, AnimalItem.Type.WOLF),
                new AnimalItem(18, AnimalItem.Type.TIGER),
                new AnimalItem(19, AnimalItem.Type.WOLF)
        ));
        assertEquals(Sets.newSet(
                new AnimalItem(0, AnimalItem.Type.LION),
                new AnimalItem(1, AnimalItem.Type.TIGER),
                new AnimalItem(2, AnimalItem.Type.WOLF)
            ),
                manager.getSelectedItems()
        );
    }

    @Test
    public void removingTheItemsFromMainCollection_shouldRemoveItemsFromSelection() {
        manager.setSelectedItems(Sets.newSet(
                new AnimalItem(0, AnimalItem.Type.LION),
                new AnimalItem(1, AnimalItem.Type.TIGER),
                new AnimalItem(2, AnimalItem.Type.WOLF)
        ));
        manager.setItems(Arrays.asList(
                //new AnimalItem(0, AnimalItem.Type.LION),
                new AnimalItem(1, AnimalItem.Type.TIGER),
                //new AnimalItem(2, AnimalItem.Type.WOLF),
                new AnimalItem(3, AnimalItem.Type.MONKEY),
                new AnimalItem(4, AnimalItem.Type.LION),
                new AnimalItem(5, AnimalItem.Type.TIGER),
                new AnimalItem(6, AnimalItem.Type.WOLF),
                new AnimalItem(7, AnimalItem.Type.MONKEY)
        ));
        assertEquals(Sets.newSet(
                new AnimalItem(1, AnimalItem.Type.TIGER)
            ),
                manager.getSelectedItems()
        );
    }

    @Test
    public void onlyInitialLongClick_shouldSelectItem() {
        manager.itemLongClick(new SelectableItem<>(new AnimalItem(0, AnimalItem.Type.LION), false));
        manager.itemLongClick(new SelectableItem<>(new AnimalItem(0, AnimalItem.Type.LION), false));
        manager.itemLongClick(new SelectableItem<>(new AnimalItem(1, AnimalItem.Type.TIGER), false));
        manager.itemLongClick(new SelectableItem<>(new AnimalItem(2, AnimalItem.Type.WOLF), false));
        assertEquals(Sets.newSet(
                new AnimalItem(0, AnimalItem.Type.LION)
        ), manager.getSelectedItems());
    }

    @Test
    public void anyClickOnUnselectedItemAfterLongClick_shouldSelectItem() {
        manager.itemLongClick(new SelectableItem<>(new AnimalItem(0, AnimalItem.Type.LION), false));
        manager.itemClick(new SelectableItem<>(new AnimalItem(1, AnimalItem.Type.TIGER), false));
        manager.itemClick(new SelectableItem<>(new AnimalItem(2, AnimalItem.Type.WOLF), false));
        manager.itemClick(new SelectableItem<>(new AnimalItem(3, AnimalItem.Type.MONKEY), false));
        assertEquals(Sets.newSet(
                new AnimalItem(0, AnimalItem.Type.LION),
                new AnimalItem(1, AnimalItem.Type.TIGER),
                new AnimalItem(2, AnimalItem.Type.WOLF),
                new AnimalItem(3, AnimalItem.Type.MONKEY)
        ), manager.getSelectedItems());
    }

    @Test
    public void anyClickOnSelectedItem_shouldUnselectIt() {
        Set<AnimalItem> toSelect = Sets.newSet(
                new AnimalItem(0, AnimalItem.Type.LION),
                new AnimalItem(1, AnimalItem.Type.TIGER),
                new AnimalItem(2, AnimalItem.Type.WOLF),
                new AnimalItem(3, AnimalItem.Type.MONKEY),
                new AnimalItem(4, AnimalItem.Type.LION),
                new AnimalItem(5, AnimalItem.Type.TIGER),
                new AnimalItem(6, AnimalItem.Type.WOLF),
                new AnimalItem(7, AnimalItem.Type.MONKEY)
        );
        manager.setSelectedItems(toSelect);
        for (AnimalItem item: toSelect) {
            manager.itemClick(new SelectableItem<>(item, true));
        }
        assertEquals(new HashSet<AnimalItem>(), manager.getSelectedItems());
    }

}
