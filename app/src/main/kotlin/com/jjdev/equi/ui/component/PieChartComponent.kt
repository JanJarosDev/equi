package com.jjdev.equi.ui.component

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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jjdev.equi.core.ui.theme.dimens
import com.jjdev.equi.dashboard.presentation.model.Investment
import com.jjdev.equi.ui.PieChartColor
import com.jjdev.equi.ui.preview.PieChartPreviewProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

private const val CIRCLE_RADIUS = 360
private const val LOADING_ANIMATION_DURATION = 3000

@Composable
fun PieChartComponent(
    investments: ImmutableList<Investment>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val outerRadius = 280.dp
    val chartBarWidth: Dp = 35.dp

    //TODO Move to VM
    val totalSum = investments.sumOf { it.weight }
    val floatValue = mutableListOf<Float>()

    //TODO Move to VM
    investments.forEachIndexed { index, values ->
        floatValue.add(
            index,
            CIRCLE_RADIUS * investments[index].weight.toFloat() / totalSum.toFloat()
        )
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
                        animation = tween(LOADING_ANIMATION_DURATION, easing = LinearEasing),
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
                            color = PieChartColor.entries[index].color,
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

@Preview
@Composable
private fun PieChartComponentPreview(
    @PreviewParameter(PieChartPreviewProvider::class)
    pieChartPreviewModel: PieChartPreviewProvider.PieChartPreviewModel,
) {
    PieChartComponent(
        investments = pieChartPreviewModel.investments,
    )
}
