package com.anafthdev.remindme.runtime

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.anafthdev.remindme.common.*
import com.anafthdev.remindme.data.local.LocalRemindersDataProvider
import com.anafthdev.remindme.data.repository.ReminderRepository
import com.anafthdev.remindme.data.repository.UserPreferencesRepository
import com.anafthdev.remindme.theme.RemindMeTheme
import com.anafthdev.remindme.ui.remind_me.RemindMeApp
import com.anafthdev.remindme.ui.remind_me.RemindMeUiState
import com.anafthdev.remindme.ui.remind_me.RemindMeViewModel
import com.anafthdev.remindme.ui.remind_me.RemindMeViewModelFactory
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : FragmentActivity() {
	
	@Inject lateinit var reminderRepository: ReminderRepository
	@Inject lateinit var userPreferencesRepository: UserPreferencesRepository
	
	private lateinit var pickerManager: PickerManager
	
	private val remindMeViewModel: RemindMeViewModel by viewModels(
		factoryProducer = { RemindMeViewModelFactory(reminderRepository, userPreferencesRepository) }
	)
	
	private val _pickerData = MutableStateFlow(PickerData())
	private val pickerData: StateFlow<PickerData> = _pickerData
	
	private val pickerListener = object : PickerListener {
		override fun onShowRequest() {
			lifecycleScope.launch {
				_pickerData.emit(
					pickerData.value.copy(
						dismiss = false
					)
				)
			}
		}
		
		override fun onDismissRequest() {
			lifecycleScope.launch {
				_pickerData.emit(
					pickerData.value.copy(
						dismiss = true
					)
				)
			}
		}
		
		override fun onDateSelected(data: Any?, timeInMillis: Long) {
			lifecycleScope.launch {
				_pickerData.emit(
					pickerData.value.copy(
						data = data,
						timeInMillis = timeInMillis
					)
				)
			}
		}
		
		override fun onDateRangeSelected(data: Any?, from: Long, to: Long) {
			lifecycleScope.launch {
				_pickerData.emit(
					pickerData.value.copy(
						data = data,
						from = from,
                        to = to
					)
				)
			}
		}
		
		override fun onTimeSelected(data: Any?, hour: Int, minute: Int) {
			lifecycleScope.launch {
				_pickerData.emit(
					pickerData.value.copy(
						data = data,
						hour = hour,
                        minute = minute
					)
				)
			}
		}
	}
	
	@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalLifecycleComposeApi::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		WindowCompat.setDecorFitsSystemWindows(window, false)
		
		pickerManager = PickerManager(this, pickerListener)
		
		setContent {
			RemindMeTheme(darkTheme = false) {
				val systemUiController = rememberSystemUiController()
				
				val windowSize = calculateWindowSizeClass(this)
				val displayFeatures = calculateDisplayFeatures(this)
				
				val uiState by remindMeViewModel.uiState.collectAsStateWithLifecycle()
				val collectedPickerData by pickerData.collectAsStateWithLifecycle()
				
				SideEffect {
					systemUiController.setSystemBarsColor(
						color = Color.Transparent,
						darkIcons = true
					)
				}
				
				CompositionLocalProvider(
					LocalPickerData provides collectedPickerData,
					LocalPickerManager provides pickerManager
				) {
					RemindMeApp(
						uiState = uiState,
						windowSize = windowSize,
						displayFeatures = displayFeatures,
						closeReminderScreen = {
							remindMeViewModel.closeReminderScreen()
						},
						navigateToReminder = { id, contentType ->
							remindMeViewModel.setSelectedReminder(
								reminderID = id,
								contentType = contentType
							)
						},
						updateReminder = { updatedReminder ->
							remindMeViewModel.updateReminder(updatedReminder)
						}
					)
				}
			}
		}
	}
	
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 400, heightDp = 900)
@Composable
fun RemindMeAppPreview() {
	RemindMeTheme {
		RemindMeApp(
			uiState = RemindMeUiState(reminders = LocalRemindersDataProvider.allReminders),
			windowSize = WindowSizeClass.calculateFromSize(DpSize(400.dp, 900.dp)),
			displayFeatures = emptyList()
		)
	}
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 700, heightDp = 500)
@Composable
fun RemindMeAppPreviewTablet() {
	RemindMeTheme {
		RemindMeApp(
			uiState = RemindMeUiState(reminders = LocalRemindersDataProvider.allReminders),
			windowSize = WindowSizeClass.calculateFromSize(DpSize(700.dp, 500.dp)),
			displayFeatures = emptyList()
		)
	}
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 500, heightDp = 700)
@Composable
fun RemindMeAppPreviewTabletPortrait() {
	RemindMeTheme {
		RemindMeApp(
			uiState = RemindMeUiState(reminders = LocalRemindersDataProvider.allReminders),
			windowSize = WindowSizeClass.calculateFromSize(DpSize(500.dp, 700.dp)),
			displayFeatures = emptyList()
		)
	}
}
