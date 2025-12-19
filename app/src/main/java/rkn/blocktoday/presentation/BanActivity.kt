package rkn.blocktoday.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import rkn.blocktoday.R
import rkn.blocktoday.domain.model.ImageItem
import rkn.blocktoday.presentation.components.BanViewModel

class BanActivity : ComponentActivity() {
    private val viewModel: BanViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Get selected image data from intent
        val imageUri = intent.getStringExtra("selected_image")
        val imageName = intent.getStringExtra("image_name")
        val imageSize = intent.getLongExtra("image_size", 0)
        val imageMimeType = intent.getStringExtra("image_mime_type")
        
        if (imageUri == null) {
            // If no image data, finish this activity
            finish()
            return
        }
        
        val selectedImage = ImageItem(
            uri = imageUri,
            displayName = imageName ?: "Unknown",
            size = imageSize,
            mimeType = imageMimeType ?: ""
        )
        
        setContent {
            MaterialTheme {
                Surface {
                    BanScreen(
                        selectedImage = selectedImage,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun BanScreen(
    selectedImage: ImageItem,
    viewModel: BanViewModel
) {
    var currentStage by remember { mutableStateOf(BanStage.Drawing) }
    val context = LocalContext.current
    
    // Handle animation sequence
    LaunchedEffect(Unit) {
        // Stage 1: Drawing red X (5 seconds)
        delay(5000)
        currentStage = BanStage.Fading
        
        // Stage 2: Fading (6 seconds)
        delay(6000)
        currentStage = BanStage.ShowingResult
        
        // Stage 3: Show "ЗАПРЕЩЕНО!" (2 seconds)
        delay(2000)
        currentStage = BanStage.Completed
        
        // Stage 4: Return to main activity
        delay(1000)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (currentStage) {
                BanStage.Drawing -> {
                    // Title
                    Text(
                        text = stringResource(R.string.ban_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Image with red X animation
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = selectedImage.uri,
                                contentDescription = stringResource(R.string.selected_image_description),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 400.dp)
                            )
                            
                            // Red X overlay
                            RedXOverlay(
                                stage = currentStage,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 400.dp)
                            )
                        }
                    }
                }
                
                BanStage.Fading -> {
                    // Same as drawing but with fading effect
                    Text(
                        text = stringResource(R.string.ban_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = selectedImage.uri,
                                contentDescription = stringResource(R.string.selected_image_description),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 400.dp)
                            )
                            
                            RedXOverlay(
                                stage = currentStage,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 400.dp)
                            )
                        }
                    }
                }
                
                BanStage.ShowingResult, BanStage.Completed -> {
                    // Final result screen
                    Text(
                        text = stringResource(R.string.banned_message),
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.Red,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
fun RedXOverlay(
    stage: BanStage,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
    ) {
        val alpha = when (stage) {
            BanStage.Drawing -> 1f
            BanStage.Fading -> 0f // Simplified for now
            else -> 0f
        }
        
        val padding = 16.dp.toPx()
        val x1 = padding
        val y1 = padding
        val x2 = size.width - padding
        val y2 = size.height - padding
        
        // Red color with alpha
        val redColor = Color.Red.copy(alpha = alpha)
        
        // Draw first diagonal line (top-left to bottom-right)
        drawLine(
            color = redColor,
            start = Offset(x1, y1),
            end = Offset(x2, y2),
            strokeWidth = 8.dp.toPx()
        )
        
        // Draw second diagonal line (top-right to bottom-left)
        drawLine(
            color = redColor,
            start = Offset(x2, y1),
            end = Offset(x1, y2),
            strokeWidth = 8.dp.toPx()
        )
    }
}

enum class BanStage {
    Drawing,
    Fading,
    ShowingResult,
    Completed
}