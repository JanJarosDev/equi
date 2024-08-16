package com.jjdev.equi.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.jjdev.equi.core.base.domain.onSuccess
import com.jjdev.equi.core.base.presentation.BaseViewModel
import com.jjdev.equi.dashboard.domain.RebalanceUseCase
import com.jjdev.equi.dashboard.domain.model.Investment
import com.jjdev.equi.dashboard.presentation.DashboardReducer.DashboardEffect
import com.jjdev.equi.dashboard.presentation.DashboardReducer.DashboardEvent
import com.jjdev.equi.dashboard.presentation.DashboardReducer.DashboardState
import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

private const val PERCENTAGE_DIVISOR = 100

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val rebalanceUseCase: RebalanceUseCase,
) : BaseViewModel<DashboardState, DashboardEvent, DashboardEffect>(
    initialState = DashboardState.initial(),
    reducer = DashboardReducer(),
) {
    init {
        Timber.i("Initializing DashboardViewModel")
    }

    fun onRebalanceClick(amountToInvest: BigDecimal, investments: List<InvestmentUIModel>) {
        sendEvent(DashboardEvent.UpdateRebalanceLoading(isLoading = true))
        viewModelScope.launch {
            rebalanceUseCase(amountToInvest to investments.toInvestmentDomainModelList()).onSuccess {
                Timber.i("Rebalanced investments: $it")
                sendEvent(DashboardEvent.Rebalance(it.toInvestmentUIModel()))
            }
        }
    }

    private fun InvestmentUIModel.toInvestmentDomainModel(): Investment {
        return Investment(
            ticker = this.ticker,
            weight = BigDecimal.valueOf(this.weight / PERCENTAGE_DIVISOR),
            currentValue = this.value,
            valueToInvest = null,
            targetValue = null
        )
    }

    private fun List<InvestmentUIModel>.toInvestmentDomainModelList(): ImmutableList<Investment> {
        return this.map { it.toInvestmentDomainModel() }.toPersistentList()
    }

    private fun Investment.toInvestmentUIModel(): InvestmentUIModel {
        return InvestmentUIModel(
            ticker = this.ticker,
            weight = this.weight.toDouble(),
            value = this.currentValue,
            valueToInvest = this.valueToInvest ?: BigDecimal.ZERO
        )
    }

    private fun List<Investment>.toInvestmentUIModel(): ImmutableList<InvestmentUIModel> {
        return this.map { it.toInvestmentUIModel() }.toPersistentList()
    }
}
