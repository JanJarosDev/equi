package com.jjdev.equi.dashboard.domain

import com.jjdev.equi.core.base.domain.AppError
import com.jjdev.equi.core.base.domain.Result
import com.jjdev.equi.core.base.domain.UseCase
import com.jjdev.equi.dashboard.domain.model.Investment
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.floor

private const val INCREMENT_VALUE = 0.1

class RebalanceUseCase @Inject constructor() :
    UseCase<Pair<Double, List<Investment>>, List<Investment>, AppError?>(
        dispatcher = Dispatchers.Default
    ) {

    override suspend fun execute(parameters: Pair<Double, List<Investment>>): Result<List<Investment>, AppError?> {
        val (totalAmountToInvest, investments) = parameters

        Timber.i("Starting rebalance with totalAmountToInvest: $totalAmountToInvest and investments: $investments")

        // Preserve the original order by indexing the investments
        val investmentsWithIndex = investments.mapIndexed { index, investment ->
            IndexedInvestment(index, investment)
        }.toMutableList()

        var remainingAmount = totalAmountToInvest

        while (remainingAmount > 0.0) {
            remainingAmount -= addIncrementalFunds(INCREMENT_VALUE, investmentsWithIndex)
        }

        val finalInvestments = investmentsWithIndex
            .map {
                it.copy(
                    investment = it.investment.copy(
                        investedAmount = floor(it.investment.investedAmount ?: 0.0),
                        targetValue = it.investment.currentValue
                    ),
                )
            }
            .sortedBy { it.index } // Sort back to original order

        val resultInvestments = finalInvestments.map { it.investment }

        Timber.i("Final rebalanced investments: $resultInvestments")
        return Result.Success(resultInvestments)
    }

    private fun addIncrementalFunds(
        increment: Double,
        investments: MutableList<IndexedInvestment>,
    ): Double {
        // Calculate the current total value including the increment
        val totalValue = investments.sumOf { it.investment.currentValue } + increment

        // Find the most underweighted investment
        val targetInvestment = investments.maxByOrNull {
            val currentWeight = it.investment.currentValue / totalValue
            it.investment.weight - currentWeight
        } ?: return 0.0

        // Allocate the incremental fund to the most underweighted investment
        targetInvestment.investment.investedAmount =
            (targetInvestment.investment.investedAmount ?: 0.0) + increment
        targetInvestment.investment.currentValue += increment
        Timber.i(
            "Added $increment to ${targetInvestment.investment.ticker}, " +
                    "new currentValue: ${targetInvestment.investment.currentValue}"
        )
        return increment
    }

    data class IndexedInvestment(
        val index: Int,
        val investment: Investment,
    )
}

