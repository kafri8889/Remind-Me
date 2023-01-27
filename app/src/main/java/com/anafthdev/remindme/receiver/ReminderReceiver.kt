package com.anafthdev.remindme.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.anafthdev.remindme.common.RemindMeAlarmManager
import com.anafthdev.remindme.common.RemindMeNotificationManager
import com.anafthdev.remindme.data.AlarmAction
import com.anafthdev.remindme.data.model.Reminder
import dagger.hilt.android.AndroidEntryPoint
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
					add(Calendar.DAY_OF_MONTH, 1)
					
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