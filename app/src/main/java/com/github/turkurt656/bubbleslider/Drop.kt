package com.github.turkurt656.bubbleslider

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.abs

internal fun DrawScope.drawDrop(
    color: Color,
    center: Offset,
    radius: Float,
    velocity: Float,
) {
    /*
               X1   Y1   X2   Y2   X3    Y3
            M  0    0
            C  80  -20   200 -172  340  -172
            C  440 -172  512 -100  512   0
            C  512  100  440  172  340   172
            C  200  172  80   20   0     0
            z

            M  0    0
            C  0   -100  72  -172  172  -172
            C  272 -172  340 -100  340  0
            C  340  100  272  172  172  172
            C  72   172  0    100  0    0
            z
    */
    val pathRadius = 172f
    val velocityFraction = abs(velocity) / 4 // velocity between 0..1 = 0..VELOCITY_MAX
    val ratio = radius / (pathRadius)
    val a = lerp(0f, 80f, velocityFraction)
    val b = lerp(100f, 20f, velocityFraction)
    val c = lerp(pathRadius, 340f, velocityFraction)
    val d = lerp(100f, 140f, velocityFraction)
    val pathWidth = c + pathRadius
    val centerNew = Offset(center.x - pathWidth * ratio / 2, center.y)
    val dropPath = Path().apply {
        moveTo(0f * ratio, 0f * ratio)
        cubicTo(a * ratio, -b * ratio, (c - d) * ratio, -pathRadius * ratio, c * ratio, -pathRadius * ratio)
        cubicTo((c + 100) * ratio, -pathRadius * ratio, pathWidth * ratio, (-100f) * ratio, pathWidth * ratio, 0f * ratio)
        cubicTo(pathWidth * ratio, 100f * ratio, (c + 100) * ratio, pathRadius * ratio, c * ratio, pathRadius * ratio)
        cubicTo((c - d) * ratio, pathRadius * ratio, a * ratio, b * ratio, 0f * ratio, 0f * ratio)
        close()
    }
    withTransform(
        {
            scale(scaleX = if (velocity < 0) -1f else 1f, scaleY = 1f, pivot = center)
            translate(centerNew.x, centerNew.y)
        }) {
        drawPath(path = dropPath, color = color)
    }
}

/**
 * Linearly interpolates the fraction [amount] between [start] and [stop]
 *
 * @param start starting value
 * @param stop ending value
 * @param amount normalized between 0 - 1
 */
private fun lerp(start: Float, stop: Float, amount: Float): Float {
    return start + (stop - start) * amount
}
