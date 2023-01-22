package com.anafthdev.remindme.data.local

import androidx.room.TypeConverter
import com.anafthdev.remindme.data.DayOfWeek
import com.google.gson.Gson

object AppDatabaseTypeConverter {
	
	@TypeConverter
	fun listDayOfWeekToJSON(dayOfWeeks: List<DayOfWeek>): String = Gson().toJson(dayOfWeeks)
	
	@TypeConverter
	fun listDayOfWeekFromJSON(json: String): List<DayOfWeek> = Gson().fromJson(json, Array<DayOfWeek>::class.java).toList()
	
	@TypeConverter
	fun listStringToJSON(list: List<String>): String = Gson().toJson(list)
	
	@TypeConverter
	fun listStringFromJSON(json: String): List<String> = Gson().fromJson(json, Array<String>::class.java).toList()
	
}