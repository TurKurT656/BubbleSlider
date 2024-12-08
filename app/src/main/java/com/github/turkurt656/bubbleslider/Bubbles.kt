package com.github.turkurt656.bubbleslider

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun Bubbles(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    bubbleCount: Int,
    radius: Dp,
    colors: BubbleSliderColors,
) {
    repeat(bubbleCount) { index ->

        val properties = rememberBubbleProperties(
            key = index,
            radius = radius,
            colors = colors,
        )

        val bubbleProgress = remember(index) { Animatable(0f) }
        LaunchedEffect(index) {
            bubbleProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    BUBBLE_ANIMATION_DURATION.toInt(),
                    easing = FastOutLinearInEasing
                ),
            )
        }

        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                drawBubble(
                    sliderProgress = calculateProgress(value, valueRange),
                    bubbleProgress = bubbleProgress.value,
                    properties = properties,
                )
            }
        )
    }

}