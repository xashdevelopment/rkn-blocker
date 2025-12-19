package rkn.blocktoday.domain.repository

import android.content.Context
import rkn.blocktoday.domain.model.ImageItem
import rkn.blocktoday.domain.model.AppState

/**
 * Repository for managing folder selection and image enumeration
 */
interface FolderRepository {
    /**
     * Check if a folder is selected and accessible
     */
    fun isFolderSelected(): Boolean
    
    /**
     * Get the currently selected folder URI
     */
    fun getSelectedFolderUri(): String?
    
    /**
     * Load images from the selected folder, filtering out banned ones
     */
    suspend fun loadImagesFromFolder(banRepository: BanRepository): List<ImageItem>
    
    /**
     * Check if permissions are still valid for the selected folder
     */
    fun checkPermissions(): Boolean
}