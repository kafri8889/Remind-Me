package com.anafthdev.remindme.extension

import com.anafthdev.remindme.data.DayOfWeek

val DayOfWeek.shortName: String
	get() = when (this) {
		DayOfWeek.Sunday -> "Sun"
		DayOfWeek.Monday -> "Mon"
		DayOfWeek.Tuesday -> "Tue"
		DayOfWeek.Wednesday -> "Wed"
		DayOfWeek.Thursday -> "Thu"
		DayOfWeek.Friday -> "Fri"
		DayOfWeek.Saturday -> "Sat"
	}
