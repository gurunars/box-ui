package com.gurunars.state_machine_crawler

import kotlin.reflect.KClass

class StateMachineCrawler<SystemType>(
    private val rootState: State<SystemType>
) {

    private val qualifiers: List<Qualifier<SystemType>> = flattenGraph(
        { it.dependencies.map { it.first } },
        rootState.quallifiers.map { it.first }
    )
    private val states: List<State<SystemType>> = flattenGraph(
        { it.transitions.map { it.target }},
        listOf(rootState)
    )

}