package rkn.blocktoday.presentation.components

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import rkn.blocktoday.domain.model.ImageItem
import rkn.blocktoday.domain.repository.BanRepository
import rkn.blocktoday.domain.repository.FolderRepository
import rkn.blocktoday.presentation.SpinnerUiState

class SpinnerViewModel(
    application: Application,
    private val folderRepository: FolderRepository,
    private val banRepository: BanRepository
) : AndroidViewModel(application) {
    
    private val _uiState = MutableStateFlow(SpinnerUiState())
    val uiState: StateFlow<SpinnerUiState> = _uiState.asStateFlow()
    
    init {
        loadImages()
    }
    
    fun reloadImages() {
        loadImages()
    }
    
    fun startSpin(selectedIndex: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSpinning = true)
            
            // Simulate 10-second spin animation
            kotlinx.coroutines.delay(10000)
            
            _uiState.value = _uiState.value.copy(isSpinning = false)
        }
    }
    
    private fun loadImages() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                if (!folderRepository.isFolderSelected()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        filteredImages = emptyList()
                    )
                    return@launch
                }
                
                val images = folderRepository.loadImagesFromFolder(banRepository)
                val bannedImages = banRepository.getBannedImages()
                val filteredImages = images.filter { it.id !in bannedImages }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    filteredImages = filteredImages,
                    allImagesBanned = images.isNotEmpty() && filteredImages.isEmpty()
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Ошибка загрузки изображений: ${e.message}"
                )
            }
        }
    }
}