package com.jjdev.equi

data class Investment(
    val ticker: String,
    val weight: Double,
    val currentValue: Double,
    var investedAmount: Double? = null,
    var targetValue: Double? = null,
)

