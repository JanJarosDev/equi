package com.jjdev.equi

import com.jjdev.equi.core.base.domain.onError
import com.jjdev.equi.core.base.domain.onSuccess
import com.jjdev.equi.dashboard.domain.RebalanceUseCase
import com.jjdev.equi.dashboard.domain.model.Investment
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RebalanceUseCaseTest {

    private val rebalanceUseCase = RebalanceUseCase()

    @Test
    fun `rebalance should distribute investments equally when weights sum to 1`() = runTest {
        val investments = listOf(
            Investment("A", 0.5, 100.0),
            Investment("B", 0.5, 100.0)
        )

        rebalanceUseCase(Pair(100.0, investments)).onSuccess {
            assertEquals(50.0, it[0].investedAmount!!, 0.1)
            assertEquals(150.0, it[0].targetValue!!, 0.1)
            assertEquals(50.0, it[1].investedAmount!!, 0.1)
            assertEquals(150.0, it[1].targetValue!!, 0.1)
        }
    }

    @Test
    fun `rebalance should handle uneven weights and distribute investments correctly`() = runTest {
        val investments = listOf(
            Investment("A", 0.2, 200.0),
            Investment("B", 0.8, 100.0)
        )

        rebalanceUseCase(Pair(1000.0, investments)).onSuccess {
            assertEquals(60.0, it[0].investedAmount!!, 0.1)
            assertEquals(260.0, it[0].targetValue!!, 0.1)
            assertEquals(940.0, it[1].investedAmount!!, 0.1)
            assertEquals(1040.0, it[1].targetValue!!, 0.1)
        }
    }

    @Test
    fun `rebalance should throw exception when weights do not sum to 1`() = runTest {
        val investments = listOf(
            Investment("A", 0.4, 100.0),
            Investment("B", 0.5, 100.0)
        )

        rebalanceUseCase(Pair(100.0, investments)).onError {
            assertEquals("Weights must sum to 1.0", it.originalException.message)
        }
    }

    @Test
    fun `rebalance should handle investments with zero weight`() = runTest {
        val investments = listOf(
            Investment("A", 0.5, 100.0),
            Investment("B", 0.0, 100.0),
            Investment("C", 0.5, 100.0),
        )

        rebalanceUseCase(Pair(100.0, investments)).onSuccess {
            assertEquals(50.0, it[0].investedAmount!!, 0.1)
            assertEquals(150.0, it[0].targetValue!!, 0.1)
            assertEquals(0.0, it[1].investedAmount!!, 0.1)
            assertEquals(100.0, it[1].targetValue!!, 0.1)
            assertEquals(50.0, it[2].investedAmount!!, 0.1)
            assertEquals(150.0, it[2].targetValue!!, 0.1)
        }
    }

    @Test
    fun `rebalance should correctly allocate funds without excess amount`() = runTest {
        val investments = listOf(
            Investment("A", 0.5, 100.0),
            Investment("B", 0.5, 100.0),
        )

        rebalanceUseCase(Pair(100.0, investments)).onSuccess {
            assertEquals(50.0, it[0].investedAmount!!, 0.1)
            assertEquals(150.0, it[0].targetValue!!, 0.1)
            assertEquals(50.0, it[1].investedAmount!!, 0.1)
            assertEquals(150.0, it[1].targetValue!!, 0.1)
        }
    }

    @Test
    fun `rebalance should reallocate excess amount correctly`() = runTest {
        val investments = listOf(
            Investment("A", 0.5, 200.0),
            Investment("B", 0.5, 100.0)
        )

        rebalanceUseCase(Pair(100.0, investments)).onSuccess {
            assertEquals(0.0, it[0].investedAmount!!, 0.1)
            assertEquals(200.0, it[0].targetValue!!, 0.1)
            assertEquals(100.0, it[1].investedAmount!!, 0.1)
            assertEquals(200.0, it[1].targetValue!!, 0.1)
        }
    }

    @Test
    fun `rebalance should handle multiple adjustment cycles correctly`() = runTest {
        val investments = listOf(
            Investment("A", 0.4, 500.0),
            Investment("B", 0.3, 100.0),
            Investment("C", 0.3, 200.0),
        )

        rebalanceUseCase(Pair(150.0, investments)).onSuccess {
            assertEquals(0.0, it[0].investedAmount!!, 0.1)
            assertEquals(500.0, it[0].targetValue!!, 0.1)

            assertEquals(125.0, it[1].investedAmount!!, 0.1)
            assertEquals(225.0, it[1].targetValue!!, 0.1)

            assertEquals(25.0, it[2].investedAmount!!, 0.1)
            assertEquals(225.0, it[2].targetValue!!, 0.1)

            val totalFutureValue = investments.sumOf { investment -> investment.currentValue }
            assertEquals(
                totalFutureValue,
                it.sumOf { investment -> investment.targetValue!! },
                0.1
            )
        }
    }
}
