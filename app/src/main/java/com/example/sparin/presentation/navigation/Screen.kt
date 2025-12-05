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
    
    // Competitive Mode Screens
    object CompetitiveRadar : Screen("competitive_radar/{sport}/{matchType}") {
        fun createRoute(sport: String = "All", matchType: String = "1v1") = 
            "competitive_radar/$sport/$matchType"
    }
    
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
    object MatchHistory : Screen("match_history")
    object MatchDetail : Screen("match_detail/{matchId}") {
        fun createRoute(matchId: String) = "match_detail/$matchId"
    }
    
    object CommunityFeed : Screen("community_feed/{communityId}/{name}/{emoji}") {
        fun createRoute(communityId: String, name: String, emoji: String) =
            "community_feed/$communityId/$name/$emoji"
    }

    object CampaignDetail : Screen("campaign_detail/{campaignId}") {
        fun createRoute(campaignId: String) = "campaign_detail/$campaignId"
    }

    // Community Additional Screens
    object AllMyCommunities : Screen("all_my_communities")
    object CreateCommunity : Screen("create_community")
    object AllUpcomingEvents : Screen("all_upcoming_events/{communityId}/{communityName}") {
        fun createRoute(communityId: String, communityName: String) =
            "all_upcoming_events/$communityId/${communityName.replace("/", "-")}"
    }
    object FindCommunitiesNearby : Screen("find_communities_nearby")
}
