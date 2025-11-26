package com.example.sparin.di

import com.example.sparin.data.remote.FirebaseAuthService
import com.example.sparin.data.remote.FirestoreService
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import org.koin.dsl.module

/**
 * Firebase Module untuk Firebase dependencies
 */
val firebaseModule = module {
    
    // Firebase Auth
    single { FirebaseAuth.getInstance() }
    
    // Firebase Firestore
    single { Firebase.firestore }
    
    // Firebase Storage
    single { Firebase.storage }
    
    // Firebase Auth Service
    single { FirebaseAuthService(get()) }
    
    // Firestore Service
    single { FirestoreService(get()) }
}
