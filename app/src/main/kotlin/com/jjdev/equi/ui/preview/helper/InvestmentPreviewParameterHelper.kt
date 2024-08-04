package com.jjdev.equi.ui.preview.helper

import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import java.math.BigDecimal

@Suppress("MagicNumber")
object InvestmentPreviewParameterHelper {
    private const val TICKER_GOOGL_MOCK = "GOOGL"
    private const val TICKER_AAPL_MOCK = "AAPL"
    private const val TICKER_MSFT_MOCK = "MSFT"
    private const val PERCENTAGE_MOCK_10 = 10.0
    private const val PERCENTAGE_MOCK_20 = 20.0
    private const val PERCENTAGE_MOCK_30 = 30.0
    private val VALUE_MOCK_100 = BigDecimal.valueOf(100)
    private val VALUE_MOCK_200 = BigDecimal.valueOf(200)
    private val VALUE_MOCK_300 = BigDecimal.valueOf(300)

    private fun createInvestmentMock(
        ticker: String = TICKER_GOOGL_MOCK,
        percentage: Double = PERCENTAGE_MOCK_10,
        value: BigDecimal = VALUE_MOCK_100,
    ) = InvestmentUIModel(
        ticker,
        percentage,
        value,
    )

    val values = sequenceOf(
        createInvestmentMock(),
        createInvestmentMock(TICKER_AAPL_MOCK, PERCENTAGE_MOCK_20, VALUE_MOCK_200),
        createInvestmentMock(TICKER_MSFT_MOCK, PERCENTAGE_MOCK_30, VALUE_MOCK_300),
    )
}
