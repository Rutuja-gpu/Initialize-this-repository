package com.bloom.wellness.ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.SystemClock
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.lang.ref.WeakReference

/**
 * Owns the whole App Open Ad lifecycle: preloading, cache-expiry, and deciding *when* it's
 * safe to show one.
 *
 * Two distinct triggers call into this class, and both are needed to satisfy the spec:
 *  - The splash flow ([Splash] screen) explicitly drives the very first show for a
 *    returning user, so it can control the "don't block navigation" timeout.
 *  - [ProcessLifecycleOwner] drives every subsequent background→foreground transition
 *    automatically, which Compose navigation has no visibility into.
 *
 * The very first [onStart] call always corresponds to the cold launch already being
 * handled by the splash flow, so it's intentionally ignored here to avoid a double show.
 */
class AppOpenAdManager(private val application: Application) :
    Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    private var isShowingAd = false
    private var loadTimeElapsedRealtime = 0L
    private var currentActivityRef: WeakReference<Activity>? = null

    /** Ads are only ever shown once onboarding has completed — never during first launch. */
    private var adsEnabled = false
    private var isColdStart = true

    init {
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun setAdsEnabled(enabled: Boolean) {
        adsEnabled = enabled
    }

    /** Requests a new ad if none is cached (or the cached one has expired). Safe to call repeatedly. */
    fun loadAd(onResult: (() -> Unit)? = null) {
        if (isLoadingAd || isAdAvailable()) {
            onResult?.invoke()
            return
        }
        isLoadingAd = true
        AdAnalytics.appOpenAdRequested()
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            application,
            AdConfig.appOpenAdUnitId,
            request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTimeElapsedRealtime = SystemClock.elapsedRealtime()
                    AdAnalytics.appOpenAdLoaded()
                    onResult?.invoke()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    isLoadingAd = false
                    // Covers "no internet" / no-fill / SDK errors alike — we simply have no ad to show.
                    AdAnalytics.appOpenAdFailed(error.message)
                    onResult?.invoke()
                }
            }
        )
    }

    private fun isAdAvailable(): Boolean {
        appOpenAd ?: return false
        val ageMs = SystemClock.elapsedRealtime() - loadTimeElapsedRealtime
        val expired = ageMs > AdConfig.APP_OPEN_AD_MAX_CACHE_DURATION_MS
        if (expired) appOpenAd = null
        return !expired
    }

    /**
     * Shows the preloaded ad over the current activity if — and only if — one is ready,
     * ads are enabled, and nothing is showing already. [onComplete] always fires exactly
     * once, whether or not an ad was actually shown, so callers never block on it.
     */
    fun showAdIfAvailable(onComplete: () -> Unit) {
        if (isShowingAd) {
            // Another show is already in flight — never stack two App Open Ads.
            onComplete()
            return
        }
        if (!adsEnabled || !isAdAvailable()) {
            onComplete()
            loadAd()
            return
        }
        val activity = currentActivityRef?.get()
        if (activity == null || activity.isFinishing) {
            onComplete()
            return
        }

        val ad = appOpenAd
        if (ad == null) {
            onComplete()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                AdAnalytics.appOpenAdShown()
            }

            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                isShowingAd = false
                AdAnalytics.appOpenAdDismissed()
                loadAd()
                onComplete()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                appOpenAd = null
                isShowingAd = false
                AdAnalytics.appOpenAdFailed(error.message)
                loadAd()
                onComplete()
            }
        }
        isShowingAd = true
        ad.show(activity)
    }

    // -- ProcessLifecycleOwner: fires on every app-level background -> foreground transition --

    override fun onStart(owner: LifecycleOwner) {
        if (isColdStart) {
            // The splash flow owns the first show explicitly; skip it here to avoid a duplicate.
            isColdStart = false
            return
        }
        if (!isShowingAd) {
            showAdIfAvailable {}
        }
    }

    // -- Application.ActivityLifecycleCallbacks: only used to know which Activity to show on --

    override fun onActivityResumed(activity: Activity) {
        currentActivityRef = WeakReference(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}
