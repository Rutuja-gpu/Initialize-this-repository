package com.bloom.wellness.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

/**
 * A full-width, adaptive anchored banner. Sized to the current screen width so it looks
 * right on phones and tablets alike (no hardcoded banner dimensions), and it collapses to
 * zero height if the ad fails to load so a missing ad never leaves a broken placeholder
 * in the middle of the wellness content.
 */
@Composable
fun BloomBannerAd(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    var loadFailed by remember { mutableStateOf(false) }
    val adSize = remember(screenWidthDp) {
        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidthDp)
    }

    val adView = remember(adSize) {
        AdView(context).apply {
            setAdSize(adSize)
            adUnitId = AdConfig.bannerAdUnitId
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    loadFailed = false
                    AdAnalytics.bannerAdLoaded()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    loadFailed = true
                    AdAnalytics.bannerAdFailed(error.message)
                }

                override fun onAdImpression() {
                    AdAnalytics.bannerAdImpression()
                }
            }
        }
    }

    DisposableEffect(adView, lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> adView.resume()
                Lifecycle.Event.ON_PAUSE -> adView.pause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        adView.loadAd(AdRequest.Builder().build())
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            adView.destroy()
        }
    }

    if (!loadFailed) {
        AndroidView(
            modifier = modifier.fillMaxWidth().height(adSize.height.dp),
            factory = { adView }
        )
    }
}
