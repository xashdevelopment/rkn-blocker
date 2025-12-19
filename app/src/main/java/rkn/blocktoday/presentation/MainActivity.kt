package rkn.blocktoday.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import rkn.blocktoday.R
import rkn.blocktoday.domain.model.ImageItem
import rkn.blocktoday.presentation.components.ImageSpinner
import rkn.blocktoday.presentation.components.SpinnerViewModel
import rkn.blocktoday.presentation.components.ViewModelFactory
import rkn.blocktoday.util.SAFHelper
import rkn.blocktoday.util.AnimationUtils

class MainActivity : ComponentActivity() {
    
    private val folderSelectionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                // Persist permissions and save folder selection
                val repository = (application as BlockTodayApp).folderRepository
                repository.saveFolderSelection(uri.toString())
                SAFHelper.persistFolderPermissions(this, uri)
                
                // Reload images
                viewModel.reloadImages()
            }
        }
    }
    
    private val viewModel: SpinnerViewModel by viewModels {
        val app = application as BlockTodayApp
        ViewModelFactory(application, app.folderRepository, app.banRepository)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                Surface {
                    MainScreen(
                        viewModel = viewModel,
                        onFolderSelect = { folderSelectionLauncher.launch(SAFHelper.createFolderSelectionIntent()) },
                        onImageSelected = { image ->
                            val intent = Intent(this, ResultActivity::class.java).apply {
                                putExtra("selected_image", image.uri)
                                putExtra("image_name", image.displayName)
                                putExtra("image_size", image.size)
                                putExtra("image_mime_type", image.mimeType)
                            }
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: SpinnerViewModel = viewModel(),
    onFolderSelect: () -> Unit,
    onImageSelected: (ImageItem) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Handle folder exhaustion
    LaunchedEffect(uiState.allImagesBanned) {
        if (uiState.allImagesBanned) {
            onFolderSelect()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        when {
            !uiState.hasImages -> {
                // Folder selection screen
                FolderSelectionScreen(
                    onSelectFolder = onFolderSelect
                )
            }
            else -> {
                // Main spinner screen
                SpinnerScreen(
                    uiState = uiState,
                    onStartSpin = {
                        val selectedIndex = AnimationUtils.selectRandomIndex(uiState.filteredImages.size)
                        viewModel.startSpin(selectedIndex)
                    },
                    onImageSelected = onImageSelected,
                    onChangeFolder = onFolderSelect
                )
            }
        }
        
        // Loading indicator
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        
        // Error message
        uiState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun FolderSelectionScreen(
    onSelectFolder: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = stringResource(R.string.select_folder_prompt),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onSelectFolder,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = stringResource(R.string.folder_selection_title),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun SpinnerScreen(
    uiState: SpinnerUiState,
    onStartSpin: () -> Unit,
    onImageSelected: (ImageItem) -> Unit,
    onChangeFolder: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar with title and folder change button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.main_title),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontStyle = FontStyle.Italic
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            TextButton(
                onClick = onChangeFolder,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.change_folder_button),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        
        // Spinner area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.hasImages) {
                ImageSpinner(
                    images = uiState.filteredImages,
                    isSpinning = uiState.isSpinning,
                    onSpinComplete = { selectedIndex ->
                        if (selectedIndex < uiState.filteredImages.size) {
                            onImageSelected(uiState.filteredImages[selectedIndex])
                        }
                    },
                    spinDuration = 10000L
                )
            } else {
                Text(
                    text = stringResource(R.string.no_images_found),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Start button
        if (uiState.hasImages && !uiState.isSpinning) {
            Button(
                onClick = onStartSpin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.start_spin),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

// UI State for Main Screen
data class SpinnerUiState(
    val filteredImages: List<ImageItem> = emptyList(),
    val isSpinning: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val allImagesBanned: Boolean = false
) {
    val hasImages: Boolean
        get() = filteredImages.isNotEmpty()
}