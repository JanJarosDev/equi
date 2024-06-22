package com.jjdev.equi

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jjdev.equi.ui.theme.dimens

@Composable
fun PieChartComponent(
    data: Map<String, Int>,
) {
    val outerRadius = 280.dp
    val chartBarWidth: Dp = 35.dp

    //TODO Move to VM
    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    //TODO Move to VM
    data.values.forEachIndexed { index, values ->
        floatValue.add(index, 360 * values.toFloat() / totalSum.toFloat())
    }

    //TODO Move to VM
    var lastValue = 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(outerRadius)
                    .padding(MaterialTheme.dimens.large)
            ) {
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = PieChartColors.entries[index].color,
                        startAngle = lastValue,
                        sweepAngle = value,
                        useCenter = false,
                        style = Stroke(
                            width = chartBarWidth.toPx(),
                            cap = StrokeCap.Butt
                        )
                    )
                    lastValue += value
                }
            }
        }
    }
}

@Preview
@Composable
fun PieChartComponentPreview() {
    PieChartComponent(
        data = mapOf(Pair("EGU", 10), Pair("XXX", 10))
    )
}