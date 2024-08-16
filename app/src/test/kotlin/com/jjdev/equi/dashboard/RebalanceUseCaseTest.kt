package com.jjdev.equi.dashboard

import com.jjdev.equi.core.base.domain.onError
import com.jjdev.equi.core.base.domain.onSuccess
import com.jjdev.equi.dashboard.domain.RebalanceUseCase
import com.jjdev.equi.dashboard.domain.model.Investment
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class RebalanceUseCaseTest {

    private companion object {
        const val TICKER_A = "A"
        const val TICKER_B = "B"
        const val TICKER_C = "C"
        val WEIGHT_0_5: BigDecimal = BigDecimal.valueOf(0.5)
        val WEIGHT_0_4: BigDecimal = BigDecimal.valueOf(0.4)
        val WEIGHT_0_3: BigDecimal = BigDecimal.valueOf(0.3)
        val WEIGHT_0_2: BigDecimal = BigDecimal.valueOf(0.2)
        val WEIGHT_0_8: BigDecimal = BigDecimal.valueOf(0.8)
        val WEIGHT_0_0: BigDecimal = BigDecimal.valueOf(0.0)
        val VALUE_100: BigDecimal = BigDecimal.valueOf(100.0)
        val VALUE_200: BigDecimal = BigDecimal.valueOf(200.0)
        val VALUE_500: BigDecimal = BigDecimal.valueOf(500.0)
        val VALUE_1000: BigDecimal = BigDecimal.valueOf(1000.0)
        val VALUE_150: BigDecimal = BigDecimal.valueOf(150.0)
        val VALUE_60: BigDecimal = BigDecimal.valueOf(60.0)
        val VALUE_50: BigDecimal = BigDecimal.valueOf(50.0)
        val VALUE_225: BigDecimal = BigDecimal.valueOf(225.0)
        val VALUE_260: BigDecimal = BigDecimal.valueOf(260.0)
        val VALUE_125: BigDecimal = BigDecimal.valueOf(125.0)
        val VALUE_25: BigDecimal = BigDecimal.valueOf(25.0)
        val VALUE_940: BigDecimal = BigDecimal.valueOf(940.0)
        val VALUE_1040: BigDecimal = BigDecimal.valueOf(1040.0)
        val VALUE_ZERO: BigDecimal = BigDecimal.ZERO
    }

    private val rebalanceUseCase = RebalanceUseCase()

    @Test
    fun `rebalance should distribute investments equally when weights sum to 1`() = runTest {
        val investments = listOf(
            Investment(TICKER_A, WEIGHT_0_5, VALUE_100),
            Investment(TICKER_B, WEIGHT_0_5, VALUE_100)
        )

        rebalanceUseCase(Pair(VALUE_100, investments)).onSuccess {
            assertEquals(VALUE_50, it[0].valueToInvest!!)
            assertEquals(VALUE_150, it[0].targetValue!!)
            assertEquals(VALUE_50, it[1].valueToInvest!!)
            assertEquals(VALUE_150, it[1].targetValue!!)
        }
    }

    @Test
    fun `rebalance should handle uneven weights and distribute investments correctly`() = runTest {
        val investments = listOf(
            Investment(TICKER_A, WEIGHT_0_2, VALUE_200),
            Investment(TICKER_B, WEIGHT_0_8, VALUE_100)
        )

        rebalanceUseCase(Pair(VALUE_1000, investments)).onSuccess {
            assertEquals(VALUE_60, it[0].valueToInvest!!)
            assertEquals(VALUE_260, it[0].targetValue!!)
            assertEquals(VALUE_940, it[1].valueToInvest!!)
            assertEquals(VALUE_1040, it[1].targetValue!!)
        }
    }

    @Test
    fun `rebalance should throw exception when weights do not sum to 1`() = runTest {
        val investments = listOf(
            Investment(TICKER_A, WEIGHT_0_4, VALUE_100),
            Investment(TICKER_B, WEIGHT_0_5, VALUE_100)
        )

        rebalanceUseCase(Pair(VALUE_100, investments)).onError {
            assertEquals("Weights must sum to 1.0", it.originalException.message)
        }
    }

    @Test
    fun `rebalance should handle investments with zero weight`() = runTest {
        val investments = listOf(
            Investment(TICKER_A, WEIGHT_0_5, VALUE_100),
            Investment(TICKER_B, WEIGHT_0_0, VALUE_100),
            Investment(TICKER_C, WEIGHT_0_5, VALUE_100)
        )

        rebalanceUseCase(Pair(VALUE_100, investments)).onSuccess {
            assertEquals(VALUE_50, it[0].valueToInvest!!)
            assertEquals(VALUE_150, it[0].targetValue!!)
            assertEquals(VALUE_ZERO, it[1].valueToInvest!!)
            assertEquals(VALUE_100, it[1].targetValue!!)
            assertEquals(VALUE_50, it[2].valueToInvest!!)
            assertEquals(VALUE_150, it[2].targetValue!!)
        }
    }

    @Test
    fun `rebalance should correctly allocate funds without excess amount`() = runTest {
        val investments = listOf(
            Investment(TICKER_A, WEIGHT_0_5, VALUE_100),
            Investment(TICKER_B, WEIGHT_0_5, VALUE_100)
        )

        rebalanceUseCase(Pair(VALUE_100, investments)).onSuccess {
            assertEquals(VALUE_50, it[0].valueToInvest!!)
            assertEquals(VALUE_150, it[0].targetValue!!)
            assertEquals(VALUE_50, it[1].valueToInvest!!)
            assertEquals(VALUE_150, it[1].targetValue!!)
        }
    }

    @Test
    fun `rebalance should reallocate excess amount correctly`() = runTest {
        val investments = listOf(
            Investment(TICKER_A, WEIGHT_0_5, VALUE_200),
            Investment(TICKER_B, WEIGHT_0_5, VALUE_100)
        )

        rebalanceUseCase(Pair(VALUE_100, investments)).onSuccess {
            assertEquals(VALUE_ZERO, it[0].valueToInvest!!)
            assertEquals(VALUE_200, it[0].targetValue!!)
            assertEquals(VALUE_100, it[1].valueToInvest!!)
            assertEquals(VALUE_200, it[1].targetValue!!)
        }
    }

    @Test
    fun `rebalance should handle multiple adjustment cycles correctly`() = runTest {
        val investments = listOf(
            Investment(TICKER_A, WEIGHT_0_4, VALUE_500),
            Investment(TICKER_B, WEIGHT_0_3, VALUE_100),
            Investment(TICKER_C, WEIGHT_0_3, VALUE_200)
        )

        rebalanceUseCase(Pair(VALUE_150, investments)).onSuccess {
            assertEquals(VALUE_ZERO, it[0].valueToInvest!!)
            assertEquals(VALUE_500, it[0].targetValue!!)

            assertEquals(VALUE_125, it[1].valueToInvest!!)
            assertEquals(VALUE_225, it[1].targetValue!!)

            assertEquals(VALUE_25, it[2].valueToInvest!!)
            assertEquals(VALUE_225, it[2].targetValue!!)

            val totalFutureValue = investments.sumOf { investment -> investment.currentValue }
            assertEquals(
                totalFutureValue,
                it.sumOf { investment -> investment.targetValue!! }
            )
        }
    }

    @Test
    fun `rebalance should not distribute if there are no investments`() = runTest {
        val investments = emptyList<Investment>()

        rebalanceUseCase(Pair(VALUE_100, investments)).onSuccess {
            assertEquals(investments, it)
        }
    }
}

