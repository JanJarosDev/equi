package com.jjdev.equi

import java.util.logging.Logger

class Rebalancer {

    private val logger = Logger.getLogger(Rebalancer::class.java.name)

    fun rebalance(amountToInvest: Double, investments: List<Investment>): List<Investment> {
        logger.info("Starting rebalance with amountToInvest: $amountToInvest and investments: $investments")

        val totalWeight = investments.sumOf { it.weight }
        if (totalWeight != 1.0) {
            logger.severe("Weights must sum to 1.0. Current total weight: $totalWeight")
            throw IllegalArgumentException("Weights must sum to 1.0")
        }

        val totalFutureValue = investments.sumOf { it.currentValue } + amountToInvest
        logger.info("Total future value calculated: $totalFutureValue")

        val adjustedInvestments = investments.map {
            val investedAmount = amountToInvest * it.weight
            val targetValue = totalFutureValue * it.weight
            logger.info("Investment ${it.ticker}: investedAmount=$investedAmount, targetValue=$targetValue")
            it.copy(investedAmount = investedAmount, targetValue = targetValue)
        }.toMutableList()

        logger.info("Initial investments adjusted: $adjustedInvestments")

        var excessAmount = adjustInvestments(adjustedInvestments)
        while (excessAmount > 0.0) {
            logger.info("Excess amount to reallocate: $excessAmount")
            excessAmount = adjustInvestments(
                adjustedInvestments.filter { it.investedAmount!! > 0 },
                excessAmount
            )
        }

        logger.info("Final rebalanced investments: $adjustedInvestments")
        return adjustedInvestments
    }

    private fun adjustInvestments(
        investments: List<Investment>,
        excessAmount: Double = 0.0,
    ): Double {
        logger.info("Adjusting investments with initial excessAmount: $excessAmount")
        var totalExcessAmount = excessAmount

        investments.forEach {
            if (it.currentValue >= it.targetValue!!) {
                totalExcessAmount += it.investedAmount!!
                logger.info(
                    "No excess from ${it.ticker}, adding investedAmount=${it.investedAmount}, " +
                            "new totalExcessAmount=$totalExcessAmount"
                )
                it.investedAmount = 0.0
            } else {
                val combinedValue = it.investedAmount!! + it.currentValue
                if (combinedValue > it.targetValue!!) {
                    val excess = combinedValue - it.targetValue!!
                    totalExcessAmount += excess
                    logger.info(
                        "Excess from ${it.ticker}: $excess, " +
                                "new investedAmount=${it.investedAmount}"
                    )
                    it.investedAmount = it.investedAmount!! - excess
                }
            }
        }

        val remainingInvestments = investments.filter { it.investedAmount!! > 0 }
        val weightLeft = remainingInvestments.sumOf { it.weight }
        logger.info("Weight left after adjustment: $weightLeft")

        remainingInvestments.forEach {
            if (totalExcessAmount > 0.0) {
                val movingAmount = (totalExcessAmount / weightLeft) * it.weight
                it.investedAmount = it.investedAmount!! + movingAmount
                totalExcessAmount -= movingAmount
                logger.info(
                    "Investment ${it.ticker}: Adjusted investedAmount=${it.investedAmount}, " +
                            "remaining totalExcessAmount=$totalExcessAmount"
                )
            }
        }

        logger.info("Total excess amount after adjustment: $totalExcessAmount")
        return totalExcessAmount
    }
}
