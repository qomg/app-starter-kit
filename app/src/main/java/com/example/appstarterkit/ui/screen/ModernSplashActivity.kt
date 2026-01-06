package com.example.appstarterkit.ui.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.fillMaxSize
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.appstarterkit.MainActivity
import com.example.appstarterkit.ui.theme.AppStarterKitTheme

/**
 * Modern Splash Screen using androidx.core.splashscreen
 * 
 * Benefits of using core-splashscreen:
 * - Shows on app start automatically
 * - No separate activity needed
 * - Seamless transition to main activity
 * - Better performance
 */
class ModernSplashActivity : ComponentActivity() {

    companion object {
        private const val SPLASH_DELAY_MS = 2000L
        private const val FADE_IN_DURATION_MS = 600L
        private const val FADE_OUT_DURATION_MS = 400L
    }

    private val contentAlpha = mutableFloatStateOf(0f)
    private val logoScale = mutableFloatStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set content with splash screen
        setContent {
            AppStarterKitTheme {
                ModernSplashContent(
                    contentAlpha = contentAlpha.value,
                    logoScale = logoScale.value
                )
            }
        }

        // Animate splash screen
        lifecycleScope.launch {
            // Fade in logo
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = FADE_IN_DURATION_MS.toInt(),
                    easing = FastOutSlowInEasing
                )
            ) { value, _ -> logoScale.value = value }
            
            // Fade in content
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = FADE_IN_DURATION_MS.toInt(),
                    easing = FastOutSlowInEasing
                )
            ) { value, _ -> contentAlpha.value = value }

            // Wait for splash duration
            delay(SPLASH_DELAY_MS)

            // Navigate to main activity
            navigateToMain()
        }
    }

    @Composable
    fun ModernSplashContent(
        contentAlpha: Float,
        logoScale: Float
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF6750A4)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                // App Logo
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(128.dp)
                        .scale(logoScale),
                    colorFilter = ColorFilter.tint(Color.White)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // App Name with fade effect
                Text(
                    text = "AppStarterKit",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .alpha(contentAlpha)
                )
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
