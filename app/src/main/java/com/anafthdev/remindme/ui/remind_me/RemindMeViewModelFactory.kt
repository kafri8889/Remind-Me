package com.anafthdev.remindme.ui.remind_me

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anafthdev.remindme.data.repository.ReminderRepository
import com.anafthdev.remindme.data.repository.UserPreferencesRepository

class RemindMeViewModelFactory(
	private val reminderRepository: ReminderRepository,
	private val userPreferencesRepository: UserPreferencesRepository
): ViewModelProvider.Factory {
	
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (RemindMeViewModel::class.java.isAssignableFrom(modelClass)) {
			return RemindMeViewModel(reminderRepository, userPreferencesRepository) as T
		}
		
		return modelClass
			.getConstructor(ReminderRepository::class.java, UserPreferencesRepository::class.java)
			.newInstance(reminderRepository, userPreferencesRepository)
	}
}