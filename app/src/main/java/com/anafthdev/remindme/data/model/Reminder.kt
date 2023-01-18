package com.anafthdev.remindme.data.model

import com.anafthdev.remindme.data.DayOfWeek

data class Reminder(
	val id: Int,
	val name: String,
	
	/**
	 * In 24H format
	 */
	val hour: Int,
	
	val minute: Int,
	val messages: List<String>,
	val repeatOnDays: List<DayOfWeek>,
	val isActive: Boolean
) {
	companion object {
		val Null = Reminder(
			id = -1,
			name = "",
			hour = 0,
			minute = 0,
			messages = emptyList(),
			repeatOnDays = emptyList(),
			isActive = false
		)
	}
}
