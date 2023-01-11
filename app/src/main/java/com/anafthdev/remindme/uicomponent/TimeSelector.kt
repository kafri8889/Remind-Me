package com.anafthdev.remindme.uicomponent

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anafthdev.remindme.data.TimeType
import com.anafthdev.remindme.extension.hourMinuteFormat

@Preview
@Composable
fun TimeSelectorPreview() {
	
	TimeSelector(
		hours = 1,
		minutes = 24
	)
}

@Composable
fun TimeSelector(
	hours: Int,
	minutes: Int,
	modifier: Modifier = Modifier,
	onTimeTypeChanged: (TimeType) -> Unit = {}
) {

	var selectedTimeType by remember { mutableStateOf(TimeType.Hours) }
	
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
	) {
		TimeItem(
			value = hours,
			selected = selectedTimeType == TimeType.Hours,
			onClick = {
				selectedTimeType = TimeType.Hours
				onTimeTypeChanged(selectedTimeType)
			}
		)
		
		Text(text = ":")
		
		TimeItem(
			value = minutes,
			selected = selectedTimeType == TimeType.Minutes,
			onClick = {
				selectedTimeType = TimeType.Minutes
				onTimeTypeChanged(selectedTimeType)
			}
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeItem(
	value: Int,
	selected: Boolean,
	modifier: Modifier = Modifier,
	onClick: () -> Unit = {}
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
			text = hourMinuteFormat(value),
			modifier = Modifier
				.padding(12.dp)
		)
	}
}
