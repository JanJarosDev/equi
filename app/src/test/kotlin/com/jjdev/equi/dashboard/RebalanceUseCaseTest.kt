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

    private val rebalanceUseCase = RebalanceUseCase()

    @Test
    fun `rebalance should distribute investments equally when weights sum to 1`() = runTest {
        val investments = listOf(
            Investment("A", BigDecimal.valueOf(0.5), BigDecimal.valueOf(100.0)),
            Investment("B", BigDecimal.valueOf(0.5), BigDecimal.valueOf(100.0))
        )

        rebalanceUseCase(Pair(BigDecimal.valueOf(100.0), investments)).onSuccess {
            assertEquals(BigDecimal.valueOf(50.0), it[0].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(150.0), it[0].targetValue!!)
            assertEquals(BigDecimal.valueOf(50.0), it[1].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(150.0), it[1].targetValue!!)
        }
    }

    @Test
    fun `rebalance should handle uneven weights and distribute investments correctly`() = runTest {
        val investments = listOf(
            Investment("A", BigDecimal.valueOf(0.2), BigDecimal.valueOf(200.0)),
            Investment("B", BigDecimal.valueOf(0.8), BigDecimal.valueOf(100.0))
        )

        rebalanceUseCase(Pair(BigDecimal.valueOf(1000.0), investments)).onSuccess {
            assertEquals(BigDecimal.valueOf(60.0), it[0].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(260.0), it[0].targetValue!!)
            assertEquals(BigDecimal.valueOf(940.0), it[1].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(1040.0), it[1].targetValue!!)
        }
    }

    @Test
    fun `rebalance should throw exception when weights do not sum to 1`() = runTest {
        val investments = listOf(
            Investment("A", BigDecimal.valueOf(0.4), BigDecimal.valueOf(100.0)),
            Investment("B", BigDecimal.valueOf(0.5), BigDecimal.valueOf(100.0))
        )

        rebalanceUseCase(Pair(BigDecimal.valueOf(100.0), investments)).onError {
            assertEquals("Weights must sum to 1.0", it.originalException.message)
        }
    }

    @Test
    fun `rebalance should handle investments with zero weight`() = runTest {
        val investments = listOf(
            Investment("A", BigDecimal.valueOf(0.5), BigDecimal.valueOf(100.0)),
            Investment("B", BigDecimal.valueOf(0.0), BigDecimal.valueOf(100.0)),
            Investment("C", BigDecimal.valueOf(0.5), BigDecimal.valueOf(100.0)),
        )

        rebalanceUseCase(Pair(BigDecimal.valueOf(100.0), investments)).onSuccess {
            assertEquals(BigDecimal.valueOf(50.0), it[0].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(150.0), it[0].targetValue!!)
            assertEquals(BigDecimal.ZERO, it[1].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(100.0), it[1].targetValue!!)
            assertEquals(BigDecimal.valueOf(50.0), it[2].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(150.0), it[2].targetValue!!)
        }
    }

    @Test
    fun `rebalance should correctly allocate funds without excess amount`() = runTest {
        val investments = listOf(
            Investment("A", BigDecimal.valueOf(0.5), BigDecimal.valueOf(100.0)),
            Investment("B", BigDecimal.valueOf(0.5), BigDecimal.valueOf(100.0)),
        )

        rebalanceUseCase(Pair(BigDecimal.valueOf(100.0), investments)).onSuccess {
            assertEquals(BigDecimal.valueOf(50.0), it[0].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(150.0), it[0].targetValue!!)
            assertEquals(BigDecimal.valueOf(50.0), it[1].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(150.0), it[1].targetValue!!)
        }
    }

    @Test
    fun `rebalance should reallocate excess amount correctly`() = runTest {
        val investments = listOf(
            Investment("A", BigDecimal.valueOf(0.5), BigDecimal.valueOf(200.0)),
            Investment("B", BigDecimal.valueOf(0.5), BigDecimal.valueOf(100.0))
        )

        rebalanceUseCase(Pair(BigDecimal.valueOf(100.0), investments)).onSuccess {
            assertEquals(BigDecimal.ZERO, it[0].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(200.0), it[0].targetValue!!)
            assertEquals(BigDecimal.valueOf(100.0), it[1].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(200.0), it[1].targetValue!!)
        }
    }

    @Test
    fun `rebalance should handle multiple adjustment cycles correctly`() = runTest {
        val investments = listOf(
            Investment("A", BigDecimal.valueOf(0.4), BigDecimal.valueOf(500.0)),
            Investment("B", BigDecimal.valueOf(0.3), BigDecimal.valueOf(100.0)),
            Investment("C", BigDecimal.valueOf(0.3), BigDecimal.valueOf(200.0)),
        )

        rebalanceUseCase(Pair(BigDecimal.valueOf(150.0), investments)).onSuccess {
            assertEquals(BigDecimal.ZERO, it[0].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(500.0), it[0].targetValue!!)

            assertEquals(BigDecimal.valueOf(125.0), it[1].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(225.0), it[1].targetValue!!)

            assertEquals(BigDecimal.valueOf(25.0), it[2].valueToInvest!!)
            assertEquals(BigDecimal.valueOf(225.0), it[2].targetValue!!)

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

        rebalanceUseCase(Pair(BigDecimal.valueOf(100.0), investments)).onSuccess {
            assertEquals(investments, it)
        }
    }
}
