package com.github.turkurt656.bubbleslider

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.runtime.mutableLongStateOf
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
import kotlin.random.Random

@Composable
fun BubbleSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: BubbleSliderColors = BubbleSliderDefaults.colors(),
    trackThickness: Dp = 8.dp,
    thumbSize: Dp = 16.dp,
) {
    var componentWidth = 0f
    Box(modifier = modifier.height(max(trackThickness, thumbSize))) {

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
                delay(Random.nextLong(BUBBLE_INTERVAL_MIN, BUBBLE_INTERVAL_MAX))
            }
        }

        var lastDragTime by remember { mutableLongStateOf(0L) }
        var velocity by remember { mutableFloatStateOf(0f) }
        val velocityAnimation = remember { Animatable(initialValue = velocity) }
        LaunchedEffect(key1 = velocity) {
            velocityAnimation.animateTo(velocity, tween(250, easing = LinearEasing))
            if (velocity == 0f) return@LaunchedEffect
            velocityAnimation.animateTo(0f, tween(250, easing = LinearEasing))
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

        Bubbles(
            value = allowedValue,
            valueRange = valueRange,
            bubbleCount = bubbleCount,
            radius = thumbSize / 2,
            colors = colors,
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .draggable(
                    enabled = enabled,
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val currentTime = System.currentTimeMillis()
                        val timeSinceLastDrag = currentTime - lastDragTime
                        // Ignore very rapid changes
                        if (timeSinceLastDrag > 100L) {
                            velocity = delta.coerceIn(-8f, 8f) * 0.5f
                            lastDragTime = currentTime
                        }
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