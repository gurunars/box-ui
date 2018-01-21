package com.gurunars.state_machine_crawler

interface State<SystemType>: KeyBasedItem {
    val quallifiers: Set<Pair<Qualifier<SystemType>, Boolean>>
    val transitions: Set<Transition<SystemType, State<SystemType>>>
}