package com.github.turkurt656.bubbleslider

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

object BubbleSliderDefaults {

    private var _cachedColor: BubbleSliderColors? = null
    private val cachedColor: BubbleSliderColors
        @Composable
        get() {
            return _cachedColor
                ?: BubbleSliderColors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledThumbColor = MaterialTheme.colorScheme.onSurface
                        .copy(alpha = 0.38f)
                        .compositeOver(MaterialTheme.colorScheme.surface),
                    disabledActiveTrackColor = MaterialTheme.colorScheme.onSurface
                        .copy(alpha = 0.38f),
                    disabledInactiveTrackColor = MaterialTheme.colorScheme.onSurface
                        .copy(alpha = 0.12f),
                ).also {
                    _cachedColor = it
                }
        }

    @Composable
    fun colors(
        thumbColor: Color = Color.Unspecified,
        activeTrackColor: Color = Color.Unspecified,
        inactiveTrackColor: Color = Color.Unspecified,
        disabledThumbColor: Color = Color.Unspecified,
        disabledActiveTrackColor: Color = Color.Unspecified,
        disabledInactiveTrackColor: Color = Color.Unspecified,
    ): BubbleSliderColors =
        cachedColor.copy(
            thumbColor = thumbColor,
            activeTrackColor = activeTrackColor,
            inactiveTrackColor = inactiveTrackColor,
            disabledThumbColor = disabledThumbColor,
            disabledActiveTrackColor = disabledActiveTrackColor,
            disabledInactiveTrackColor = disabledInactiveTrackColor,
        )
}