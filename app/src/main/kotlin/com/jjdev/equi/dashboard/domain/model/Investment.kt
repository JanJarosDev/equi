package com.jjdev.equi.dashboard.domain.model

data class Investment(
    val ticker: String,
    val weight: Double,
    val currentValue: Double,
    var investedAmount: Double? = null,
    var targetValue: Double? = null,
)

