package com.jjdev.equi.dashboard.presentation

import androidx.compose.runtime.Immutable
import com.jjdev.equi.core.base.presentation.Reducer
import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

class DashboardScreenReducer :
    Reducer<DashboardScreenReducer.DashboardState,
            DashboardScreenReducer.DashboardEvent,
            DashboardScreenReducer.DashboardEffect> {
    @Immutable
    sealed class DashboardEvent : Reducer.ViewEvent {
        data class UpdateRebalanceLoading(val isLoading: Boolean) : DashboardEvent()
        data class UpdateDialog(val show: Boolean) : DashboardEvent()
        data class AddInvestment(val newInvestment: InvestmentUIModel) : DashboardEvent()
        data class Rebalance(val rebalancedInvestments: ImmutableList<InvestmentUIModel>) :
            DashboardEvent()
    }

    @Immutable
    sealed class DashboardEffect : Reducer.ViewEffect

    @Immutable
    data class DashboardState(
        val isLoading: Boolean,
        val dialogShown: Boolean,
        val investments: ImmutableList<InvestmentUIModel>,
    ) : Reducer.ViewState {
        companion object {
            fun initial(): DashboardState = DashboardState(
                isLoading = false,
                dialogShown = false,
                investments = persistentListOf(),
            )
        }
    }

    override fun reduce(
        previousState: DashboardState,
        event: DashboardEvent,
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
                    investments = (previousState.investments + event.newInvestment).toPersistentList()
                ) to null
            }

            is DashboardEvent.Rebalance -> {
                previousState.copy(
                    investments = event.rebalancedInvestments,
                    isLoading = false
                ) to null
            }
        }
    }
}
