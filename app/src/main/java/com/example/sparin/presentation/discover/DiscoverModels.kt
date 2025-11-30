package com.example.sparin.presentation.discover

import androidx.compose.ui.graphics.Color

/**
 * Shared models for Discover feature
 */

// Room mode enum
enum class RoomMode {
    CASUAL, COMPETITIVE
}

// Sport categories
data class DiscoverSportCategory(
    val name: String,
    val emoji: String
)

// Skill level enum
enum class SkillLevel(val label: String, val color: Color) {
    BEGINNER("Beginner", Color(0xFF4ECDC4)),
    INTERMEDIATE("Intermediate", Color(0xFFFFBE0B)),
    ADVANCED("Advanced", Color(0xFFFF006E))
}
