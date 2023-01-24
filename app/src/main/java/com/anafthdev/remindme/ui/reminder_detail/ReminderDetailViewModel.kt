package com.anafthdev.remindme.ui.reminder_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.remindme.data.DayOfWeek
import com.anafthdev.remindme.data.HourClockType
import com.anafthdev.remindme.data.ReminderMessageType
import com.anafthdev.remindme.data.TimeType
import com.anafthdev.remindme.data.model.Reminder
import com.anafthdev.remindme.data.repository.ReminderRepository
import com.anafthdev.remindme.data.repository.UserPreferencesRepository
import com.anafthdev.remindme.extension.convert12HourTo24Hour
import com.anafthdev.remindme.extension.convert24HourTo12Hour
import com.anafthdev.remindme.extension.toReminderDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ReminderDetailViewModel @Inject constructor(
	private val userPreferencesRepository: UserPreferencesRepository,
	private val reminderRepository: ReminderRepository
): ViewModel() {
	
	var reminderName by mutableStateOf("")
		private set
	
	var hours by mutableStateOf(0)
		private set
	
	var minutes by mutableStateOf(0)
		private set
	
	var clockPositionValue by mutableStateOf(0)
		private set
	
	var animateClockPositionValue by mutableStateOf(false)
	    private set
	
	var isReminderActive by mutableStateOf(false)
		private set
	
	var is24Hour by mutableStateOf(false)
		private set
	
	var autoSave by mutableStateOf(false)
		private set
	
	var selectedTimeType by mutableStateOf(TimeType.Hours)
		private set
	
	var hourClockType by mutableStateOf(HourClockType.AM)
		private set
	
	var currentReminder by mutableStateOf(Reminder.Null)
		private set
	
	var messages = mutableStateListOf<Pair<String, ReminderMessageType>>()
		private set
	
	var repeatOnDays = mutableStateListOf<DayOfWeek>()
		private set
	
	private val _currentReminderId = MutableStateFlow(Reminder.Null.id)
	private val currentReminderId: StateFlow<Int> = _currentReminderId
	
	init {
		viewModelScope.launch {
			userPreferencesRepository.getUserPreferences.collect { preferences ->
				is24Hour = preferences.is24Hour
				autoSave = preferences.autoSave
			}
		}
		
		viewModelScope.launch {
			currentReminderId.collect { id ->
				reminderRepository.getReminderById(id).collect { collectedReminder ->
					Timber.i("remingder: $collectedReminder")
					updateWithReminder(collectedReminder)
				}
			}
//			currentReminderId.flatMapConcat {
//				reminderRepository.getReminderById(it)
//			}.collect { collectedReminder ->
//				Timber.i("remingder: $collectedReminder")
//				updateWithReminder(collectedReminder)
//			}
		}
	}
	
	fun updateWithReminder(reminder: Reminder) {
		updateReminderName(reminder.name, false)
		updateIsReminderActive(reminder.isActive, false)
		
		hours = reminder.hour
		minutes = reminder.minute
		currentReminder = reminder
		clockPositionValue = if (selectedTimeType == TimeType.Hours) hours + 1 else minutes + 1
		repeatOnDays.apply {
			clear()
			addAll(reminder.repeatOnDays)
		}
		messages.apply {
			clear()
			addAll(reminder.messages.map { it to ReminderMessageType.Fixed })
		}
		
		viewModelScope.launch {
			_currentReminderId.emit(reminder.id)
		}
	}
	
	fun updateClockPosition(pos: Int, save: Boolean = true) {
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
		
		if (autoSave && save) saveReminder()
	}
	
	fun updateTimeType(type: TimeType) {
		selectedTimeType = type
		
		clockPositionValue = when (type) {
			TimeType.Hours -> convert24HourTo12Hour(hours).first.toInt() + 1
			TimeType.Minutes -> minutes + 1
		}
		
		animateClockPositionValue = true
	}
	
	fun updateHourClockType(type: HourClockType, save: Boolean = true) {
		hourClockType = type
		
		hours = when (type) {
			HourClockType.AM -> convert12HourTo24Hour(hours, "am")
			HourClockType.PM -> convert12HourTo24Hour(hours, "pm")
		}
		
		clockPositionValue = convert24HourTo12Hour(hours).first.toInt()
		
		if (autoSave && save) saveReminder()
	}
	
	fun updateAnimateClockPositionValue(animate: Boolean) {
		animateClockPositionValue = animate
	}
	
	fun updateRepeatOnDays(days: List<DayOfWeek>, save: Boolean = true) {
		repeatOnDays.apply {
			clear()
			addAll(days)
		}
		
		if (autoSave && save) saveReminder()
	}
	
	fun updateReminderName(name: String, save: Boolean = true) {
		reminderName = name
		
		if (autoSave && save) saveReminder()
	}
	
	fun updateIsReminderActive(active: Boolean, save: Boolean = true) {
		isReminderActive = active
		
		if (autoSave && save) saveReminder()
	}
	
	/**
	 * @return true if saved, false otherwise
	 */
	fun saveReminder(): Result<Boolean> {
		if (reminderName.isBlank()) return Result.failure(
			Throwable("Reminder name cannot be empty!")
		)
		
		viewModelScope.launch(Dispatchers.IO) {
			reminderRepository.updateReminder(
				currentReminder.copy(
					name = reminderName,
					hour = hours,
					minute = minutes,
					messages = messages.map { it.first },
					repeatOnDays = repeatOnDays,
					isActive = isReminderActive
				).toReminderDb()
			)
		}
		
		return Result.success(true)
	}
	
}