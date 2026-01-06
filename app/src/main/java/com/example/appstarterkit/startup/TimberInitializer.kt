package com.example.appstarterkit.startup

import android.content.Context
import androidx.startup.Initializer
import timber.log.Timber

/**
 * Timber Initializer
 * Initializes Timber logging library at app startup
 */
class TimberInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (com.example.appstarterkit.BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber initialized")
        } else {
            // Release tree implementation can be added here
            // For example: Crashlytics tree, Firebase Analytics, etc.
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    // Production logging implementation
                }
            })
        }
    }

    /**
     * Define dependencies to ensure other initializers run before Timber
     */
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList() // Timber has no dependencies
    }
}
