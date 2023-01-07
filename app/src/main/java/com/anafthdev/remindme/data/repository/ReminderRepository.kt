package com.anafthdev.remindme.data.repository

import com.anafthdev.remindme.data.local.LocalRemindersDataProvider
import com.anafthdev.remindme.data.model.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReminderRepository @Inject constructor() {
	
	fun getAllReminders(): Flow<List<Reminder>> {
		return flow { emit(LocalRemindersDataProvider.allReminders) }
	}
	
	suspend fun updateReminder(reminder: Reminder) {
	
	}
	
}