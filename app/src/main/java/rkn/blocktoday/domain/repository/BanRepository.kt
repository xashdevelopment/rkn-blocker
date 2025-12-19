package rkn.blocktoday.domain.repository

/**
 * Repository for managing banned images
 */
interface BanRepository {
    /**
     * Get the set of banned image IDs for the current folder
     */
    suspend fun getBannedImages(): Set<String>
    
    /**
     * Add an image to the banned list
     */
    suspend fun banImage(imageId: String)
    
    /**
     * Clear all bans for the current folder
     */
    suspend fun clearBans()
    
    /**
     * Check if an image is banned
     */
    suspend fun isImageBanned(imageId: String): Boolean
}