package com.example.appstarterkit.startup

import android.content.Context
import androidx.startup.Initializer
import timber.log.Timber

/**
 * Analytics Initializer Example
 * Demonstrates how to create a custom startup initializer for analytics services
 * 
 * This is an example initializer that can be integrated when needed.
 * To enable, add this to AndroidManifest.xml:
 * <meta-data
 *     android:name="androidx.startup"
 *     android:value="com.example.appstarterkit.startup.AnalyticsInitializer" />
 */
class AnalyticsInitializer : Initializer<AnalyticsService> {

    override fun create(context: Context): AnalyticsService {
        Timber.d("Initializing Analytics...")

        return AnalyticsService.initialize(context).also {
            Timber.d("Analytics initialized")
        }
    }

    /**
     * Define dependencies
     * Analytics should be initialized after Timber for logging
     */
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }
}

/**
 * Analytics Service (Example)
 * 
 * This is a placeholder class demonstrating how a service could be structured.
 * In a real app, this would integrate with services like:
 * - Firebase Analytics
 * - Crashlytics
 * - Mixpanel
 * - Amplitude
 * etc.
 */
object AnalyticsService {

    private var instance: AnalyticsService? = null

    fun initialize(context: Context): AnalyticsService {
        if (instance == null) {
            // Initialize your analytics service here
            // Example:
            // FirebaseAnalytics.getInstance(context)
            
            instance = this
        }
        return this
    }

    fun trackEvent(eventName: String, params: Map<String, Any>? = null) {
        // Track analytics event
        // Example:
        // firebaseAnalytics.logEvent(eventName, params)
        Timber.d("Analytics event: $eventName, params: $params")
    }

    fun setUserProperty(propertyName: String, value: String) {
        // Set user property
        // Example:
        // firebaseAnalytics.setUserProperty(propertyName, value)
        Timber.d("Analytics property: $propertyName = $value")
    }

    fun setUserId(userId: String?) {
        // Set user ID
        // Example:
        // firebaseAnalytics.setUserId(userId)
        Timber.d("Analytics user ID: $userId")
    }
}
