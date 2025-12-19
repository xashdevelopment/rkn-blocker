package rkn.blocktoday.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import rkn.blocktoday.R
import rkn.blocktoday.domain.model.ImageItem

class ResultActivity : ComponentActivity() {
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
                    ResultScreen(
                        selectedImage = selectedImage,
                        onSkip = { finish() },
                        onBan = {
                            val intent = Intent(this, BanActivity::class.java).apply {
                                putExtra("selected_image", selectedImage.uri)
                                putExtra("image_name", selectedImage.displayName)
                                putExtra("image_size", selectedImage.size)
                                putExtra("image_mime_type", selectedImage.mimeType)
                            }
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ResultScreen(
    selectedImage: ImageItem,
    onSkip: () -> Unit,
    onBan: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = stringResource(R.string.result_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Selected image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                AsyncImage(
                    model = selectedImage.uri,
                    contentDescription = stringResource(R.string.selected_image_description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Skip button
                OutlinedButton(
                    onClick = onSkip,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text(
                        text = stringResource(R.string.skip_button),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                // Ban button
                Button(
                    onClick = onBan,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text(
                        text = stringResource(R.string.ban_button),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Image info (optional)
            Text(
                text = selectedImage.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (selectedImage.size > 0) {
                Text(
                    text = "${(selectedImage.size / 1024 / 1024).toInt()} MB",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}