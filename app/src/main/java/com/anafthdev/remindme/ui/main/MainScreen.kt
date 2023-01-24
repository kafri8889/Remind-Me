package com.anafthdev.remindme.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.window.layout.DisplayFeature
import com.anafthdev.remindme.R
import com.anafthdev.remindme.data.RemindMeRoute
import com.anafthdev.remindme.data.RemindMeTopLevelDestination
import com.anafthdev.remindme.data.RemindMeTopLevelDestinations
import com.anafthdev.remindme.data.ReminderMessageType
import com.anafthdev.remindme.data.model.Reminder
import com.anafthdev.remindme.extension.toast
import com.anafthdev.remindme.ui.remind_me.RemindMeUiState
import com.anafthdev.remindme.ui.reminder_detail.ReminderDetailScreen
import com.anafthdev.remindme.ui.reminder_detail.ReminderDetailViewModel
import com.anafthdev.remindme.uicomponent.RemindMeTopAppBar
import com.anafthdev.remindme.uicomponent.ReminderItem
import com.anafthdev.remindme.uicomponent.ReminderMessageItem
import com.anafthdev.remindme.utils.RemindMeContentType
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@Composable
fun MainScreen(
	contentType: RemindMeContentType,
	displayFeatures: List<DisplayFeature>,
	remindMeUiState: RemindMeUiState,
	navigateToTopLevelDestination: (RemindMeTopLevelDestination) -> Unit,
	closeReminderScreen: () -> Unit,
	navigateToReminder: (Int, RemindMeContentType) -> Unit,
	updateReminder: (Reminder) -> Unit,
	onDeleteReminder: () -> Unit
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
					is24Hour = remindMeUiState.userPreferences.is24Hour,
					reminders = remindMeUiState.reminders,
					contentType = contentType,
					reminderLazyListState = reminderLazyListState,
					navigateToTopLevelDestination = navigateToTopLevelDestination,
					navigateToReminder = navigateToReminder,
					updateReminder = updateReminder
				)
			},
			second = {
				RemindMeReminderDetail(
					reminder = remindMeUiState.selectedReminder,
					contentType = contentType,
					onBackPressed = closeReminderScreen,
					onDeleteReminder = onDeleteReminder
				)
			}
		)
	} else {
		Box(modifier = Modifier.fillMaxSize()) {
			RemindMeSinglePaneContent(
				remindMeUiState = remindMeUiState,
				reminderLazyListState = reminderLazyListState,
				navigateToTopLevelDestination = navigateToTopLevelDestination,
				closeDetailScreen = closeReminderScreen,
				navigateToReminder = navigateToReminder,
				updateReminder = updateReminder,
				onDeleteReminder = onDeleteReminder
			)
		}
	}
}

@Composable
fun RemindMeSinglePaneContent(
	remindMeUiState: RemindMeUiState,
	reminderLazyListState: LazyListState,
	modifier: Modifier = Modifier,
	navigateToTopLevelDestination: (RemindMeTopLevelDestination) -> Unit,
	closeDetailScreen: () -> Unit,
	navigateToReminder: (Int, RemindMeContentType) -> Unit,
	updateReminder: (Reminder) -> Unit,
	onDeleteReminder: () -> Unit
) {
	
	if (remindMeUiState.selectedReminder != null && remindMeUiState.isDetailOnlyOpen) {
		BackHandler {
			closeDetailScreen()
		}
		
		RemindMeReminderDetail(
			reminder = remindMeUiState.selectedReminder,
			contentType = RemindMeContentType.SINGLE_PANE,
			onDeleteReminder = onDeleteReminder,
			onBackPressed = closeDetailScreen
		)
	} else {
		RemindMeReminderList(
			is24Hour = remindMeUiState.userPreferences.is24Hour,
			reminders = remindMeUiState.reminders,
			contentType = RemindMeContentType.SINGLE_PANE,
			reminderLazyListState = reminderLazyListState,
			navigateToTopLevelDestination = navigateToTopLevelDestination,
			navigateToReminder = navigateToReminder,
			updateReminder = updateReminder,
			modifier = modifier
		)
	}
}

@Composable
fun RemindMeReminderList(
	is24Hour: Boolean,
	reminders: List<Reminder>,
	contentType: RemindMeContentType,
	reminderLazyListState: LazyListState,
	modifier: Modifier = Modifier,
	navigateToTopLevelDestination: (RemindMeTopLevelDestination) -> Unit,
	navigateToReminder: (Int, RemindMeContentType) -> Unit,
	updateReminder: (Reminder) -> Unit
) {

	LazyColumn(
		modifier = modifier,
		state = reminderLazyListState
	) {
		item {
			RemindMeTopAppBar(
				route = RemindMeRoute.REMINDER_LIST,
				contentType = contentType,
				onNavigationIconClicked = {},
				onSettingClicked = {
					navigateToTopLevelDestination(RemindMeTopLevelDestinations.setting)
				}
			)
		}
		
		items(
			items = reminders,
			key = { item: Reminder -> item.id }
		) { reminder ->
			ReminderItem(
				reminder = reminder,
				is24Hour = is24Hour,
				onClick = {
					navigateToReminder(reminder.id, contentType)
				},
				onCheckedChange = { isActive ->
					updateReminder(
						reminder.copy(
							isActive = isActive
						)
					)
				},
				modifier = Modifier
					.padding(8.dp)
					.fillMaxWidth()
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun RemindMeReminderDetail(
	reminder: Reminder?,
	contentType: RemindMeContentType,
	modifier: Modifier = Modifier,
	onBackPressed: () -> Unit = {},
	onDeleteReminder: () -> Unit = {}
) {
	
	val context = LocalContext.current
	val focusManager = LocalFocusManager.current
	
	val isKeyboardShowed = WindowInsets.isImeVisible
	
	val viewModel = hiltViewModel<ReminderDetailViewModel>()
	
	LaunchedEffect(reminder) {
//		if (reminder == null) {
//			"No reminder selected".toast(context)
//			onBackPressed()
//		} else {
//			viewModel.updateWithReminder(reminder)
//		}
		
		if (reminder != null) {
			viewModel.updateWithReminder(reminder)
		}
	}
	
	LaunchedEffect(isKeyboardShowed) {
		if (!isKeyboardShowed) {
			focusManager.clearFocus()
			
			viewModel.messages.apply {
				removeIf { (text, type) ->
					type == ReminderMessageType.Add || text.isBlank()
				}
			}
		}
	}
	
	BackHandler(enabled = reminder != null) {
		onBackPressed()
	}
	
	if (reminder == null) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = modifier
				.fillMaxSize()
				.systemBarsPadding()
		) {
			Text(
				text = stringResource(id = R.string.no_reminder_selected),
				style = MaterialTheme.typography.titleMedium
			)
		}
	} else {
		LazyColumn(
			modifier = modifier
				.fillMaxSize()
				.systemBarsPadding()
		) {
			item {
				RemindMeTopAppBar(
					route = RemindMeRoute.REMINDER_DETAIL,
					contentType = contentType,
					showSaveButton = !viewModel.autoSave,
					onNavigationIconClicked = onBackPressed,
					onTrashClicked = onDeleteReminder,
					onSaveClicked = {
						val saved = viewModel.saveReminder()
						
						if (saved.isSuccess) context.getString(R.string.saved).toast(context)
						else saved.exceptionOrNull()?.let { it.message.toast(context) }
					}
				)
			}
			
			item {
				ReminderDetailScreen(
					viewModel = viewModel
				)
			}
			
			itemsIndexed(viewModel.messages) { i, (text, type) ->
				val reminderMessageModifier = Modifier
					.padding(
						horizontal = 8.dp,
						vertical = 4.dp
					)
					.fillMaxWidth()
					.animateItemPlacement()
				
				if (type == ReminderMessageType.Fixed) {
					ReminderMessageItem(
						message = text,
						onDelete = {
							viewModel.messages.apply {
								removeAt(i)
							}
						},
						modifier = reminderMessageModifier
					)
				} else {
					ReminderMessageItem(
						onSave = { message ->
							viewModel.messages.apply {
								removeIf { (text, type) ->
									type == ReminderMessageType.Add || text.isBlank()
								}
								
								add(message to ReminderMessageType.Fixed)
							}
						},
						modifier = reminderMessageModifier
					)
				}
			}
			
			item {
				FilledTonalButton(
					shape = MaterialTheme.shapes.medium,
					onClick = {
						viewModel.messages.apply {
							add("" to ReminderMessageType.Add)
						}
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(8.dp)
				) {
					Icon(
						imageVector = Icons.Rounded.Add,
						contentDescription = null
					)
				}
			}
			
			item {
				Spacer(modifier = Modifier.height(24.dp))
			}
		}
	}
}
