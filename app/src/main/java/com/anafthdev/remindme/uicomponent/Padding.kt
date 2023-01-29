package com.anafthdev.remindme.uicomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PaddingCol(
	vertical: Dp = 0.dp,
	horizontal: Dp = 0.dp,
	content: @Composable ColumnScope.() -> Unit
) {
	Column(modifier = Modifier.padding(vertical = vertical, horizontal = horizontal)) {
		content()
	}
}

@Composable
fun PaddingRow(
	vertical: Dp = 0.dp,
	horizontal: Dp = 0.dp,
	verticalAlignment: Alignment.Vertical = Alignment.Top,
	horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
	content: @Composable RowScope.() -> Unit
) {
	Row(
		verticalAlignment = verticalAlignment,
		horizontalArrangement = horizontalArrangement,
		modifier = Modifier
			.padding(vertical = vertical, horizontal = horizontal)
	) {
		content()
	}
}
