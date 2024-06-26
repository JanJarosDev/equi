package com.jjdev.equi

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RebalancerTest {

    private val rebalancer = Rebalancer()

    @Test
    fun `rebalance should distribute investments equally when weights sum to 1`() {
        val investments = listOf(
            Investment("A", 0.5, 100.0),
            Investment("B", 0.5, 100.0)
        )

        val result = rebalancer.rebalance(100.0, investments)

        assertEquals(50.0, result[0].investedAmount)
        assertEquals(150.0, result[0].targetValue)
        assertEquals(50.0, result[1].investedAmount)
        assertEquals(150.0, result[1].targetValue)
    }

    @Test
    fun `rebalance should handle uneven weights and distribute investments correctly`() {
        val investments = listOf(
            Investment("A", 0.2, 200.0),
            Investment("B", 0.8, 100.0)
        )

        val result = rebalancer.rebalance(1000.0, investments)

        assertEquals(60.0, result[0].investedAmount!!, 0.01)
        assertEquals(260.0, result[0].targetValue)
        assertEquals(940.0, result[1].investedAmount!!, 0.01)
        assertEquals(1040.0, result[1].targetValue)
    }

    @Test
    fun `rebalance should throw exception when weights do not sum to 1`() {
        val investments = listOf(
            Investment("A", 0.4, 100.0),
            Investment("B", 0.5, 100.0)
        )

        val exception = assertThrows<IllegalArgumentException> {
            rebalancer.rebalance(100.0, investments)
        }
        assertEquals("Weights must sum to 1.0", exception.message)
    }

    @Test
    fun `rebalance should handle investments with zero weight`() {
        val investments = listOf(
            Investment("A", 0.5, 100.0),
            Investment("B", 0.0, 100.0),
            Investment("C", 0.5, 100.0)
        )

        val result = rebalancer.rebalance(100.0, investments)

        assertEquals(50.0, result[0].investedAmount)
        assertEquals(200.0, result[0].targetValue)
        assertEquals(0.0, result[1].investedAmount)
        assertEquals(0.0, result[1].targetValue)
        assertEquals(50.0, result[2].investedAmount)
        assertEquals(200.0, result[2].targetValue)
    }

    @Test
    fun `rebalance should correctly allocate funds without excess amount`() {
        val investments = listOf(
            Investment("A", 0.5, 100.0),
            Investment("B", 0.5, 100.0)
        )

        val result = rebalancer.rebalance(100.0, investments)

        assertEquals(50.0, result[0].investedAmount)
        assertEquals(150.0, result[0].targetValue)
        assertEquals(50.0, result[1].investedAmount)
        assertEquals(150.0, result[1].targetValue)
    }

    @Test
    fun `rebalance should reallocate excess amount correctly`() {
        val investments = listOf(
            Investment("A", 0.5, 200.0),
            Investment("B", 0.5, 100.0)
        )

        val result = rebalancer.rebalance(100.0, investments)

        assertEquals(0.0, result[0].investedAmount)
        assertEquals(200.0, result[0].targetValue)
        assertEquals(100.0, result[1].investedAmount)
        assertEquals(200.0, result[1].targetValue)
    }

    @Test
    fun `rebalance should handle multiple adjustment cycles correctly`() {
        val investments = listOf(
            Investment("A", 0.4, 500.0),
            Investment("B", 0.3, 100.0),
            Investment("C", 0.3, 200.0),
        )

        val result = rebalancer.rebalance(150.0, investments)

        assertEquals(0.0, result[0].investedAmount!!, 0.01)
        assertTrue(result[0].targetValue!! == 380.0)

        assertTrue(result[1].investedAmount!! == 85.0)
        assertTrue(result[1].targetValue!! == 285.0)

        assertTrue(result[2].investedAmount!! == 65.0)
        assertTrue(result[2].targetValue!! == 285.0)

        val totalFutureValue = investments.sumOf { it.currentValue } + 150.0
        assertEquals(totalFutureValue, result.sumOf { it.targetValue!! }, 0.01)
    }

}
