package com.gurunars.state_machine_crawler

import kotlin.reflect.KClass

interface Transition<SystemType, TargetState: State<SystemType>> {
    val target: KClass<TargetState>
    fun perform(system: SystemType): Unit
}
