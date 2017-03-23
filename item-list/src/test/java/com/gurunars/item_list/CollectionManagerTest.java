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

    private List<SelectableItem<AnimalPayload>> selectableItems;

    private CollectionManager<AnimalPayload> manager = new CollectionManager<>(new Consumer<List<Item<SelectableItem<AnimalPayload>>>>() {
        @Override
        public void accept(List<Item<SelectableItem<AnimalPayload>>> items) {
            CollectionManagerTest.this.selectableItems = new ArrayList<>();
            for (Item<SelectableItem<AnimalPayload>> item : items) {
                selectableItems.add(item.getPayload());
            }
        }
    }, new Runnable() {
        @Override
        public void run() {

        }
    });

    @Before
    public void before() {
        manager.setItems(Arrays.asList(
                new Item<>(0, new AnimalPayload(0, AnimalPayload.Type.LION)),
                new Item<>(1, new AnimalPayload(0, AnimalPayload.Type.TIGER)),
                new Item<>(2, new AnimalPayload(0, AnimalPayload.Type.WOLF)),
                new Item<>(3, new AnimalPayload(0, AnimalPayload.Type.MONKEY)),
                new Item<>(4, new AnimalPayload(0, AnimalPayload.Type.LION)),
                new Item<>(5, new AnimalPayload(0, AnimalPayload.Type.TIGER)),
                new Item<>(6, new AnimalPayload(0, AnimalPayload.Type.WOLF)),
                new Item<>(7, new AnimalPayload(0, AnimalPayload.Type.MONKEY))
        ));
        selectableItems = new ArrayList<>();
    }

    @Test
    public void selectingItems_shouldGenerateProperListOfSelectableItems() {
        manager.setSelectedItems(Sets.newSet(
                new Item<>(0, new AnimalPayload(0, AnimalPayload.Type.LION)),
                new Item<>(1, new AnimalPayload(0, AnimalPayload.Type.TIGER)),
                new Item<>(2, new AnimalPayload(0, AnimalPayload.Type.WOLF))
        ));
        assertEquals(Arrays.asList(
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.LION), true),
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.TIGER), true),
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.WOLF), true),
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.MONKEY), false),
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.LION), false),
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.TIGER), false),
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.WOLF), false),
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.MONKEY), false)
        ), selectableItems);
    }

    @Test
    public void selectingNonexistentItems_shouldRemoveNonExsistentItemsFromSelection() {
        manager.setSelectedItems(Sets.newSet(
                new Item<>(0, new AnimalPayload(0, AnimalPayload.Type.LION)),
                new Item<>(1, new AnimalPayload(0, AnimalPayload.Type.TIGER)),
                new Item<>(2, new AnimalPayload(0, AnimalPayload.Type.WOLF)),
                new Item<>(18, new AnimalPayload(0, AnimalPayload.Type.TIGER)),
                new Item<>(19, new AnimalPayload(0, AnimalPayload.Type.WOLF))
        ));
        assertEquals(Sets.newSet(
                new Item<>(0, new AnimalPayload(0, AnimalPayload.Type.LION)),
                new Item<>(1, new AnimalPayload(0, AnimalPayload.Type.TIGER)),
                new Item<>(2, new AnimalPayload(0, AnimalPayload.Type.WOLF))
            ),
                manager.getSelectedItems()
        );
    }

    @Test
    public void removingTheItemsFromMainCollection_shouldRemoveItemsFromSelection() {
        manager.setSelectedItems(Sets.newSet(
                new Item<>(0, new AnimalPayload(0, AnimalPayload.Type.LION)),
                new Item<>(1, new AnimalPayload(0, AnimalPayload.Type.TIGER)),
                new Item<>(2, new AnimalPayload(0, AnimalPayload.Type.WOLF))
        ));
        manager.setItems(Arrays.asList(
                //new Item<>(0, new AnimalPayload(0, AnimalPayload.Type.LION)),
                new Item<>(1, new AnimalPayload(0, AnimalPayload.Type.TIGER)),
                //new Item<>(2, new AnimalPayload(0, AnimalPayload.Type.WOLF)),
                new Item<>(3, new AnimalPayload(0, AnimalPayload.Type.MONKEY)),
                new Item<>(4, new AnimalPayload(0, AnimalPayload.Type.LION)),
                new Item<>(5, new AnimalPayload(0, AnimalPayload.Type.TIGER)),
                new Item<>(6, new AnimalPayload(0, AnimalPayload.Type.WOLF)),
                new Item<>(7, new AnimalPayload(0, AnimalPayload.Type.MONKEY))
        ));
        assertEquals(Sets.newSet(
                new Item<>(1, new AnimalPayload(0, AnimalPayload.Type.TIGER))
            ),
                manager.getSelectedItems()
        );
    }

    @Test
    public void onlyInitialLongClick_shouldSelectItem() {
        manager.itemLongClick(new Item<>(0,
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.LION), false)));
        manager.itemLongClick(new Item<>(0,
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.LION), false)));
        manager.itemLongClick(new Item<>(1,
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.TIGER), false)));
        manager.itemLongClick(new Item<>(2,
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.WOLF), false)));
        assertEquals(Sets.newSet(
                new Item<>(0, new AnimalPayload(0, AnimalPayload.Type.LION))
        ), manager.getSelectedItems());
    }

    @Test
    public void anyClickOnUnselectedItemAfterLongClick_shouldSelectItem() {
        manager.itemLongClick(new Item<>(0,
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.LION), false)));
        manager.itemClick(new Item<>(1,
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.TIGER), false)));
        manager.itemClick(new Item<>(2,
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.WOLF), false)));
        manager.itemClick(new Item<>(3,
                new SelectableItem<>(new AnimalPayload(0, AnimalPayload.Type.MONKEY), false)));
        assertEquals(Sets.newSet(
                new Item<>(0, new AnimalPayload(0, AnimalPayload.Type.LION)),
                new Item<>(1, new AnimalPayload(0, AnimalPayload.Type.TIGER)),
                new Item<>(2, new AnimalPayload(0, AnimalPayload.Type.WOLF)),
                new Item<>(3, new AnimalPayload(0, AnimalPayload.Type.MONKEY))
        ), manager.getSelectedItems());
    }

    @Test
    public void anyClickOnSelectedItem_shouldUnselectIt() {
        Set<Item<AnimalPayload>> toSelect = Sets.newSet(
                new Item<>(0, new AnimalPayload(0, AnimalPayload.Type.LION)),
                new Item<>(1, new AnimalPayload(0, AnimalPayload.Type.TIGER)),
                new Item<>(2, new AnimalPayload(0, AnimalPayload.Type.WOLF)),
                new Item<>(3, new AnimalPayload(0, AnimalPayload.Type.MONKEY)),
                new Item<>(4, new AnimalPayload(0, AnimalPayload.Type.LION)),
                new Item<>(5, new AnimalPayload(0, AnimalPayload.Type.TIGER)),
                new Item<>(6, new AnimalPayload(0, AnimalPayload.Type.WOLF)),
                new Item<>(7, new AnimalPayload(0, AnimalPayload.Type.MONKEY))
        );
        manager.setSelectedItems(toSelect);
        for (Item<AnimalPayload> item: toSelect) {
            manager.itemClick(new Item<>(item.getId(), new SelectableItem<>(item.getPayload(), true)));
        }
        assertEquals(new HashSet<Item<AnimalPayload>>(), manager.getSelectedItems());
    }

}
