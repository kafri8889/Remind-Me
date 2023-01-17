package com.anafthdev.remindme.uicomponent

import androidx.compose.animation.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedTextNumber(
	text: String,
	modifier: Modifier = Modifier
) {
	
	AnimatedContent(
		targetState = text,
		transitionSpec = {
			// Compare the incoming number with the previous number.
			if (targetState > initialState) {
				// If the target number is larger, it slides up and fades in
				// while the initial (smaller) number slides up and fades out.
				slideInVertically { height -> height } + fadeIn() with
						slideOutVertically { height -> -height } + fadeOut()
			} else {
				// If the target number is smaller, it slides down and fades in
				// while the initial number slides down and fades out.
				slideInVertically { height -> -height } + fadeIn() with
						slideOutVertically { height -> height } + fadeOut()
			}.using(
				// Disable clipping since the faded slide-in/out should
				// be displayed out of bounds.
				SizeTransform(clip = false)
			)
		}
	) { targetCount ->
		Text(
			text = targetCount,
			modifier = modifier
		)
	}
}
