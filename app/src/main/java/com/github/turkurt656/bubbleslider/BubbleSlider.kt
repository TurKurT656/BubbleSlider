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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.github.turkurt656.bubbleslider.ui.theme.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BubbleSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    progressThickness: Dp = 6.dp,
    selectorSize: Dp = 12.dp,
    maxValue: Float = 100f,
) {
    val componentHeight = max(progressThickness, selectorSize)
    val allowedValue = value.coerceIn(0f, maxValue)

    var componentWidth = 0f

    Box(modifier = modifier.height(componentHeight)) {

        var isPressed by remember { mutableStateOf(false) }

        var velocity by remember { mutableFloatStateOf(0f) }
        val velocityAnimation = remember { Animatable(initialValue = 0f) }
        LaunchedEffect(key1 = velocity) {
            velocityAnimation.animateTo(velocity)
        }

        val circleRadius by animateDpAsState(
            targetValue = if (isPressed) (selectorSize / 2 * 1.2f) else selectorSize / 2,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessLow,
            ),
            label = "Circle Size Animation",
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        velocity = delta.coerceIn(-8f, 8f) * 0.3f
                        val deltaAsValue = (delta * maxValue / componentWidth)
                        onValueChange((value + deltaAsValue).coerceIn(0f, maxValue))
                    },
                    onDragStopped = {
                        isPressed = false
                        velocity = 0f
                    },
                    onDragStarted = { isPressed = true },
                    startDragImmediately = true,
                ),
            onDraw = {
                val canvasWidth = size.width
                val canvasHeight = size.height
                componentWidth = canvasWidth
                val progressWidth = (allowedValue / maxValue) * canvasWidth

                drawLine(
                    start = Offset(x = 0f, y = canvasHeight / 2),
                    end = Offset(x = canvasWidth, y = canvasHeight / 2),
                    brush = Brush.horizontalGradient(
                        colors = listOf(Gray1, Gray2),
                        startX = 0f,
                        endX = canvasWidth,
                    ),
                    strokeWidth = progressThickness.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    start = Offset(x = 0f, y = canvasHeight / 2),
                    end = Offset(x = progressWidth, y = canvasHeight / 2),
                    color = Blue,
                    strokeWidth = progressThickness.toPx(),
                    cap = StrokeCap.Round
                )
                drawDrop(
                    color = Blue,
                    center = Offset(x = progressWidth, y = canvasHeight / 2),
                    radius = circleRadius.toPx(),
                    velocity = velocityAnimation.value,
                )
            }
        )
    }
}

private fun DrawScope.drawDrop(
    color: Color,
    center: Offset,
    radius: Float,
    velocity: Float,
) {
    scale(scaleX = 1 + abs(velocity) * 0.05f, scaleY = 1f) {
        drawCircle(
            color = color,
            radius = radius,
            center = center,
        )
        val path = Path().apply {
            val startAngle = if (velocity > 0) 3 * PI / 4 else PI / 4
            val startX = center.x + cos(startAngle).toFloat() * radius
            val tail = radius * velocity * -1
            moveTo(
                x = startX,
                y = center.y - sin(startAngle).toFloat() * radius,
            )
            quadraticTo(
                x1 = startX + tail / 2,
                y1 = center.y,
                x2 = startX + tail,
                y2 = center.y
            )
            quadraticTo(
                x1 = startX + tail / 2,
                y1 = center.y,
                x2 = startX,
                y2 = center.y + sin(startAngle).toFloat() * radius
            )
            close()
        }
        drawPath(
            path = path,
            color = color,
            style = Fill
        )
    }
}
