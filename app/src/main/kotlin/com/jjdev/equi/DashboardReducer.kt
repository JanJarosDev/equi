package com.jjdev.equi

import androidx.compose.runtime.Immutable
import com.jjdev.equi.core.base.presentation.Reducer

class DashboardScreenReducer :
    Reducer<DashboardScreenReducer.DashboardState, DashboardScreenReducer.DashboardEvent, DashboardScreenReducer.DashboardEffect> {
    @Immutable
    sealed class DashboardEvent : Reducer.ViewEvent {
        data class UpdateRebalanceLoading(val isLoading: Boolean) : DashboardEvent()
    }

    @Immutable
    sealed class DashboardEffect : Reducer.ViewEffect {
        object Rebalance : DashboardEffect()
    }

    @Immutable
    data class DashboardState(
        val isLoading: Boolean,
        val investments: List<Investment>,
    ) : Reducer.ViewState {
        companion object {
            fun initial(): DashboardState = DashboardState(
                isLoading = false,
                investments = emptyList()
            )
        }
    }

    override fun reduce(
        previousState: DashboardState,
        event: DashboardEvent
    ): Pair<DashboardState, DashboardEffect?> {
        return when (event) {
            is DashboardEvent.UpdateRebalanceLoading -> {
                previousState.copy(
                    isLoading = event.isLoading
                ) to DashboardEffect.Rebalance
            }
        }
    }
}