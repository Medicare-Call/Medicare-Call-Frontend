package com.konkuk.medicarecall

import dagger.hilt.android.HiltAndroidApp
import android.app.Application

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
