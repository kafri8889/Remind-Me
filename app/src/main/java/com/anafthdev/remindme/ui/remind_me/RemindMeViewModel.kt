package com.anafthdev.remindme.ui.remind_me

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.remindme.data.model.Reminder
import com.anafthdev.remindme.data.repository.ReminderRepository
import com.anafthdev.remindme.extension.toReminderDb
import com.anafthdev.remindme.utils.RemindMeContentType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemindMeViewModel @Inject constructor(
	private val reminderRepository: ReminderRepository
): ViewModel() {
	
	// UI state exposed to the UI
	private val _uiState = MutableStateFlow(RemindMeUiState())
	val uiState: StateFlow<RemindMeUiState> = _uiState
	
	init {
		observeReminders()
	}
	
	private fun observeReminders() {
		viewModelScope.launch {
			reminderRepository.getAllReminders()
				.catch { ex ->
					_uiState.value = RemindMeUiState(error = ex.message)
				}
				.collect { reminders ->
					_uiState.value = RemindMeUiState(
						reminders = reminders
					)
				}
		}
	}
	
	fun updateReminder(reminder: Reminder) {
		viewModelScope.launch(Dispatchers.IO) {
			reminderRepository.updateReminder(reminder.toReminderDb())
		}
	}
	
	fun setSelectedReminder(reminderID: Int, contentType: RemindMeContentType) {
		/**
		 * We only set isDetailOnlyOpen to true when it's only single pane layout
		 */
		val reminder = uiState.value.reminders.find { it.id == reminderID }
		_uiState.value = _uiState.value.copy(
			selectedReminder = reminder,
			isDetailOnlyOpen = contentType == RemindMeContentType.SINGLE_PANE
		)
	}
	
	fun closeReminderScreen() {
		_uiState.value = _uiState
			.value.copy(
				isDetailOnlyOpen = false,
				selectedReminder = null
			)
	}

}