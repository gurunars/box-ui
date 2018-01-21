package com.gurunars.state_machine_crawler

interface Qualifier<SystemType>: KeyBasedItem {
    fun SystemType.assert(): Boolean
    val dependencies: Set<Pair<Qualifier<SystemType>, Boolean>>
}
