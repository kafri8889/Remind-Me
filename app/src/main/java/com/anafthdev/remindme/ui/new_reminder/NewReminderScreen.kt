package com.anafthdev.remindme.ui.new_reminder

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anafthdev.remindme.R
import com.anafthdev.remindme.uicomponent.DragHandle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReminderScreen(
	viewModel: NewReminderViewModel,
	onBack: () -> Unit
) {

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.padding(8.dp)
	) {
		Spacer(modifier = Modifier.height(8.dp))
		
		DragHandle()
		
		Spacer(modifier = Modifier.height(16.dp))
		
		OutlinedTextField(
			value = viewModel.reminderName,
			singleLine = true,
			onValueChange = viewModel::updateName,
			placeholder = {
				Text(stringResource(id = R.string.reminder_name))
			},
			modifier = Modifier
				.fillMaxWidth()
		)
		
		Spacer(modifier = Modifier.height(8.dp))
		
		Button(
			enabled = viewModel.reminderName.isNotBlank(),
			onClick = {
				viewModel.save()
				onBack()
			},
			modifier = Modifier
				.fillMaxWidth()
		) {
			Text(stringResource(id = R.string.save))
		}
		
		Spacer(modifier = Modifier.height(4.dp))
		
		TextButton(
			onClick = onBack,
			modifier = Modifier
				.fillMaxWidth()
		) {
			Text(stringResource(id = R.string.cancel))
		}
		
		Spacer(modifier = Modifier.navigationBarsPadding().imePadding())
	}
}
