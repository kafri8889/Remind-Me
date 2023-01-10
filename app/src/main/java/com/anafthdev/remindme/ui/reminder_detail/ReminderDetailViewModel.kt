package com.anafthdev.remindme.ui.reminder_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.anafthdev.remindme.data.TimeType
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
 
	
	fun updateClockPosition(pos: Int) {
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
	}
	
}