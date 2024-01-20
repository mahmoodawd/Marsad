package com.example.marsad

import android.app.Application
import androidx.work.WorkManager
import com.example.marsad.core.sync.SyncWorker

class MarsadApplication : Application() {
    companion object {
        private const val TAG = "MarsadApp"
    }

    override fun onCreate() {
        super.onCreate()

        WorkManager.getInstance(applicationContext)
            .enqueue(SyncWorker.startUpSyncWork())

    }
}

const val SyncWorkName = "SyncWorkName"