package com.anafthdev.remindme.data.model

import android.os.Parcelable
import com.anafthdev.remindme.data.DayOfWeek
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Reminder(
	val id: Int,
	val name: String,
	
	/**
	 * In 24H format
	 */
	val hour: Int,
	
	val minute: Int,
	val messages: @RawValue List<String>,
	val repeatOnDays: @RawValue List<DayOfWeek>,
	val randomMessage: Boolean,
	val isActive: Boolean
): Parcelable {
	companion object {
		val Null = Reminder(
			id = -1,
			name = "",
			hour = 0,
			minute = 0,
			messages = emptyList(),
			repeatOnDays = emptyList(),
			randomMessage = true,
			isActive = false
		)
	}
}
