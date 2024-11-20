package com.github.turkurt656.bubbleslider.ui

import android.util.Log
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    fun onValueChange(tag: String, value: Float) {
        Log.d(tag, "value:$value")
    }

}