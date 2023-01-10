package com.anafthdev.remindme.uicomponent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.*

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
		maxValue = 12,
		primaryColor = Color.Green,
		circleRadius = 230f,
		onPositionChange = {
		
		},
		modifier = Modifier
			.fillMaxWidth()
			.aspectRatio(1f/1f)
	)
}

@Composable
fun TimePicker(
	modifier: Modifier = Modifier,
	initialValue: Int,
	circleRadius: Float,
	minValue:Int = 0,
	maxValue:Int = 100,
	primaryColor: Color = MaterialTheme.colorScheme.primary,
	onPositionChange: (Int)->Unit
) {
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
				.pointerInput(true){
					detectDragGestures(
						onDragStart = {offset ->
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
							
							val currentAngle = oldPositionValue*360f/(maxValue-minValue)
							changeAngle = touchAngle - currentAngle
							
							val lowerThreshold = currentAngle - (360f / (maxValue-minValue) * 5)
							val higherThreshold = currentAngle + (360f / (maxValue-minValue) * 5)
							
							if (dragStartedAngle in lowerThreshold .. higherThreshold){
								positionValue = (oldPositionValue + (changeAngle / (360f / (maxValue-minValue))).roundToInt())
								onPositionChange(positionValue)
							}
							
						},
						onDragEnd = {
							oldPositionValue = positionValue
							onPositionChange(positionValue)
						}
					)
				}
		){
			val width = size.width
			val height = size.height
			val circleThickness = width / 8f
			
			circleCenter = Offset(
				x = width / 2f,
				y = height / 2f
			)
			
			val outerRadius = circleRadius + circleThickness/2f
			val gap = 32f  // Jarak line ke center
			for (i in 0 .. (maxValue-minValue)){
				val color = if(i < positionValue - minValue) primaryColor
				else primaryColor.copy(alpha = 0.3f)
				
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
					angleInDegrees,
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
