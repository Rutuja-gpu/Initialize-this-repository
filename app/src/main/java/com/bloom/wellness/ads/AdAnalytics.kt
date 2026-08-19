package com.bloom.wellness.ads

import android.util.Log

/**
 * Minimal ad-event tracker for the optional analytics bonus. Logs locally via Logcat so
 * events are visible during development/QA; swap [AdAnalytics.logger] for a Firebase
 * Analytics (or any other) sink in a real app without touching call sites.
 */
object AdAnalytics {

    private const val TAG = "BloomAdAnalytics"

    var logger: (event: String, params: Map<String, Any?>) -> Unit = { event, params ->
        Log.d(TAG, "$event ${params.takeIf { it.isNotEmpty() } ?: ""}")
    }

    private fun log(event: String, params: Map<String, Any?> = emptyMap()) = logger(event, params)

    fun appOpenAdRequested() = log("app_open_ad_requested")
    fun appOpenAdLoaded() = log("app_open_ad_loaded")
    fun appOpenAdFailed(reason: String) = log("app_open_ad_failed", mapOf("reason" to reason))
    fun appOpenAdShown() = log("app_open_ad_shown")
    fun appOpenAdDismissed() = log("app_open_ad_dismissed")

    fun bannerAdLoaded() = log("banner_ad_loaded")
    fun bannerAdFailed(reason: String) = log("banner_ad_failed", mapOf("reason" to reason))
    fun bannerAdImpression() = log("banner_ad_impression")
}
