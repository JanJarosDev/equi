package com.jjdev.equi.dashboard.presentation.model

data class RebalancedInvestment(
    val ticker: String,
    val percentage: Int,
    val amount: Double,
    val newAmount: Double,
)