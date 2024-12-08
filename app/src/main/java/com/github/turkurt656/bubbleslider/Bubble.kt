package com.github.turkurt656.bubbleslider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

internal const val BUBBLE_INTERVAL_MIN = 300L
internal const val BUBBLE_INTERVAL_MAX = 1_500L
internal const val BUBBLE_ANIMATION_DURATION = 3_000L

@Stable
internal data class BubbleProperties(
    val radius: Dp,
    val flyHeight: Dp,
    val waveProperties: WaveProperties,
    val colors: BubbleSliderColors,
) {

    @Stable
    data class WaveProperties(
        val waveAmplitude: Int,
        val waveLength: Double,
        val isWaveForward: Boolean,
    )
}

@Composable
internal fun rememberBubbleProperties(
    key: Any?,
    radius: Dp,
    colors: BubbleSliderColors
) = remember(key) {
    BubbleProperties(
        radius = radius,
        flyHeight = Random.nextInt(24, 36).dp,
        waveProperties = BubbleProperties.WaveProperties(
            waveAmplitude = Random.nextInt(5, 15),
            waveLength = Math.PI * Random.nextInt(1, 3),
            isWaveForward = Random.nextBoolean(),
        ),
        colors = colors
    )
}

internal fun DrawScope.drawBubble(
    sliderProgress: Float,
    bubbleProgress: Float,
    properties: BubbleProperties,
) {
    val bubbleMultiplier = 4 - 2 * abs(sin(bubbleProgress * Math.PI.toFloat()))
    val bubbleSize: Dp = properties.radius / bubbleMultiplier

    val x = sliderProgress +
            (if (properties.waveProperties.isWaveForward) 1 else -1) *
            properties.waveProperties.waveAmplitude *
            sin(bubbleProgress * properties.waveProperties.waveLength).toFloat()
    val y = -(properties.flyHeight * bubbleProgress).toPx()

    drawCircle(
        color = properties.colors.thumbColor.copy(alpha = 1 - bubbleProgress),
        radius = bubbleSize.toPx(),
        center = Offset(x = x, y = y),
    )
}