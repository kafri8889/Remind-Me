package com.anafthdev.remindme.ui.confirmation_dialog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.anafthdev.remindme.data.RemindMeArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfirmationDialogViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle
): ViewModel() {

	val title = savedStateHandle.getStateFlow(RemindMeArgument.ARG_TITLE, "")
	val text = savedStateHandle.getStateFlow(RemindMeArgument.ARG_TEXT, "")

}