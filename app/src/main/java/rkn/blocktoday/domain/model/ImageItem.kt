package rkn.blocktoday.domain.model

/**
 * Data model representing an image in the selected folder
 * @param uri Content URI of the image
 * @param displayName Human-readable name of the file
 * @param size File size in bytes
 * @param mimeType MIME type of the image
 */
data class ImageItem(
    val uri: String,
    val displayName: String,
    val size: Long,
    val mimeType: String
) {
    val id: String
        get() = uri // Use URI as unique identifier for banned images
    
    val isImage: Boolean
        get() = mimeType.startsWith("image/")
}