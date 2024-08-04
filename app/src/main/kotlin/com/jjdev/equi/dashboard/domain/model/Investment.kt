package com.jjdev.equi.dashboard.domain.model

import java.math.BigDecimal

data class Investment(
    val ticker: String,
    val weight: BigDecimal,
    var currentValue: BigDecimal,
    var valueToInvest: BigDecimal? = null,
    var targetValue: BigDecimal? = null,
)

