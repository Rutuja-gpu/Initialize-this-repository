package com.bloom.wellness.ads

import com.bloom.wellness.BuildConfig

/**
 * Central place for every AdMob ad unit ID used by the app.
 *
 * All IDs below are Google's official public test IDs (safe to ship, never earn real
 * revenue, always fill). Swap them for the real AdMob ad unit IDs before a production
 * release — ideally by wiring [BuildConfig.DEBUG] (or a build-flavor field) to select
 * between test and production IDs, exactly as done here, rather than hardcoding either
 * one at every call site.
 */
object AdConfig {

    private const val TEST_APP_OPEN_AD_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"
    private const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741"

    // TODO: replace with your real AdMob ad unit IDs before a release build.
    private const val PROD_APP_OPEN_AD_UNIT_ID = TEST_APP_OPEN_AD_UNIT_ID
    private const val PROD_BANNER_AD_UNIT_ID = TEST_BANNER_AD_UNIT_ID

    val appOpenAdUnitId: String
        get() = if (BuildConfig.DEBUG) TEST_APP_OPEN_AD_UNIT_ID else PROD_APP_OPEN_AD_UNIT_ID

    val bannerAdUnitId: String
        get() = if (BuildConfig.DEBUG) TEST_BANNER_AD_UNIT_ID else PROD_BANNER_AD_UNIT_ID

    /** Max age of a preloaded App Open Ad before it's discarded and reloaded, per AdMob policy. */
    const val APP_OPEN_AD_MAX_CACHE_DURATION_MS = 4L * 60L * 60L * 1000L // 4 hours

    /** Upper bound on how long the splash screen will wait for a first App Open Ad load. */
    const val APP_OPEN_AD_LOAD_TIMEOUT_MS = 6_000L
}
