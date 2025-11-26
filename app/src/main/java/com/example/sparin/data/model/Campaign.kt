package com.example.sparin.data.model

/**
 * Campaign data class untuk event/campaign olahraga
 */
data class Campaign(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val posterUrl: String = "",
    val organizerId: String = "",
    val organizerName: String = "",
    val dateTime: Long = 0,
    val location: String = "",
    val registrationFee: Double = 0.0,
    val participants: List<String> = emptyList(), // List of user IDs
    val maxParticipants: Int? = null,
    val status: String = "active", // active, completed, cancelled
    val category: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val registrationDeadline: Long? = null
)
