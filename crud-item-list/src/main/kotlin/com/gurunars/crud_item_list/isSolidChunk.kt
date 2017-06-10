package com.gurunars.crud_item_list

internal fun isSolidChunk(positions: List<Int>) =
    positions.isNotEmpty() && (1..positions.size - 1).none { positions[it] - positions[it - 1] != 1 }

