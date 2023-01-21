package com.anafthdev.remindme.uicomponent

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anafthdev.remindme.R
import com.anafthdev.remindme.theme.RemindMeTheme

@Preview(showBackground = true)
@Composable
private fun ReminderMessageItemPreview() {
	
	RemindMeTheme {
		ReminderMessageItem(
			message = "Meseg",
			modifier = Modifier
				.fillMaxWidth()
		)
	}
}

@Composable
fun ReminderMessageItem(
	message: String,
	modifier: Modifier = Modifier,
	onDelete: () -> Unit = {}
) {

	OutlinedCard(
		modifier = modifier
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.padding(8.dp)
		) {
			Text(
				text = message,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				style = MaterialTheme.typography.bodyMedium,
				modifier = Modifier
					.weight(1f)
			)
			
			Spacer(modifier = Modifier.width(8.dp))
			
			IconButton(onClick = onDelete) {
				Icon(
					painter = painterResource(id = R.drawable.ic_trash),
					contentDescription = null
				)
			}
		}
	}
}
