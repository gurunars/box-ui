package com.gurunars.item_list

import org.junit.Test

import java.util.Arrays

import org.junit.Assert.assertEquals

class FetcherComplexPermutationTest {

    private val fetcher = FetcherComplexPermutation<AnimalItem>()

    @Test
    fun getNormal() {
        assertEquals(fetcher.apply(
                Arrays.asList(
                        AnimalItem(1, 1),
                        AnimalItem(2, 1),
                        AnimalItem(3, 1),
                        AnimalItem(4, 1)
                ),
                Arrays.asList(
                        AnimalItem(1, 1),
                        AnimalItem(3, 1),
                        AnimalItem(2, 1),
                        AnimalItem(4, 1)
                ), 0),
                ChangeComplexPermutation(1, Arrays.asList(
                        AnimalItem(3, 1),
                        AnimalItem(2, 1)
                ))
        )
    }
}