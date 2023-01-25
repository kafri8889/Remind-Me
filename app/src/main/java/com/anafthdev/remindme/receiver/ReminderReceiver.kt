package com.anafthdev.remindme.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.anafthdev.remindme.data.AlarmAction
import com.anafthdev.remindme.extension.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderReceiver: BroadcastReceiver() {
	
	override fun onReceive(context: Context, intent: Intent?) {
		if (intent?.action == null) return
		
		when (intent.action) {
			AlarmAction.ACTION_NOTIFY -> {
				// TODO: notify and update nextTrigger
				"Notify".toast(context)
			}
		}
	}
}