package com.anafthdev.remindme.ui.reminder_detail

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.anafthdev.remindme.uicomponent.TimePicker
import com.anafthdev.remindme.uicomponent.TimeSelector

@Composable
fun ReminderDetailScreen() {
	
	val viewModel = hiltViewModel<ReminderDetailViewModel>()

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
		) {
			TimePicker(
				initialValue = viewModel.clockPositionValue,
				circleRadius = 90f,
				maxValue = 12,
				onPositionChange = viewModel::updateClockPosition,
				modifier = Modifier
					.fillMaxWidth(0.7f)
					.aspectRatio(1f / 1f)
			)
			
			TimeSelector(
				hours = viewModel.hours,
				minutes = viewModel.minutes,
				is24Hour = true,
				onTimeTypeChanged = viewModel::updateTimeType
			)
		}
	}
}
