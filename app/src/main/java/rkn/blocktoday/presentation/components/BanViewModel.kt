package rkn.blocktoday.presentation.components

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import rkn.blocktoday.domain.repository.BanRepository

class BanViewModel(
    application: Application,
    private val banRepository: BanRepository
) : AndroidViewModel(application) {
    
    fun banImage(imageId: String) {
        viewModelScope.launch {
            banRepository.banImage(imageId)
        }
    }
    
    fun clearBans() {
        viewModelScope.launch {
            banRepository.clearBans()
        }
    }
}