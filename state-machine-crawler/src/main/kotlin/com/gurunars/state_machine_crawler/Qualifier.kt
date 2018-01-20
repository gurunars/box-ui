package com.gurunars.state_machine_crawler

import kotlin.reflect.KClass

interface Qualifier<SystemType> {
    fun assert(system: SystemType): Boolean
    val dependencies: List<Pair<KClass<Qualifier<SystemType>>, Boolean>>
}
