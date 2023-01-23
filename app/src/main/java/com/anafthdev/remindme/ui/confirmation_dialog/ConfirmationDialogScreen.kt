package com.anafthdev.remindme.ui.confirmation_dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ConfirmationDialogScreen(
	viewModel: ConfirmationDialogViewModel
) {

	val title by viewModel.title.collectAsStateWithLifecycle()
	val text by viewModel.text.collectAsStateWithLifecycle()
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
	) {
		Text(text = title)
		
		Spacer(modifier = Modifier.height(8.dp))
		
		Text(text = text)
	}
	
}
