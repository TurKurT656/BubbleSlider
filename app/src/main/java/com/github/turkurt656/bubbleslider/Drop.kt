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
    val r = 172f
    val ratio = radius / r
    val hand1x = lerp(0f, r, abs(velocity))
    val hand1y = lerp(r / 2, r / 8, abs(velocity))
    val tail = lerp(r, 3 * r, abs(velocity))
    val hand2 = lerp(r / 2, 3 * r / 4, abs(velocity))
    val width = tail + r
    val m = floatArrayOf(
        // X1, Y1, X2, Y2, X3, Y3
        hand1x, -hand1y, tail - hand2, -r, tail, -r,
        tail + (r / 2), -r, width, -(r / 2), width, 0f,
        width, r / 2, tail + (r / 2), r, tail, r,
        tail - hand2, r, hand1x, hand1y, 0f, 0f,
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
            translate(center.x - width * ratio / 2, center.y)
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

private operator fun FloatArray.times(m: Float): FloatArray =
    FloatArray(size) { this[it] * m }
