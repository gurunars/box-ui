package com.gurunars.item_list

import android.support.v7.widget.RecyclerView

internal class ChangeComplexPermutation<ItemType : Item>// start is inclusive
(private val startPosition: Int, private val mutatedSubList: List<ItemType>) : Change<ItemType> {

    override fun apply(adapter: RecyclerView.Adapter<*>, scroller: Scroller, items: MutableList<ItemType>,
                       currentPosition: Int): Int {
        items.subList(startPosition, startPosition + mutatedSubList.size).clear()
        items.addAll(startPosition, mutatedSubList)
        adapter.notifyItemRangeChanged(startPosition, mutatedSubList.size)
        return currentPosition
    }

    override fun hashCode(): Int {
        val hash = javaClass.name.hashCode()
        return hash * startPosition +
                startPosition * mutatedSubList.hashCode() +
                hash * mutatedSubList.hashCode()
    }

    override fun toString(): String {
        return "new " + javaClass.name + "<>(" + startPosition + ", " + mutatedSubList.toString() + ")"
    }

    private fun sameRanges(change: ChangeComplexPermutation<*>): Boolean {
        return startPosition == change.startPosition && mutatedSubList == change.mutatedSubList
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other.javaClass == javaClass && sameRanges(other as ChangeComplexPermutation<*>)
    }
}
