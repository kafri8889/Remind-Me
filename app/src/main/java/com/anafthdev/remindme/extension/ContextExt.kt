package com.anafthdev.remindme.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings


fun Context.openSettings() {
	startActivity(
		Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
			data = Uri.fromParts("package", packageName, null)
		}
	)
}
