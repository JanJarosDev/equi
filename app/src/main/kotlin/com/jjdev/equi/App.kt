package com.jjdev.equi

import android.app.Application
import com.jjdev.equi.core.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@SuppressWarnings("deprecation")
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
