package com.github.turkurt656.bubbleslider

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import kotlinx.coroutines.delay

@Composable
fun BubbleSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: BubbleSliderColors = BubbleSliderDefaults.colors(),
    trackThickness: Dp = 6.dp,
    thumbSize: Dp = 12.dp,
) {
    val componentHeight = max(trackThickness, thumbSize)
    var componentWidth = 0f

    Box(modifier = modifier.height(componentHeight)) {

        var isPressed by remember { mutableStateOf(false) }

        var bubbleCount by remember { mutableIntStateOf(0) }
        // Create bubbles when pressed
        LaunchedEffect(isPressed) {
            if (!isPressed) {
                delay(BUBBLE_ANIMATION_DURATION)
                bubbleCount = 0
            }
            while (isPressed) {
                bubbleCount += 1
                delay(BUBBLE_INTERVAL) // Create new bubble every [BUBBLE_INTERVAL]ms
            }
        }

        var velocity by remember { mutableFloatStateOf(0f) }
        val velocityAnimation = remember { Animatable(initialValue = 0f) }
        LaunchedEffect(key1 = velocity) {
            velocityAnimation.animateTo(velocity)
        }

        val thumbRadius by animateDpAsState(
            targetValue = if (isPressed) (thumbSize / 1.5f) else thumbSize / 2,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessLow,
            ),
            label = "Thumb Size Animation",
        )

        val allowedValue = value.coerceIn(valueRange.start, valueRange.endInclusive)

        // Bubbles
        repeat(bubbleCount) { index ->

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
                        calculateProgress(allowedValue, valueRange),
                        bubbleProgress.value,
                        thumbSize / 2,
                        colors,
                    )
                }
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .draggable(
                    enabled = enabled,
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        velocity = delta.coerceIn(-8f, 8f) * 0.3f
                        val deltaAsValue = (delta * valueRange.range / componentWidth)
                        val newValue = (value + deltaAsValue)
                            .coerceIn(valueRange.start, valueRange.endInclusive)
                        onValueChange(newValue)
                    },
                    onDragStopped = {
                        isPressed = false
                        velocity = 0f
                        onValueChangeFinished?.invoke()
                    },
                    onDragStarted = { isPressed = true },
                    startDragImmediately = true,
                ),
            onDraw = {
                val canvasWidth = size.width
                val canvasHeight = size.height
                componentWidth = canvasWidth
                val progress = calculateProgress(allowedValue, valueRange)

                drawLine(
                    start = Offset(x = 0f, y = canvasHeight / 2),
                    end = Offset(x = canvasWidth, y = canvasHeight / 2),
                    color = colors.trackColor(enabled, false),
                    strokeWidth = trackThickness.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    start = Offset(x = 0f, y = canvasHeight / 2),
                    end = Offset(x = progress, y = canvasHeight / 2),
                    color = colors.trackColor(enabled, true),
                    strokeWidth = trackThickness.toPx(),
                    cap = StrokeCap.Round
                )
                drawDrop(
                    color = colors.thumbColor(enabled),
                    center = Offset(x = progress, y = canvasHeight / 2),
                    radius = thumbRadius.toPx(),
                    velocity = velocityAnimation.value,
                )
            }
        )
    }
}