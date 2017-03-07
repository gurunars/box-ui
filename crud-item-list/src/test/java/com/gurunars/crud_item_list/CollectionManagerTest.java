package com.gurunars.crud_item_list;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import java8.util.function.Consumer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class CollectionManagerTest {

    private CollectionManager<AnimalItem> collectionManager;
    private List<SelectableItem<AnimalItem>> selectableItems;
    private List<AnimalItem> items;

    private AnimalItem editedItem;

    private boolean expItemsSelected, expCanEdit, expCanMoveUp, expCanMoveDown, expCanDelete, expCanSelectAll;

    private void assertStatus() {
        assertEquals(expCanDelete, collectionManager.canDelete());
        assertEquals(expCanEdit, collectionManager.canEdit());
        assertEquals(expCanMoveDown, collectionManager.canMoveDown());
        assertEquals(expCanMoveUp, collectionManager.canMoveUp());
        assertEquals(expCanSelectAll, collectionManager.canSelectAll());
        assertEquals(expItemsSelected, collectionManager.hasSelection());
    }

    @Before
    public void before() {

        editedItem = null;

        expItemsSelected=true;
        expCanEdit=false;
        expCanMoveUp=true;
        expCanMoveDown=true;
        expCanDelete=true;
        expCanSelectAll=true;

        collectionManager = new CollectionManager<>(new Consumer<List<SelectableItem<AnimalItem>>>() {
            @Override
            public void accept(List<SelectableItem<AnimalItem>> selectableItems) {
                CollectionManagerTest.this.selectableItems = selectableItems;
            }
        }, new Consumer<List<AnimalItem>>() {
            @Override
            public void accept(List<AnimalItem> animalItems) {
                items = animalItems;
            }
        });

        items = Arrays.asList(
                new AnimalItem(1, AnimalItem.Type.MONKEY),
                new AnimalItem(2, AnimalItem.Type.TIGER),
                new AnimalItem(3, AnimalItem.Type.WOLF),
                new AnimalItem(4, AnimalItem.Type.LION),
                new AnimalItem(5, AnimalItem.Type.MONKEY),
                new AnimalItem(6, AnimalItem.Type.TIGER),
                new AnimalItem(7, AnimalItem.Type.WOLF),
                new AnimalItem(8, AnimalItem.Type.LION)
        );

        collectionManager.setItems(items);

    }

    @Test
    public void containmentList() {
        List<AnimalItem> itemList = Arrays.asList(
                new AnimalItem(1, AnimalItem.Type.MONKEY),
                new AnimalItem(2, AnimalItem.Type.TIGER)
        );
        assertTrue(itemList.contains(
                new AnimalItem(1, AnimalItem.Type.MONKEY)));
    }

    @Test
    public void containmentSet() {
        Set<AnimalItem> itemList = Sets.newSet(
                new AnimalItem(1, AnimalItem.Type.MONKEY),
                new AnimalItem(2, AnimalItem.Type.TIGER)
        );
        assertTrue(itemList.contains(
                new AnimalItem(1, AnimalItem.Type.MONKEY)));
    }

    @Test
    public void shortClickWithoutSelection_noAction() {
        collectionManager.itemClick(items.get(0));
        assertFalse(selectableItems.get(0).isSelected());

        expItemsSelected = expCanDelete = expCanMoveDown = expCanMoveUp = false;

        assertStatus();
    }

    @Test
    public void doubleLongClick_noExtraAction() {
        collectionManager.itemLongClick(items.get(1));
        collectionManager.itemLongClick(items.get(1));
        collectionManager.itemLongClick(items.get(0));
        assertTrue(selectableItems.get(1).isSelected());
        assertFalse(selectableItems.get(0).isSelected());

        expCanEdit = true;

        assertStatus();
    }

    @Test
    public void shortClickOnUnselectedAfterLongClick_shouldSelectAdditional() {
        collectionManager.itemLongClick(items.get(1));
        collectionManager.itemClick(items.get(2));

        assertTrue(selectableItems.get(1).isSelected());
        assertTrue(selectableItems.get(2).isSelected());

        assertStatus();
    }

    @Test
    public void shortClickOnSelection_shouldUnselect() {
        collectionManager.itemLongClick(items.get(0));
        collectionManager.itemClick(items.get(0));
        assertFalse(selectableItems.get(0).isSelected());

        expItemsSelected = expCanDelete = expCanMoveDown = expCanMoveUp = false;

        assertStatus();
    }

    @Test
    public void unselectingSelectedItems_shouldUnselect() {
        collectionManager.itemLongClick(items.get(0));
        collectionManager.itemClick(items.get(1));

        collectionManager.unselectAll();

        assertFalse(selectableItems.get(0).isSelected());
        assertFalse(selectableItems.get(1).isSelected());

        expItemsSelected = expCanDelete = expCanMoveDown = expCanMoveUp = false;

        assertStatus();
    }

    @Test
    public void selectAll_shouldSelectAll() {
        collectionManager.selectAll();

        for (SelectableItem item: selectableItems) {
            assertTrue(item.isSelected());
        }

        expCanMoveDown = expCanMoveUp = expCanSelectAll = false;

        assertStatus();
    }

    @Test
    public void moveDown() {
        collectionManager.itemLongClick(items.get(0));

        expCanEdit = true;
        expCanMoveUp = false;

        assertStatus();

        collectionManager.moveSelectionDown();

        assertEquals(items.get(0).getId(), 2);
        assertEquals(items.get(1).getId(), 1);
        assertEquals(items.get(2).getId(), 3);

        expCanMoveUp = true;

        assertStatus();
    }

    @Test
    public void moveUp() {
        collectionManager.itemLongClick(items.get(items.size() - 1));

        expCanEdit = true;
        expCanMoveDown = false;

        assertStatus();

        collectionManager.moveSelectionUp();

        assertEquals(items.get(items.size() - 1).getId(), 7);
        assertEquals(items.get(items.size() - 2).getId(), 8);
        assertEquals(items.get(items.size() - 3).getId(), 6);

        expCanMoveDown = true;

        assertStatus();
    }

    @Test
    public void delete() {
        collectionManager.selectAll();
        collectionManager.deleteSelected();

        assertEquals(new ArrayList<>(), selectableItems);

        expItemsSelected = expCanDelete = expCanMoveDown = expCanMoveUp = expCanSelectAll = false;

        assertStatus();
    }

    @Test
    public void resettingItems_shouldRetainSelection() {
        collectionManager.itemLongClick(items.get(1));
        collectionManager.setItems(items);

        assertTrue(selectableItems.get(1).isSelected());

        expCanEdit = true;

        assertStatus();
    }

    @Test
    public void resettingItemsWithSelectionRemoved_shouldCancelSelection() {
        items = new ArrayList<>(items);

        collectionManager.itemLongClick(items.get(1));
        items.remove(1);
        collectionManager.setItems(items);

        expItemsSelected = expCanDelete = expCanMoveDown = expCanMoveUp = false;

        assertStatus();
    }

    @Test
    public void triggerConsumption() {
        collectionManager.triggerConsumption();

        assertEquals(editedItem, null);

        collectionManager.setItemConsumer(new Consumer<AnimalItem>() {
            @Override
            public void accept(AnimalItem animalItem) {
                editedItem = animalItem;
            }
        });

        collectionManager.triggerConsumption();

        assertEquals(editedItem, null);

        collectionManager.itemLongClick(items.get(1));
        collectionManager.triggerConsumption();

        assertEquals(editedItem, new AnimalItem(2, AnimalItem.Type.TIGER));
    }

    @Test
    public void stateSaveAndLoad() {
        collectionManager.itemLongClick(items.get(1));

        Serializable payload = collectionManager.saveState();
        collectionManager.itemClick(items.get(1));

        assertFalse(selectableItems.get(1).isSelected());

        collectionManager.loadState(payload);

        assertTrue(selectableItems.get(1).isSelected());

        expCanEdit = true;

        assertStatus();
    }

    @Test
    public void deleteWithNoSelection_noError() {
        collectionManager.deleteSelected();
    }

    @Test
    public void moveUp_noError() {
        collectionManager.moveSelectionUp();
    }

    @Test
    public void moveDown_noError() {
        collectionManager.moveSelectionDown();
    }

}
