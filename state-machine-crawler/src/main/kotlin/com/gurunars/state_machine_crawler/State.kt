package com.gurunars.state_machine_crawler

import kotlin.reflect.KClass

interface State<SystemType> {
    val quallifiers: List<Pair<KClass<Qualifier<SystemType>>, Boolean>>
    val transitions: List<Transition<SystemType, State<SystemType>>>
}