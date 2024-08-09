package com.jjdev.equi.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.unit.dp
import com.jjdev.equi.core.ui.theme.dimens
import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import com.jjdev.equi.ui.PieChartColor
import com.jjdev.equi.ui.preview.PieChartPreviewProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

private const val FULL_CIRCLE_DEGREES = 360
private const val LOADING_ANIMATION_DURATION = 3000
private const val CIRCLE_OUTER_RADIUS = 280
private const val CHART_BAR_WIDTH = 35

@Composable
fun PieChartComponent(
    investments: ImmutableList<InvestmentUIModel>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val investmentsWeightsSum = investments.sumOf { it.weight }
    val pieChartSegments = mutableListOf<Float>()
    var lastSegmentEndingValueHolder = 0f

    val animationAngle = remember {
        Animatable(0f)
    }

    investments.forEachIndexed { index, _ ->
        pieChartSegments.add(
            index,
            FULL_CIRCLE_DEGREES * investments[index].weight.toFloat() / investmentsWeightsSum.toFloat()
        )
    }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            launch {
                animationAngle.animateTo(
                    targetValue = FULL_CIRCLE_DEGREES.toFloat(),
                    animationSpec = infiniteRepeatable(
                        animation = tween(LOADING_ANIMATION_DURATION, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart,
                    ),
                )
            }
        } else {
            animationAngle.animateTo(0f)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier
                    .size(CIRCLE_OUTER_RADIUS.dp)
                    .padding(MaterialTheme.dimens.large),
            ) {
                pieChartSegments.forEachIndexed { index, value ->
                    rotate(degrees = animationAngle.value) {
                        drawArc(
                            color = PieChartColor.entries[index].color,
                            startAngle = lastSegmentEndingValueHolder,
                            sweepAngle = value,
                            useCenter = false,
                            style = Stroke(
                                width = CHART_BAR_WIDTH.dp.toPx(),
                                cap = StrokeCap.Butt,
                            ),
                        )
                        lastSegmentEndingValueHolder += value
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
