package com.anafthdev.remindme.common

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.core.util.toAndroidXPair
import androidx.fragment.app.FragmentActivity
import com.anafthdev.remindme.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

/**
@author kafri8889
 **/
class PickerManager(
	private val context: FragmentActivity,
	private val listener: PickerListener
) {
	
	fun datePicker(
		data: Any? = null,
		min: Long = 0,
		selection: Long = MaterialDatePicker.todayInUtcMilliseconds()
	) {
		listener.onShowRequest()
		
		val datePicker = MaterialDatePicker.Builder.datePicker()
			.setTitleText(context.getString(R.string.select_date))
			.setSelection(selection)
			.setCalendarConstraints(CalendarConstraints.Builder().setStart(min).build())
			.build()
		
		datePicker.addOnPositiveButtonClickListener {
			listener.onDateSelected(data, it)
		}
		
		datePicker.addOnNegativeButtonClickListener {
			listener.onDismissRequest()
		}
		
		datePicker.addOnCancelListener {
			listener.onDismissRequest()
		}
		
		datePicker.show(context.supportFragmentManager, datePicker.tag)
	}
	
	fun dateRangePicker(
		data: Any? = null,
		min: Long = 0,
		selection: Pair<Long, Long> = Pair(
			MaterialDatePicker.thisMonthInUtcMilliseconds(),
			MaterialDatePicker.todayInUtcMilliseconds()
		)
	) {
		listener.onShowRequest()
		
		val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
			.setTitleText(context.getString(R.string.select_date))
			.setSelection(selection.toAndroidXPair())
			.setCalendarConstraints(CalendarConstraints.Builder().setStart(min).build())
			.build()
		
		dateRangePicker.addOnPositiveButtonClickListener {
			listener.onDateRangeSelected(data, it.first, it.second)
		}
		
		dateRangePicker.addOnNegativeButtonClickListener {
			listener.onDismissRequest()
		}
		
		dateRangePicker.addOnCancelListener {
			listener.onDismissRequest()
		}
		
		dateRangePicker.show(context.supportFragmentManager, dateRangePicker.tag)
	}
	
	fun timePicker(
		data: Any? = null,
		hour: Int? = null,
		minute: Int? = null,
		timeFormat: Int = TimeFormat.CLOCK_24H,
		inputMode: Int = MaterialTimePicker.INPUT_MODE_CLOCK,
	) {
		listener.onShowRequest()
		
		val calendar = Calendar.getInstance()
		
		val mMinute = calendar.get(Calendar.MINUTE)
		val mHour = if (timeFormat == TimeFormat.CLOCK_12H) calendar.get(Calendar.HOUR)
		else calendar.get(Calendar.HOUR_OF_DAY)
		
		val timePicker = MaterialTimePicker.Builder()
			.setTimeFormat(timeFormat)
			.setHour(hour ?: mHour)
			.setMinute(minute ?: mMinute)
			.setInputMode(inputMode)
			.setTitleText(context.getString(R.string.select_time))
			.build()
		
		timePicker.addOnPositiveButtonClickListener {
			listener.onTimeSelected(data, timePicker.hour, timePicker.minute)
		}
		
		timePicker.addOnNegativeButtonClickListener {
			listener.onDismissRequest()
		}
		
		timePicker.addOnCancelListener {
			listener.onDismissRequest()
		}
		
		timePicker.show(context.supportFragmentManager, timePicker.tag)
	}
	
}

class PickerListenerWrapper(
	private val onShowRequest: () -> Unit = {},
	private val onDismissRequest: () -> Unit = {},
	private val onDateSelected: (data: Any?, timeInMillis: Long) -> Unit = { _, _ -> },
	private val onDateRangeSelected: (data: Any?, from: Long, to: Long) -> Unit = { _, _, _ -> },
	private val onTimeSelected: (data: Any?, hour: Int, minute: Int) -> Unit = { _, _, _ -> },
): PickerListener {
	override fun onShowRequest() {
		onShowRequest.invoke()
	}
	
	override fun onDismissRequest() {
		onDismissRequest.invoke()
	}
	
	override fun onDateSelected(data: Any?, timeInMillis: Long) {
		onDateSelected.invoke(data, timeInMillis)
	}
	
	override fun onDateRangeSelected(data: Any?, from: Long, to: Long) {
		onDateRangeSelected.invoke(data, from, to)
	}
	
	override fun onTimeSelected(data: Any?, hour: Int, minute: Int) {
		onTimeSelected.invoke(data, hour, minute)
	}
}

data class PickerData(
	/**
	 * `true` if picker dialog has dismissed, `false` if showed
	 */
	val dismiss: Boolean = false,
	
	/**
	 * Custom picker data
	 */
	val data: Any? = null,
	
	/**
	 * Used for date picker
	 */
	val timeInMillis: Long = 0L,
	
	/**
	 * Used for date range picker
	 */
	val from: Long = 0L,
	/**
	 * Used for date range picker
	 */
	val to: Long = 0L,
	
	/**
	 * Used for time picker
	 */
	val hour: Int = 0,
	/**
	 * Used for time picker
	 */
	val minute: Int = 0
)

interface PickerListener {
	
	/**
	 * Invoked when the picker is shown
	 */
	fun onShowRequest()
	
	/**
	 * Invoked when the picker is dismissed
	 */
	fun onDismissRequest()
	
	fun onDateSelected(data: Any?, timeInMillis: Long)
	
	fun onDateRangeSelected(data: Any?, from: Long, to: Long)
	
	fun onTimeSelected(data: Any?, hour: Int, minute: Int)
	
}

val LocalPickerData: ProvidableCompositionLocal<PickerData> = compositionLocalOf { PickerData() }
val LocalPickerManager: ProvidableCompositionLocal<PickerManager?> = compositionLocalOf { null }
