package com.anafthdev.remindme.uicomponent

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anafthdev.remindme.common.TransparentIndication
import com.anafthdev.remindme.data.local.LocalRemindersDataProvider
import com.anafthdev.remindme.data.model.Reminder
import com.anafthdev.remindme.extension.convert24HourTo12Hour
import com.anafthdev.remindme.extension.hourMinuteFormat
import com.anafthdev.remindme.extension.toast
import com.anafthdev.remindme.theme.RemindMeTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReminderItem(
	reminder: Reminder,
	is24Hour: Boolean,
	modifier: Modifier = Modifier,
	onClick: () -> Unit,
	onCheckedChange: (Boolean) -> Unit
) {
	
	val context = LocalContext.current
	
	val time = remember(reminder) {
		val (hour, format) = if (is24Hour) hourMinuteFormat(reminder.hour) to ""
		else convert24HourTo12Hour(reminder.hour)
		
		val formattedTime = "$hour:${hourMinuteFormat(reminder.minute)}"
		
		if (format.isEmpty()) formattedTime else "$formattedTime ${format.uppercase()}"
	}

	BoxWithConstraints {
		Card(
			onClick = onClick,
			modifier = modifier
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.padding(horizontal = 8.dp)
			) {
				Row(
					verticalAlignment = Alignment.Bottom
				) {
					Text(
						text = time,
						style = MaterialTheme.typography.titleLarge
					)
					
					Spacer(modifier = Modifier.width(8.dp))
					
					Text(
						text = reminder.name,
						style = MaterialTheme.typography.bodySmall
					)
				}
				
				Spacer(modifier = Modifier.weight(1f))
				
				Switch(
					checked = reminder.isActive,
					onCheckedChange = onCheckedChange
				)
			}
			
			CompositionLocalProvider(
				LocalOverscrollConfiguration provides null,
				LocalIndication provides TransparentIndication,
			) {
				LazyRow(
					modifier = Modifier
						.padding(horizontal = 8.dp)
				) {
					items(reminder.messages) { message ->
						FilterChip(
							selected = true,
							onClick = {
								message.toast(context)
							},
							label = {
								Text(
									text = message,
									maxLines = 1,
									overflow = TextOverflow.Ellipsis
								)
							},
							modifier = Modifier
								.padding(horizontal = 4.dp)
								.widthIn(max = this@BoxWithConstraints.maxWidth / 3)
						)
					}
				}
			}
		}
	}
}

@Preview(widthDp = 400)
@Composable
fun ReminderItem24HourPreview() {
	RemindMeTheme {
		ReminderItem(
			reminder = LocalRemindersDataProvider.allReminders[0],
			is24Hour = true,
			onClick = {},
			onCheckedChange = {}
		)
	}
}

@Preview(widthDp = 400)
@Composable
fun ReminderItem12HourPreview() {
	RemindMeTheme {
		ReminderItem(
			reminder = LocalRemindersDataProvider.allReminders[1],
			is24Hour = false,
			onClick = {},
			onCheckedChange = {}
		)
	}
}
