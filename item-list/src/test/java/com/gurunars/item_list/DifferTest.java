package com.gurunars.item_list;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;


public class DifferTest {

    private Differ<TestItem> differ = new Differ<>();

    @Test
    public void itemCreate_shouldProduceSingleItemListWithCreation() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeCreate<>(new TestItem(7, 1), 6, 6)),
                differ.apply(
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(2, 1),
                                new TestItem(3, 1),
                                new TestItem(4, 1),
                                new TestItem(5, 1),
                                new TestItem(6, 1)
                        ),
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(2, 1),
                                new TestItem(3, 1),
                                new TestItem(4, 1),
                                new TestItem(5, 1),
                                new TestItem(6, 1),
                                new TestItem(7, 1)
                        ))
        );

    }

    @Test
    public void itemDelete_shouldProduceSingleItemListWithRemoval() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeDelete<>(new TestItem(3, 1), 2, 2)),
                differ.apply(
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(2, 1),
                                new TestItem(3, 1),
                                new TestItem(4, 1),
                                new TestItem(5, 1),
                                new TestItem(6, 1)
                        ),
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(2, 1),
                                new TestItem(4, 1),
                                new TestItem(5, 1),
                                new TestItem(6, 1)
                        ))
        );

    }

    @Test
    public void moveUp_shouldProduceSingleItemList() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeMove<>(new TestItem(5, 1), 4, 1)),
                differ.apply(
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(2, 1),
                                new TestItem(3, 1),
                                new TestItem(4, 1),
                                new TestItem(5, 1),
                                new TestItem(6, 1)
                        ),
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(5, 1),  // moved up
                                new TestItem(2, 1),
                                new TestItem(3, 1),
                                new TestItem(4, 1),
                                new TestItem(6, 1)
                        ))
        );

    }

    @Test
    public void moveDown_shouldProduceSingleItemList() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeMove<>(new TestItem(2, 1), 1, 4)),
                differ.apply(
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(2, 1),
                                new TestItem(3, 1),
                                new TestItem(4, 1),
                                new TestItem(5, 1),
                                new TestItem(6, 1)
                        ),
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(3, 1),
                                new TestItem(4, 1),
                                new TestItem(5, 1),
                                new TestItem(2, 1), // moved down
                                new TestItem(6, 1)
                        ))
        );
    }

    @Test
    public void itemEdit_shouldProduceSingleItemListWithUpdate() throws Exception {
        assertEquals(
                Collections.singletonList(
                        new ChangeUpdate<>(new TestItem(3, 2), 2, 2)),
                differ.apply(
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(2, 1),
                                new TestItem(3, 1),
                                new TestItem(4, 1),
                                new TestItem(5, 1),
                                new TestItem(6, 1)
                        ),
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(2, 1),
                                new TestItem(3, 2), // updated
                                new TestItem(4, 1),
                                new TestItem(5, 1),
                                new TestItem(6, 1)
                        ))
        );

    }

    @Test
    public void simpleDiff_isCorrect() throws Exception {

        assertEquals(
                Arrays.asList(
                        new ChangeDelete<>(new TestItem(5, 1), 4, 4),
                        new ChangeDelete<>(new TestItem(2, 1), 1, 1),
                        new ChangeCreate<>(new TestItem(7, 2), 0, 0),
                        new ChangeCreate<>(new TestItem(9, 1), 4, 4),
                        new ChangeMove<>(new TestItem(1, 1), 1, 3),
                        new ChangeMove<>(new TestItem(3, 1), 1, 2),
                        new ChangeUpdate<>(new TestItem(1, 2), 3, 3),
                        new ChangeUpdate<>(new TestItem(3, 2), 2, 2)
                ),
                differ.apply(
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(2, 1),
                                new TestItem(3, 1),
                                new TestItem(4, 1),
                                new TestItem(5, 1)
                        ),
                        Arrays.asList(
                                new TestItem(7, 2),
                                new TestItem(4, 1),
                                new TestItem(3, 2),
                                new TestItem(1, 2),
                                new TestItem(9, 1)
                        ))
        );

    }

    @Test
    public void tooManyMoves_resultInComplexPermutation() {

        assertEquals(
                Collections.singletonList(new ChangeComplexPermutation<>(1, Arrays.asList(
                        new TestItem(7, 1),
                        new TestItem(5, 1),
                        new TestItem(8, 1),
                        new TestItem(6, 1),
                        new TestItem(4, 1),
                        new TestItem(2, 1),
                        new TestItem(3, 1)
                ))),
                differ.apply(
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(2, 1),
                                new TestItem(3, 1),
                                new TestItem(4, 1),
                                new TestItem(5, 1),
                                new TestItem(6, 1),
                                new TestItem(7, 1),
                                new TestItem(8, 1),
                                new TestItem(9, 1)
                        ),
                        Arrays.asList(
                                new TestItem(1, 1),
                                new TestItem(7, 1),
                                new TestItem(5, 1),
                                new TestItem(8, 1),
                                new TestItem(6, 1),
                                new TestItem(4, 1),
                                new TestItem(2, 1),
                                new TestItem(3, 1),
                                new TestItem(9, 1)
                        ))
        );

    }

}