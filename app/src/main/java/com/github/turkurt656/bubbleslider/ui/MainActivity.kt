package com.github.turkurt656.bubbleslider.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.turkurt656.bubbleslider.BubbleSlider
import com.github.turkurt656.bubbleslider.BubbleSliderDefaults
import com.github.turkurt656.bubbleslider.ui.theme.BubbleSliderTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BubbleSliderTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 48.dp)
                ) {

                    var value by remember {
                        mutableFloatStateOf(70f)
                    }

                    var enabled by remember {
                        mutableStateOf(true)
                    }

                    val modifier = Modifier
                        .padding(horizontal = 96.dp, vertical = 16.dp)
                        .fillMaxWidth()

                    val valueRange = 0f..100f

                    Text(
                        modifier = modifier,
                        text = "Material3 Slider vs Bubble Slider",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Text(
                        modifier = modifier,
                        textAlign = TextAlign.Center,
                        text = "Current value: ${"%.2f".format(value)}",
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Slider(
                        modifier = modifier,
                        valueRange = valueRange,
                        enabled = enabled,
                        colors = SliderDefaults.colors(),
                        value = value,
                        onValueChange = { value = it },
                        onValueChangeFinished = {
                            viewModel.onValueChange("Slider", value)
                        }
                    )

                    BubbleSlider(
                        modifier = modifier,
                        valueRange = valueRange,
                        enabled = enabled,
                        colors = BubbleSliderDefaults.colors(),
                        value = value,
                        onValueChange = { value = it },
                        onValueChangeFinished = {
                            viewModel.onValueChange("BubbleSlider", value)
                        }
                    )

                    Button(
                        modifier = modifier,
                        onClick = {
                            enabled = !enabled
                        }) {
                        Text(if (enabled) "Disable" else "Enable")
                    }
                }
            }
        }
    }
}