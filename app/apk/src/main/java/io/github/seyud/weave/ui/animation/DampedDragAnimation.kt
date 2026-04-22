package io.github.seyud.weave.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.MutatorMutex
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.IntSize
import io.github.seyud.weave.ui.modifier.inspectDragGestures
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * محرك أنيميشن السحب المخمد (Damped Drag)
 * تم تحسين المعاملات الفيزيائية لمحاكاة استجابة الـ Retina العالية.
 */
class DampedDragAnimation(
    private val animationScope: CoroutineScope,
    val initialValue: Float,
    val valueRange: ClosedRange<Float>,
    val visibilityThreshold: Float,
    val initialScale: Float,
    val pressedScale: Float,
    val canDrag: (Offset) -> Boolean = { true },
    val onDragStarted: DampedDragAnimation.(position: Offset) -> Unit,
    val onDragStopped: DampedDragAnimation.() -> Unit,
    val onDrag: DampedDragAnimation.(size: IntSize, dragAmount: Offset) -> Unit,
) {

    // إعدادات الـ Spring لتعطي إحساساً مريحاً وفخماً (Premium Feel)
    private val valueAnimationSpec =
        spring(dampingRatio = 0.8f, stiffness = 1000f, visibilityThreshold = visibilityThreshold)
    
    private val velocityAnimationSpec =
        spring(dampingRatio = 0.6f, stiffness = 400f, visibilityThreshold = visibilityThreshold * 10f)
    
    private val pressProgressAnimationSpec =
        spring(dampingRatio = 1f, stiffness = 1000f, visibilityThreshold = 0.001f)
    
    private val scaleXAnimationSpec =
        spring(dampingRatio = 0.65f, stiffness = 300f, visibilityThreshold = 0.001f)
    
    private val scaleYAnimationSpec =
        spring(dampingRatio = 0.75f, stiffness = 300f, visibilityThreshold = 0.001f)

    private val valueAnimation =
        Animatable(initialValue, visibilityThreshold)
    
    private val velocityAnimation =
        Animatable(0f, 5f)
    
    private val pressProgressAnimation =
        Animatable(0f, 0.001f)
    
    private val scaleXAnimation =
        Animatable(initialScale, 0.001f)
    
    private val scaleYAnimation =
        Animatable(initialScale, 0.001f)

    private val mutatorMutex = MutatorMutex()
    private val velocityTracker = VelocityTracker()

    val value: Float get() = valueAnimation.value
    val targetValue: Float get() = valueAnimation.targetValue
    val pressProgress: Float get() = pressProgressAnimation.value
    val scaleX: Float get() = scaleXAnimation.value
    val scaleY: Float get() = scaleYAnimation.value
    val velocity: Float get() = velocityAnimation.value

    val modifier: Modifier = Modifier.pointerInput(Unit) {
        inspectDragGestures(
            onDragStart = { down ->
                onDragStarted(down.position)
                press()
            },
            onDragEnd = {
                onDragStopped()
                release()
            },
            onDragCancel = {
                onDragStopped()
                release()
            }
        ) { change, dragAmount ->
            val position = change.position
            val previousPosition = change.previousPosition

            val isInside = canDrag(position)
            val wasInside = canDrag(previousPosition)

            if (isInside && wasInside) {
                onDrag(size, dragAmount)
            }
        }
    }

    fun press() {
        velocityTracker.resetTracking()
        animationScope.launch {
            mutatorMutex.mutate {
                launch { pressProgressAnimation.animateTo(1f, pressProgressAnimationSpec) }
                launch { scaleXAnimation.animateTo(pressedScale, scaleXAnimationSpec) }
                launch { scaleYAnimation.animateTo(pressedScale, scaleYAnimationSpec) }
            }
        }
    }

    fun release() {
        animationScope.launch {
            mutatorMutex.mutate {
                awaitFrame()
                if (value != targetValue) {
                    val threshold = (valueRange.endInclusive - valueRange.start) * 0.025f
                    snapshotFlow { valueAnimation.value }
                        .filter { abs(it - valueAnimation.targetValue) < threshold }
                        .first()
                }
                launch { pressProgressAnimation.animateTo(0f, pressProgressAnimationSpec) }
                launch { scaleXAnimation.animateTo(initialScale, scaleXAnimationSpec) }
                launch { scaleYAnimation.animateTo(initialScale, scaleYAnimationSpec) }
            }
        }
    }

    fun updateValue(value: Float) {
        val targetValue = value.coerceIn(valueRange)
        animationScope.launch {
            // استخدام animateTo مع تحديث السرعة لضمان سلاسة الانتقال أثناء السحب المستمر
            valueAnimation.animateTo(targetValue, valueAnimationSpec) {
                updateVelocity()
            }
        }
    }

    fun animateToValue(value: Float) {
        animationScope.launch {
            mutatorMutex.mutate {
                val targetValue = value.coerceIn(valueRange)
                launch { valueAnimation.animateTo(targetValue, valueAnimationSpec) }
                if (velocity != 0f) {
                    launch { velocityAnimation.animateTo(0f, velocityAnimationSpec) }
                }
                // ضمان العودة للحجم الطبيعي بعد انتهاء الأنيميشن التلقائي
                launch { scaleXAnimation.animateTo(initialScale, scaleXAnimationSpec) }
                launch { scaleYAnimation.animateTo(initialScale, scaleYAnimationSpec) }
            }
        }
    }

    private fun updateVelocity() {
        velocityTracker.addPosition(
            System.currentTimeMillis(),
            Offset(value, 0f)
        )
        val rangeSize = valueRange.endInclusive - valueRange.start
        if (rangeSize > 0) {
            val targetVelocity = velocityTracker.calculateVelocity().x / rangeSize
            animationScope.launch { 
                velocityAnimation.animateTo(targetVelocity, velocityAnimationSpec) 
            }
        }
    }
}
