package com.example.sparin

import android.app.Application
import com.example.sparin.di.appModule
import com.example.sparin.di.firebaseModule
import com.example.sparin.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Application class untuk inisialisasi Koin DI dan Firebase
 */
class SparINApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@SparINApplication)
            modules(
                appModule,
                firebaseModule,
                repositoryModule
            )
        }
    }
}
