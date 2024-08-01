package com.jjdev.equi

import com.jjdev.equi.core.base.domain.AppError
import com.jjdev.equi.core.base.domain.Result
import com.jjdev.equi.core.base.domain.UseCase
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class RebalanceUseCase :
    UseCase<Pair<Double, List<Investment>>, List<Investment>, AppError?>(
        dispatcher = Dispatchers.Default
    ) {

    override suspend fun execute(parameters: Pair<Double, List<Investment>>): Result<List<Investment>, AppError?> {

        val (amountToInvest, investments) = parameters

        Timber.i("Starting rebalance with amountToInvest: $amountToInvest and investments: $investments")

        val totalWeight = investments.sumOf { it.weight }
        if (totalWeight != 1.0) {
            Timber.i("Weights must sum to 1.0. Current total weight: $totalWeight")
            throw IllegalArgumentException("Weights must sum to 1.0")
        }

        val totalFutureValue = investments.sumOf { it.currentValue } + amountToInvest
        Timber.i("Total future value calculated: $totalFutureValue")

        val adjustedInvestments = investments.map {
            val investedAmount = amountToInvest * it.weight
            val targetValue = totalFutureValue * it.weight
            Timber.i("Investment ${it.ticker}: investedAmount=$investedAmount, targetValue=$targetValue")
            it.copy(investedAmount = investedAmount, targetValue = targetValue)
        }.toMutableList()

        Timber.i("Initial investments adjusted: $adjustedInvestments")

        var excessAmount = adjustInvestments(adjustedInvestments)
        while (excessAmount > 0.0) {
            Timber.i("Excess amount to reallocate: $excessAmount")
            excessAmount = adjustInvestments(
                adjustedInvestments.filter { it.investedAmount!! > 0 }, excessAmount
            )
        }

        Timber.i("Final rebalanced investments: $adjustedInvestments")
        return Result.Success(adjustedInvestments)
    }

    private fun adjustInvestments(
        investments: List<Investment>,
        excessAmount: Double = 0.0,
    ): Double {
        Timber.i("Adjusting investments with initial excessAmount: $excessAmount")
        var totalExcessAmount = excessAmount

        investments.forEach {
            if (it.currentValue >= it.targetValue!!) {
                totalExcessAmount += it.investedAmount!!
                Timber.i(
                    "No excess from ${it.ticker}, adding investedAmount=${it.investedAmount}," +
                            " new totalExcessAmount=$totalExcessAmount"
                )
                it.investedAmount = 0.0
            } else {
                val combinedValue = it.investedAmount!! + it.currentValue
                if (combinedValue > it.targetValue!!) {
                    val excess = combinedValue - it.targetValue!!
                    totalExcessAmount += excess
                    Timber.i(
                        "Excess from ${it.ticker}: $excess, " + "new investedAmount=${it.investedAmount}"
                    )
                    it.investedAmount = it.investedAmount!! - excess
                }
            }
        }

        val remainingInvestments = investments.filter { it.investedAmount!! > 0 }
        val weightLeft = remainingInvestments.sumOf { it.weight }
        Timber.i("Weight left after adjustment: $weightLeft")

        remainingInvestments.forEach {
            if (totalExcessAmount > 0.0) {
                val movingAmount = (totalExcessAmount / weightLeft) * it.weight
                it.investedAmount = it.investedAmount!! + movingAmount
                totalExcessAmount -= movingAmount
                Timber.i(
                    "Investment ${it.ticker}: Adjusted investedAmount=${it.investedAmount}," +
                            " remaining totalExcessAmount=$totalExcessAmount"
                )
            }
        }

        Timber.i("Total excess amount after adjustment: $totalExcessAmount")
        return totalExcessAmount
    }
}
