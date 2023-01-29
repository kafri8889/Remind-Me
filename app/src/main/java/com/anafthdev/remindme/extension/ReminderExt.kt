package com.anafthdev.remindme.extension

import com.anafthdev.remindme.data.local.model.ReminderDb
import com.anafthdev.remindme.data.model.Reminder

fun Reminder.toReminderDb(): ReminderDb {
	return ReminderDb(
		id = id,
		name = name,
		hour = hour,
		minute = minute,
		messages = messages,
		repeatOnDays = repeatOnDays,
		randomMessage = randomMessage,
		isActive = isActive
	)
}
