package com.jjdev.equi.dashboard.presentation

import androidx.compose.runtime.Immutable
import com.jjdev.equi.core.base.presentation.Reducer
import com.jjdev.equi.dashboard.domain.model.Investment

class DashboardScreenReducer :
    Reducer<DashboardScreenReducer.DashboardState, DashboardScreenReducer.DashboardEvent, DashboardScreenReducer.DashboardEffect> {
    @Immutable
    sealed class DashboardEvent : Reducer.ViewEvent {
        data class UpdateRebalanceLoading(val isLoading: Boolean) : DashboardEvent()
        data class UpdateDialog(val show: Boolean) : DashboardEvent()
    }

    @Immutable
    sealed class DashboardEffect : Reducer.ViewEffect {
    }

    @Immutable
    data class DashboardState(
        val isLoading: Boolean,
        val dialogShown: Boolean,
        val investments: List<Investment>,
    ) : Reducer.ViewState {
        companion object {
            fun initial(): DashboardState = DashboardState(
                isLoading = false,
                dialogShown = false,
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
                ) to null
            }

            is DashboardEvent.UpdateDialog -> {
                previousState.copy(
                    dialogShown = event.show
                ) to null

            }
        }
    }
}