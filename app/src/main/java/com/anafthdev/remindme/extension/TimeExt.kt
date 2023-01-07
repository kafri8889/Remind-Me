package com.anafthdev.remindme.extension

/**
 * @param hourOrMinute 0 - 23
 */
fun hourMinuteFormat(hourOrMinute: Int): String {
	return if (hourOrMinute < 10) "0$hourOrMinute" else hourOrMinute.toString()
}

/**
 * @param hour 0 - 23
 * @return [[hour, hour format]] | [[06, "pm"]]
 */
fun convert24HourTo12Hour(hour: Int): Pair<String, String> {
	return hourMinuteFormat(if (hour > 12) hour - 12 else hour) to (if (hour > 12) "pm" else "am")
}
