package rkn.blocktoday.domain.model

/**
 * Represents the current application state
 */
data class AppState(
    val selectedFolderUri: String? = null,
    val images: List<ImageItem> = emptyList(),
    val bannedImages: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedImage: ImageItem? = null
) {
    val filteredImages: List<ImageItem>
        get() = images.filter { it.id !in bannedImages }
    
    val hasImages: Boolean
        get() = filteredImages.isNotEmpty()
    
    val allImagesBanned: Boolean
        get() = images.isNotEmpty() && filteredImages.isEmpty()
}