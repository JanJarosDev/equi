package com.jjdev.equi.dashboard.presentation.model

import java.math.BigDecimal

data class InvestmentUIModel(
    val ticker: String,
    val weight: Double,
    val value: BigDecimal,
    val valueToInvest: BigDecimal = BigDecimal.ZERO,
)
