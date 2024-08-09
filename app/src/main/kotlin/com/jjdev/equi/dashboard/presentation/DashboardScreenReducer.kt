package com.jjdev.equi.dashboard.presentation

import androidx.compose.runtime.Immutable
import com.jjdev.equi.core.base.presentation.Reducer
import com.jjdev.equi.dashboard.presentation.model.AddInvestmentDialogModel
import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import java.math.BigDecimal

class DashboardScreenReducer :
    Reducer<DashboardScreenReducer.DashboardState,
            DashboardScreenReducer.DashboardEvent,
            DashboardScreenReducer.DashboardEffect> {
    @Immutable
    sealed class DashboardEvent : Reducer.ViewEvent {
        data class UpdateRebalanceLoading(val isLoading: Boolean) : DashboardEvent()
        data class UpdateDialog(val show: Boolean) : DashboardEvent()
        data class UpdateAmountToInvest(val amount: String) : DashboardEvent()
        data class AddInvestment(val ticker: String, val value: String, val weight: String) :
            DashboardEvent()

        data class Rebalance(val rebalancedInvestments: ImmutableList<InvestmentUIModel>) :
            DashboardEvent()

        data class UpdateDialogTicker(val ticker: String) : DashboardEvent()
        data class UpdateDialogValue(val value: String) : DashboardEvent()
        data class UpdateDialogWeight(val weight: String) : DashboardEvent()
    }

    @Immutable
    sealed class DashboardEffect : Reducer.ViewEffect

    @Immutable
    data class DashboardState(
        val isLoading: Boolean,
        val dialogShown: Boolean,
        val investments: ImmutableList<InvestmentUIModel>,
        val amountToInvest: Double? = null,
        val addInvestmentDialogModel: AddInvestmentDialogModel? = null,
    ) : Reducer.ViewState {
        companion object {
            fun initial(): DashboardState = DashboardState(
                isLoading = false,
                dialogShown = false,
                investments = persistentListOf(),
                amountToInvest = null,
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
                    investments = (previousState.investments + InvestmentUIModel(
                        ticker = event.ticker,
                        weight = event.weight.toDouble(),
                        value = BigDecimal.valueOf(event.value.toDouble())
                    )).toPersistentList()
                ) to null
            }

            is DashboardEvent.Rebalance -> {
                previousState.copy(
                    investments = event.rebalancedInvestments,
                    isLoading = false
                ) to null
            }

            is DashboardEvent.UpdateAmountToInvest -> {
                previousState.copy(
                    amountToInvest = event.amount.toDouble()
                ) to null
            }

            is DashboardEvent.UpdateDialogTicker -> {
                previousState.copy(
                    addInvestmentDialogModel = previousState.addInvestmentDialogModel?.copy(
                        ticker = event.ticker
                    ) ?: AddInvestmentDialogModel(
                        ticker = event.ticker
                    )
                ) to null
            }

            is DashboardEvent.UpdateDialogValue -> {
                previousState.copy(
                    addInvestmentDialogModel = previousState.addInvestmentDialogModel?.copy(
                        value = event.value
                    ) ?: AddInvestmentDialogModel(
                        value = event.value
                    )
                ) to null
            }

            is DashboardEvent.UpdateDialogWeight -> {
                previousState.copy(
                    addInvestmentDialogModel = previousState.addInvestmentDialogModel?.copy(
                        weight = event.weight
                    ) ?: AddInvestmentDialogModel(
                        weight = event.weight
                    )
                ) to null
            }
        }
    }
}
