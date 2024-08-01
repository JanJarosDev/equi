package com.jjdev.equi.dashboard.presentation

import androidx.compose.runtime.Immutable
import com.jjdev.equi.core.base.domain.onSuccess
import com.jjdev.equi.core.base.presentation.Reducer
import com.jjdev.equi.dashboard.domain.RebalanceUseCase
import com.jjdev.equi.dashboard.domain.model.Investment
import com.jjdev.equi.dashboard.presentation.model.NewInvestment
import com.jjdev.equi.dashboard.presentation.model.RebalancedInvestment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DashboardScreenReducer:
    Reducer<DashboardScreenReducer.DashboardState, DashboardScreenReducer.DashboardEvent, DashboardScreenReducer.DashboardEffect> {
    @Immutable
    sealed class DashboardEvent : Reducer.ViewEvent {
        data class UpdateRebalanceLoading(val isLoading: Boolean) : DashboardEvent()
        data class UpdateDialog(val show: Boolean) : DashboardEvent()
        data class AddInvestment(val newInvestment: NewInvestment) : DashboardEvent()
        data class Rebalance(val rebalancedInvestments: List<RebalancedInvestment>) : DashboardEvent()
    }

    @Immutable
    sealed class DashboardEffect : Reducer.ViewEffect {
    }

    @Immutable
    data class DashboardState(
        val isLoading: Boolean,
        val dialogShown: Boolean,
        val investments: List<NewInvestment>,
        val rebalancedInvestments: List<RebalancedInvestment>,
    ) : Reducer.ViewState {
        companion object {
            fun initial(): DashboardState = DashboardState(
                isLoading = false,
                dialogShown = false,
                investments = emptyList(),
                rebalancedInvestments = emptyList()
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

            is DashboardEvent.AddInvestment -> {
                previousState.copy(
                    investments = previousState.investments + event.newInvestment
                ) to null
            }

            is DashboardEvent.Rebalance -> {
                previousState.copy(
                    rebalancedInvestments = event.rebalancedInvestments
                ) to null
            }
        }
    }
}