package com.anafthdev.remindme.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class ReminderDb(
	@PrimaryKey @ColumnInfo(name = "reminder_id") val id: Int
)
