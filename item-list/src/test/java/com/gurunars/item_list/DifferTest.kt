package com.gurunars.item_list

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*


class DifferTest {

    private val differ = Differ<AnimalItem>()

    @Test
    @Throws(Exception::class)
    fun itemCreate_shouldProduceSingleItemListWithCreation() {
        assertEquals(
                listOf(ChangeCreate(AnimalItem(7, 1), 6, 6)),
                differ.apply(
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(2, 1),
                                AnimalItem(3, 1),
                                AnimalItem(4, 1),
                                AnimalItem(5, 1),
                                AnimalItem(6, 1)
                        ),
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(2, 1),
                                AnimalItem(3, 1),
                                AnimalItem(4, 1),
                                AnimalItem(5, 1),
                                AnimalItem(6, 1),
                                AnimalItem(7, 1)
                        ))
        )

    }

    @Test
    @Throws(Exception::class)
    fun itemDelete_shouldProduceSingleItemListWithRemoval() {
        assertEquals(
                listOf(ChangeDelete(AnimalItem(3, 1), 2, 2)),
                differ.apply(
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(2, 1),
                                AnimalItem(3, 1),
                                AnimalItem(4, 1),
                                AnimalItem(5, 1),
                                AnimalItem(6, 1)
                        ),
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(2, 1),
                                AnimalItem(4, 1),
                                AnimalItem(5, 1),
                                AnimalItem(6, 1)
                        ))
        )

    }

    @Test
    @Throws(Exception::class)
    fun moveUp_shouldProduceSingleItemList() {
        assertEquals(
                listOf(ChangeMove(AnimalItem(5, 1), 4, 1)),
                differ.apply(
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(2, 1),
                                AnimalItem(3, 1),
                                AnimalItem(4, 1),
                                AnimalItem(5, 1),
                                AnimalItem(6, 1)
                        ),
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(5, 1), // moved up
                                AnimalItem(2, 1),
                                AnimalItem(3, 1),
                                AnimalItem(4, 1),
                                AnimalItem(6, 1)
                        ))
        )

    }

    @Test
    @Throws(Exception::class)
    fun moveDown_shouldProduceSingleItemList() {
        assertEquals(
                listOf(ChangeMove(AnimalItem(2, 1), 1, 4)),
                differ.apply(
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(2, 1),
                                AnimalItem(3, 1),
                                AnimalItem(4, 1),
                                AnimalItem(5, 1),
                                AnimalItem(6, 1)
                        ),
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(3, 1),
                                AnimalItem(4, 1),
                                AnimalItem(5, 1),
                                AnimalItem(2, 1), // moved down
                                AnimalItem(6, 1)
                        ))
        )
    }

    @Test
    @Throws(Exception::class)
    fun itemEdit_shouldProduceSingleItemListWithUpdate() {
        assertEquals(
                listOf(ChangeUpdate(AnimalItem(3, 2), 2, 2)),
                differ.apply(
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(2, 1),
                                AnimalItem(3, 1),
                                AnimalItem(4, 1),
                                AnimalItem(5, 1),
                                AnimalItem(6, 1)
                        ),
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(2, 1),
                                AnimalItem(3, 2), // updated
                                AnimalItem(4, 1),
                                AnimalItem(5, 1),
                                AnimalItem(6, 1)
                        ))
        )

    }

    @Test
    @Throws(Exception::class)
    fun simpleDiff_isCorrect() {

        assertEquals(
                Arrays.asList(
                        ChangeDelete(AnimalItem(5, 1), 4, 4),
                        ChangeDelete(AnimalItem(2, 1), 1, 1),
                        ChangeCreate(AnimalItem(7, 2), 0, 0),
                        ChangeCreate(AnimalItem(9, 1), 4, 4),
                        ChangeMove(AnimalItem(1, 1), 1, 3),
                        ChangeMove(AnimalItem(3, 1), 1, 2),
                        ChangeUpdate(AnimalItem(1, 2), 3, 3),
                        ChangeUpdate(AnimalItem(3, 2), 2, 2)
                ),
                differ.apply(
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(2, 1),
                                AnimalItem(3, 1),
                                AnimalItem(4, 1),
                                AnimalItem(5, 1)
                        ),
                        Arrays.asList(
                                AnimalItem(7, 2),
                                AnimalItem(4, 1),
                                AnimalItem(3, 2),
                                AnimalItem(1, 2),
                                AnimalItem(9, 1)
                        ))
        )

    }

    @Test
    fun tooManyMoves_resultInComplexPermutation() {

        assertEquals(
                listOf(ChangeComplexPermutation(1, Arrays.asList(
                        AnimalItem(7, 1),
                        AnimalItem(5, 1),
                        AnimalItem(8, 1),
                        AnimalItem(6, 1),
                        AnimalItem(4, 1),
                        AnimalItem(2, 1),
                        AnimalItem(3, 1)
                ))),
                differ.apply(
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(2, 1),
                                AnimalItem(3, 1),
                                AnimalItem(4, 1),
                                AnimalItem(5, 1),
                                AnimalItem(6, 1),
                                AnimalItem(7, 1),
                                AnimalItem(8, 1),
                                AnimalItem(9, 1)
                        ),
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(7, 1),
                                AnimalItem(5, 1),
                                AnimalItem(8, 1),
                                AnimalItem(6, 1),
                                AnimalItem(4, 1),
                                AnimalItem(2, 1),
                                AnimalItem(3, 1),
                                AnimalItem(9, 1)
                        ))
        )

    }

    @Test
    fun movesDeltaOne() {
        assertEquals(
                listOf(ChangeMove(AnimalItem(4, 2), 3, 4)),
                differ.apply(
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(2, 1),
                                AnimalItem(3, 1),
                                AnimalItem(4, 2),
                                AnimalItem(5, 1),
                                AnimalItem(6, 1),
                                AnimalItem(7, 1),
                                AnimalItem(8, 1),
                                AnimalItem(9, 1)
                        ),
                        Arrays.asList(
                                AnimalItem(1, 1),
                                AnimalItem(2, 1),
                                AnimalItem(3, 1),
                                AnimalItem(5, 1),
                                AnimalItem(4, 2),
                                AnimalItem(6, 1),
                                AnimalItem(7, 1),
                                AnimalItem(8, 1),
                                AnimalItem(9, 1)
                        ))
        )
    }

}