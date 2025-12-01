package com.example.sparin.presentation.navigation

/**
 * Sealed class untuk Screen routes
 */
sealed class Screen(val route: String) {
    // Onboarding & Auth
    object Onboarding : Screen("onboarding")
    object SignIn : Screen("sign_in")
    object Personalization : Screen("personalization")
    
    // Bottom Nav Screens
    object Home : Screen("home")
    object Community : Screen("community")
    object Discover : Screen("discover") // Now points to ModeSelector
    object DiscoverCasual : Screen("discover_casual")
    object DiscoverCompetitive : Screen("discover_competitive")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
    
    // Detail Screens
    object CreateRoom : Screen("create_room")
    object RoomDetail : Screen("room_detail/{roomId}") {
        fun createRoute(roomId: String) = "room_detail/$roomId"
    }
    object ChatRoom : Screen("chat_room/{roomId}/{mode}/{roomTitle}/{sport}") {
        fun createRoute(
            roomId: String, 
            mode: String = "casual",
            roomTitle: String = "Chat Room",
            sport: String = "Sport"
        ) = "chat_room/$roomId/$mode/${roomTitle.replace("/", "-")}/${sport.replace("/", "-")}"
    }
    object EditProfile : Screen("edit_profile")
    object CampaignDetail : Screen("campaign_detail/{campaignId}") {
        fun createRoute(campaignId: String) = "campaign_detail/$campaignId"
    }
}
