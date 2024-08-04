package com.jjdev.equi.dashboard.domain

import com.jjdev.equi.core.base.domain.AppError
import com.jjdev.equi.core.base.domain.Result
import com.jjdev.equi.core.base.domain.UseCase
import com.jjdev.equi.dashboard.domain.model.Investment
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

private val INCREMENT_VALUE = BigDecimal("0.1")
private const val BIG_DECIMAL_DIVISION_SCALE = 10

class RebalanceUseCase @Inject constructor() :
    UseCase<Pair<BigDecimal, List<Investment>>, List<Investment>, AppError?>(
        dispatcher = Dispatchers.Default
    ) {

    override suspend fun execute(parameters: Pair<BigDecimal, List<Investment>>): Result<List<Investment>, AppError?> {
        val (totalAmountToInvest, investments) = parameters

        Timber.i("Starting rebalance with totalAmountToInvest: $totalAmountToInvest and investments: $investments")

        // Preserve the original order by indexing the investments
        val investmentsWithIndex = investments.mapIndexed { index, investment ->
            IndexedInvestment(index, investment)
        }.toMutableList()

        var remainingAmount = totalAmountToInvest

        while (remainingAmount > BigDecimal.ZERO) {
            remainingAmount =
                remainingAmount.subtract(addIncrementalFunds(investments = investmentsWithIndex))
        }

        val finalInvestments = investmentsWithIndex
            .map {
                it.copy(
                    investment = it.investment.copy(
                        valueToInvest = it.investment.valueToInvest?.setScale(0, RoundingMode.FLOOR)
                            ?: BigDecimal.ZERO,
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
        increment: BigDecimal = INCREMENT_VALUE,
        investments: MutableList<IndexedInvestment>,
    ): BigDecimal {
        // Calculate the current total value including the increment
        val totalValue = investments.sumOf { it.investment.currentValue } + increment

        // Find the most underweighted investment
        val targetInvestment = investments.maxByOrNull {
            val currentWeight =
                it.investment.currentValue.divide(totalValue, BIG_DECIMAL_DIVISION_SCALE, RoundingMode.HALF_UP)
            it.investment.weight.subtract(currentWeight)
        } ?: return BigDecimal.ZERO

        // Allocate the incremental fund to the most underweighted investment
        targetInvestment.investment.valueToInvest =
            (targetInvestment.investment.valueToInvest ?: BigDecimal.ZERO).add(increment)
        targetInvestment.investment.currentValue =
            targetInvestment.investment.currentValue.add(increment)
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
