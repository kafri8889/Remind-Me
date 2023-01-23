package com.anafthdev.remindme.ui.remind_me

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.anafthdev.remindme.R
import com.anafthdev.remindme.data.RemindMeNavigationActions
import com.anafthdev.remindme.data.RemindMeRoute
import com.anafthdev.remindme.data.RemindMeTopLevelDestination
import com.anafthdev.remindme.data.RemindMeTopLevelDestinations
import com.anafthdev.remindme.data.model.Reminder
import com.anafthdev.remindme.ui.main.MainScreen
import com.anafthdev.remindme.ui.new_reminder.NewReminderScreen
import com.anafthdev.remindme.ui.new_reminder.NewReminderViewModel
import com.anafthdev.remindme.ui.setting.SettingScreen
import com.anafthdev.remindme.ui.setting.SettingViewModel
import com.anafthdev.remindme.uicomponent.ModalNavigationDrawerContent
import com.anafthdev.remindme.uicomponent.PermanentNavigationDrawerContent
import com.anafthdev.remindme.uicomponent.RemindMeNavigationRail
import com.anafthdev.remindme.utils.*
import com.google.accompanist.navigation.material.*
import kotlinx.coroutines.launch

@Composable
fun RemindMeApp(
	uiState: RemindMeUiState,
	windowSize: WindowSizeClass,
	displayFeatures: List<DisplayFeature>,
	closeReminderScreen: () -> Unit = {},
	navigateToReminder: (Int, RemindMeContentType) -> Unit = { _, _ -> },
	updateReminder: (Reminder) -> Unit = {},
	onDeleteReminder: (showConfirmationDialog: Boolean, deleteCurrentReminder: Boolean) -> Unit = { _, _ -> }
) {
	
	var contentType by remember { mutableStateOf(RemindMeContentType.SINGLE_PANE) }
	var navigationType by remember { mutableStateOf(RemindMeNavigationType.TOP_APP_BAR) }
	
	/**
	 * We are using display's folding features to map the device postures a fold is in.
	 * In the state of folding device If it's half fold in BookPosture we want to avoid content
	 * at the crease/hinge
	 */
	val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
	
	val foldingDevicePosture = when {
		isBookPosture(foldingFeature) ->
			DevicePosture.BookPosture(foldingFeature.bounds)
		
		isSeparating(foldingFeature) ->
			DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)
		
		else -> DevicePosture.NormalPosture
	}
	
	when (windowSize.widthSizeClass) {
		WindowWidthSizeClass.Compact -> {
			contentType = RemindMeContentType.SINGLE_PANE
			navigationType = RemindMeNavigationType.TOP_APP_BAR
		}
		WindowWidthSizeClass.Medium -> {
			navigationType = RemindMeNavigationType.NAVIGATION_RAIL
			contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
				RemindMeContentType.DUAL_PANE
			} else {
				RemindMeContentType.SINGLE_PANE
			}
		}
		WindowWidthSizeClass.Expanded -> {
			contentType = RemindMeContentType.DUAL_PANE
			navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
				RemindMeNavigationType.NAVIGATION_RAIL
			} else {
				RemindMeNavigationType.PERMANENT_NAVIGATION_DRAWER
			}
		}
		else -> {
			contentType = RemindMeContentType.SINGLE_PANE
			navigationType = RemindMeNavigationType.TOP_APP_BAR
		}
	}
	
	val navigationContentPosition = when (windowSize.heightSizeClass) {
		WindowHeightSizeClass.Compact -> {
			RemindMeNavigationContentPosition.TOP
		}
		WindowHeightSizeClass.Medium,
		WindowHeightSizeClass.Expanded -> {
			RemindMeNavigationContentPosition.CENTER
		}
		else -> {
			RemindMeNavigationContentPosition.TOP
		}
	}
	
	RemindMeNavigationWrapper(
		contentType = contentType,
		navigationType = navigationType,
		navigationContentPosition = navigationContentPosition,
		displayFeatures = displayFeatures,
		remindMeUiState = uiState,
		closeReminderScreen = closeReminderScreen,
		navigateToReminder = navigateToReminder,
		updateReminder = updateReminder,
		onDeleteReminder = onDeleteReminder
	)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class)
@Composable
private fun RemindMeNavigationWrapper(
	contentType: RemindMeContentType,
	navigationType: RemindMeNavigationType,
	navigationContentPosition: RemindMeNavigationContentPosition,
	displayFeatures: List<DisplayFeature>,
	remindMeUiState: RemindMeUiState,
	closeReminderScreen: () -> Unit,
	navigateToReminder: (Int, RemindMeContentType) -> Unit,
	updateReminder: (Reminder) -> Unit,
	onDeleteReminder: (showConfirmationDialog: Boolean, deleteCurrentReminder: Boolean) -> Unit
) {
	val scope = rememberCoroutineScope()
	val drawerState = rememberDrawerState(DrawerValue.Closed)
	val bottomSheetNavigator = rememberBottomSheetNavigator()
	val navController = rememberNavController(bottomSheetNavigator)
	
	val navigationActions = remember(navController) {
		RemindMeNavigationActions(navController)
	}
	
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val selectedDestination = navBackStackEntry?.destination?.route ?: RemindMeRoute.HOME
	
	ModalBottomSheetLayout(
		sheetShape = MaterialTheme.shapes.large,
		sheetBackgroundColor = MaterialTheme.colorScheme.surface,
		bottomSheetNavigator = bottomSheetNavigator
	) {
		AnimatedVisibility(
			visible = remindMeUiState.showDeleteConfirmationDialog,
			enter = fadeIn(
				animationSpec = tween(250)
			),
			exit = fadeOut(
				animationSpec = tween(250)
			)
		) {
			AlertDialog(
				onDismissRequest = {
					onDeleteReminder(false, false)
				},
				icon = {
					Icon(
						painter = painterResource(id = R.drawable.ic_trash),
						contentDescription = null
					)
				},
				title = {
					Text(stringResource(id = R.string.delete_reminder))
				},
				text = {
					Text(stringResource(id = R.string.delete_reminder_message))
				},
				confirmButton = {
					Button(
						onClick = {
							onDeleteReminder(false, true)
						}
					) {
						Text(stringResource(id = R.string.delete))
					}
				},
				dismissButton = {
					TextButton(
						onClick = {
							onDeleteReminder(false, false)
						}
					) {
						Text(stringResource(id = R.string.cancel))
					}
				}
			)
		}
		
		if (navigationType == RemindMeNavigationType.PERMANENT_NAVIGATION_DRAWER) {
			// TODO check on custom width of PermanentNavigationDrawer: b/232495216
			PermanentNavigationDrawer(drawerContent = {
				PermanentNavigationDrawerContent(
					selectedDestination = selectedDestination,
					navigationContentPosition = navigationContentPosition,
					navigateToTopLevelDestination = navigationActions::navigateTo,
					onFABClicked = {
						navigationActions.navigateTo(RemindMeTopLevelDestinations.newReminder)
					}
				)
			}) {
				RemindMeAppContent(
					navController = navController,
					contentType = contentType,
					displayFeatures = displayFeatures,
					remindMeUiState = remindMeUiState,
					navigateToTopLevelDestination = navigationActions::navigateTo,
					closeReminderScreen = closeReminderScreen,
					navigateToReminder = navigateToReminder,
					updateReminder = updateReminder,
					navigationContentPosition = navigationContentPosition,
					selectedDestination = selectedDestination,
					navigationType = navigationType,
					onDeleteReminder = {
						onDeleteReminder(true, false)
					}
				)
			}
		} else {
			ModalNavigationDrawer(
				drawerState = drawerState,
				drawerContent = {
					ModalNavigationDrawerContent(
						selectedDestination = selectedDestination,
						navigationContentPosition = navigationContentPosition,
						navigateToTopLevelDestination = navigationActions::navigateTo,
						onDrawerClicked = {
							scope.launch {
								drawerState.close()
							}
						},
						onFABClicked = {
							navigationActions.navigateTo(RemindMeTopLevelDestinations.newReminder)
							
							scope.launch {
								drawerState.close()
							}
						}
					)
				}
			) {
				RemindMeAppContent(
					navController = navController,
					contentType = contentType,
					displayFeatures = displayFeatures,
					remindMeUiState = remindMeUiState,
					navigateToTopLevelDestination = navigationActions::navigateTo,
					closeReminderScreen = closeReminderScreen,
					navigateToReminder = navigateToReminder,
					updateReminder = updateReminder,
					navigationContentPosition = navigationContentPosition,
					selectedDestination = selectedDestination,
					navigationType = navigationType,
					onDeleteReminder = {
						onDeleteReminder(true, false)
					},
					onDrawerClicked = {
						scope.launch {
							drawerState.open()
						}
					}
				)
			}
		}
	}
}

@Composable
fun RemindMeAppContent(
	modifier: Modifier = Modifier,
	navigationType: RemindMeNavigationType,
	contentType: RemindMeContentType,
	displayFeatures: List<DisplayFeature>,
	navigationContentPosition: RemindMeNavigationContentPosition,
	remindMeUiState: RemindMeUiState,
	navController: NavHostController,
	selectedDestination: String,
	navigateToTopLevelDestination: (RemindMeTopLevelDestination) -> Unit,
	closeReminderScreen: () -> Unit,
	navigateToReminder: (Int, RemindMeContentType) -> Unit,
	updateReminder: (Reminder) -> Unit,
	onDrawerClicked: () -> Unit = {},
	onDeleteReminder: () -> Unit = {}
) {
	Row(modifier = modifier.fillMaxSize()) {
		AnimatedVisibility(visible = navigationType == RemindMeNavigationType.NAVIGATION_RAIL) {
			RemindMeNavigationRail(
				selectedDestination = selectedDestination,
				navigationContentPosition = navigationContentPosition,
				navigateToTopLevelDestination = navigateToTopLevelDestination,
				onDrawerClicked = onDrawerClicked,
				onFABClicked = {
					navigateToTopLevelDestination(RemindMeTopLevelDestinations.newReminder)
				}
			)
		}
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.inverseOnSurface)
		) {
			RemindMeNavHost(
				navController = navController,
				contentType = contentType,
				displayFeatures = displayFeatures,
				remindMeUiState = remindMeUiState,
				navigateToTopLevelDestination = navigateToTopLevelDestination,
				closeReminderScreen = closeReminderScreen,
				navigateToReminder = navigateToReminder,
				updateReminder = updateReminder,
				onDeleteReminder = onDeleteReminder,
				modifier = Modifier
					.weight(1f)
			)
//			AnimatedVisibility(visible = navigationType == RemindMeNavigationType.TOP_APP_BAR) {
//				RemindMeBottomNavigationBar(
//					selectedDestination = selectedDestination,
//					navigateToTopLevelDestination = navigateToTopLevelDestination
//				)
//			}
		}
	}
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
private fun RemindMeNavHost(
	navController: NavHostController,
	contentType: RemindMeContentType,
	displayFeatures: List<DisplayFeature>,
	remindMeUiState: RemindMeUiState,
	navigateToTopLevelDestination: (RemindMeTopLevelDestination) -> Unit,
	closeReminderScreen: () -> Unit,
	navigateToReminder: (Int, RemindMeContentType) -> Unit,
	updateReminder: (Reminder) -> Unit,
	onDeleteReminder: () -> Unit,
	modifier: Modifier = Modifier,
) {
	NavHost(
		modifier = modifier,
		navController = navController,
		startDestination = RemindMeRoute.HOME,
	) {
		composable(RemindMeRoute.HOME) {
			MainScreen(
				contentType = contentType,
				remindMeUiState = remindMeUiState,
				displayFeatures = displayFeatures,
				navigateToTopLevelDestination = navigateToTopLevelDestination,
				closeReminderScreen = closeReminderScreen,
				navigateToReminder = navigateToReminder,
				updateReminder = updateReminder,
				onDeleteReminder = onDeleteReminder
			)
		}
		
		composable(RemindMeRoute.SETTING) { backEntry ->
			val viewModel = hiltViewModel<SettingViewModel>(backEntry)
			
			SettingScreen(
				viewModel = viewModel,
				contentType = contentType,
				onBackPressed = navController::popBackStack
			)
		}
		
		bottomSheet(RemindMeRoute.NEW_REMINDER) { backEntry ->
			val viewModel = hiltViewModel<NewReminderViewModel>(backEntry)
			
			NewReminderScreen(
				viewModel = viewModel,
				onBack = navController::popBackStack
			)
		}
	}
}
