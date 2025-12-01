package com.example.sparin.presentation.discover

import androidx.compose.ui.graphics.Color
import com.example.sparin.ui.theme.*

/**
 * Helper function to get emoji for a sport category
 */
fun getEmojiForCategory(category: String): String {
    return when (category) {
        "Badminton" -> "🏸"
        "Futsal" -> "⚽"
        "Basket" -> "🏀"
        "Voli" -> "🏐"
        "Tennis" -> "🎾"
        "Hiking" -> "🥾"
        "Cycling" -> "🚴"
        "Swimming" -> "🏊"
        "Muaythai" -> "🥋"
        "Boxing" -> "🥊"
        else -> "🌟"
    }
}

/**
 * Helper function to get color for a sport category
 */
fun getColorForCategory(category: String): Color {
    return when (category) {
        "Badminton" -> MintBreeze
        "Futsal" -> SkyMist
        "Basket" -> PeachGlow
        "Voli" -> SoftLavender
        else -> MintBreeze
    }
}
