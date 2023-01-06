package com.anafthdev.remindme.runtime

import android.app.Application
import com.anafthdev.remindme.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RemindMeApplication: Application() {
	
	override fun onCreate() {
		super.onCreate()
		if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
	}
}