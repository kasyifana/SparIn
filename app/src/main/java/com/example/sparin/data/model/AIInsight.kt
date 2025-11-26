package com.example.sparin.data.model

/**
 * AIInsight data class untuk AI profile analysis
 */
data class AIInsight(
    val userId: String = "",
    val lastAnalysis: Long = System.currentTimeMillis(),
    val insights: List<String> = emptyList(),
    val recommendations: List<String> = emptyList(), // List of room IDs
    val performanceTrend: String = "stable", // improving, stable, declining
    val suggestedTraining: List<String> = emptyList()
)
