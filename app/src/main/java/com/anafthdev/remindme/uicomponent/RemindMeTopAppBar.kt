package com.anafthdev.remindme.uicomponent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.anafthdev.remindme.R
import com.anafthdev.remindme.data.RemindMeRoute
import com.anafthdev.remindme.data.RemindMeScreenRoute
import com.anafthdev.remindme.utils.RemindMeContentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindMeTopAppBar(
	route: String,
	contentType: RemindMeContentType,
	modifier: Modifier = Modifier,
	onNavigationIconClicked: () -> Unit
) {
	
	CenterAlignedTopAppBar(
		modifier = modifier,
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
			if (route == RemindMeScreenRoute.REMINDER_DETAIL) {
				IconButton(onClick = onNavigationIconClicked) {
					Icon(
						painter = painterResource(id = R.drawable.ic_trash),
						contentDescription = null
					)
				}
			} else {
				IconButton(onClick = onNavigationIconClicked) {
					Icon(
						painter = painterResource(id = R.drawable.ic_setting),
						contentDescription = null
					)
				}
			}
		}
	)
}
