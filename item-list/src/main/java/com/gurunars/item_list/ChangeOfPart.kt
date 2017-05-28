package com.gurunars.item_list

internal abstract class ChangeOfPart<ItemType : Item>(var item: ItemType, sourcePosition: Int, targetPosition: Int) : Change<ItemType> {

    var sourcePosition = -1
    var targetPosition = -1

    init {
        this.sourcePosition = sourcePosition
        this.targetPosition = targetPosition
    }

    override fun hashCode(): Int {
        val hash = javaClass.name.hashCode()
        return hash * sourcePosition +
                sourcePosition * targetPosition +
                hash * targetPosition
    }

    override fun toString(): String {
        return "new " + javaClass.name + "<>(" + item.toString() + ", " + sourcePosition + ", " + targetPosition + ")"
    }

    private fun samePositions(change: ChangeOfPart<*>): Boolean {
        return sourcePosition == change.sourcePosition && targetPosition == change.targetPosition
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other.javaClass == javaClass && samePositions(other as ChangeOfPart<*>)
    }

}

