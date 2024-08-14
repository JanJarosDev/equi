package com.jjdev.equi.dashboard

import com.jjdev.equi.core.base.domain.Result
import com.jjdev.equi.dashboard.domain.RebalanceUseCase
import com.jjdev.equi.dashboard.domain.model.Investment
import com.jjdev.equi.dashboard.presentation.DashboardScreenReducer.DashboardEvent
import com.jjdev.equi.dashboard.presentation.DashboardViewModel
import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DashboardViewModelTest {

    private val rebalanceUseCase = mockk<RebalanceUseCase>()
    lateinit var viewModel: DashboardViewModel

    @BeforeEach
    fun setup() {
        viewModel = DashboardViewModel(rebalanceUseCase)
    }

    @Test
    fun `on rebalance click should call rebalance use case`() = runTest {
        coEvery { rebalanceUseCase(any()) } returns Result.Success(
            listOf(
                Investment(
                    ticker = "A",
                    weight = BigDecimal.valueOf(0.5),
                    currentValue = BigDecimal.valueOf(100.0),
                    valueToInvest = BigDecimal.valueOf(500.0),
                ),
                Investment(
                    ticker = "B",
                    weight = BigDecimal.valueOf(0.5),
                    currentValue = BigDecimal.valueOf(100.0),
                    valueToInvest = BigDecimal.valueOf(500.0),
                )
            )
        )

        viewModel.onRebalanceClick(
            BigDecimal.valueOf(1000.0), listOf(
                InvestmentUIModel(
                    ticker = "A",
                    weight = 0.5,
                    value = BigDecimal.valueOf(100.0),
                    valueToInvest = BigDecimal.ZERO
                ),
                InvestmentUIModel(
                    ticker = "B",
                    weight = 0.5,
                    value = BigDecimal.valueOf(100.0),
                    valueToInvest = BigDecimal.ZERO
                )
            )
        )

        verifySequence {
            viewModel.sendEvent(DashboardEvent.UpdateRebalanceLoading(isLoading = true))
            viewModel.sendEvent(DashboardEvent.Rebalance(
                persistentListOf(
                    InvestmentUIModel(
                        ticker = "A",
                        weight = 0.5,
                        value = BigDecimal.valueOf(100.0),
                        valueToInvest = BigDecimal.valueOf(500.0),
                    ),
                    InvestmentUIModel(
                        ticker = "B",
                        weight = 0.5,
                        value = BigDecimal.valueOf(100.0),
                        valueToInvest = BigDecimal.valueOf(500.0),
                    )
                )
            ))
        }

    }
}
