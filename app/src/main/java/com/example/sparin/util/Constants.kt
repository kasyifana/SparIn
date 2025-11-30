package com.example.sparin.util

/**
 * Constants untuk aplikasi SparIN
 */
object Constants {
    
    // Firebase Configuration
    object Firebase {
        // Web Client ID dari Firebase Console (client_type: 3 di google-services.json)
        // Updated: 2025-11-30 - Verified from Firebase Console
        const val WEB_CLIENT_ID = "565058219412-9gj8dgjon4cq512oos1noqt227ngo0m6.apps.googleusercontent.com"
    }
    
    // Firestore Collections
    object Collections {
        const val USERS = "users"
        const val ROOMS = "rooms"
        const val COMMUNITIES = "communities"
        const val CAMPAIGNS = "campaigns"
        const val MATCHES = "matches"
        const val CHATS = "chats"
        const val MESSAGES = "messages"
        const val AI_INSIGHTS = "aiInsights"
    }
    
    // SharedPreferences Keys
    object Prefs {
        const val PREF_NAME = "sparin_prefs"
        const val KEY_USER_ID = "user_id"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
    
    // Sport Categories
    object SportCategories {
        // Racket & Ball Sports
        const val BADMINTON = "Badminton"
        const val FUTSAL = "Futsal"
        const val BASKET = "Basket"
        const val VOLI = "Voli"
        const val TENIS_MEJA = "Tenis Meja"
        const val TENIS = "Tenis"
        const val PADEL = "Padel"
        const val GOLF = "Golf"
        const val SEPAK_BOLA = "Sepak Bola"
        const val MINI_SOCCER = "Mini Soccer"
        
        // Endurance Sports
        const val JOGGING = "Jogging"
        const val LARI = "Lari"
        const val SEPEDA = "Sepeda"
        const val RENANG = "Renang"
        
        // Strength & Combat
        const val GYM = "Gym"
        const val BOXING = "Boxing"
        const val MUAYTHAI = "Muay Thai"
        const val TAEKWONDO = "Taekwondo"
        
        // Recreation
        const val BILLIARD = "Billiard"
        const val CATUR = "Catur"
        const val HIKING = "Hiking"
        const val BOWLING = "Bowling"
        
        val ALL_CATEGORIES = listOf(
            // Racket & Ball
            BADMINTON, FUTSAL, BASKET, VOLI, TENIS_MEJA, TENIS, 
            PADEL, GOLF, SEPAK_BOLA, MINI_SOCCER,
            // Endurance
            JOGGING, LARI, SEPEDA, RENANG,
            // Strength & Combat
            GYM, BOXING, MUAYTHAI, TAEKWONDO,
            // Recreation
            BILLIARD, CATUR, HIKING, BOWLING
        )
    }
    
    // Skill Levels
    object SkillLevel {
        const val BEGINNER = "Beginner"
        const val INTERMEDIATE = "Intermediate"
        const val ADVANCED = "Advanced"
        
        val ALL_LEVELS = listOf(BEGINNER, INTERMEDIATE, ADVANCED)
    }
    
    // Room Modes
    object RoomMode {
        const val CASUAL = "Casual"
        const val COMPETITIVE = "Competitive"
    }
    
    // Gender Options
    object Gender {
        const val MALE = "Pria"
        const val FEMALE = "Wanita"
        const val OTHER = "Lainnya"
        
        val ALL_GENDERS = listOf(MALE, FEMALE, OTHER)
    }
    
    // Subscription Types
    object Subscription {
        const val FREE = "free"
        const val PREMIUM = "premium"
    }
    
    // Default Values
    object Defaults {
        const val DEFAULT_PROFILE_PHOTO = ""
        const val DEFAULT_BIO = "Saya pecinta olahraga dan ingin menemukan partner sparring!"
        const val DEFAULT_WINRATE = 0.0
        const val DEFAULT_TOTAL_MATCHES = 0
        const val DEFAULT_POINTS = 0
        const val DEFAULT_RANK = "Rookie"
    }
}
