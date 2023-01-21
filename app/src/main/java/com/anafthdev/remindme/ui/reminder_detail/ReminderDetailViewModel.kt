package com.anafthdev.remindme.ui.reminder_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.anafthdev.remindme.data.DayOfWeek
import com.anafthdev.remindme.data.HourClockType
import com.anafthdev.remindme.data.ReminderMessageType
import com.anafthdev.remindme.data.TimeType
import com.anafthdev.remindme.data.model.Reminder
import com.anafthdev.remindme.extension.convert12HourTo24Hour
import com.anafthdev.remindme.extension.convert24HourTo12Hour
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReminderDetailViewModel @Inject constructor(

): ViewModel() {
	
	var hours by mutableStateOf(0)
		private set
	
	var minutes by mutableStateOf(0)
		private set
	
	var selectedTimeType by mutableStateOf(TimeType.Hours)
		private set
	
	var clockPositionValue by mutableStateOf(0)
	    private set
	
	var animateClockPositionValue by mutableStateOf(false)
	    private set
	
	var hourClockType by mutableStateOf(HourClockType.AM)
		private set
	
	var reminderName by mutableStateOf("")
		private set
	
	var messages = mutableStateListOf<Pair<String, ReminderMessageType>>()
		private set
	
	var repeatOnDays = mutableStateListOf<DayOfWeek>()
		private set
	
	fun updateWithReminder(reminder: Reminder) {
		updateReminderName(reminder.name)
		
		messages.apply {
			clear()
			addAll(reminder.messages.map { it to ReminderMessageType.Fixed })
		}
	}
	
	fun updateClockPosition(pos: Int) {
		clockPositionValue = pos
		
		when (selectedTimeType) {
			TimeType.Hours -> {
				hours = when (hourClockType) {
					HourClockType.AM -> convert12HourTo24Hour((pos - 1).coerceAtLeast(0), "am")
					HourClockType.PM -> convert12HourTo24Hour((pos - 1).coerceAtLeast(0), "pm")
				}
			}
			TimeType.Minutes -> {
				minutes = (pos - 1).coerceAtLeast(0)
			}
		}
	}
	
	fun updateTimeType(type: TimeType) {
		selectedTimeType = type
		
		clockPositionValue = when (type) {
			TimeType.Hours -> convert24HourTo12Hour(hours).first.toInt() + 1
			TimeType.Minutes -> minutes + 1
		}
		
		animateClockPositionValue = true
	}
	
	fun updateHourClockType(type: HourClockType) {
		hourClockType = type
		
		hours = when (type) {
			HourClockType.AM -> convert12HourTo24Hour(hours, "am")
			HourClockType.PM -> convert12HourTo24Hour(hours, "pm")
		}
		
		clockPositionValue = convert24HourTo12Hour(hours).first.toInt()
	}
	
	fun updateAnimateClockPositionValue(animate: Boolean) {
		animateClockPositionValue = animate
	}
	
	fun updateRepeatOnDays(days: List<DayOfWeek>) {
		repeatOnDays.apply {
			clear()
			addAll(days)
		}
	}
	
	fun updateReminderName(name: String) {
		reminderName = name
	}
	
}