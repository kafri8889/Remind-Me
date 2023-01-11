package com.anafthdev.remindme.uicomponent

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anafthdev.remindme.data.HourClockType

@Composable
fun HourClockSelector(
	type: HourClockType,
	modifier: Modifier = Modifier,
	onValueChanged: (HourClockType) -> Unit
) {
	
	Box(
		modifier = modifier
	) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			HourClockItem(
				type = HourClockType.AM,
				selected = type == HourClockType.AM,
				onClick = {
					onValueChanged(HourClockType.AM)
				}
			)
			
			HourClockItem(
				type = HourClockType.PM,
				selected = type == HourClockType.PM,
				onClick = {
					onValueChanged(HourClockType.PM)
				}
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HourClockItem(
	type: HourClockType,
	selected: Boolean,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	
	val containerColor by animateColorAsState(
		targetValue = if (selected) MaterialTheme.colorScheme.tertiaryContainer
		else MaterialTheme.colorScheme.inverseOnSurface,
		animationSpec = tween(
			durationMillis = 500
		)
	)
	
	Card(
		onClick = onClick,
		modifier = modifier,
		shape = MaterialTheme.shapes.small,
		colors = CardDefaults.cardColors(
			containerColor = containerColor
		)
	) {
		Text(
			text = type.name,
			textAlign = TextAlign.Center,
			modifier = Modifier
				.padding(12.dp)
		)
	}
}
