package com.mayuresh.newsstation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Enabling debug logs.
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.tag("NewsStation")
        } else {
            Timber.uprootAll()
        }
    }
}