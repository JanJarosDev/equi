package com.jjdev.equi.ui.preview.helper

import com.jjdev.equi.dashboard.presentation.model.Investment

object InvestmentPreviewParameterHelper {
    private const val TICKER_GOOGL_MOCK = "GOOGL"
    private const val TICKER_AAPL_MOCK = "AAPL"
    private const val TICKER_MSFT_MOCK = "MSFT"
    private const val PERCENTAGE_MOCK_10 = 10
    private const val PERCENTAGE_MOCK_20 = 20
    private const val PERCENTAGE_MOCK_30 = 30
    private const val VALUE_MOCK_100 = 100
    private const val VALUE_MOCK_200 = 200
    private const val VALUE_MOCK_300 = 300

    private fun createInvestmentMock(
        ticker: String = TICKER_GOOGL_MOCK,
        percentage: Int = PERCENTAGE_MOCK_10,
        value: Int = VALUE_MOCK_100,
    ) = Investment(
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
