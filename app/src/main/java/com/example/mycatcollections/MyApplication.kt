package com.example.mycatcollections

import android.app.Application
import com.example.mycatcollections.di.koinModule
import com.example.mycatcollections.diviewmodel.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(koinModule, viewModelModule)
        }
    }
}