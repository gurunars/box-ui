package com.gurunars.item_list;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;


public class DifferTest {

    private Differ<AnimalItem> differ = new Differ<>();

    @Test
    public void itemCreate_shouldProduceSingleItemListWithCreation() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeCreate<>(new ItemHolder<>(new AnimalItem(7, 1)), 6, 6)),
                differ.apply(
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(2, 1),
                                new AnimalItem(3, 1),
                                new AnimalItem(4, 1),
                                new AnimalItem(5, 1),
                                new AnimalItem(6, 1)
                        )),
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(2, 1),
                                new AnimalItem(3, 1),
                                new AnimalItem(4, 1),
                                new AnimalItem(5, 1),
                                new AnimalItem(6, 1),
                                new AnimalItem(7, 1)
                        )))
        );

    }

    @Test
    public void itemDelete_shouldProduceSingleItemListWithRemoval() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeDelete<>(new ItemHolder<>(new AnimalItem(3, 1)), 2, 2)),
                differ.apply(
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(2, 1),
                                new AnimalItem(3, 1),
                                new AnimalItem(4, 1),
                                new AnimalItem(5, 1),
                                new AnimalItem(6, 1)
                        )),
                                ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(2, 1),
                                new AnimalItem(4, 1),
                                new AnimalItem(5, 1),
                                new AnimalItem(6, 1)
                        )))
        );

    }

    @Test
    public void moveUp_shouldProduceSingleItemList() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeMove<>(new ItemHolder<>(new AnimalItem(5, 1)), 4, 1)),
                differ.apply(
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(2, 1),
                                new AnimalItem(3, 1),
                                new AnimalItem(4, 1),
                                new AnimalItem(5, 1),
                                new AnimalItem(6, 1)
                        )),
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(5, 1),  // moved up
                                new AnimalItem(2, 1),
                                new AnimalItem(3, 1),
                                new AnimalItem(4, 1),
                                new AnimalItem(6, 1)
                        )))
        );

    }

    @Test
    public void moveDown_shouldProduceSingleItemList() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeMove<>(new ItemHolder<>(new AnimalItem(2, 1)), 1, 4)),
                differ.apply(
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(2, 1),
                                new AnimalItem(3, 1),
                                new AnimalItem(4, 1),
                                new AnimalItem(5, 1),
                                new AnimalItem(6, 1)
                        )),
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(3, 1),
                                new AnimalItem(4, 1),
                                new AnimalItem(5, 1),
                                new AnimalItem(2, 1), // moved down
                                new AnimalItem(6, 1)
                        )))
        );
    }

    @Test
    public void itemEdit_shouldProduceSingleItemListWithUpdate() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeUpdate<>(new ItemHolder<>(new AnimalItem(3, 2)), 2, 2)),
                differ.apply(
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(2, 1),
                                new AnimalItem(3, 1),
                                new AnimalItem(4, 1),
                                new AnimalItem(5, 1),
                                new AnimalItem(6, 1)
                        )),
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(2, 1),
                                new AnimalItem(3, 2), // updated
                                new AnimalItem(4, 1),
                                new AnimalItem(5, 1),
                                new AnimalItem(6, 1)
                        )))
        );

    }

    @Test
    public void simpleDiff_isCorrect() throws Exception {

        assertEquals(
                Arrays.asList(
                        new ChangeDelete<>(new ItemHolder<>(new AnimalItem(5, 1)), 4, 4),
                        new ChangeDelete<>(new ItemHolder<>(new AnimalItem(2, 1)), 1, 1),
                        new ChangeCreate<>(new ItemHolder<>(new AnimalItem(7, 2)), 0, 0),
                        new ChangeCreate<>(new ItemHolder<>(new AnimalItem(9, 1)), 4, 4),
                        new ChangeMove<>(new ItemHolder<>(new AnimalItem(1, 1)), 1, 3),
                        new ChangeMove<>(new ItemHolder<>(new AnimalItem(3, 1)), 1, 2),
                        new ChangeUpdate<>(new ItemHolder<>(new AnimalItem(1, 2)), 3, 3),
                        new ChangeUpdate<>(new ItemHolder<>(new AnimalItem(3, 2)), 2, 2)
                ),
                differ.apply(
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(2, 1),
                                new AnimalItem(3, 1),
                                new AnimalItem(4, 1),
                                new AnimalItem(5, 1)
                        )),
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(7, 2),
                                new AnimalItem(4, 1),
                                new AnimalItem(3, 2),
                                new AnimalItem(1, 2),
                                new AnimalItem(9, 1)
                        )))
        );

    }

    @Test
    public void tooManyMoves_resultInComplexPermutation() {

        assertEquals(
                Collections.singletonList(new ChangeComplexPermutation<>(1, ItemHolder.wrap(Arrays.asList(
                        new AnimalItem(7, 1),
                        new AnimalItem(5, 1),
                        new AnimalItem(8, 1),
                        new AnimalItem(6, 1),
                        new AnimalItem(4, 1),
                        new AnimalItem(2, 1),
                        new AnimalItem(3, 1)
                )))),
                differ.apply(
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(2, 1),
                                new AnimalItem(3, 1),
                                new AnimalItem(4, 1),
                                new AnimalItem(5, 1),
                                new AnimalItem(6, 1),
                                new AnimalItem(7, 1),
                                new AnimalItem(8, 1),
                                new AnimalItem(9, 1)
                        )),
                        ItemHolder.wrap(Arrays.asList(
                                new AnimalItem(1, 1),
                                new AnimalItem(7, 1),
                                new AnimalItem(5, 1),
                                new AnimalItem(8, 1),
                                new AnimalItem(6, 1),
                                new AnimalItem(4, 1),
                                new AnimalItem(2, 1),
                                new AnimalItem(3, 1),
                                new AnimalItem(9, 1)
                        )))
        );

    }

}