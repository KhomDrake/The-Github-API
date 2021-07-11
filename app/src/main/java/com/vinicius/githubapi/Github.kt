package com.vinicius.githubapi

import android.app.Application
import com.vinicius.githubapi.di.networkModule
import com.vinicius.githubapi.di.repositoryModule
import com.vinicius.githubapi.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Github : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Github)
            modules(
                listOf(
                    networkModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }

}