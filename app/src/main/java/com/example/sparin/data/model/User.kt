package com.example.sparin.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

/**
 * User data class
 */
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val city: String = "",
    val gender: String = "",
    val age: Int? = null,
    val sportInterests: List<String> = emptyList(),
    val skillLevel: String = "",
    val profilePhoto: String = "",
    val bio: String = "",
    val stats: UserStats = UserStats(),
    val joinedCommunities: List<String> = emptyList(),
    val joinedRooms: List<String> = emptyList(),
    val subscription: String = "free",
    val createdAt: Long = System.currentTimeMillis(),
    val lastSeen: Long = System.currentTimeMillis()
)

/**
 * User Stats nested data class
 */
data class UserStats(
    val winrate: Double = 0.0,
    val totalMatches: Int = 0,
    val totalWins: Int = 0,
    val totalLosses: Int = 0,
    val points: Int = 0,
    val rank: String = "Rookie",
    val elo: Int = 1000
)
