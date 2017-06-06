package com.gurunars.crud_item_list

import java8.util.function.Function

internal class CheckerSolidChunk : Function<List<Int>, Boolean> {

    override fun apply(positions: List<Int>) =
        positions.isNotEmpty() &&
        (1..positions.size - 1).none { positions[it] - positions[it - 1] != 1 }

}
