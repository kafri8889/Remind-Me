package com.anafthdev.remindme.uicomponent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.anafthdev.remindme.R
import com.anafthdev.remindme.data.RemindMeScreenRoute
import com.anafthdev.remindme.data.RemindMeTopLevelDestination
import com.anafthdev.remindme.data.TOP_LEVEL_DESTINATIONS
import com.anafthdev.remindme.utils.RemindMeContentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindMeTopAppBar(
	route: String,
	contentType: RemindMeContentType,
	modifier: Modifier = Modifier,
	onNavigationIconClicked: () -> Unit,
	onSettingClicked: () -> Unit = {},
	onTrashClicked: () -> Unit = {}
) {
	
	CenterAlignedTopAppBar(
		modifier = modifier,
		colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
			containerColor = Color.Transparent
		),
		title = {},
		navigationIcon = {
			if (route == RemindMeScreenRoute.REMINDER_DETAIL && contentType == RemindMeContentType.SINGLE_PANE) {
				IconButton(onClick = onNavigationIconClicked) {
					Icon(
						imageVector = Icons.Rounded.ArrowBack,
						contentDescription = null
					)
				}
			}
		},
		actions = {
			if (contentType == RemindMeContentType.SINGLE_PANE) {
				if (route == RemindMeScreenRoute.REMINDER_DETAIL) {
					IconButton(onClick = onTrashClicked) {
						Icon(
							painter = painterResource(id = R.drawable.ic_trash),
							contentDescription = null
						)
					}
				} else {
					IconButton(onClick = onSettingClicked) {
						Icon(
							painter = painterResource(id = R.drawable.ic_setting),
							contentDescription = null
						)
					}
				}
			} else {
				if (route == RemindMeScreenRoute.REMINDER_DETAIL) {
					IconButton(onClick = onTrashClicked) {
						Icon(
							painter = painterResource(id = R.drawable.ic_trash),
							contentDescription = null
						)
					}
				}
			}
		}
	)
}

@Composable
fun RemindMeBottomNavigationBar(
	selectedDestination: String,
	navigateToTopLevelDestination: (RemindMeTopLevelDestination) -> Unit
) {
	NavigationBar(modifier = Modifier.fillMaxWidth()) {
		TOP_LEVEL_DESTINATIONS.forEach { replyDestination ->
			NavigationBarItem(
				selected = selectedDestination == replyDestination.route,
				onClick = { navigateToTopLevelDestination(replyDestination) },
				icon = {
					Icon(
						painter = painterResource(id = replyDestination.selectedIcon),
						contentDescription = stringResource(id = replyDestination.iconTextId)
					)
				}
			)
		}
	}
}
