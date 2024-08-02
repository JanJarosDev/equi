package com.jjdev.equi.dashboard.domain.model

data class Investment(
    val ticker: String,
    val weight: Double,
    var currentValue: Double,
    var investedAmount: Double? = null,
    var targetValue: Double? = null,
)

