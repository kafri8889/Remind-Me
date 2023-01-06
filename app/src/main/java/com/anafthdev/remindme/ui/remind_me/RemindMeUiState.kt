package com.anafthdev.remindme.ui.remind_me

import com.anafthdev.remindme.data.model.Reminder

data class RemindMeUiState(
	val reminders: List<Reminder> = emptyList(),
	val selectedReminder: Reminder? = null,
	val isDetailOnlyOpen: Boolean = false,
	val error: String? = null
)
