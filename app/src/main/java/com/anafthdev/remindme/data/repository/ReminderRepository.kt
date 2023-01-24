package com.anafthdev.remindme.data.repository

import com.anafthdev.remindme.data.local.dao.ReminderDao
import com.anafthdev.remindme.data.local.model.ReminderDb
import com.anafthdev.remindme.data.model.Reminder
import com.anafthdev.remindme.extension.toReminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReminderRepository @Inject constructor(
	private val reminderDao: ReminderDao
) {
	
	fun getAllReminders(): Flow<List<Reminder>> {
		return reminderDao.getAllReminders()
			.map { list ->
				list.map { it.toReminder() }
			}
	}
	
	fun getReminderById(id: Int): Flow<Reminder> {
		return reminderDao.getReminderById(id)
			.filterNotNull()
			.map { it.toReminder() }
	}
	
	suspend fun updateReminder(vararg reminder: ReminderDb) {
		reminderDao.updateReminder(*reminder)
	}
	
	suspend fun deleteReminder(vararg reminder: ReminderDb) {
		reminderDao.deleteReminder(*reminder)
	}
	
	suspend fun insertReminder(vararg reminder: ReminderDb) {
		reminderDao.insertReminder(*reminder)
	}
	
}