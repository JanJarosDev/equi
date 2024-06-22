package com.jjdev.equi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jjdev.equi.ui.theme.dimens

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
) {
    //Todo replace with state arguments
    val tempData = mapOf(Pair("EGU", 10), Pair("XXX", 10))
    val tempColors = PieChartColors.entries.map { it.color }

    Column(
        modifier = modifier
            .padding(top = MaterialTheme.dimens.tripleExtraLarge)
    ) {
        PieChartComponent(
            data = tempData
        )
        DetailsListComponent(
            data = tempData,
            colors = tempColors
        )
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}