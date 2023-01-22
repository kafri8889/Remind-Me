/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anafthdev.remindme.data

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.anafthdev.remindme.R

object RemindMeRoute {
	const val HOME = "Home"
	const val SETTING = "Setting"
	const val NEW_REMINDER = "New Reminder"
	const val REMINDER_LIST = "reminder_list"
	const val REMINDER_DETAIL = "reminder_detail"
}

data class RemindMeTopLevelDestination(
	val route: String,
	val selectedIcon: Int,
	val iconTextId: Int
)

class RemindMeNavigationActions(private val navController: NavHostController) {
	
	fun navigateTo(destination: RemindMeTopLevelDestination) {
		navController.navigate(destination.route) {
			// Pop up to the start destination of the graph to
			// avoid building up a large stack of destinations
			// on the back stack as users select items
			popUpTo(navController.graph.findStartDestination().id) {
				saveState = true
			}
			// Avoid multiple copies of the same destination when
			// reselecting the same item
			launchSingleTop = true
			// Restore state when reselecting a previously selected item
			restoreState = true
		}
	}
}

object RemindMeTopLevelDestinations {
	
	val home = RemindMeTopLevelDestination(
		route = RemindMeRoute.HOME,
		selectedIcon = R.drawable.ic_dashboard,
		iconTextId = R.string.tab_home
	)
	
	val setting = RemindMeTopLevelDestination(
		route = RemindMeRoute.SETTING,
		selectedIcon = R.drawable.ic_setting,
		iconTextId = R.string.tab_setting
	)
	
	val newReminder = RemindMeTopLevelDestination(
		route = RemindMeRoute.NEW_REMINDER,
		selectedIcon = -1,
		iconTextId = -1
	)
	
}

val TOP_LEVEL_DESTINATIONS = listOf(
	RemindMeTopLevelDestinations.home,
	RemindMeTopLevelDestinations.setting
)