package com.anafthdev.remindme.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.remindme.data.DayOfWeek

@Entity(tableName = "reminder")
data class ReminderDb(
	@PrimaryKey @ColumnInfo(name = "reminder_id") val id: Int,
	@ColumnInfo(name = "reminder_name") val name: String,
	@ColumnInfo(name = "reminder_hour") val hour: Int,
	@ColumnInfo(name = "reminder_minute") val minute: Int,
	@ColumnInfo(name = "reminder_messages") val messages: List<String>,
	@ColumnInfo(name = "reminder_repeatOnDays") val repeatOnDays: List<DayOfWeek>,
	@ColumnInfo(name = "reminder_isActive") val isActive: Boolean
)
