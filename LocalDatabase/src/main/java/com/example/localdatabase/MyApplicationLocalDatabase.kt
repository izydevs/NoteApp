package com.example.localdatabase

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplicationLocalDatabase : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}