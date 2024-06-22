package com.jjdev.porfoliodelta

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DetailsListComponent(
    data: Map<String, Int>,
    colors: List<Color>
) {
    Column(
        modifier = Modifier
            .padding(top = 80.dp)
            .fillMaxWidth()
    ) {
        data.values.forEachIndexed { index, value ->
            DetailItemComponent(
                data = Pair(data.keys.elementAt(index), value),
                color = colors[index]
            )
        }
    }
}

@Preview
@Composable
fun DetailsListComponentPreview() {
    DetailsListComponent(
        data = mapOf(Pair("EGU", 10), Pair("XXX", 10)),
        colors = PieChartColors.entries.map { it.color }
    )
}