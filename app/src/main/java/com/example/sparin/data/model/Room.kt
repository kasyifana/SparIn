package com.example.sparin.data.model

import com.google.firebase.firestore.GeoPoint

/**
 * Room data class untuk sparring room
 */
data class Room(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val mode: String = "Casual", // Casual atau Competitive
    val location: GeoPoint? = null,
    val locationName: String = "",
    val maxPlayers: Int = 2,
    val currentPlayers: Int = 1,
    val members: List<String> = emptyList(), // List of user IDs
    val price: Double? = null,
    val dateTime: Long = 0,
    val status: String = "open", // open, full, completed, cancelled
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val description: String = ""
)
