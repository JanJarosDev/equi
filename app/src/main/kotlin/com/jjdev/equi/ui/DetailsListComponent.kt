package com.jjdev.equi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.jjdev.equi.dashboard.presentation.model.Investment
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun DetailsListComponent(
    investments: ImmutableList<Investment>,
    colors: ImmutableList<Color>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    //Todo lazycolumn
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        investments.forEachIndexed { index, value ->
            DetailItemComponent(
                investment = value,
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
    val investmentMock = Investment(TICKER_MOCK, PERCENTAGE_MOCK, VALUE_MOCK)
    DetailsListComponent(
        investments = persistentListOf(
            investmentMock,
            investmentMock,
        ),
        colors = PieChartColors.entries.map { it.color }.toPersistentList()
    )
}
