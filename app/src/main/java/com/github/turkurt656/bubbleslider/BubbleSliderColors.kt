package com.github.turkurt656.bubbleslider

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse


@Immutable
class BubbleSliderColors(
    val thumbColor: Color,
    val activeTrackColor: Color,
    val inactiveTrackColor: Color,
    val disabledThumbColor: Color,
    val disabledActiveTrackColor: Color,
    val disabledInactiveTrackColor: Color,
) {

    fun copy(
        thumbColor: Color = this.thumbColor,
        activeTrackColor: Color = this.activeTrackColor,
        inactiveTrackColor: Color = this.inactiveTrackColor,
        disabledThumbColor: Color = this.disabledThumbColor,
        disabledActiveTrackColor: Color = this.disabledActiveTrackColor,
        disabledInactiveTrackColor: Color = this.disabledInactiveTrackColor,
    ) =
        BubbleSliderColors(
            thumbColor.takeOrElse { this.thumbColor },
            activeTrackColor.takeOrElse { this.activeTrackColor },
            inactiveTrackColor.takeOrElse { this.inactiveTrackColor },
            disabledThumbColor.takeOrElse { this.disabledThumbColor },
            disabledActiveTrackColor.takeOrElse { this.disabledActiveTrackColor },
            disabledInactiveTrackColor.takeOrElse { this.disabledInactiveTrackColor },
        )

    @Stable
    internal fun thumbColor(enabled: Boolean): Color =
        if (enabled) thumbColor else disabledThumbColor

    @Stable
    internal fun trackColor(enabled: Boolean, active: Boolean): Color =
        if (enabled) {
            if (active) activeTrackColor else inactiveTrackColor
        } else {
            if (active) disabledActiveTrackColor else disabledInactiveTrackColor
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is BubbleSliderColors) return false

        if (thumbColor != other.thumbColor) return false
        if (activeTrackColor != other.activeTrackColor) return false
        if (inactiveTrackColor != other.inactiveTrackColor) return false
        if (disabledThumbColor != other.disabledThumbColor) return false
        if (disabledActiveTrackColor != other.disabledActiveTrackColor) return false
        if (disabledInactiveTrackColor != other.disabledInactiveTrackColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = thumbColor.hashCode()
        result = 31 * result + activeTrackColor.hashCode()
        result = 31 * result + inactiveTrackColor.hashCode()
        result = 31 * result + disabledThumbColor.hashCode()
        result = 31 * result + disabledActiveTrackColor.hashCode()
        result = 31 * result + disabledInactiveTrackColor.hashCode()
        return result
    }
}