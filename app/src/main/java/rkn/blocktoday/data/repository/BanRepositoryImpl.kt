package rkn.blocktoday.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.set
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rkn.blocktoday.domain.repository.BanRepository

class BanRepositoryImpl(
    private val context: Context
) : BanRepository {
    
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ban_preferences")
        private val BANNED_IMAGES_KEY = longPreferencesKey("banned_images_count")
    }
    
    // Using a simple approach with a counter and individual keys for banned images
    private suspend fun getBannedImageKeys(): List<Preferences.Key<String>> {
        val prefs = context.dataStore.data.map { it }.firstOrNull() ?: return emptyList()
        return prefs.asMap().keys.filter { it.name.startsWith("banned_image_") }
            .map { it as Preferences.Key<String> }
    }
    
    override suspend fun getBannedImages(): Set<String> {
        return context.dataStore.data.map { preferences ->
            preferences.asMap().entries
                .filter { it.key.name.startsWith("banned_image_") }
                .mapNotNull { it.value as? String }
                .toSet()
        }.firstOrNull() ?: emptySet()
    }
    
    override suspend fun banImage(imageId: String) {
        context.dataStore.edit { preferences ->
            val keyName = "banned_image_${imageId.hashCode()}"
            preferences[Preferences.Key<String>(keyName)] = imageId
        }
    }
    
    override suspend fun clearBans() {
        context.dataStore.edit { preferences ->
            val bannedKeys = preferences.asMap().keys
                .filter { it.name.startsWith("banned_image_") }
            
            bannedKeys.forEach { key ->
                preferences.remove(key)
            }
        }
    }
    
    override suspend fun isImageBanned(imageId: String): Boolean {
        return getBannedImages().contains(imageId)
    }
}