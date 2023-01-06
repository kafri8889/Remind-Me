package com.anafthdev.remindme.ui.remind_me

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.anafthdev.remindme.data.RemindMeRoute
import com.anafthdev.remindme.ui.main.MainScreen
import com.anafthdev.remindme.utils.*

@Composable
fun RemindMeApp(
	uiState: RemindMeUiState,
	windowSize: WindowSizeClass,
	displayFeatures: List<DisplayFeature>,
	closeReminderScreen: () -> Unit = {},
	navigateToReminder: (Int, RemindMeContentType) -> Unit = { _, _ -> }
) {
	
	var contentType by remember { mutableStateOf(RemindMeContentType.SINGLE_PANE) }
	
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
		}
		WindowWidthSizeClass.Medium -> {
			contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
				RemindMeContentType.DUAL_PANE
			} else {
				RemindMeContentType.SINGLE_PANE
			}
		}
		WindowWidthSizeClass.Expanded -> {
			contentType = RemindMeContentType.DUAL_PANE
		}
		else -> {
			contentType = RemindMeContentType.SINGLE_PANE
		}
	}
	
	RemindMeNavigationWrapper(
		contentType = contentType,
		displayFeatures = displayFeatures,
		remindMeUiState = uiState,
		closeReminderScreen = closeReminderScreen,
		navigateToReminder = navigateToReminder
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RemindMeNavigationWrapper(
	contentType: RemindMeContentType,
	displayFeatures: List<DisplayFeature>,
	remindMeUiState: RemindMeUiState,
	closeReminderScreen: () -> Unit,
	navigateToReminder: (Int, RemindMeContentType) -> Unit
) {
	val navController = rememberNavController()
	
	RemindMeNavHost(
		navController = navController,
		contentType = contentType,
		displayFeatures = displayFeatures,
		remindMeUiState = remindMeUiState,
		closeReminderScreen = closeReminderScreen,
		navigateToReminder = navigateToReminder
	)
}

@Composable
private fun RemindMeNavHost(
	navController: NavHostController,
	contentType: RemindMeContentType,
	displayFeatures: List<DisplayFeature>,
	remindMeUiState: RemindMeUiState,
	closeReminderScreen: () -> Unit,
	navigateToReminder: (Int, RemindMeContentType) -> Unit,
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
				closeReminderScreen = closeReminderScreen,
				navigateToReminder = navigateToReminder
			)
		}
		composable(RemindMeRoute.SETTING) {
		
		}
	}
}
