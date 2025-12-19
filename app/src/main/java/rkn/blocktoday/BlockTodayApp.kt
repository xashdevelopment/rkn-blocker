package rkn.blocktoday

import android.app.Application
import rkn.blocktoday.data.repository.BanRepositoryImpl
import rkn.blocktoday.data.repository.FolderRepositoryImpl
import rkn.blocktoday.domain.repository.BanRepository
import rkn.blocktoday.domain.repository.FolderRepository

class BlockTodayApp : Application() {
    
    private lateinit var _folderRepository: FolderRepository
    private lateinit var _banRepository: BanRepository
    
    val folderRepository: FolderRepository
        get() = _folderRepository
    
    val banRepository: BanRepository
        get() = _banRepository
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize repositories
        _folderRepository = FolderRepositoryImpl(this)
        _banRepository = BanRepositoryImpl(this)
    }
}