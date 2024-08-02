package com.jjdev.equi.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.jjdev.equi.core.base.domain.onSuccess
import com.jjdev.equi.core.base.presentation.BaseViewModel
import com.jjdev.equi.dashboard.domain.RebalanceUseCase
import com.jjdev.equi.dashboard.domain.model.Investment
import com.jjdev.equi.dashboard.presentation.DashboardScreenReducer.DashboardEffect
import com.jjdev.equi.dashboard.presentation.DashboardScreenReducer.DashboardEvent
import com.jjdev.equi.dashboard.presentation.DashboardScreenReducer.DashboardState
import com.jjdev.equi.dashboard.presentation.model.NewInvestment
import com.jjdev.equi.dashboard.presentation.model.RebalancedInvestment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val rebalanceUseCase: RebalanceUseCase,
) : BaseViewModel<DashboardState, DashboardEvent, DashboardEffect>(
    initialState = DashboardState.initial(),
    reducer = DashboardScreenReducer(),
) {
    init {
        Timber.i("Initializing DashboardViewModel")
    }

    fun onRebalanceClick(amountToInvest: Double, investments: List<NewInvestment>) {
        viewModelScope.launch {
            rebalanceUseCase(amountToInvest to investments.toInvestmentList()).onSuccess {
                Timber.i("Rebalanced investments: $it")
                sendEvent(DashboardEvent.Rebalance(it.toRebalancedInvestmentList()))
            }
        }
    }

    private fun NewInvestment.toInvestment(): Investment {
        return Investment(
            ticker = this.ticker,
            weight = this.percentage.toDouble() / 100,
            currentValue = this.amount,
            investedAmount = null,
            targetValue = null
        )
    }

    private fun List<NewInvestment>.toInvestmentList(): List<Investment> {
        return this.map { it.toInvestment() }
    }

    private fun Investment.toRebalancedInvestment(): RebalancedInvestment {
        return RebalancedInvestment(
            ticker = this.ticker,
            percentage = (this.weight * 100).toInt(),
            amount = this.currentValue,
            newAmount = this.investedAmount ?: 0.0
        )
    }

    private fun List<Investment>.toRebalancedInvestmentList(): List<RebalancedInvestment> {
        return this.map { it.toRebalancedInvestment() }
    }
}
