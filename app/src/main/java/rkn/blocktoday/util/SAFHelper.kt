package rkn.blocktoday.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Helper class for Storage Access Framework operations
 */
object SAFHelper {
    
    /**
     * Create intent for folder selection
     */
    fun createFolderSelectionIntent(): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            // Show only directories that can be opened
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse("content://com.android.externalstorage.documents/root"))
        }
    }
    
    /**
     * Get folder URI from intent result
     */
    fun getFolderUriFromResult(data: Intent?): Uri? {
        return data?.data
    }
    
    /**
     * Persist permissions for the selected folder
     */
    fun persistFolderPermissions(context: Context, folderUri: Uri): Boolean {
        return try {
            val contentResolver = context.contentResolver
            contentResolver.takePersistableUriPermission(
                folderUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            true
        } catch (e: SecurityException) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Create activity result launcher for folder selection
     */
    fun createFolderSelectionLauncher(activity: Activity, callback: (Uri?) -> Unit) {
        val folderSelectionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val folderUri = getFolderUriFromResult(result.data)
                callback(folderUri)
            } else {
                callback(null)
            }
        }
        
        return folderSelectionLauncher
    }
}