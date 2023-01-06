package com.anafthdev.remindme.data.repository

import com.anafthdev.remindme.data.model.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class ReminderRepository @Inject constructor() {
	
	fun getAllReminders(): Flow<List<Reminder>> {
		return emptyFlow()
	}
	
}