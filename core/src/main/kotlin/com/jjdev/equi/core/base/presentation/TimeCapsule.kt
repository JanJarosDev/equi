package com.jjdev.equi.core.base.presentation

interface TimeCapsule<State : Reducer.ViewState> {
    fun addState(state: State)
    fun selectState(position: Int)
    fun getStates(): List<State>
}

class TimeTravelCapsule<State : Reducer.ViewState>(
    private val onStateSelected: (State) -> Unit
) : TimeCapsule<State> {

    private val states = mutableListOf<State>()

    override fun addState(state: State) {
        states.add(state)
    }

    override fun selectState(position: Int) {
        onStateSelected(states[position])
    }

    override fun getStates(): List<State> {
        return states
    }
}
