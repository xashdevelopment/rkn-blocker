package rkn.blocktoday.util

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.*

/**
 * Utility class for custom animations
 */
object AnimationUtils {
    
    /**
     * Spinner animation configuration
     */
    data class SpinnerConfig(
        val duration: Long = 10000L, // 10 seconds
        val rotations: Int = 8, // Number of full rotations
        val easing: Easing = FastOutSlowInEasing
    )
    
    /**
     * Calculate spinner position based on animation progress
     */
    fun calculateSpinnerPosition(
        progress: Float,
        totalItems: Int,
        selectedIndex: Int,
        config: SpinnerConfig = SpinnerConfig()
    ): Float {
        val easedProgress = config.easing.transform(progress)
        val totalRotation = config.rotations * 2 * Math.PI.toFloat()
        val position = easedProgress * totalRotation
        
        // Calculate final position relative to selected item
        val itemAngle = (2 * Math.PI.toFloat()) / totalItems
        val finalOffset = selectedIndex * itemAngle
        
        return (position + finalOffset) % (2 * Math.PI.toFloat())
    }
    
    /**
     * Red X drawing animation state
     */
    data class RedXAnimationState(
        val isDrawing: Boolean = false,
        val progress: Float = 0f,
        val isFading: Boolean = false,
        val fadeProgress: Float = 0f
    )
    
    /**
     * Red X drawing animation
     */
    @Composable
    fun rememberRedXAnimationState(): RedXAnimationState {
        val infiniteTransition = rememberInfiniteTransition(label = "red_x_transition")
        
        val drawingProgress by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(durationMillis = 5000, easing = LinearEasing),
            label = "red_x_drawing"
        )
        
        val fadeProgress by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(durationMillis = 6000, easing = LinearEasing),
            label = "red_x_fading"
        )
        
        return remember {
            RedXAnimationState(
                isDrawing = drawingProgress > 0f,
                progress = drawingProgress,
                isFading = drawingProgress >= 1f,
                fadeProgress = if (drawingProgress >= 1f) fadeProgress else 0f
            )
        }
    }
    
    /**
     * Draw animated red X
     */
    fun DrawScope.drawRedX(
        size: Size,
        animationState: RedXAnimationState,
        color: Color = Color.Red
    ) {
        val padding = 16.dp.toPx()
        val x1 = padding
        val y1 = padding
        val x2 = size.width - padding
        val y2 = size.height - padding
        
        val alpha = if (animationState.isFading) {
            1f - animationState.fadeProgress
        } else {
            1f
        }
        
        // Draw first diagonal line (top-left to bottom-right)
        if (animationState.progress > 0f) {
            val progress1 = animationState.progress
            val endX1 = x1 + (x2 - x1) * progress1
            val endY1 = y1 + (y2 - y1) * progress1
            
            drawLine(
                color = color.copy(alpha = alpha),
                start = Offset(x1, y1),
                end = Offset(endX1, endY1),
                strokeWidth = 8.dp.toPx()
            )
        }
        
        // Draw second diagonal line (top-right to bottom-left)
        if (animationState.progress > 0.5f) {
            val progress2 = (animationState.progress - 0.5f) * 2f
            val startX2 = x2
            val startY2 = padding
            val endX2 = x2 - (x2 - x1) * progress2
            val endY2 = y2 - (y2 - y1) * progress2
            
            drawLine(
                color = color.copy(alpha = alpha),
                start = Offset(startX2, startY2),
                end = Offset(endX2, endY2),
                strokeWidth = 8.dp.toPx()
            )
        }
    }
    
    /**
     * Generate deterministic random selection for spinner
     */
    fun selectRandomIndex(totalItems: Int, seed: Long = System.currentTimeMillis()): Int {
        if (totalItems <= 0) return 0
        if (totalItems == 1) return 0
        
        val random = kotlin.random.Random(seed)
        return random.nextInt(totalItems)
    }
}