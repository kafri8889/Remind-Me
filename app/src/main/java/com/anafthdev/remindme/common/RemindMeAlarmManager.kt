package com.anafthdev.remindme.common

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.anafthdev.remindme.data.AlarmAction
import com.anafthdev.remindme.data.DayOfWeek
import com.anafthdev.remindme.data.model.Reminder
import com.anafthdev.remindme.extension.calendarDayOfWeekFromRemindMeDayOfWeek
import com.anafthdev.remindme.extension.calendarDayOfWeekToRemindMeDayOfWeek
import com.anafthdev.remindme.extension.next
import com.anafthdev.remindme.receiver.ReminderReceiver
import java.util.Calendar
import javax.inject.Inject

class RemindMeAlarmManager @Inject constructor(
	private val context: Context
) {

	private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
	
	private fun getPendingIntent(reminder: Reminder): PendingIntent? {
		return PendingIntent.getBroadcast(
			context,
			reminder.id,
			Intent(
				context,
				ReminderReceiver::class.java
			).apply {
				action = AlarmAction.ACTION_NOTIFY
				putExtra(
					"bundle",
					Bundle().apply {
						setExtrasClassLoader(Reminder::class.java.classLoader)
						putParcelable("reminder", reminder)
					}
				)
			},
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
			} else PendingIntent.FLAG_CANCEL_CURRENT
		)
	}
	
	private fun isPendingIntentExists(reminder: Reminder): PendingIntent? {
		return PendingIntent.getBroadcast(
			context,
			reminder.id,
			Intent(
				context,
				ReminderReceiver::class.java
			).apply {
				action = AlarmAction.ACTION_NOTIFY
				putExtra(
					"bundle",
					Bundle().apply {
						setExtrasClassLoader(Reminder::class.java.classLoader)
						putParcelable("reminder", reminder)
					}
				)
			},
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
			} else PendingIntent.FLAG_NO_CREATE
		)
	}
	
	fun startReminder(reminder: Reminder, triggerAt: Long) {
		alarmManager.setExactAndAllowWhileIdle(
			AlarmManager.RTC_WAKEUP,
			triggerAt,
			getPendingIntent(reminder)
		)
	}
	
	fun validateAndStart(reminder: Reminder) {
		val nextTrigger =  Calendar.getInstance().apply {
			set(Calendar.HOUR_OF_DAY, reminder.hour)
			set(Calendar.MINUTE, reminder.minute)
			set(Calendar.SECOND, 0)
			
			when {
				System.currentTimeMillis() < timeInMillis -> {}
				reminder.repeatOnDays.size == DayOfWeek.values().size -> {
					add(Calendar.DAY_OF_MONTH, 1)
				}
				else -> {
					val currentDay = calendarDayOfWeekToRemindMeDayOfWeek(get(Calendar.DAY_OF_WEEK))
					val nextDay = run {
						var day = currentDay
						
						do {
							day = day.next()
							
							if (day in reminder.repeatOnDays) {
								return@run day
							}
						} while (day !in reminder.repeatOnDays)
						
						return@run currentDay
					}
					
					if (nextDay.ordinal < currentDay.ordinal) {
						add(Calendar.WEEK_OF_MONTH, 1)
					}
					
					set(Calendar.DAY_OF_WEEK, calendarDayOfWeekFromRemindMeDayOfWeek(nextDay))
				}
			}
		}.timeInMillis
		
		startReminder(reminder, nextTrigger)
	}
	
	fun cancelReminder(reminder: Reminder) {
		alarmManager.cancel(getPendingIntent(reminder))
	}
}