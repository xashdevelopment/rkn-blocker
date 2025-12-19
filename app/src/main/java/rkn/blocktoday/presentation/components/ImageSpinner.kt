package rkn.blocktoday.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import rkn.blocktoday.domain.model.ImageItem
import rkn.blocktoday.util.AnimationUtils
import kotlin.math.*

@Composable
fun ImageSpinner(
    images: List<ImageItem>,
    isSpinning: Boolean,
    onSpinComplete: (Int) -> Unit,
    spinDuration: Long = 10000L,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spinner_transition")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = spinDuration.toInt(),
            easing = FastOutSlowInEasing
        ),
        label = "spinner_progress"
    )
    
    val density = LocalDensity.current
    val spinnerSize = 300.dp
    val itemSize = 60.dp
    
    Box(
        modifier = modifier
            .size(spinnerSize),
        contentAlignment = Alignment.Center
    ) {
        if (isSpinning) {
            val selectedIndex = remember { 
                AnimationUtils.selectRandomIndex(images.size) 
            }
            
            Canvas(
                modifier = Modifier.size(spinnerSize)
            ) {
                drawSpinnerBackground()
            }
            
            Box(
                modifier = Modifier
                    .size(spinnerSize)
                    .rotate(animationProgress * 360f * 8) // 8 full rotations
            ) {
                images.forEachIndexed { index, image ->
                    val angle = (index * 360f / images.size)
                    val radius = (spinnerSize.toPx() - itemSize.toPx()) / 2
                    
                    Box(
                        modifier = Modifier
                            .size(itemSize)
                            .align(Alignment.Center)
                            .rotate(angle)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(itemSize)
                                .offset(
                                    x = with(density) { 
                                        (radius - itemSize.toPx() / 2).toDp() 
                                    }
                                )
                        ) {
                            AsyncImage(
                                model = image.uri,
                                contentDescription = "Image ${index + 1}",
                                modifier = Modifier.size(itemSize)
                            )
                        }
                    }
                }
            }
            
            // Call onSpinComplete when animation finishes
            LaunchedEffect(animationProgress) {
                if (animationProgress >= 0.99f) {
                    val selectedIndex = remember { 
                        AnimationUtils.selectRandomIndex(images.size) 
                    }
                    onSpinComplete(selectedIndex)
                }
            }
        } else {
            // Static view when not spinning
            Canvas(
                modifier = Modifier.size(spinnerSize)
            ) {
                drawSpinnerBackground()
            }
            
            LazyVerticalGrid(
                columns = GridCells.Adaptive(itemSize),
                modifier = Modifier.size(spinnerSize)
            ) {
                items(images) { image ->
                    AsyncImage(
                        model = image.uri,
                        contentDescription = "Image",
                        modifier = Modifier.size(itemSize)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawSpinnerBackground() {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = min(size.width, size.height) / 2 - 20.dp.toPx()
    
    // Draw outer circle
    drawCircle(
        color = Color.LightGray.copy(alpha = 0.3f),
        radius = radius,
        center = center
    )
    
    // Draw center circle
    drawCircle(
        color = MaterialTheme.colorScheme.primary,
        radius = radius * 0.1f,
        center = center
    )
    
    // Draw selection indicator (red arrow at top)
    val indicatorAngle = -90f // Top position
    val indicatorX = center.x + (radius - 10.dp.toPx()) * cos(Math.toRadians(indicatorAngle.toDouble())).toFloat()
    val indicatorY = center.y + (radius - 10.dp.toPx()) * sin(Math.toRadians(indicatorAngle.toDouble())).toFloat()
    
    drawLine(
        color = Color.Red,
        start = Offset(indicatorX, indicatorY - 20.dp.toPx()),
        end = Offset(indicatorX, indicatorY + 20.dp.toPx()),
        strokeWidth = 8.dp.toPx()
    )
}