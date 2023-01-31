package com.anafthdev.remindme.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.anafthdev.remindme.common.RemindMeAlarmManager
import com.anafthdev.remindme.common.RemindMeNotificationManager
import com.anafthdev.remindme.data.AlarmAction
import com.anafthdev.remindme.data.DayOfWeek
import com.anafthdev.remindme.data.model.Reminder
import com.anafthdev.remindme.extension.calendarDayOfWeekFromRemindMeDayOfWeek
import com.anafthdev.remindme.extension.calendarDayOfWeekToRemindMeDayOfWeek
import com.anafthdev.remindme.extension.next
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver: BroadcastReceiver() {
	
	@Inject lateinit var remindMeNotificationManager: RemindMeNotificationManager
	@Inject lateinit var remindMeAlarmManager: RemindMeAlarmManager
	
	override fun onReceive(context: Context, intent: Intent?) {
		if (intent?.action == null) return
		
		when (intent.action) {
			AlarmAction.ACTION_NOTIFY -> {
				val bundle = intent.getBundleExtra("bundle") ?: Bundle.EMPTY
				val reminder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
					bundle.getParcelable("reminder", Reminder::class.java)
				} else {
					bundle.getParcelable("reminder") as? Reminder
				} ?: Reminder.Null
				
				val nextTrigger =  Calendar.getInstance().apply {
					when {
						reminder.repeatOnDays.isEmpty() -> {}
						reminder.repeatOnDays.size == DayOfWeek.values().size -> {
							add(Calendar.DAY_OF_MONTH, 1)
						}
						else -> {
							val currentDay = calendarDayOfWeekToRemindMeDayOfWeek(get(Calendar.DAY_OF_WEEK))
							var nextWeek = false
							val nextDay = run {
								var day = currentDay
								
								do {
									day = day.next()
									
									if (day in reminder.repeatOnDays) {
										Timber.i("return day: ${day.name}")
										
										nextWeek = currentDay == day
										
										return@run day
									}
								} while (day !in reminder.repeatOnDays)
								
								return@run currentDay  // Default value
							}
							
							Timber.i("cur day: $currentDay, next day: $nextDay")
							
							if (nextDay.ordinal < currentDay.ordinal || nextWeek) {
								// Jika hari selanjutnya < hari sekarang
								// Berarti next triggernya minggu besok
								add(Calendar.WEEK_OF_MONTH, 1)
								
								Timber.i("trigger next week!")
							}
							
							set(Calendar.DAY_OF_WEEK, calendarDayOfWeekFromRemindMeDayOfWeek(nextDay))
						}
					}
					
					set(Calendar.HOUR_OF_DAY, reminder.hour)
					set(Calendar.MINUTE, reminder.minute)
					set(Calendar.SECOND, 0)
				}.timeInMillis
				
				remindMeAlarmManager.startReminder(reminder, nextTrigger)
				remindMeNotificationManager.notifyReminder(reminder)
			}
		}
	}
}