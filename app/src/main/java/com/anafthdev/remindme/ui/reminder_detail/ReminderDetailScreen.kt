package com.anafthdev.remindme.ui.reminder_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.hilt.navigation.compose.hiltViewModel
import com.anafthdev.remindme.data.TimeType
import com.anafthdev.remindme.uicomponent.HourClockSelector
import com.anafthdev.remindme.uicomponent.TimePicker
import com.anafthdev.remindme.uicomponent.TimeSelector

@Composable
fun ReminderDetailScreen() {
	// TODO: TimePicker minute, animate text
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
				pos = viewModel.clockPositionValue,
				maxValue = if (viewModel.selectedTimeType == TimeType.Hours) 12 else 60,
				animate = viewModel.animateClockPositionValue,
				onPositionChange = viewModel::updateClockPosition,
				onAnimationFinished = {
					viewModel.updateAnimateClockPositionValue(false)
				},
				modifier = Modifier
					.fillMaxWidth(0.7f)
					.aspectRatio(1f / 1f)
					.rotate(180f)
			)
			
			Column(
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				TimeSelector(
					hours = viewModel.hours,
					minutes = viewModel.minutes,
					onTimeTypeChanged = viewModel::updateTimeType
				)
				
				HourClockSelector(
					type = viewModel.hourClockType,
					onValueChanged = viewModel::updateHourClockType
				)
			}
		}
	}
}

@Composable
private fun TimePicker(
	pos: Int,
	maxValue: Int,
	modifier: Modifier = Modifier,
	animate: Boolean = true,
	onPositionChange: (Int) -> Unit,
	onAnimationFinished: (Int) -> Unit
) {
	
	val clockPositionValue by animateIntAsState(
		targetValue = pos,
		animationSpec = tween(600),
		finishedListener = onAnimationFinished
	)
	
	AnimatedVisibility(
		visible = maxValue == 12,
		enter = fadeIn(
			animationSpec = tween(500)
		),
		exit = fadeOut(
			animationSpec = tween(500)
		)
	) {
		TimePicker(
			initialValue = if (animate) clockPositionValue else pos,
			circleRadius = 90f,
			maxValue = 12,
			onPositionChange = onPositionChange,
			modifier = modifier
		)
	}
	
	AnimatedVisibility(
		visible = maxValue == 60,
		enter = fadeIn(
			animationSpec = tween(500)
		),
		exit = fadeOut(
			animationSpec = tween(500)
		)
	) {
		TimePicker(
			initialValue = if (animate) clockPositionValue else pos,
			circleRadius = 90f,
			maxValue = 60,
			onPositionChange = onPositionChange,
			modifier = modifier
		)
	}
}
