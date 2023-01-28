package com.anafthdev.remindme.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Padding(
	vertical: Dp = 0.dp,
	horizontal: Dp = 0.dp,
	content: @Composable ColumnScope.() -> Unit
) {
	Column(modifier = Modifier.padding(vertical = vertical, horizontal = horizontal)) {
		content()
	}
}
