package com.example.appstarterkit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Application class for AppStarterKit
 * 
 * This class is integrated with AndroidX Startup library.
 * Timber and other components are initialized via Initializers.
 * 
 * Startup Order:
 * 1. TimberInitializer - Initializes logging
 * 2. DataStoreInitializer - Initializes preferences
 * 3. RoomInitializer - Initializes database
 * 4. WorkManagerInitializer - Initializes WorkManager with Hilt
 */
@HiltAndroidApp
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Timber is now initialized via Startup Initializer
        // No need to initialize it here

        Timber.d("AppStarterKit Application started")

        // Log startup performance if debug build
        if (BuildConfig.DEBUG) {
            // Access AppStartupConfig to check initialization timings
            // This can be used for debugging startup performance
            Timber.d("Debug mode: Startup performance logging enabled")
        }
    }
}
