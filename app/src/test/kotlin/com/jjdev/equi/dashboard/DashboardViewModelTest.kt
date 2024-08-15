package com.jjdev.equi.dashboard

import app.cash.turbine.test
import com.jjdev.equi.core.base.domain.Result
import com.jjdev.equi.dashboard.domain.RebalanceUseCase
import com.jjdev.equi.dashboard.domain.model.Investment
import com.jjdev.equi.dashboard.presentation.DashboardScreenReducer
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

    private val rebalanceUseCase = mockk<RebalanceUseCase>()
    private lateinit var viewModel: DashboardViewModel

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

        viewModel.event.test {
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

            advanceUntilIdle()

            assertEquals(
                DashboardScreenReducer.DashboardEvent.UpdateRebalanceLoading(isLoading = true),
                awaitItem()
            )

            assertEquals(
                DashboardScreenReducer.DashboardEvent.Rebalance(
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
                ),
                awaitItem()
            )
        }
    }
}
