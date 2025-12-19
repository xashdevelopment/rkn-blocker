package rkn.blocktoday.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rkn.blocktoday.domain.model.ImageItem
import rkn.blocktoday.domain.repository.FolderRepository
import rkn.blocktoday.domain.repository.BanRepository

class FolderRepositoryImpl(
    private val context: Context
) : FolderRepository {
    
    companion object {
        private const val FOLDER_URI_KEY = "selected_folder_uri"
        private const val FOLDER_PERMISSION_KEY = "folder_permission_granted"
        
        private val SUPPORTED_MIME_TYPES = setOf(
            "image/png",
            "image/jpeg", 
            "image/jpg",
            "image/webp"
        )
    }
    
    override fun isFolderSelected(): Boolean {
        return getSelectedFolderUri() != null && checkPermissions()
    }
    
    override fun getSelectedFolderUri(): String? {
        val prefs = context.getSharedPreferences("block_today_prefs", Context.MODE_PRIVATE)
        return prefs.getString(FOLDER_URI_KEY, null)
    }
    
    override fun checkPermissions(): Boolean {
        val prefs = context.getSharedPreferences("block_today_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean(FOLDER_PERMISSION_KEY, false)
    }
    
    override suspend fun loadImagesFromFolder(banRepository: BanRepository): List<ImageItem> = withContext(Dispatchers.IO) {
        val folderUri = getSelectedFolderUri() ?: return@withContext emptyList()
        
        try {
            val documentFile = DocumentFile.fromTreeUri(context, Uri.parse(folderUri))
            if (documentFile == null || !documentFile.exists() || !documentFile.isDirectory) {
                return@withContext emptyList()
            }
            
            val bannedImages = banRepository.getBannedImages()
            
            val images = mutableListOf<ImageItem>()
            
            documentFile.listFiles().forEach { file ->
                if (file.isFile && file.name != null && 
                    SUPPORTED_MIME_TYPES.contains(file.type)) {
                    
                    val imageItem = ImageItem(
                        uri = file.uri.toString(),
                        displayName = file.name!!,
                        size = file.length(),
                        mimeType = file.type ?: ""
                    )
                    
                    if (!bannedImages.contains(imageItem.id)) {
                        images.add(imageItem)
                    }
                }
            }
            
            images
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Save folder URI and mark permissions as granted
     */
    fun saveFolderSelection(uri: String) {
        val prefs = context.getSharedPreferences("block_today_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putString(FOLDER_URI_KEY, uri)
            .putBoolean(FOLDER_PERMISSION_KEY, true)
            .apply()
    }
    
    /**
     * Clear folder selection and permissions
     */
    fun clearFolderSelection() {
        val prefs = context.getSharedPreferences("block_today_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .remove(FOLDER_URI_KEY)
            .putBoolean(FOLDER_PERMISSION_KEY, false)
            .apply()
    }
}