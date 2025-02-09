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
        See: https://yqnn.github.io/svg-path-editor/
        M 0 0
        C 172 -21.5 387 -172 516 -172
        C 602 -172 688 -86 688 0
        C 688 86 602 172 516 172
        C 387 172 172 21.5 0 0
        z

        M 0 0
        C 0 -86 86 -172 172 -172
        C 258 -172 344 -86 344 0
        C 344 86 258 172 172 172
        C 86 172 0 86 0 0
        z

    */
    val pathRadius = 172f
    val velocityFraction = abs(velocity) / 4 // velocity between 0..1 = 0..VELOCITY_MAX
    val ratio = radius / (pathRadius)
    val z = pathRadius / 2
    val a = lerp(0f, pathRadius, velocityFraction)
    val b = lerp(z, z / 4, velocityFraction)
    val t = lerp(pathRadius, pathRadius * 3, velocityFraction)
    val d = lerp(z, pathRadius * 3 / 4, velocityFraction)
    val pathWidth = t + pathRadius
    val m = floatArrayOf(
        // X1, Y1, X2, Y2, X3, Y3
        a, -b, t - d, -pathRadius, t, -pathRadius,
        t + z, -pathRadius, pathWidth, -z, pathWidth, 0f,
        pathWidth, z, t + z, pathRadius, t, pathRadius,
        t - d, pathRadius, a, b, 0f, 0f,
    ) * ratio
    val dropPath = Path().apply {
        moveTo(0f, 0f)
        cubicTo(m[0], m[1], m[2], m[3], m[4], m[5])
        cubicTo(m[6], m[7], m[8], m[9], m[10], m[11])
        cubicTo(m[12], m[13], m[14], m[15], m[16], m[17])
        cubicTo(m[18], m[19], m[20], m[21], m[22], m[23])
        close()
    }
    withTransform(
        {
            scale(scaleX = if (velocity < 0) -1f else 1f, scaleY = 1f, pivot = center)
            translate(center.x - pathWidth * ratio / 2, center.y)
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

private operator fun FloatArray.times(m: Float): FloatArray {
    for (i in indices) {
        this[i] *= m
    }
    return this
}
