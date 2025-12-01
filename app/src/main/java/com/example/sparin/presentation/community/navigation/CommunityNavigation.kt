package com.example.sparin.presentation.community.navigation

/**
 * Navigation routes for Community feature
 */
object CommunityRoutes {
    const val COMMUNITY_SCREEN = "community"
    const val CHAT_SCREEN = "chat/{communityName}/{communityEmoji}"
    
    fun chatScreen(communityName: String, communityEmoji: String): String {
        return "chat/$communityName/$communityEmoji"
    }
}
