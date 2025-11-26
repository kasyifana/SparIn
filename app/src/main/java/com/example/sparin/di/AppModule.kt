package com.example.sparin.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * App Module untuk dependencies umum
 */
val appModule = module {
    
    // Google Sign In Client
    single {
        provideGoogleSignInClient(androidContext())
    }
    
    // SharedPreferences
    single {
        androidContext().getSharedPreferences("sparin_prefs", Context.MODE_PRIVATE)
    }
}

private fun provideGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("565058219412-uu83h67t8q6k6lpe8s7rp6bb39djqhqa.apps.googleusercontent.com")
        .requestEmail()
        .build()
    return GoogleSignIn.getClient(context, gso)
}
