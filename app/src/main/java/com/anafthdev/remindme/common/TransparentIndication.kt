package com.anafthdev.remindme.common

import androidx.compose.foundation.Indication
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val TransparentIndication: Indication
	@Composable
    get() = rememberRipple(color = Color.Transparent)
