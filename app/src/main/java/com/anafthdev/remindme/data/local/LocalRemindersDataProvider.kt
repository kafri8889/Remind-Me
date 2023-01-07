package com.anafthdev.remindme.data.local

import com.anafthdev.remindme.data.model.Reminder

object LocalRemindersDataProvider {
	
	val allReminders = listOf(
		Reminder(
			id = 0,
			name = "Ngodinh",
			hour = 18,
			minute = 23,
			messages = listOf("Message 1", "Looooooooonnnnggggggggg Messageeeee", "Message 2"),
			isActive = true
		),
		Reminder(
			id = 1,
			name = "Bangun",
			hour = 18,
			minute = 23,
			messages = listOf("Message 1", "Looooooooonnnnggggggggg Messageeeee", "Message 2"),
			isActive = false
		),
	)
	
}