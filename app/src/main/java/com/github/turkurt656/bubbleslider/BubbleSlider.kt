package com.github.turkurt656.bubbleslider

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

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

        var velocity by remember { mutableFloatStateOf(0f) }
        val velocityAnimation = remember { Animatable(initialValue = 0f) }
        LaunchedEffect(key1 = velocity) {
            velocityAnimation.animateTo(velocity)
        }

        val circleRadius by animateDpAsState(
            targetValue = if (isPressed) (thumbSize / 2 * 1.2f) else thumbSize / 2,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessLow,
            ),
            label = "Circle Size Animation",
        )

        val allowedValue = value.coerceIn(valueRange.start, valueRange.endInclusive)

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .draggable(
                    enabled = enabled,
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        velocity = delta.coerceIn(-8f, 8f) * 0.3f
                        val deltaAsValue = (delta * valueRange.range / componentWidth)
                        onValueChange((value + deltaAsValue).coerceIn(valueRange.start, valueRange.endInclusive))
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
                val progressWidth = ((allowedValue - valueRange.start) / valueRange.range) * canvasWidth

                drawLine(
                    start = Offset(x = 0f, y = canvasHeight / 2),
                    end = Offset(x = canvasWidth, y = canvasHeight / 2),
                    color = colors.trackColor(enabled, false),
                    strokeWidth = trackThickness.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    start = Offset(x = 0f, y = canvasHeight / 2),
                    end = Offset(x = progressWidth, y = canvasHeight / 2),
                    color = colors.trackColor(enabled, true),
                    strokeWidth = trackThickness.toPx(),
                    cap = StrokeCap.Round
                )
                drawDrop(
                    color = colors.thumbColor(enabled),
                    center = Offset(x = progressWidth, y = canvasHeight / 2),
                    radius = circleRadius.toPx(),
                    velocity = velocityAnimation.value,
                )
            }
        )
    }
}