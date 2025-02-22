package com.github.turkurt656.bubbleslider

internal val ClosedFloatingPointRange<Float>.range
    get() = endInclusive - start

internal fun inverseLerp(start: Float, stop: Float, value: Float): Float {
    return ((value - start) / (stop - start))
}