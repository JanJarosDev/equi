package com.jjdev.equi.dashboard

import com.jjdev.equi.dashboard.presentation.DashboardReducer
import com.jjdev.equi.dashboard.presentation.model.AddInvestmentDialogModel
import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DashboardReducerTest {

    private lateinit var reducer: DashboardReducer

    private val investmentsUIModelListMock = persistentListOf(
        InvestmentUIModel(
            ticker = "AAPL",
            weight = 0.5,
            value = BigDecimal.valueOf(100.0)
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        reducer = DashboardReducer()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `UpdateRebalanceLoading true event should update isLoading to true`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(isLoading = true) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(isLoading = false),
                event = DashboardReducer.DashboardEvent.UpdateRebalanceLoading(isLoading = true),
            ),
        )
    }

    @Test
    fun `UpdateRebalanceLoading false event should update isLoading to false`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(isLoading = false) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(isLoading = true),
                event = DashboardReducer.DashboardEvent.UpdateRebalanceLoading(isLoading = false),
            ),
        )
    }

    @Test
    fun `UpdateDialog true event should update dialogShown to true`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(dialogShown = true) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(dialogShown = false),
                event = DashboardReducer.DashboardEvent.UpdateDialog(show = true),
            ),
        )
    }

    @Test
    fun `UpdateDialog false event should update dialogShown to false`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(
                dialogShown = false
            ) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(dialogShown = true),
                event = DashboardReducer.DashboardEvent.UpdateDialog(show = false),
            )
        )
    }

    @Test
    fun `UpdateAmountToInvest event should set amountToInvest to amountToInvest`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(amountToInvest = 100.0) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(
                    amountToInvest = null
                ),
                event = DashboardReducer.DashboardEvent.UpdateAmountToInvest(amount = "100"),
            ),
        )
    }

    @Test
    fun `AddInvestment event should add investment to investments`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(
                investments = investmentsUIModelListMock
            ) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(
                    investments = persistentListOf()
                ),
                event = DashboardReducer.DashboardEvent.AddInvestment(
                    ticker = "AAPL",
                    value = "100",
                    weight = "0.5"
                ),
            )
        )
    }

    @Test
    fun `Rebalance event should set investments to rebalancedInvestments`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(
                investments = investmentsUIModelListMock
            ) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(
                    investments = persistentListOf()
                ),
                event = DashboardReducer.DashboardEvent.Rebalance(
                    rebalancedInvestments = investmentsUIModelListMock
                )
            )
        )
    }

    @Test
    fun `UpdateDialogTicker event should update ticker in null addInvestmentDialogModel`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(
                addInvestmentDialogModel = AddInvestmentDialogModel(
                    ticker = "AAPL"
                )
                ) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(
                    addInvestmentDialogModel = null
                ),
                event = DashboardReducer.DashboardEvent.UpdateDialogTicker(
                    ticker = "AAPL"
                )
            )
        )
    }

    @Test
    fun `UpdateDialogValue event should update value in null addInvestmentDialogModel`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(
                addInvestmentDialogModel = AddInvestmentDialogModel(
                    value = "100"
                )
            ) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(
                    addInvestmentDialogModel = null
                ),
                event = DashboardReducer.DashboardEvent.UpdateDialogValue(
                    value = "100"
                )
            )
        )
    }

    @Test
    fun `UpdateDialogWeight event should update weight in null addInvestmentDialogModel`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(
                addInvestmentDialogModel = AddInvestmentDialogModel(
                    weight = "0.5"
                )
            ) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(
                    addInvestmentDialogModel = null
                ),
                event = DashboardReducer.DashboardEvent.UpdateDialogWeight(
                    weight = "0.5"
                )
            )
        )
    }

    @Test
    fun `UpdateDialogTicker event should update ticker in non null addInvestmentDialogModel`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(
                addInvestmentDialogModel = AddInvestmentDialogModel(
                    ticker = "AAPL"
                )
            ) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(
                    addInvestmentDialogModel = AddInvestmentDialogModel(ticker = "GOOGL")
                ),
                event = DashboardReducer.DashboardEvent.UpdateDialogTicker(
                    ticker = "AAPL"
                )
            )
        )
    }

    @Test
    fun `UpdateDialogValue event should update value in non null addInvestmentDialogModel`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(
                addInvestmentDialogModel = AddInvestmentDialogModel(
                    value = "100"
                )
            ) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(
                    addInvestmentDialogModel = AddInvestmentDialogModel(value = "50")
                ),
                event = DashboardReducer.DashboardEvent.UpdateDialogValue(
                    value = "100"
                )
            )
        )
    }

    @Test
    fun `UpdateDialogWeight event should update weight in non null addInvestmentDialogModel`() {
        assertEquals(
            DashboardReducer.DashboardState.initial().copy(
                addInvestmentDialogModel = AddInvestmentDialogModel(
                    weight = "0.5"
                )
            ) to null,
            reducer.reduce(
                previousState = DashboardReducer.DashboardState.initial().copy(
                    addInvestmentDialogModel = AddInvestmentDialogModel(weight = "0.1")
                ),
                event = DashboardReducer.DashboardEvent.UpdateDialogWeight(
                    weight = "0.5"
                )
            )
        )
    }


}
