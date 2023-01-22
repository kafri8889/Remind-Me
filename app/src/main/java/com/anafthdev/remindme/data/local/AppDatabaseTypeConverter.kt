package com.anafthdev.remindme.data.local

import androidx.room.TypeConverter
import com.anafthdev.remindme.data.DayOfWeek
import com.google.gson.Gson

object AppDatabaseTypeConverter {
	
	@TypeConverter
	fun dayOfWeekToOrdinal(dayOfWeek: DayOfWeek): Int = dayOfWeek.ordinal
	
	@TypeConverter
	fun dayOfWeekFromOrdinal(ordinal: Int): DayOfWeek = DayOfWeek.values()[ordinal]
	
	@TypeConverter
	fun listStringToJSON(list: List<String>): String = Gson().toJson(list)
	
	
	@TypeConverter
	fun listStringFromJSON(json: String): List<String> = Gson().fromJson(json, Array<String>::class.java).toList()
	
}