package com.jjdev.equi.dashboard

import com.jjdev.equi.dashboard.presentation.DashboardReducer
import com.jjdev.equi.dashboard.presentation.model.AddInvestmentDialogModel
import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DashboardReducerTest {

    private companion object {
        const val TICKER_AAPL = "AAPL"
        const val TICKER_GOOGL = "GOOGL"
        const val VALUE_100 = "100"
        const val VALUE_50 = "50"
        val VALUE_100_BIG_DECIMAL: BigDecimal = BigDecimal.valueOf(100.0)
        const val WEIGHT_0_5 = "0.5"
        const val WEIGHT_0_1 = "0.1"
    }

    private lateinit var reducer: DashboardReducer

    private val investmentsUIModelListMock = persistentListOf(
        InvestmentUIModel(
            ticker = TICKER_AAPL,
            weight = WEIGHT_0_5.toDouble(),
            value = VALUE_100_BIG_DECIMAL
        )
    )

    private val initialState = DashboardReducer.DashboardState.initial()
    private val initialLoadingState = initialState.copy(isLoading = true)
    private val initialDialogShownState = initialState.copy(dialogShown = true)
    private val initialAddInvestmentDialogModelNullState =
        initialState.copy(addInvestmentDialogModel = null)
    private val initialAddInvestmentDialogModelNonNullState = initialState.copy(
        addInvestmentDialogModel = AddInvestmentDialogModel(ticker = TICKER_GOOGL)
    )

    private val updateRebalanceLoadingTrueEvent =
        DashboardReducer.DashboardEvent.UpdateRebalanceLoading(isLoading = true)
    private val updateRebalanceLoadingFalseEvent =
        DashboardReducer.DashboardEvent.UpdateRebalanceLoading(isLoading = false)
    private val updateDialogTrueEvent = DashboardReducer.DashboardEvent.UpdateDialog(show = true)
    private val updateDialogFalseEvent = DashboardReducer.DashboardEvent.UpdateDialog(show = false)
    private val updateAmountToInvestEvent =
        DashboardReducer.DashboardEvent.UpdateAmountToInvest(amount = VALUE_100)
    private val addInvestmentEvent = DashboardReducer.DashboardEvent.AddInvestment(
        ticker = TICKER_AAPL,
        value = VALUE_100,
        weight = WEIGHT_0_5
    )
    private val rebalanceEvent = DashboardReducer.DashboardEvent.Rebalance(
        rebalancedInvestments = investmentsUIModelListMock
    )
    private val updateDialogTickerEvent =
        DashboardReducer.DashboardEvent.UpdateDialogTicker(ticker = TICKER_AAPL)
    private val updateDialogValueEvent =
        DashboardReducer.DashboardEvent.UpdateDialogValue(value = VALUE_100)
    private val updateDialogWeightEvent =
        DashboardReducer.DashboardEvent.UpdateDialogWeight(weight = WEIGHT_0_5)

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
    fun `Given UpdateRebalanceLoading event with true, When reducing, Then isLoading should be true`() {
        assertEquals(
            initialLoadingState to null,
            reducer.reduce(
                previousState = initialState.copy(isLoading = false),
                event = updateRebalanceLoadingTrueEvent
            ),
        )
    }

    @Test
    fun `Given UpdateRebalanceLoading event with false, When reducing, Then isLoading should be false`() {
        assertEquals(
            initialState to null,
            reducer.reduce(
                previousState = initialLoadingState,
                event = updateRebalanceLoadingFalseEvent
            ),
        )
    }

    @Test
    fun `Given UpdateDialog event with true, When reducing, Then dialogShown should be true`() {
        assertEquals(
            initialDialogShownState to null,
            reducer.reduce(
                previousState = initialState.copy(dialogShown = false),
                event = updateDialogTrueEvent
            ),
        )
    }

    @Test
    fun `Given UpdateDialog event with false, When reducing, Then dialogShown should be false`() {
        assertEquals(
            initialState to null,
            reducer.reduce(
                previousState = initialDialogShownState,
                event = updateDialogFalseEvent
            )
        )
    }

    @Test
    fun `Given UpdateAmountToInvest event, When reducing, Then amountToInvest should be updated`() {
        assertEquals(
            initialState.copy(amountToInvest = VALUE_100_BIG_DECIMAL.toDouble()) to null,
            reducer.reduce(
                previousState = initialState.copy(amountToInvest = null),
                event = updateAmountToInvestEvent
            ),
        )
    }

    @Test
    fun `Given AddInvestment event, When reducing, Then investment should be added to investments`() {
        assertEquals(
            initialState.copy(investments = investmentsUIModelListMock) to null,
            reducer.reduce(
                previousState = initialState.copy(investments = persistentListOf()),
                event = addInvestmentEvent
            )
        )
    }

    @Test
    fun `Given Rebalance event, When reducing, Then investments should be set to rebalancedInvestments`() {
        assertEquals(
            initialState.copy(investments = investmentsUIModelListMock) to null,
            reducer.reduce(
                previousState = initialState.copy(investments = persistentListOf()),
                event = rebalanceEvent
            )
        )
    }

    @Suppress("MaxLineLength")
    @Test
    fun `Given UpdateDialogTicker event, When reducing with null addInvestmentDialogModel, Then ticker should be updated`() {
        assertEquals(
            initialState.copy(
                addInvestmentDialogModel = AddInvestmentDialogModel(ticker = TICKER_AAPL)
            ) to null,
            reducer.reduce(
                previousState = initialAddInvestmentDialogModelNullState,
                event = updateDialogTickerEvent
            )
        )
    }

    @Suppress("MaxLineLength")
    @Test
    fun `Given UpdateDialogValue event, When reducing with null addInvestmentDialogModel, Then value should be updated`() {
        assertEquals(
            initialState.copy(
                addInvestmentDialogModel = AddInvestmentDialogModel(value = VALUE_100)
            ) to null,
            reducer.reduce(
                previousState = initialAddInvestmentDialogModelNullState,
                event = updateDialogValueEvent
            )
        )
    }

    @Suppress("MaxLineLength")
    @Test
    fun `Given UpdateDialogWeight event, When reducing with null addInvestmentDialogModel, Then weight should be updated`() {
        assertEquals(
            initialState.copy(
                addInvestmentDialogModel = AddInvestmentDialogModel(weight = WEIGHT_0_5)
            ) to null,
            reducer.reduce(
                previousState = initialAddInvestmentDialogModelNullState,
                event = updateDialogWeightEvent
            )
        )
    }

    @Suppress("MaxLineLength")
    @Test
    fun `Given UpdateDialogTicker event, When reducing with non-null addInvestmentDialogModel, Then ticker should be updated`() {
        assertEquals(
            initialState.copy(
                addInvestmentDialogModel = AddInvestmentDialogModel(ticker = TICKER_AAPL)
            ) to null,
            reducer.reduce(
                previousState = initialAddInvestmentDialogModelNonNullState,
                event = updateDialogTickerEvent
            )
        )
    }

    @Suppress("MaxLineLength")
    @Test
    fun `Given UpdateDialogValue event, When reducing with non-null addInvestmentDialogModel, Then value should be updated`() {
        assertEquals(
            initialState.copy(
                addInvestmentDialogModel = AddInvestmentDialogModel(value = VALUE_100)
            ) to null,
            reducer.reduce(
                previousState = initialAddInvestmentDialogModelNonNullState.copy(
                    addInvestmentDialogModel = AddInvestmentDialogModel(value = VALUE_50)
                ),
                event = updateDialogValueEvent
            )
        )
    }

    @Suppress("MaxLineLength")
    @Test
    fun `Given UpdateDialogWeight event, When reducing with non-null addInvestmentDialogModel, Then weight should be updated`() {
        assertEquals(
            initialState.copy(
                addInvestmentDialogModel = AddInvestmentDialogModel(weight = WEIGHT_0_5)
            ) to null,
            reducer.reduce(
                previousState = initialAddInvestmentDialogModelNonNullState.copy(
                    addInvestmentDialogModel = AddInvestmentDialogModel(weight = WEIGHT_0_1)
                ),
                event = updateDialogWeightEvent
            )
        )
    }

    @Test
    fun `Given DashboardEffect, When using mockK, Then it should not be null`() {
        val mockEffect = mockk<DashboardReducer.DashboardEffect>()
        assertNotNull(mockEffect)
    }
}

