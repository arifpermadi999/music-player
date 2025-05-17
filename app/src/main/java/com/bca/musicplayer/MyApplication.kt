package com.bca.musicplayer

import com.bca.musicplayer.di.networkModule
import com.bca.musicplayer.di.repositoryModule
import com.bca.musicplayer.di.useCaseModule
import com.bca.musicplayer.di.viewModelModule
import com.google.android.play.core.splitcompat.SplitCompatApplication

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber



class MyApplication : SplitCompatApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin{
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule)
        }
    }

}