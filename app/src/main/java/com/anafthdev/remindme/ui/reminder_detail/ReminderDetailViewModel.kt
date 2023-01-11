package com.anafthdev.remindme.ui.reminder_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.anafthdev.remindme.data.HourClockType
import com.anafthdev.remindme.data.TimeType
import com.anafthdev.remindme.extension.convert12HourTo24Hour
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
	
	var animateClockPositionValue by mutableStateOf(true)
	    private set
	
	var hourClockType by mutableStateOf(HourClockType.AM)
		private set
	
	fun updateClockPosition(pos: Int) {
		animateClockPositionValue = false
		clockPositionValue = pos
		
		when (selectedTimeType) {
			TimeType.Hours -> {
				hours = pos
			}
			TimeType.Minutes -> {
				minutes = pos
			}
		}
	}
	
	fun updateTimeType(type: TimeType) {
		selectedTimeType = type
		
		clockPositionValue = when (type) {
			TimeType.Hours -> hours
			TimeType.Minutes -> minutes
		}
		
		animateClockPositionValue = true
	}
	
	fun updateHourClockType(type: HourClockType) {
		hourClockType = type
		
		hours = when (type) {
			HourClockType.AM -> convert12HourTo24Hour(hours, "am")
			HourClockType.PM -> convert12HourTo24Hour(hours, "pm")
		}
	}
	
}