package com.gurunars.crud_item_list

internal fun isSolidChunk(positions: List<Int>) =
    positions.isNotEmpty() && (1 until positions.size).none { positions[it] - positions[it - 1] != 1 }

