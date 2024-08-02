package com.jjdev.equi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun DetailsListComponent(
    data: ImmutableList<Triple<String, Int, Int>>,
    colors: ImmutableList<Color>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    //Todo lazycolumn
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        data.forEachIndexed { index, value ->
            DetailItemComponent(
                data = value,
                color = colors[index],
                isLoading = isLoading,
            )
        }
    }
}

private const val TICKER_MOCK = "EGU"
private const val PERCENTAGE_MOCK = 10
private const val VALUE_MOCK = 100

@Preview
@Composable
fun DetailsListComponentPreview() {
    DetailsListComponent(
        data = persistentListOf(
            Triple(TICKER_MOCK, PERCENTAGE_MOCK, VALUE_MOCK),
            Triple(TICKER_MOCK, PERCENTAGE_MOCK, VALUE_MOCK)
        ),
        colors = PieChartColors.entries.map { it.color }.toPersistentList()
    )
}
