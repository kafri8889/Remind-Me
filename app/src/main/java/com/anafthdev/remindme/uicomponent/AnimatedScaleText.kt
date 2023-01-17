package com.anafthdev.remindme.uicomponent

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedScaleText(
	text: String,
	modifier: Modifier = Modifier,
	content: @Composable (String) -> Unit
) {

	AnimatedContent(
		targetState = text,
		modifier = modifier,
		transitionSpec = {
			scaleIn(
				animationSpec = tween(250)
			) with scaleOut(
				animationSpec = tween(250)
			)
		}
	) {
		content(it)
	}
}
