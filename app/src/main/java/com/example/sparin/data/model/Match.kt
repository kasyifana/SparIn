package com.example.sparin.data.model

/**
 * Match data class untuk match history dan results
 */
data class Match(
    val id: String = "",
    val roomId: String = "",
    val participants: List<String> = emptyList(), // List of user IDs
    val winnerId: String? = null,
    val loserId: String? = null,
    val score: String = "",
    val category: String = "",
    val mode: String = "Casual", // Casual atau Competitive
    val dateTime: Long = System.currentTimeMillis(),
    val notes: String = "",
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
