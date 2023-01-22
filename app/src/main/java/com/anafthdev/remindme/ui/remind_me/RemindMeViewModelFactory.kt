package com.anafthdev.remindme.ui.remind_me

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anafthdev.remindme.data.repository.ReminderRepository

class RemindMeViewModelFactory(
	private val reminderRepository: ReminderRepository
): ViewModelProvider.Factory {
	
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (RemindMeViewModel::class.java.isAssignableFrom(modelClass)) {
			return RemindMeViewModel(reminderRepository) as T
		}
		
		return modelClass
			.getConstructor(ReminderRepository::class.java)
			.newInstance(reminderRepository)
	}
}