package com.jjdev.equi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DetailsListComponent(
    data: List<Triple<String, Int, Double>>,
    colors: List<Color>
) {
    //Todo lazycolumn
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        data.forEachIndexed { index, value ->
            DetailItemComponent(
                data = value,
                color = colors[index]
            )
        }
    }
}

private const val TICKER_MOCK = "EGU"
private const val PERCENTAGE_MOCK = 10
private const val VALUE_MOCK = 100.0

@Preview
@Composable
fun DetailsListComponentPreview() {
    DetailsListComponent(
        data = listOf(
            Triple(TICKER_MOCK, PERCENTAGE_MOCK, VALUE_MOCK),
            Triple(TICKER_MOCK, PERCENTAGE_MOCK, VALUE_MOCK)
        ),
        colors = PieChartColors.entries.map { it.color }
    )
}
