package com.anafthdev.remindme.runtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anafthdev.remindme.data.local.LocalRemindersDataProvider
import com.anafthdev.remindme.theme.RemindMeTheme
import com.anafthdev.remindme.ui.remind_me.RemindMeApp
import com.anafthdev.remindme.ui.remind_me.RemindMeUiState
import com.anafthdev.remindme.ui.remind_me.RemindMeViewModel
import com.google.accompanist.adaptive.calculateDisplayFeatures


class MainActivity : ComponentActivity() {
	
	private val remindMeViewModel: RemindMeViewModel by viewModels()
	
	@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalLifecycleComposeApi::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			RemindMeTheme {
				val windowSize = calculateWindowSizeClass(this)
				val displayFeatures = calculateDisplayFeatures(this)
				val uiState by remindMeViewModel.uiState.collectAsStateWithLifecycle()
				
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
					}
				)
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
