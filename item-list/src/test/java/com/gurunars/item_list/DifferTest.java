package com.gurunars.item_list;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;


public class DifferTest {

    private Differ<Item<Integer>> differ = new Differ<>();

    @Test
    public void itemCreate_shouldProduceSingleItemListWithCreation() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeCreate<>(new Item<>(7, 1), 6, 6)),
                differ.apply(
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(2, 1),
                                new Item<>(3, 1),
                                new Item<>(4, 1),
                                new Item<>(5, 1),
                                new Item<>(6, 1)
                        ),
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(2, 1),
                                new Item<>(3, 1),
                                new Item<>(4, 1),
                                new Item<>(5, 1),
                                new Item<>(6, 1),
                                new Item<>(7, 1)
                        ))
        );

    }

    @Test
    public void itemDelete_shouldProduceSingleItemListWithRemoval() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeDelete<>(new Item<>(3, 1), 2, 2)),
                differ.apply(
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(2, 1),
                                new Item<>(3, 1),
                                new Item<>(4, 1),
                                new Item<>(5, 1),
                                new Item<>(6, 1)
                        ),
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(2, 1),
                                new Item<>(4, 1),
                                new Item<>(5, 1),
                                new Item<>(6, 1)
                        ))
        );

    }

    @Test
    public void moveUp_shouldProduceSingleItemList() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeMove<>(new Item<>(5, 1), 4, 1)),
                differ.apply(
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(2, 1),
                                new Item<>(3, 1),
                                new Item<>(4, 1),
                                new Item<>(5, 1),
                                new Item<>(6, 1)
                        ),
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(5, 1),  // moved up
                                new Item<>(2, 1),
                                new Item<>(3, 1),
                                new Item<>(4, 1),
                                new Item<>(6, 1)
                        ))
        );

    }

    @Test
    public void moveDown_shouldProduceSingleItemList() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeMove<>(new Item<>(2, 1), 1, 4)),
                differ.apply(
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(2, 1),
                                new Item<>(3, 1),
                                new Item<>(4, 1),
                                new Item<>(5, 1),
                                new Item<>(6, 1)
                        ),
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(3, 1),
                                new Item<>(4, 1),
                                new Item<>(5, 1),
                                new Item<>(2, 1), // moved down
                                new Item<>(6, 1)
                        ))
        );
    }

    @Test
    public void itemEdit_shouldProduceSingleItemListWithUpdate() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeUpdate<>(new Item<>(3, 2), 2, 2)),
                differ.apply(
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(2, 1),
                                new Item<>(3, 1),
                                new Item<>(4, 1),
                                new Item<>(5, 1),
                                new Item<>(6, 1)
                        ),
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(2, 1),
                                new Item<>(3, 2), // updated
                                new Item<>(4, 1),
                                new Item<>(5, 1),
                                new Item<>(6, 1)
                        ))
        );

    }

    @Test
    public void simpleDiff_isCorrect() throws Exception {

        assertEquals(
                Arrays.asList(
                        new ChangeDelete<>(new Item<>(5, 1), 4, 4),
                        new ChangeDelete<>(new Item<>(2, 1), 1, 1),
                        new ChangeCreate<>(new Item<>(7, 2), 0, 0),
                        new ChangeCreate<>(new Item<>(9, 1), 4, 4),
                        new ChangeMove<>(new Item<>(1, 1), 1, 3),
                        new ChangeMove<>(new Item<>(3, 1), 1, 2),
                        new ChangeUpdate<>(new Item<>(1, 2), 3, 3),
                        new ChangeUpdate<>(new Item<>(3, 2), 2, 2)
                ),
                differ.apply(
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(2, 1),
                                new Item<>(3, 1),
                                new Item<>(4, 1),
                                new Item<>(5, 1)
                        ),
                        Arrays.asList(
                                new Item<>(7, 2),
                                new Item<>(4, 1),
                                new Item<>(3, 2),
                                new Item<>(1, 2),
                                new Item<>(9, 1)
                        ))
        );

    }

    @Test
    public void tooManyMoves_resultInComplexPermutation() {

        assertEquals(
                Collections.singletonList(new ChangeComplexPermutation<>(1, Arrays.asList(
                        new Item<>(7, 1),
                        new Item<>(5, 1),
                        new Item<>(8, 1),
                        new Item<>(6, 1),
                        new Item<>(4, 1),
                        new Item<>(2, 1),
                        new Item<>(3, 1)
                ))),
                differ.apply(
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(2, 1),
                                new Item<>(3, 1),
                                new Item<>(4, 1),
                                new Item<>(5, 1),
                                new Item<>(6, 1),
                                new Item<>(7, 1),
                                new Item<>(8, 1),
                                new Item<>(9, 1)
                        ),
                        Arrays.asList(
                                new Item<>(1, 1),
                                new Item<>(7, 1),
                                new Item<>(5, 1),
                                new Item<>(8, 1),
                                new Item<>(6, 1),
                                new Item<>(4, 1),
                                new Item<>(2, 1),
                                new Item<>(3, 1),
                                new Item<>(9, 1)
                        ))
        );

    }

}