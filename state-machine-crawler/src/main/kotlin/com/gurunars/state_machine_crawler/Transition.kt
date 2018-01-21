package com.gurunars.state_machine_crawler

interface Transition<SystemType, out TargetState: State<SystemType>>: KeyBasedItem {
    val expectedDuration: Int
        get() = 0
    val target: TargetState
    fun perform(system: SystemType): Unit
}
