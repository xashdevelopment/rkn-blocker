package rkn.blocktoday.presentation.components

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import rkn.blocktoday.domain.repository.BanRepository
import rkn.blocktoday.domain.repository.FolderRepository

class ViewModelFactory(
    private val application: Application,
    private val folderRepository: FolderRepository,
    private val banRepository: BanRepository
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpinnerViewModel::class.java)) {
            return SpinnerViewModel(application, folderRepository, banRepository) as T
        }
        if (modelClass.isAssignableFrom(BanViewModel::class.java)) {
            return BanViewModel(application, banRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}