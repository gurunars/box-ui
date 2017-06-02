package com.gurunars.item_list

import org.junit.Before
import org.junit.Test
import org.mockito.internal.util.collections.Sets

import java.util.ArrayList
import java.util.Arrays
import java.util.HashSet

import junit.framework.Assert.assertEquals

class CollectionManagerTest {

    private var selectableItems: List<SelectableItem<AnimalItem>>? = null

    private val manager = CollectionManager<AnimalItem>({ this@CollectionManagerTest.selectableItems = it }, Runnable { })

    @Before
    fun before() {
        manager.setItems(Arrays.asList(
                AnimalItem(0, AnimalItem.Type.LION),
                AnimalItem(1, AnimalItem.Type.TIGER),
                AnimalItem(2, AnimalItem.Type.WOLF),
                AnimalItem(3, AnimalItem.Type.MONKEY),
                AnimalItem(4, AnimalItem.Type.LION),
                AnimalItem(5, AnimalItem.Type.TIGER),
                AnimalItem(6, AnimalItem.Type.WOLF),
                AnimalItem(7, AnimalItem.Type.MONKEY)
        ))
        selectableItems = ArrayList<SelectableItem<AnimalItem>>()
    }

    @Test
    fun selectingItems_shouldGenerateProperListOfSelectableItems() {
        manager.setSelectedItems(Sets.newSet(
                AnimalItem(0, AnimalItem.Type.LION),
                AnimalItem(1, AnimalItem.Type.TIGER),
                AnimalItem(2, AnimalItem.Type.WOLF)
        ))
        assertEquals(Arrays.asList(
                SelectableItem(AnimalItem(0, AnimalItem.Type.LION), true),
                SelectableItem(AnimalItem(1, AnimalItem.Type.TIGER), true),
                SelectableItem(AnimalItem(2, AnimalItem.Type.WOLF), true),
                SelectableItem(AnimalItem(3, AnimalItem.Type.MONKEY), false),
                SelectableItem(AnimalItem(4, AnimalItem.Type.LION), false),
                SelectableItem(AnimalItem(5, AnimalItem.Type.TIGER), false),
                SelectableItem(AnimalItem(6, AnimalItem.Type.WOLF), false),
                SelectableItem(AnimalItem(7, AnimalItem.Type.MONKEY), false)
        ), selectableItems)
    }

    @Test
    fun selectingNonexistentItems_shouldRemoveNonExsistentItemsFromSelection() {
        manager.setSelectedItems(Sets.newSet(
                AnimalItem(0, AnimalItem.Type.LION),
                AnimalItem(1, AnimalItem.Type.TIGER),
                AnimalItem(2, AnimalItem.Type.WOLF),
                AnimalItem(18, AnimalItem.Type.TIGER),
                AnimalItem(19, AnimalItem.Type.WOLF)
        ))
        assertEquals(Sets.newSet(
                AnimalItem(0, AnimalItem.Type.LION),
                AnimalItem(1, AnimalItem.Type.TIGER),
                AnimalItem(2, AnimalItem.Type.WOLF)
        ),
                manager.getSelectedItems()
        )
    }

    @Test
    fun removingTheItemsFromMainCollection_shouldRemoveItemsFromSelection() {
        manager.setSelectedItems(Sets.newSet(
                AnimalItem(0, AnimalItem.Type.LION),
                AnimalItem(1, AnimalItem.Type.TIGER),
                AnimalItem(2, AnimalItem.Type.WOLF)
        ))
        manager.setItems(Arrays.asList(
                //new AnimalItem(0, AnimalItem.Type.LION),
                AnimalItem(1, AnimalItem.Type.TIGER),
                //new AnimalItem(2, AnimalItem.Type.WOLF),
                AnimalItem(3, AnimalItem.Type.MONKEY),
                AnimalItem(4, AnimalItem.Type.LION),
                AnimalItem(5, AnimalItem.Type.TIGER),
                AnimalItem(6, AnimalItem.Type.WOLF),
                AnimalItem(7, AnimalItem.Type.MONKEY)
        ))
        assertEquals(Sets.newSet(
                AnimalItem(1, AnimalItem.Type.TIGER)
        ),
                manager.getSelectedItems()
        )
    }

    @Test
    fun onlyInitialLongClick_shouldSelectItem() {
        manager.itemLongClick(SelectableItem(AnimalItem(0, AnimalItem.Type.LION), false))
        manager.itemLongClick(SelectableItem(AnimalItem(0, AnimalItem.Type.LION), false))
        manager.itemLongClick(SelectableItem(AnimalItem(1, AnimalItem.Type.TIGER), false))
        manager.itemLongClick(SelectableItem(AnimalItem(2, AnimalItem.Type.WOLF), false))
        assertEquals(Sets.newSet(
                AnimalItem(0, AnimalItem.Type.LION)
        ), manager.getSelectedItems())
    }
    @Test
    fun anyClickOnUnselectedItemAfterLongClick_shouldSelectItem() {
        manager.itemLongClick(SelectableItem(AnimalItem(0, AnimalItem.Type.LION), false))
        manager.itemClick(SelectableItem(AnimalItem(1, AnimalItem.Type.TIGER), false))
        manager.itemClick(SelectableItem(AnimalItem(2, AnimalItem.Type.WOLF), false))
        manager.itemClick(SelectableItem(AnimalItem(3, AnimalItem.Type.MONKEY), false))
        assertEquals(Sets.newSet(
                AnimalItem(0, AnimalItem.Type.LION),
                AnimalItem(1, AnimalItem.Type.TIGER),
                AnimalItem(2, AnimalItem.Type.WOLF),
                AnimalItem(3, AnimalItem.Type.MONKEY)
        ), manager.getSelectedItems())
    }

    @Test
    fun anyClickOnSelectedItem_shouldUnselectIt() {
        val toSelect = Sets.newSet(
                AnimalItem(0, AnimalItem.Type.LION),
                AnimalItem(1, AnimalItem.Type.TIGER),
                AnimalItem(2, AnimalItem.Type.WOLF),
                AnimalItem(3, AnimalItem.Type.MONKEY),
                AnimalItem(4, AnimalItem.Type.LION),
                AnimalItem(5, AnimalItem.Type.TIGER),
                AnimalItem(6, AnimalItem.Type.WOLF),
                AnimalItem(7, AnimalItem.Type.MONKEY)
        )
        manager.setSelectedItems(toSelect)
        for (item in toSelect) {
            manager.itemClick(SelectableItem(item, true))
        }
        assertEquals(HashSet<AnimalItem>(), manager.getSelectedItems())
    }

}
