package com.github.turkurt656.bubbleslider

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.scale
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

internal fun DrawScope.drawDrop(
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
