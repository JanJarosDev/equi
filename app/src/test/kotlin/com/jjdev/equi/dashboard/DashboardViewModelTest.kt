package com.jjdev.equi.dashboard

import app.cash.turbine.test
import com.jjdev.equi.core.base.domain.Result
import com.jjdev.equi.dashboard.domain.RebalanceUseCase
import com.jjdev.equi.dashboard.domain.model.Investment
import com.jjdev.equi.dashboard.presentation.DashboardReducer
import com.jjdev.equi.dashboard.presentation.DashboardViewModel
import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DashboardViewModelTest {

    private companion object {
        const val TICKER_A = "A"
        const val TICKER_B = "B"
        const val TICKER_C = "C"
        val WEIGHT_0_5: BigDecimal = BigDecimal.valueOf(0.5)
        val WEIGHT_0_0: BigDecimal = BigDecimal.valueOf(0.0)
        val VALUE_100: BigDecimal = BigDecimal.valueOf(100.0)
        val VALUE_500: BigDecimal = BigDecimal.valueOf(500.0)
        val VALUE_ZERO: BigDecimal = BigDecimal.ZERO
        val TOTAL_VALUE_1000: BigDecimal = BigDecimal.valueOf(1000.0)
    }

    private val rebalanceUseCase = mockk<RebalanceUseCase>()
    private lateinit var viewModel: DashboardViewModel

    private val investmentsListMock = listOf(
        Investment(
            ticker = TICKER_A,
            weight = WEIGHT_0_5,
            currentValue = VALUE_100,
            valueToInvest = VALUE_500,
        ),
        Investment(
            ticker = TICKER_B,
            weight = WEIGHT_0_5,
            currentValue = VALUE_100,
            valueToInvest = VALUE_500,
        ),
        Investment(
            ticker = TICKER_C,
            weight = WEIGHT_0_0,
            currentValue = VALUE_100,
            valueToInvest = null,
        ),
    )

    private val investmentsUiModelListMock = listOf(
        InvestmentUIModel(
            ticker = TICKER_A,
            weight = WEIGHT_0_5.toDouble(),
            value = VALUE_100,
            valueToInvest = VALUE_ZERO,
        ),
        InvestmentUIModel(
            ticker = TICKER_B,
            weight = WEIGHT_0_5.toDouble(),
            value = VALUE_100,
            valueToInvest = VALUE_ZERO,
        ),
        InvestmentUIModel(
            ticker = TICKER_C,
            weight = WEIGHT_0_0.toDouble(),
            value = VALUE_100,
            valueToInvest = VALUE_ZERO
        ),
    )

    private val rebalancedInvestmentsUiModelListMock = persistentListOf(
        InvestmentUIModel(
            ticker = TICKER_A,
            weight = WEIGHT_0_5.toDouble(),
            value = VALUE_100,
            valueToInvest = VALUE_500,
        ),
        InvestmentUIModel(
            ticker = TICKER_B,
            weight = WEIGHT_0_5.toDouble(),
            value = VALUE_100,
            valueToInvest = VALUE_500,
        ),
        InvestmentUIModel(
            ticker = TICKER_C,
            weight = WEIGHT_0_0.toDouble(),
            value = VALUE_100,
            valueToInvest = VALUE_ZERO
        ),
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = DashboardViewModel(rebalanceUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `on rebalance click should call rebalance use case`() = runTest {
        coEvery { rebalanceUseCase(any()) } returns Result.Success(investmentsListMock)

        viewModel.event.test {
            viewModel.onRebalanceClick(
                TOTAL_VALUE_1000,
                investmentsUiModelListMock,
            )

            advanceUntilIdle()

            assertEquals(
                DashboardReducer.DashboardEvent.UpdateRebalanceLoading(isLoading = true),
                awaitItem(),
            )

            assertEquals(
                DashboardReducer.DashboardEvent.Rebalance(
                    rebalancedInvestmentsUiModelListMock
                ),
                awaitItem(),
            )
        }
    }
}
