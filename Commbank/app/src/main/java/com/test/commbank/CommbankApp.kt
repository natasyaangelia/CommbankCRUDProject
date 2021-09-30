package com.test.commbank

import android.app.Application
import android.content.Context
import com.test.commbank.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import androidx.multidex.MultiDex

class CommbankApp : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: CommbankApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CommbankApp)
            modules(
                listOf(
                    viewModelModule,
                    apiRepositoryModule,
                    remoteModule,
                    apiModule
                )
            )
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}