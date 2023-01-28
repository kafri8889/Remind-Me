package com.anafthdev.remindme.uicomponent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anafthdev.remindme.extension.toast
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Preview
@Composable
fun TimePickerPreview() {
//	TimePicker(
//		modifier = Modifier
//			.fillMaxWidth()
//			.aspectRatio(1f/1f)
//	)
	
	TimePicker(
		initialValue = 1,
		maxValue = 100,
		activeTickColor = Color.Green,
		circleRadius = 20f,
		onPositionChange = {
		
		},
		modifier = Modifier
			.fillMaxWidth()
			.aspectRatio(1f / 1f)
	)
}

@Composable
fun TimePicker(
	modifier: Modifier = Modifier,
	initialValue: Int,
	circleRadius: Float,
	minValue:Int = 0,
	maxValue:Int = 100,
	gap: Float = TimePickerDefault.getGap(), // Jarak line ke center
	circleThicknessFraction: Float = TimePickerDefault.getCircleThicknessFraction(),
	activeTickColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
	inactiveTickColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.24f),
	onPositionChange: (Int) -> Unit
) {
	LocalDensity.current.density.toast(LocalContext.current)
	var circleCenter by remember {
		mutableStateOf(Offset.Zero)
	}
	
	var positionValue by remember {
		mutableStateOf(initialValue)
	}
	
	var changeAngle by remember {
		mutableStateOf(0f)
	}
	
	var dragStartedAngle by remember {
		mutableStateOf(0f)
	}
	
	var oldPositionValue by remember {
		mutableStateOf(initialValue)
	}
	
	LaunchedEffect(initialValue) {
		if (initialValue != positionValue) {
			positionValue = initialValue
		}
	}
	
	Box(
		modifier = modifier
	){
		Canvas(
			modifier = Modifier
				.fillMaxSize()
				.clipToBounds()
				.pointerInput(true) {
					detectDragGestures(
						onDragStart = { offset ->
							dragStartedAngle = -atan2(
								x = circleCenter.y - offset.y,
								y = circleCenter.x - offset.x
							) * (180f / PI).toFloat()
							dragStartedAngle = (dragStartedAngle + 180f).mod(360f)
						},
						onDrag = { change, _ ->
							var touchAngle = -atan2(
								x = circleCenter.y - change.position.y,
								y = circleCenter.x - change.position.x
							) * (180f / PI).toFloat()
							touchAngle = (touchAngle + 180f).mod(360f)
							
							val currentAngle = oldPositionValue * 360f / (maxValue - minValue)
							changeAngle = touchAngle - currentAngle
							
							val lowerThreshold = currentAngle - (360f / (maxValue - minValue) * 5)
							val higherThreshold = currentAngle + (360f / (maxValue - minValue) * 5)
							
							if (dragStartedAngle in lowerThreshold..higherThreshold) {
								positionValue =
									(oldPositionValue + (changeAngle / (360f / (maxValue - minValue))).roundToInt())
								onPositionChange(positionValue)
							}
							
						},
						onDragEnd = {
							oldPositionValue = positionValue
							onPositionChange(positionValue)
						}
					)
				}
		) {
			val width = size.width
			val height = size.height
			val circleThickness = (width * circleThicknessFraction).coerceAtMost(width)
			
			circleCenter = Offset(
				x = width / 2f,
				y = height / 2f
			)
			
			val outerRadius = circleRadius + circleThickness/2f
			for (i in 0 .. (maxValue-minValue)){
				val color = if(i < positionValue - minValue) activeTickColor
				else inactiveTickColor
				
				val angleInDegrees = i * 360f / (maxValue - minValue).toFloat()
				val angleInRad = angleInDegrees * PI / 180f + PI/2f
				
				val yGapAdjustment = cos(angleInDegrees * PI / 180f) * gap
				val xGapAdjustment = -sin(angleInDegrees * PI / 180f) * gap
				
				val start = Offset(
					x = (outerRadius * cos(angleInRad) + circleCenter.x + xGapAdjustment).toFloat(),
					y = (outerRadius * sin(angleInRad) + circleCenter.y + yGapAdjustment).toFloat()
				)
				
				val end = Offset(
					x = (outerRadius * cos(angleInRad) + circleCenter.x + xGapAdjustment).toFloat(),
					y = (outerRadius * sin(angleInRad) + circleThickness + circleCenter.y + yGapAdjustment).toFloat()
				)
				
				rotate(
					degrees = angleInDegrees,
					pivot = start
				) {
					drawLine(
						color = color,
						start = start,
						end = end,
						strokeWidth = 1.dp.toPx()
					)
				}
				
			}
		}
	}
}

//@Composable
//fun TimePicker(
//	modifier: Modifier = Modifier
//) {
//
//	var radius by remember {
//		mutableStateOf(0f)
//	}
//
//	var shapeCenter by remember {
//		mutableStateOf(Offset.Zero)
//	}
//
//	var handleCenter by remember {
//		mutableStateOf(Offset.Zero)
//	}
//
//	var angle by remember {
//		mutableStateOf(20.0)
//	}
//
//	Canvas(
//		modifier = modifier
//			.pointerInput(Unit) {
//				detectDragGestures { change, dragAmount ->
//					handleCenter += dragAmount
//
//					angle = getRotationAngle(handleCenter, shapeCenter)
//					change.consume()
//				}
//			}
//			.padding(30.dp)
//
//	) {
//		shapeCenter = center
//
//		radius = size.minDimension / 2
//
//		val x = (shapeCenter.x + cos(Math.toRadians(angle)) * radius).toFloat()
//		val y = (shapeCenter.y + sin(Math.toRadians(angle)) * radius).toFloat()
//
//		handleCenter = Offset(x, y)
//
//		drawCircle(
//			color = Color.Black.copy(alpha = 0.10f),
//			style = Stroke(20f),
//			radius = radius
//		)
//
//		drawArc(
//			color = Color.Yellow,
//			startAngle = 0f,
//			sweepAngle = angle.toFloat(),
//			useCenter = false,
//			style = Stroke(20f)
//		)
//
//		drawCircle(color = Color.Cyan, center = handleCenter, radius = 60f)
//	}
//}
//
//private fun getRotationAngle(currentPosition: Offset, center: Offset): Double {
//	val (dx, dy) = currentPosition - center
//	val theta = atan2(dy, dx).toDouble()
//
//	var angle = Math.toDegrees(theta)
//
//	if (angle < 0) {
//		angle += 360.0
//	}
//
//	return angle
//}

object TimePickerDefault {
	
	@Composable
	fun getGap(): Float {
		
		return when (val density = LocalDensity.current.density) {
			in 0f..1f -> -54f
			in 1.1f..1.5f -> -16f
			in 1.6f..2f -> -4f
			in 2.1f..2.5f -> 16f
			in 2.6f..3f -> 36f
			in 3.1f..3.5f -> 12f
			in 3.6f..4f -> 16f
			in 4.1f..4.5f -> 20f
			else -> -32f
		}
	}
	
	@Composable
	fun getCircleThicknessFraction(): Float {
//		return when (LocalDensity.current.density) {
//			in 0f..1f -> 0.2f
//			in 1.1f..2f -> 0.18f
//			in 2.1f..3f -> 0.22f
//			in 3.1f..4f -> 0.24f
//			else -> 0.2f
//		}
		return 0.2f
	}
}
