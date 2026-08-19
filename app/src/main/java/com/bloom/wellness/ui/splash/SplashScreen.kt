package com.bloom.wellness.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.bloom.wellness.BloomApplication
import com.bloom.wellness.ui.components.BloomFlowerLogo
import com.bloom.wellness.ui.components.BloomSparkleField
import com.bloom.wellness.ui.theme.BloomCream
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

/** Minimum time the brand mark stays on screen, so it doesn't just flash by. */
private const val MIN_SPLASH_DURATION_MS = 600L

/** Cap on how long we'll hold on the splash waiting for a returning-user App Open Ad to preload. */
private const val AD_WAIT_DURING_SPLASH_MS = 2500L

/**
 * Splash screen: decides "first launch" vs "returning user" from persisted onboarding
 * state, and — only for returning users — gives a freshly-requested App Open Ad a short
 * window to preload before handing off to Home, without ever blocking indefinitely.
 */
@Composable
fun SplashScreen(onNavigateToOnboarding: () -> Unit, onNavigateToHome: () -> Unit) {
    val app = LocalContext.current.applicationContext as BloomApplication

    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        val profile = app.preferencesRepository.profile.first()

        if (!profile.onboardingCompleted) {
            val elapsed = System.currentTimeMillis() - startTime
            if (elapsed < MIN_SPLASH_DURATION_MS) delay(MIN_SPLASH_DURATION_MS - elapsed)
            onNavigateToOnboarding()
            return@LaunchedEffect
        }

        app.appOpenAdManager.setAdsEnabled(true)
        withTimeoutOrNull(AD_WAIT_DURING_SPLASH_MS) {
            kotlinx.coroutines.suspendCancellableCoroutine<Unit> { cont ->
                app.appOpenAdManager.loadAd { if (cont.isActive) cont.resumeWith(Result.success(Unit)) }
            }
        }
        val elapsed = System.currentTimeMillis() - startTime
        if (elapsed < MIN_SPLASH_DURATION_MS) delay(MIN_SPLASH_DURATION_MS - elapsed)

        onNavigateToHome()
        app.appOpenAdManager.showAdIfAvailable {}
    }

    Box(modifier = Modifier.fillMaxSize().background(BloomCream), contentAlignment = Alignment.Center) {
        BloomSparkleField(modifier = Modifier.fillMaxSize())
        BloomFlowerLogo()
    }
}
