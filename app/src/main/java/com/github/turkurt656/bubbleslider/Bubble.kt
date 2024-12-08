package com.github.turkurt656.bubbleslider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

internal data class BubbleProperties(
    val sizeProperties: SizeProperties,
    val waveProperties: WaveProperties,
    val colors: BubbleSliderColors,
) {

    data class SizeProperties(
        val radius: Dp,
        val flyHeight: Dp,
        val bubbleSizeBaseDivisor: Int,
        val bubbleSizeOscillationFactor: Int,
    )

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
        sizeProperties = BubbleProperties.SizeProperties(
            radius = radius,
            flyHeight = Random.nextInt(BUBBLE_FLY_HEIGHT_MIN, BUBBLE_FLY_HEIGHT_MAX).dp,
            bubbleSizeBaseDivisor = Random.nextInt(BUBBLE_SIZE_BASE_DIVISOR_MIN, BUBBLE_SIZE_BASE_DIVISOR_MAX),
            bubbleSizeOscillationFactor = Random.nextInt(BUBBLE_SIZE_OSCILLATION_FACTOR_MIN, BUBBLE_SIZE_OSCILLATION_FACTOR_MAX),
        ),
        waveProperties = BubbleProperties.WaveProperties(
            waveAmplitude = Random.nextInt(BUBBLE_WAVE_AMPLITUDE_MIN, BUBBLE_WAVE_AMPLITUDE_MAX),
            waveLength = Math.PI * Random.nextInt(BUBBLE_WAVE_LENGTH_MIN, BUBBLE_WAVE_LENGTH_MAX),
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
    val bubbleSizeDivisor = properties.sizeProperties.bubbleSizeBaseDivisor -
            properties.sizeProperties.bubbleSizeOscillationFactor *
            abs(sin(bubbleProgress * Math.PI.toFloat()))
    val bubbleSize: Dp = properties.sizeProperties.radius / bubbleSizeDivisor

    val x = sliderProgress +
            (if (properties.waveProperties.isWaveForward) 1 else -1) *
            properties.waveProperties.waveAmplitude *
            sin(bubbleProgress * properties.waveProperties.waveLength).toFloat()
    val y = -(properties.sizeProperties.flyHeight * bubbleProgress).toPx()

    drawCircle(
        color = properties.colors.thumbColor.copy(alpha = 1 - bubbleProgress),
        radius = bubbleSize.toPx(),
        center = Offset(x = x, y = y),
    )
}