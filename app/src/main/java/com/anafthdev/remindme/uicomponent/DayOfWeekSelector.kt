package com.anafthdev.remindme.uicomponent

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import com.anafthdev.remindme.data.DayOfWeek
import com.anafthdev.remindme.extension.shortName

@Composable
fun DayOfWeekSelector(
	selectedDays: List<DayOfWeek>,
	modifier: Modifier = Modifier,
	onSelectedDaysChanged: (List<DayOfWeek>) -> Unit
) {
	
	BoxWithConstraints(modifier = modifier) {
		Row {
			for (dayOfWeek in DayOfWeek.values()) {
				val containerColor = if (dayOfWeek in selectedDays) MaterialTheme.colorScheme.primary
				else MaterialTheme.colorScheme.onPrimary
				
				val selectedColor by animateColorAsState(
					targetValue = containerColor,
					animationSpec = tween(250)
				)
				
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.width(this@BoxWithConstraints.maxWidth / 7)
						.padding(4.dp)
						.aspectRatio(1f / 1.6f)
						.clip(MaterialTheme.shapes.medium)
						.drawBehind {
							drawRect(color = selectedColor)
						}
						.clickable {
							onSelectedDaysChanged(
								selectedDays
									.toMutableList()
									.apply {
										if (dayOfWeek in selectedDays) remove(dayOfWeek)
										else add(dayOfWeek)
									}
							)
						}
				) {
					Text(
						text = dayOfWeek.shortName,
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.contentColorFor(containerColor)
						)
					)
				}
			}
		}
	}
}
