package com.anafthdev.remindme.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun DragHandle(
	modifier: Modifier = Modifier
) {
	
	Box(
		modifier = modifier
			.width(32.dp)
			.height(4.dp)
			.clip(CircleShape)
			.background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
	)
}
