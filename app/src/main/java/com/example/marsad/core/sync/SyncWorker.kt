package com.example.marsad.core.sync

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkerParameters
import com.example.marsad.data.database.localdatasources.WeatherDetailsLocalDataSource
import com.example.marsad.data.network.remotesource.WeatherRemoteDataSource
import com.example.marsad.data.repositories.UserPrefsRepository
import com.example.marsad.data.repositories.WeatherDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

class SyncWorker(
    context: Context,
    workerParameters: WorkerParameters,
//    private val weatherDetailsRepository: WeatherDetailsRepositoryInterface,
//    private val userPrefsRepository: UserPrefsRepository,
) : CoroutineWorker(context, workerParameters) {

    val weatherDetailsRepository = WeatherDetailsRepository.getInstance(
        WeatherRemoteDataSource, WeatherDetailsLocalDataSource(applicationContext)
    )
    val userPrefsRepository = UserPrefsRepository.getInstance(applicationContext)

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        Log.d(TAG, "doWork:  Started ")
        val syncSuccessfully: Boolean = awaitAll(
            async {
                val lat = userPrefsRepository.prefs.value.lat
                val lon = userPrefsRepository.prefs.value.lon
                val lang = userPrefsRepository.prefs.value.language
                Log.d(TAG, "doWork: Caching Started ")
                weatherDetailsRepository.syncWeatherDetails(lat, lon, lang)
            }
        ).all {
            Log.i(TAG, "doWork: Caching Finished ")
            it
        }

        if (syncSuccessfully) {
            Result.Success()
        } else {
            Result.retry()
        }
    }

    companion object {
        /**
         * Sync Data on Startup
         */
        private const val TAG = "SyncWorker"
        private const val WORKER_CLASS_NAME = "SyncWorker"
        private val syncConstraints
            get() = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        private fun KClass<out CoroutineWorker>.delegatedData() =
            Data.Builder()
                .putString(WORKER_CLASS_NAME, qualifiedName)
                .build()

        fun startUpSyncWork(): OneTimeWorkRequest {
            Log.d(TAG, "startUpSyncWork")
            return OneTimeWorkRequest.Builder(SyncWorker::class.java)
                .setConstraints(syncConstraints)
                .build()
        }
    }
}

