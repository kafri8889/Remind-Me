package com.anafthdev.remindme.ui.remind_me

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.remindme.common.RemindMeAlarmManager
import com.anafthdev.remindme.data.model.Reminder
import com.anafthdev.remindme.data.repository.ReminderRepository
import com.anafthdev.remindme.data.repository.UserPreferencesRepository
import com.anafthdev.remindme.extension.toReminderDb
import com.anafthdev.remindme.utils.RemindMeContentType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RemindMeViewModel @Inject constructor(
	private val reminderRepository: ReminderRepository,
	private val remindMeAlarmManager: RemindMeAlarmManager,
	private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
	
	// UI state exposed to the UI
	private val _uiState = MutableStateFlow(RemindMeUiState())
	val uiState: StateFlow<RemindMeUiState> = _uiState
	
	init {
		observeReminders()
		observeUserPreference()
	}
	
	private fun observeReminders() {
		viewModelScope.launch {
			reminderRepository.getAllReminders()
				.catch { ex ->
					_uiState.value = _uiState.value.copy(error = ex.message)
				}
				.collect { reminders ->
					val currentReminder = reminders.find {
						it.id == (uiState.value.selectedReminder?.id ?: Reminder.Null.id)
					} ?: _uiState.value.selectedReminder
					
					_uiState.value = _uiState.value.copy(
						reminders = reminders,
						selectedReminder = currentReminder
					)
				}
		}
	}
	
	private fun observeUserPreference() {
		viewModelScope.launch {
			userPreferencesRepository.getUserPreferences
				.catch { ex ->
					_uiState.value = _uiState.value.copy(error = ex.message)
				}
				.collect { preferences ->
					_uiState.value = _uiState.value.copy(
						userPreferences = preferences
					)
				}
		}
	}
	
	fun updateReminder(reminder: Reminder) {
		if (reminder.isActive) remindMeAlarmManager.validateAndStart(reminder)
		else remindMeAlarmManager.cancelReminder(reminder)
		
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
	
	fun onDeleteReminder(showConfirmationDialog: Boolean, deleteCurrentReminder: Boolean) {
		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(
				showDeleteConfirmationDialog = showConfirmationDialog
			)
			
			if (deleteCurrentReminder) {
				_uiState.value.selectedReminder?.let {
					_uiState.value = _uiState.value.copy(
						selectedReminder = null
					)
					
					withContext(Dispatchers.IO) {
						reminderRepository.deleteReminder(it.toReminderDb())
					}
				}
			}
		}
	}

}