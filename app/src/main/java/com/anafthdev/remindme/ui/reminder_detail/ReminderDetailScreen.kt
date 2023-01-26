package com.anafthdev.remindme.ui.reminder_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anafthdev.remindme.R
import com.anafthdev.remindme.data.HourClockType
import com.anafthdev.remindme.data.TimeType
import com.anafthdev.remindme.uicomponent.AnimatedScaleText
import com.anafthdev.remindme.uicomponent.DayOfWeekSelector
import com.anafthdev.remindme.uicomponent.HourClockSelector
import com.anafthdev.remindme.uicomponent.Padding
import com.anafthdev.remindme.uicomponent.TimePicker
import com.anafthdev.remindme.uicomponent.TimeSelector

@OptIn(ExperimentalMaterial3Api::class,
	ExperimentalAnimationApi::class
)
@Composable
fun ReminderDetailScreen(
	viewModel: ReminderDetailViewModel
) {
	
	val focusManager = LocalFocusManager.current

	Column(
		modifier = Modifier
			.fillMaxSize()
			.pointerInput(Unit) {
				detectTapGestures {
					focusManager.clearFocus()
				}
			}
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
		) {
			TimePicker(
				pos = viewModel.clockPositionValue,
				timeType = viewModel.selectedTimeType,
				hourClockType = viewModel.hourClockType,
				animate = viewModel.animateClockPositionValue,
				onPositionChange = viewModel::updateClockPosition,
				onAnimationFinished = {
					viewModel.updateAnimateClockPositionValue(false)
				},
				modifier = Modifier
					.fillMaxWidth(0.7f)
			)
			
			Column(
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				TimeSelector(
					hours = viewModel.hours,
					minutes = viewModel.minutes,
					selectedTimeType = viewModel.selectedTimeType,
					onTimeTypeChanged = viewModel::updateTimeType
				)
				
				HourClockSelector(
					type = viewModel.hourClockType,
					onValueChanged = viewModel::updateHourClockType
				)
			}
		}
		
		Spacer(modifier = Modifier.height(16.dp))
		
		Padding(
			horizontal = 8.dp
		) {
			Text(
				text = stringResource(id = R.string.repeat),
				style = MaterialTheme.typography.titleMedium
			)
			
			Spacer(modifier = Modifier.height(8.dp))
			
			DayOfWeekSelector(
				selectedDays = viewModel.repeatOnDays,
				onSelectedDaysChanged = viewModel::updateRepeatOnDays,
				modifier = Modifier
					.fillMaxWidth()
			)
			
			
			
			Spacer(modifier = Modifier.height(16.dp))
			
			
			
			Text(
				text = stringResource(id = R.string.reminder_name),
				style = MaterialTheme.typography.titleMedium
			)
			
			OutlinedTextField(
				value = viewModel.reminderName,
				onValueChange = viewModel::updateReminderName,
				singleLine = true,
				colors = TextFieldDefaults.outlinedTextFieldColors(
					unfocusedBorderColor = Color.Transparent,
					focusedBorderColor = Color.Transparent
				),
				placeholder = {
					Text("Name")
				},
				trailingIcon = {
					AnimatedVisibility(
						visible = viewModel.reminderName.isNotBlank(),
						enter = scaleIn(
							animationSpec = tween(250)
						),
						exit = scaleOut(
							animationSpec = tween(250)
						)
					) {
						IconButton(
							onClick = {
								viewModel.updateReminderName("")
							}
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_close_circle),
								contentDescription = null
							)
						}
					}
				},
				modifier = Modifier
					.fillMaxWidth()
			)
			
			
			
			Spacer(modifier = Modifier.height(16.dp))
			
			
			
			Text(
				text = stringResource(id = R.string.message),
				style = MaterialTheme.typography.titleMedium
			)
		}
	}
}

@Composable
private fun TimePicker(
	pos: Int,
	timeType: TimeType,
	hourClockType: HourClockType,
	modifier: Modifier = Modifier,
	animate: Boolean = true,
	onPositionChange: (Int) -> Unit,
	onAnimationFinished: (Int) -> Unit
) {
	
	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier
	) {
		ShowedClockTime(
			timeType = timeType,
			hourClockType = hourClockType
		) {
			val clockPositionValue by animateIntAsState(
				targetValue = pos,
				animationSpec = tween(600),
				finishedListener = onAnimationFinished
			)

			AnimatedVisibility(
				visible = timeType == TimeType.Hours,
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
					modifier = Modifier
						.fillMaxWidth(0.9f)
						.aspectRatio(1f / 1f)
						.rotate(180f)
				)
			}

			AnimatedVisibility(
				visible = timeType == TimeType.Minutes,
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
					modifier = Modifier
						.fillMaxWidth(0.9f)
						.aspectRatio(1f / 1f)
						.rotate(180f)
				)
			}
		}
	}
}

@Composable
fun ShowedClockTime(
	timeType: TimeType,
	hourClockType: HourClockType,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
	) {
		AnimatedScaleText(
			text = stringResource(
				id = if (timeType == TimeType.Hours) {
					if (hourClockType == HourClockType.AM) R.string.num_zero else R.string.num_twelve
				} else R.string.num_zero
			)
		) {
			Text(
				text = it,
				style = MaterialTheme.typography.titleMedium.copy(
					fontWeight = FontWeight.SemiBold
				)
			)
		}
		
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			AnimatedScaleText(
				text = stringResource(
					id = if (timeType == TimeType.Hours) {
						if (hourClockType == HourClockType.AM) R.string.num_nine else R.string.num_twenty_one
					} else R.string.num_fourty_five
				)
			) {
				Text(
					text = it,
					style = MaterialTheme.typography.titleMedium.copy(
						fontWeight = FontWeight.SemiBold
					)
				)
			}
			
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.fillMaxWidth(0.9f)
					.aspectRatio(1f / 1f)
			) {
				content()
			}
			
			AnimatedScaleText(
				text = stringResource(
					id = if (timeType == TimeType.Hours) {
						if (hourClockType == HourClockType.AM) R.string.num_three else R.string.num_five_teen
					} else R.string.num_five_teen
				)
			) {
				Text(
					text = it,
					style = MaterialTheme.typography.titleMedium.copy(
						fontWeight = FontWeight.SemiBold
					)
				)
			}
		}
		
		AnimatedScaleText(
			text = stringResource(
				id = if (timeType == TimeType.Hours) {
					if (hourClockType == HourClockType.AM) R.string.num_six else R.string.num_eight_teen
				} else R.string.num_thirty
			)
		) {
			Text(
				text = it,
				style = MaterialTheme.typography.titleMedium.copy(
					fontWeight = FontWeight.SemiBold
				)
			)
		}
	}
}
