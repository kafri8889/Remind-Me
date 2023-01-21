package com.anafthdev.remindme.extension

import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastAny
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

private val NoPressGesture: suspend PressGestureScope.(Offset) -> Unit = {}

private class PressGestureScopeImpl(
	density: Density
) : PressGestureScope, Density by density {
	private var isReleased = false
	private var isCanceled = false
	private val mutex = Mutex(locked = false)
	
	/**
	 * Called when a gesture has been canceled.
	 */
	fun cancel() {
		isCanceled = true
		mutex.unlock()
	}
	
	/**
	 * Called when all pointers are up.
	 */
	fun release() {
		isReleased = true
		mutex.unlock()
	}
	
	/**
	 * Called when a new gesture has started.
	 */
	fun reset() {
		mutex.tryLock() // If tryAwaitRelease wasn't called, this will be unlocked.
		isReleased = false
		isCanceled = false
	}
	
	override suspend fun awaitRelease() {
		if (!tryAwaitRelease()) {
			throw GestureCancellationException("The press gesture was canceled.")
		}
	}
	
	override suspend fun tryAwaitRelease(): Boolean {
		if (!isReleased && !isCanceled) {
			mutex.lock()
		}
		return isReleased
	}
}

suspend fun PointerInputScope.detectTapUnconsumed(
	onTap: ((Offset) -> Unit)
) {
	val pressScope = PressGestureScopeImpl(this)
	forEachGesture {
		coroutineScope {
			pressScope.reset()
			awaitPointerEventScope {
				awaitFirstDown(requireUnconsumed = false).also {
					if (it.pressed != it.previousPressed) it.consume()
				}
				val up = waitForUpOrCancellationInitial()
				if (up == null) {
					pressScope.cancel()
				} else {
					pressScope.release()
					onTap(up.position)
				}
			}
		}
	}
}

suspend fun PointerInputScope.detectTapAndPressUnconsumed(
	onPress: suspend PressGestureScope.(Offset) -> Unit = NoPressGesture,
	onTap: ((Offset) -> Unit)? = null
) {
	val pressScope = PressGestureScopeImpl(this)
	
	forEachGesture {
		coroutineScope {
			pressScope.reset()
			awaitPointerEventScope {
				
				val down = awaitFirstDown(requireUnconsumed = false).also { it.consumeDownChange() }
				
				if (onPress !== NoPressGesture) {
					launch { pressScope.onPress(down.position) }
				}
				
				val up = waitForUpOrCancellationInitial()
				if (up == null) {
					pressScope.cancel() // tap-up was canceled
				} else {
					pressScope.release()
					onTap?.invoke(up.position)
				}
			}
		}
	}
}

suspend fun AwaitPointerEventScope.waitForUpOrCancellationInitial(): PointerInputChange? {
	while (true) {
		val event = awaitPointerEvent(PointerEventPass.Initial)
		if (event.changes.fastAll { it.changedToUp() }) {
			// All pointers are up
			return event.changes[0]
		}
		
		if (event.changes.fastAny { it.consumed.downChange || it.isOutOfBounds(size) }) {
			return null // Canceled
		}
		
		// Check for cancel by position consumption. We can look on the Final pass of the
		// existing pointer event because it comes after the Main pass we checked above.
		val consumeCheck = awaitPointerEvent(PointerEventPass.Final)
		if (consumeCheck.changes.fastAny { it.positionChangeConsumed() }) {
			return null
		}
	}
}
