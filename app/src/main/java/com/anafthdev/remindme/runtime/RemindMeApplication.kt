package com.anafthdev.remindme.runtime

import android.app.Application
import android.os.Build
import com.anafthdev.remindme.BuildConfig
import com.anafthdev.remindme.common.RemindMeNotificationManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class RemindMeApplication: Application() {
	
	@Inject lateinit var remindMeNotificationManager: RemindMeNotificationManager
	override fun onCreate() {
		super.onCreate()
		if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			remindMeNotificationManager.createChannel()
		}
	}
}