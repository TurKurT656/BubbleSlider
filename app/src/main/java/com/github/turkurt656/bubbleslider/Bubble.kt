package com.github.turkurt656.bubbleslider

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.sin

internal const val BUBBLE_INTERVAL = 1_000L
internal const val BUBBLE_ANIMATION_DURATION = 3_000L

internal fun DrawScope.drawBubble(
    sliderProgress: Float,
    bubbleProgress: Float,
    bubbleRadius: Dp,
    colors: BubbleSliderColors,
) {
    val bubbleMultiplier = 4 - 2 * abs(sin(bubbleProgress * Math.PI.toFloat()))
    val bubbleSize: Dp = bubbleRadius / bubbleMultiplier

    drawCircle(
        color = colors.thumbColor.copy(alpha = 1 - bubbleProgress),
        radius = bubbleSize.toPx(),
        center = Offset(
            x = sliderProgress + 10 * sin(bubbleProgress * Math.PI * 2).toFloat(),
            y = -(36.dp * bubbleProgress).toPx(),
        ),
    )
}