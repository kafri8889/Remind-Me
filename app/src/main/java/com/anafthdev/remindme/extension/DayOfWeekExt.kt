package com.anafthdev.remindme.extension

import com.anafthdev.remindme.data.DayOfWeek
import java.util.Calendar

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

fun DayOfWeek.next(): DayOfWeek {
	return when (this) {
		DayOfWeek.Sunday -> DayOfWeek.Monday
		DayOfWeek.Monday -> DayOfWeek.Tuesday
		DayOfWeek.Tuesday -> DayOfWeek.Wednesday
		DayOfWeek.Wednesday -> DayOfWeek.Thursday
		DayOfWeek.Thursday -> DayOfWeek.Friday
		DayOfWeek.Friday -> DayOfWeek.Saturday
		DayOfWeek.Saturday -> DayOfWeek.Sunday
	}
}

fun calendarDayOfWeekToRemindMeDayOfWeek(dayOfWeek: Int): DayOfWeek {
	return when (dayOfWeek) {
		Calendar.SUNDAY -> DayOfWeek.Sunday
		Calendar.MONDAY -> DayOfWeek.Monday
		Calendar.TUESDAY -> DayOfWeek.Tuesday
		Calendar.WEDNESDAY -> DayOfWeek.Wednesday
		Calendar.THURSDAY -> DayOfWeek.Thursday
		Calendar.FRIDAY -> DayOfWeek.Friday
		Calendar.SATURDAY -> DayOfWeek.Saturday
		else -> DayOfWeek.Sunday
	}
}

fun calendarDayOfWeekFromRemindMeDayOfWeek(dayOfWeek: DayOfWeek): Int {
	return when (dayOfWeek) {
		DayOfWeek.Sunday -> Calendar.SUNDAY
		DayOfWeek.Monday -> Calendar.MONDAY
		DayOfWeek.Tuesday -> Calendar.TUESDAY
		DayOfWeek.Wednesday -> Calendar.WEDNESDAY
		DayOfWeek.Thursday -> Calendar.THURSDAY
		DayOfWeek.Friday -> Calendar.FRIDAY
		DayOfWeek.Saturday -> Calendar.SATURDAY
	}
}
