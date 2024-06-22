package com.jjdev.equi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DetailsListComponent(
    data: Map<String, Int>,
    colors: List<Color>
) {
    //Todo lazycolumn
    Column(
        modifier = Modifier.fillMaxWidth()
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