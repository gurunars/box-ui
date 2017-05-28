package com.gurunars.item_list

import org.junit.Test

import java.util.ArrayList
import java.util.Arrays

import org.junit.Assert.assertEquals

class PartitionerTest {

    private val optimizer = Partitioner<Int>()

    private fun checkOffsets(expectedStartOffset: Int, expectedEndOffset: Int,
                             one: List<Int>, two: List<Int>) {
        assertEquals(expectedStartOffset.toLong(), optimizer.getStartOffset(one, two).toLong())
        assertEquals(expectedEndOffset.toLong(), optimizer.getEndOffset(one, two).toLong())
    }

    @Test
    @Throws(Exception::class)
    fun getOffsetsWithRemoval() {
        checkOffsets(6, 2,
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10, 11, 12))
    }

    @Test
    @Throws(Exception::class)
    fun getOffsetsWithReplacement() {
        checkOffsets(6, 2,
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 14, 16, 10, 11, 12))
    }

    @Test
    @Throws(Exception::class)
    fun getOffsetsWithPrepend() {
        checkOffsets(0, 11,
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
                Arrays.asList(16, 17, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
    }

    @Test
    @Throws(Exception::class)
    fun getOffsetsWithAppend() {
        checkOffsets(11, 0,
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 16, 17))
    }

    @Test
    @Throws(Exception::class)
    fun getOffsetsWithPrependAndRemove() {
        checkOffsets(0, 9,
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
                Arrays.asList(16, 17, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
    }

    @Test
    @Throws(Exception::class)
    fun getOffsetsWithAppendAndRemove() {
        checkOffsets(9, 0,
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17))
    }

    @Test
    @Throws(Exception::class)
    fun getPartition() {
        val partition = Partitioner.Partition(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), 4, 4
        )

        assertEquals(Arrays.asList(1, 2, 3, 4), partition.head)
        assertEquals(Arrays.asList(5, 6, 7, 8), partition.middle)
        assertEquals(Arrays.asList(9, 10, 11, 12), partition.tail)
    }

    @Test
    @Throws(Exception::class)
    fun getPartitionEmptyTail() {
        val partition = Partitioner.Partition(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), 4, 0
        )

        assertEquals(Arrays.asList(1, 2, 3, 4), partition.head)
        assertEquals(Arrays.asList(5, 6, 7, 8, 9, 10, 11, 12), partition.middle)
        assertEquals(ArrayList<Int>(), partition.tail)
    }

    @Test
    @Throws(Exception::class)
    fun getPartitionEmptyHead() {
        val partition = Partitioner.Partition(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), 0, 4
        )

        assertEquals(ArrayList<Int>(), partition.head)
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), partition.middle)
        assertEquals(Arrays.asList(9, 10, 11, 12), partition.tail)
    }

    @Test
    @Throws(Exception::class)
    fun getPartitionEmptyMiddle() {
        val partition = Partitioner.Partition(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), 6, 6
        )

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), partition.head)
        assertEquals(ArrayList<Int>(), partition.middle)
        assertEquals(Arrays.asList(7, 8, 9, 10, 11, 12), partition.tail)
    }

}