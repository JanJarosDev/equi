package com.jjdev.equi.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jjdev.equi.core.ui.theme.dimens
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

private const val CIRCLE_RADIUS = 360

@Composable
fun PieChartComponent(
    data: ImmutableList<Triple<String, Int, Int>>,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val outerRadius = 280.dp
    val chartBarWidth: Dp = 35.dp

    //TODO Move to VM
    val totalSum = data.sumOf { it.second }
    val floatValue = mutableListOf<Float>()

    //TODO Move to VM
    data.forEachIndexed { index, values ->
        floatValue.add(index, CIRCLE_RADIUS * data[index].second.toFloat() / totalSum.toFloat())
    }

    //TODO Move to VM
    var lastValue = 0f

    val angle = remember {
        Animatable(0f)
    }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            launch {
                angle.animateTo(
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
        } else {
            angle.animateTo(0f)
        }
    }

    Column(
        modifier = modifier
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
                    rotate(degrees = angle.value) {
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
}

private const val TICKER_MOCK = "EGU"
private const val PERCENTAGE_MOCK = 10
private const val VALUE_MOCK = 100

@Preview
@Composable
fun PieChartComponentPreview() {
    PieChartComponent(
        data = persistentListOf(
            Triple(TICKER_MOCK, PERCENTAGE_MOCK, VALUE_MOCK),
            Triple(TICKER_MOCK, PERCENTAGE_MOCK, VALUE_MOCK)
        ),
    )
}
