package com.anafthdev.remindme.data.model

data class Reminder(
	val id: Int,
	val name: String,
	
	/**
	 * In 24H format
	 */
	val hour: Int,
	
	val minute: Int,
	val messages: List<String>,
	val isActive: Boolean
)
