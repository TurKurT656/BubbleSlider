package com.github.turkurt656.bubbleslider.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.turkurt656.bubbleslider.BubbleSlider
import com.github.turkurt656.bubbleslider.ui.theme.*
import com.github.turkurt656.bubbleslider.ui.theme.BubbleSliderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BubbleSliderTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Purple40)
                        .padding(top = 48.dp)
                ) {

                    var value by remember {
                        mutableFloatStateOf(70f)
                    }

                    val modifier = Modifier
                        .padding(24.dp)
                        .width(220.dp)

                    val valueRange = 60f..100f

                    Slider(
                        modifier = modifier,
                        valueRange = valueRange,
                        enabled = true,
                        colors = SliderDefaults.colors(
                            thumbColor = Blue,
                            activeTrackColor = Gray1,
                            inactiveTrackColor = Gray2,
                        ),
                        value = value,
                        onValueChange = { value = it },
                        onValueChangeFinished = {
                            // action to VM
                        }
                    )

                    BubbleSlider(
                        modifier = modifier,
                        valueRange = valueRange,
                        value = value,
                        onValueChange = { value = it },
                    )
                }
            }
        }
    }
}