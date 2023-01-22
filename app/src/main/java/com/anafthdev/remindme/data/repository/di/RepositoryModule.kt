package com.anafthdev.remindme.data.repository.di

import androidx.datastore.core.DataStore
import com.anafthdev.remindme.UserPreferences
import com.anafthdev.remindme.data.repository.ReminderRepository
import com.anafthdev.remindme.data.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
	
	@Provides
	@Singleton
	fun provideReminderRepository(): ReminderRepository = ReminderRepository()
	
	@Provides
	@Singleton
	fun provideUserPreferencesRepository(
		userPreferencesDataStore: DataStore<UserPreferences>
	): UserPreferencesRepository = UserPreferencesRepository(
		userPreferencesDataStore = userPreferencesDataStore
	)
	
}