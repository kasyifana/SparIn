package com.example.sparin.data.model

/**
 * Community data class untuk komunitas olahraga
 */
data class Community(
    val id: String = "",
    val name: String = "",
    val sportCategory: String = "",
    val description: String = "",
    val coverPhoto: String = "",
    val memberCount: Int = 0,
    val members: List<String> = emptyList(), // List of user IDs
    val admins: List<String> = emptyList(), // List of user IDs
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isPublic: Boolean = true
)
