package com.jjdev.equi.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.shimmerLoadingAnimation(
    isLoading: Boolean = false,
    isDarkModeActive: Boolean = false,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier {
    if (!isLoading) {
        return this
    } else {
        return composed {
            val shimmerColors = ShimmerAnimationData(isDarkMode = isDarkModeActive).getColours()

            val transition = rememberInfiniteTransition(label = "")

            val translateAnimation = transition.animateFloat(
                initialValue = 0f,
                targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = durationMillis,
                        easing = LinearEasing,
                    ),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "Shimmer loading animation",
            )

            this.background(
                brush = Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
                    end = Offset(x = translateAnimation.value, y = angleOfAxisY),
                ),
            )
        }
    }
}

data class ShimmerAnimationData(
    private val isDarkMode: Boolean,
) {
    fun getColours(): List<Color> {
        return if (isDarkMode) {
            val color = Color.White
            listOf(
                color.copy(alpha = 0.0f),
                color.copy(alpha = 0.1f),
                color.copy(alpha = 0.2f),
                color.copy(alpha = 0.1f),
                color.copy(alpha = 0.0f),
            )
        } else {
            val color = Color.Black
            listOf(
                color.copy(alpha = 0.0f),
                color.copy(alpha = 0.1f),
                color.copy(alpha = 0.2f),
                color.copy(alpha = 0.1f),
                color.copy(alpha = 0.0f),
            )

        }
    }
}
