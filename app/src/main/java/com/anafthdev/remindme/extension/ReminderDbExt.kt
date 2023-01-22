package com.anafthdev.remindme.extension

import com.anafthdev.remindme.data.local.model.ReminderDb
import com.anafthdev.remindme.data.model.Reminder

fun ReminderDb.toReminder(): Reminder {
	return Reminder(
		id = id,
		name = name,
		hour = hour,
		minute = minute,
		messages = messages,
		repeatOnDays = repeatOnDays,
		isActive = isActive
	)
}
