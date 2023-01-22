package com.anafthdev.remindme.uicomponent

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.anafthdev.remindme.data.LayoutType
import com.anafthdev.remindme.data.RemindMeTopLevelDestination
import com.anafthdev.remindme.data.TOP_LEVEL_DESTINATIONS
import com.anafthdev.remindme.utils.RemindMeNavigationContentPosition

@Composable
fun RemindMeNavigationRail(
	selectedDestination: String,
	navigationContentPosition: RemindMeNavigationContentPosition,
	navigateToTopLevelDestination: (RemindMeTopLevelDestination) -> Unit,
	onDrawerClicked: () -> Unit = {},
	onFABClicked: () -> Unit = {}
) {
	NavigationRail(
		modifier = Modifier.fillMaxHeight(),
		containerColor = MaterialTheme.colorScheme.inverseOnSurface
	) {
		// TODO remove custom nav rail positioning when NavRail component supports it. ticket : b/232495216
		Layout(
			modifier = Modifier.widthIn(max = 80.dp),
			content = {
				Column(
					modifier = Modifier.layoutId(LayoutType.HEADER),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(4.dp)
				) {
					NavigationRailItem(
						selected = false,
						onClick = onDrawerClicked,
						icon = {
							Icon(
								imageVector = Icons.Default.Menu,
								contentDescription = null
							)
						}
					)
					FloatingActionButton(
						onClick = onFABClicked,
						containerColor = MaterialTheme.colorScheme.tertiaryContainer,
						contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
						modifier = Modifier
							.padding(top = 8.dp, bottom = 32.dp)
					) {
						Icon(
							imageVector = Icons.Rounded.Add,
							contentDescription = null,
							modifier = Modifier.size(18.dp)
						)
					}
					
					Spacer(modifier = Modifier.height(8.dp)) // NavigationRailHeaderPadding
					Spacer(modifier = Modifier.height(4.dp)) // NavigationRailVerticalPadding
				}
				
				Column(
					modifier = Modifier.layoutId(LayoutType.CONTENT),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(4.dp)
				) {
					TOP_LEVEL_DESTINATIONS.forEach { replyDestination ->
						NavigationRailItem(
							selected = selectedDestination == replyDestination.route,
							onClick = { navigateToTopLevelDestination(replyDestination) },
							icon = {
								Icon(
									painter = painterResource(id = replyDestination.selectedIcon),
									contentDescription = null
								)
							}
						)
					}
				}
			},
			measurePolicy = { measurables, constraints ->
				lateinit var headerMeasurable: Measurable
				lateinit var contentMeasurable: Measurable
				measurables.forEach {
					when (it.layoutId) {
						LayoutType.HEADER -> headerMeasurable = it
						LayoutType.CONTENT -> contentMeasurable = it
						else -> error("Unknown layoutId encountered!")
					}
				}
				
				val headerPlaceable = headerMeasurable.measure(constraints)
				val contentPlaceable = contentMeasurable.measure(
					constraints.offset(vertical = -headerPlaceable.height)
				)
				layout(constraints.maxWidth, constraints.maxHeight) {
					// Place the header, this goes at the top
					headerPlaceable.placeRelative(0, 0)
					
					// Determine how much space is not taken up by the content
					val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height
					
					val contentPlaceableY = when (navigationContentPosition) {
						// Figure out the place we want to place the content, with respect to the
						// parent (ignoring the header for now)
						RemindMeNavigationContentPosition.TOP -> 0
						RemindMeNavigationContentPosition.CENTER -> nonContentVerticalSpace / 2
					}
						// And finally, make sure we don't overlap with the header.
						.coerceAtLeast(headerPlaceable.height)
					
					contentPlaceable.placeRelative(0, contentPlaceableY)
				}
			}
		)
	}
}

