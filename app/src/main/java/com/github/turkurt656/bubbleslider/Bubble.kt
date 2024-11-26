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
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    progress: Float,
    thumbRadius: Dp,
    colors: BubbleSliderColors,
) {
    val progressWidth =
        ((value - valueRange.start) / valueRange.range) * size.width

    val bubbleMultiplier = 4 - 2 * abs(sin(progress * Math.PI.toFloat()))
    val bubbleSize: Dp = thumbRadius / bubbleMultiplier

    drawCircle(
        color = colors.thumbColor.copy(alpha = 1 - progress),
        radius = bubbleSize.toPx(),
        center = Offset(
            x = progressWidth + 10 * sin(progress * Math.PI * 2).toFloat(),
            y = -(36.dp * progress).toPx(),
        ),
    )
}