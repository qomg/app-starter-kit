package com.example.appstarterkit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.appstarterkit.MainActivity

/**
 * Splash Activity
 * Serves as the app entry point with splash screen
 * After splash screen duration, it launches MainActivity
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Install splash screen
        // This will show the splash screen defined in theme
        installSplashScreen()

        // Navigate to MainActivity after splash screen duration
        // The splash screen will automatically dismiss after the duration
        // defined in theme (or when we call setKeepOnScreenCondition)
        startMainActivity()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
