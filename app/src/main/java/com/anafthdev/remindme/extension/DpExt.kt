package com.anafthdev.remindme.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

val Dp.asPx: Float
	@Composable
	get() = with(LocalDensity.current) { this@asPx.toPx() }
