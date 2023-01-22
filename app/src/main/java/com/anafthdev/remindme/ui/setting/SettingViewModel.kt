package com.anafthdev.remindme.ui.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.remindme.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
	private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
	
	var is24Hour by mutableStateOf(false)
		private set
	
	var autoSave by mutableStateOf(false)
		private set
	
	init {
		viewModelScope.launch {
			userPreferencesRepository.getUserPreferences.collect { preferences ->
				is24Hour = preferences.is24Hour
				autoSave = preferences.autoSave
			}
		}
	}

	fun setUse24Hour(use: Boolean) {
		viewModelScope.launch(Dispatchers.IO) {
			userPreferencesRepository.setUse24Hour(use)
		}
	}
	
	fun setUseAutoSave(use: Boolean) {
		viewModelScope.launch(Dispatchers.IO) {
			userPreferencesRepository.setAutoSave(use)
		}
	}

}