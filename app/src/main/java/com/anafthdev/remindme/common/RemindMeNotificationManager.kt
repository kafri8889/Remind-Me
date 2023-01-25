package com.anafthdev.remindme.common

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.anafthdev.remindme.R
import com.anafthdev.remindme.data.model.Reminder
import javax.inject.Inject

class RemindMeNotificationManager @Inject constructor(
	private val context: Context
) {
	
	private val REMINDER_CHANNEL_ID = "reminder_id"
	private val REMINDER_CHANNEL_NAME = "Reminder"
	
	private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
	
	private val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
		Notification.Builder(context, REMINDER_CHANNEL_ID)
	} else Notification.Builder(context)
	
	@RequiresApi(Build.VERSION_CODES.O)
	fun createChannel() {
		val channel = NotificationChannel(REMINDER_CHANNEL_ID, REMINDER_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
		
		channel.apply {
			enableVibration(true)
			vibrationPattern = longArrayOf(0, 250, 250, 250)  // default
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				setAllowBubbles(true)
			}
		}
		
		notificationManager.createNotificationChannel(channel)
	}
	
	fun notifyReminder(reminder: Reminder) {
		notificationManager.notify(
			reminder.id,
			builder.setAutoCancel(true)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(reminder.name)
				.setContentText(reminder.messages.randomOrNull() ?: "")
				.setCategory(Notification.CATEGORY_REMINDER)
				.setAutoCancel(true)
				.build()
		)
	}
	
}