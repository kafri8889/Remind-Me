package com.anafthdev.remindme.ui.remind_me

import androidx.compose.animation.AnimatedVisibility
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.anafthdev.remindme.data.RemindMeNavigationActions
import com.anafthdev.remindme.data.RemindMeRoute
import com.anafthdev.remindme.data.RemindMeTopLevelDestination
import com.anafthdev.remindme.data.model.Reminder
import com.anafthdev.remindme.ui.main.MainScreen
import com.anafthdev.remindme.uicomponent.ModalNavigationDrawerContent
import com.anafthdev.remindme.uicomponent.PermanentNavigationDrawerContent
import com.anafthdev.remindme.uicomponent.RemindMeNavigationRail
import com.anafthdev.remindme.utils.*
import kotlinx.coroutines.launch

@Composable
fun RemindMeApp(
	uiState: RemindMeUiState,
	windowSize: WindowSizeClass,
	displayFeatures: List<DisplayFeature>,
	closeReminderScreen: () -> Unit = {},
	navigateToReminder: (Int, RemindMeContentType) -> Unit = { _, _ -> },
	updateReminder: (Reminder) -> Unit = {}
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
		updateReminder = updateReminder
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RemindMeNavigationWrapper(
	contentType: RemindMeContentType,
	navigationType: RemindMeNavigationType,
	navigationContentPosition: RemindMeNavigationContentPosition,
	displayFeatures: List<DisplayFeature>,
	remindMeUiState: RemindMeUiState,
	closeReminderScreen: () -> Unit,
	navigateToReminder: (Int, RemindMeContentType) -> Unit,
	updateReminder: (Reminder) -> Unit
) {
	val scope = rememberCoroutineScope()
	val drawerState = rememberDrawerState(DrawerValue.Closed)
	val navController = rememberNavController()
	
	val navigationActions = remember(navController) {
		RemindMeNavigationActions(navController)
	}
	
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val selectedDestination = navBackStackEntry?.destination?.route ?: RemindMeRoute.HOME
	
	if (navigationType == RemindMeNavigationType.PERMANENT_NAVIGATION_DRAWER) {
		// TODO check on custom width of PermanentNavigationDrawer: b/232495216
		PermanentNavigationDrawer(drawerContent = {
			PermanentNavigationDrawerContent(
				selectedDestination = selectedDestination,
				navigationContentPosition = navigationContentPosition,
				navigateToTopLevelDestination = navigationActions::navigateTo,
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
				navigationType = navigationType
			)
		}
	} else {
		ModalNavigationDrawer(
			drawerContent = {
				ModalNavigationDrawerContent(
					selectedDestination = selectedDestination,
					navigationContentPosition = navigationContentPosition,
					navigateToTopLevelDestination = navigationActions::navigateTo,
					onDrawerClicked = {
						scope.launch {
							drawerState.close()
						}
					}
				)
			},
			drawerState = drawerState
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
				onDrawerClicked = {
					scope.launch {
						drawerState.open()
					}
				}
			)
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
	onDrawerClicked: () -> Unit = {}
) {
	Row(modifier = modifier.fillMaxSize()) {
		AnimatedVisibility(visible = navigationType == RemindMeNavigationType.NAVIGATION_RAIL) {
			RemindMeNavigationRail(
				selectedDestination = selectedDestination,
				navigationContentPosition = navigationContentPosition,
				navigateToTopLevelDestination = navigateToTopLevelDestination,
				onDrawerClicked = onDrawerClicked,
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
				updateReminder = updateReminder
			)
		}
		composable(RemindMeRoute.SETTING) {
		
		}
	}
}
