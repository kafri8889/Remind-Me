package com.anafthdev.remindme.data.local

import com.anafthdev.remindme.data.model.Reminder

object LocalRemindersDataProvider {
	
	val allReminders = listOf(
		Reminder(
			id = 0,
			name = "Ngodinh",
			isActive = true
		),
		Reminder(
			id = 0,
			name = "Bangun",
			isActive = false
		),
	)
	
}