package com.instacart.formula.compose.stopwatch

import com.instacart.formula.Listener

data class StopwatchRenderModel(
    val timePassed: String,
    val startStopButton: ButtonRenderModel,
    val resetButton: ButtonRenderModel,
    val switchControl: SwitchRenderModel
)

data class ButtonRenderModel(
    val text: String,
    val onSelected: Listener<Unit>,
)

data class SwitchRenderModel(
    val isChecked: Boolean,
    val onCheckedChange: Listener<Boolean>
)
