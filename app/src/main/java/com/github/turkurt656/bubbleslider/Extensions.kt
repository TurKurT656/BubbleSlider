package com.github.turkurt656.bubbleslider

import androidx.compose.ui.graphics.drawscope.DrawScope

internal val ClosedFloatingPointRange<Float>.range
    get() = endInclusive - start

internal fun DrawScope.calculateProgress(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
): Float {
    return ((value - valueRange.start) / valueRange.range) * size.width
}