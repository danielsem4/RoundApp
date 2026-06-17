package org.example.roundapp

import android.app.Application
import org.example.roundapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class RoundAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin { androidContext(this@RoundAppApplication) }
    }
}
