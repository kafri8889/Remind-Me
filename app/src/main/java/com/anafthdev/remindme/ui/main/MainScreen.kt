package com.anafthdev.remindme.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.anafthdev.remindme.data.RemindMeScreenRoute
import com.anafthdev.remindme.data.model.Reminder
import com.anafthdev.remindme.extension.toast
import com.anafthdev.remindme.ui.remind_me.RemindMeUiState
import com.anafthdev.remindme.uicomponent.RemindMeTopAppBar
import com.anafthdev.remindme.utils.RemindMeContentType
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@Composable
fun MainScreen(
	contentType: RemindMeContentType,
	displayFeatures: List<DisplayFeature>,
	remindMeUiState: RemindMeUiState,
	closeReminderScreen: () -> Unit,
	navigateToReminder: (Int, RemindMeContentType) -> Unit
) {
	
	/**
	 * When moving from LIST_AND_REMINDER page to LIST page clear the selection and user should see LIST screen.
	 */
	LaunchedEffect(key1 = contentType) {
		if (contentType == RemindMeContentType.SINGLE_PANE && !remindMeUiState.isDetailOnlyOpen) {
			closeReminderScreen()
		}
	}
	
	val reminderLazyListState = rememberLazyListState()
	
	if (contentType == RemindMeContentType.DUAL_PANE) {
		TwoPane(
			displayFeatures = displayFeatures,
			strategy = HorizontalTwoPaneStrategy(
				splitFraction = 0.5f,
				gapWidth = 16.dp
			),
			first = {
				RemindMeReminderList(
					reminders = remindMeUiState.reminders,
					contentType = contentType,
					reminderLazyListState = reminderLazyListState,
					navigateToReminder = navigateToReminder
				)
			},
			second = {
				RemindMeReminderDetail(
					reminder = remindMeUiState.selectedReminder,
					contentType = contentType,
					onBackPressed = closeReminderScreen
				)
			}
		)
	} else {
		Box(modifier = Modifier.fillMaxSize()) {
			RemindMeSinglePaneContent(
				remindMeUiState = remindMeUiState,
				reminderLazyListState = reminderLazyListState,
				closeDetailScreen = closeReminderScreen,
				navigateToReminder = navigateToReminder
			)
		}
	}
}

@Composable
fun RemindMeSinglePaneContent(
	remindMeUiState: RemindMeUiState,
	reminderLazyListState: LazyListState,
	modifier: Modifier = Modifier,
	closeDetailScreen: () -> Unit,
	navigateToReminder: (Int, RemindMeContentType) -> Unit
) {
	
	if (remindMeUiState.selectedReminder != null && remindMeUiState.isDetailOnlyOpen) {
		BackHandler {
			closeDetailScreen()
		}
		
		RemindMeReminderDetail(
			reminder = remindMeUiState.selectedReminder,
			contentType = RemindMeContentType.SINGLE_PANE
		) {
			closeDetailScreen()
		}
	} else {
		RemindMeReminderList(
			reminders = remindMeUiState.reminders,
			contentType = RemindMeContentType.SINGLE_PANE,
			reminderLazyListState = reminderLazyListState,
			modifier = modifier,
			navigateToReminder = navigateToReminder
		)
	}
}

@Composable
fun RemindMeReminderList(
	reminders: List<Reminder>,
	contentType: RemindMeContentType,
	reminderLazyListState: LazyListState,
	modifier: Modifier = Modifier,
	navigateToReminder: (Int, RemindMeContentType) -> Unit
) {

	LazyColumn(
		modifier = modifier,
		state = reminderLazyListState
	) {
		item {
			RemindMeTopAppBar(
				route = RemindMeScreenRoute.REMINDER_LIST,
				contentType = contentType,
				onNavigationIconClicked = {}
			)
		}
		
		items(
			items = reminders,
			key = { item: Reminder -> item.id }
		) { reminder ->
			// TODO: Reminder item
		}
	}
}

@Composable
fun RemindMeReminderDetail(
	reminder: Reminder?,
	contentType: RemindMeContentType,
	modifier: Modifier = Modifier,
	onBackPressed: () -> Unit = {}
) {
	
	val context = LocalContext.current
	
	LaunchedEffect(reminder) {
		if (reminder == null) {
			"No reminder selected".toast(context)
			onBackPressed()
		}
	}
	
	LazyColumn(
		modifier = modifier
			.fillMaxSize()
	) {
		item {
			RemindMeTopAppBar(
				route = RemindMeScreenRoute.REMINDER_DETAIL,
				contentType = contentType,
				onNavigationIconClicked = onBackPressed
			)
		}
		
		item {
			// TODO: Reminder detail screen
		}
	}
}
