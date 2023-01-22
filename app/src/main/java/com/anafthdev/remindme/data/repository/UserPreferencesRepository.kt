package com.anafthdev.remindme.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.anafthdev.remindme.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
	private val userPreferencesDataStore: DataStore<UserPreferences>
) {
	
	suspend fun setUse24Hour(use: Boolean) {
		userPreferencesDataStore.updateData { currentPreferences ->
			currentPreferences.copy(
				is24Hour = use
			)
		}
	}
	
	suspend fun setAutoSave(auto: Boolean) {
		userPreferencesDataStore.updateData { currentPreferences ->
			currentPreferences.copy(
				autoSave = auto
			)
		}
	}
	
	val getUserPreferences: Flow<UserPreferences> = userPreferencesDataStore.data
	
	suspend fun getInitialPreferences(): UserPreferences = userPreferencesDataStore.data.first()
	
	companion object {
		val corruptionHandler = ReplaceFileCorruptionHandler(
			produceNewData = { UserPreferences(true) }
		)
	}
}