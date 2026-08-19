package com.bloom.wellness

import android.app.Application
import com.bloom.wellness.ads.AppOpenAdManager
import com.bloom.wellness.data.BloomPreferencesRepository
import com.google.android.gms.ads.MobileAds

class BloomApplication : Application() {

    lateinit var preferencesRepository: BloomPreferencesRepository
        private set

    lateinit var appOpenAdManager: AppOpenAdManager
        private set

    override fun onCreate() {
        super.onCreate()
        preferencesRepository = BloomPreferencesRepository(this)
        appOpenAdManager = AppOpenAdManager(this)
        MobileAds.initialize(this)
    }
}
