package com.example.sparin.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sparin.presentation.auth.SignInScreen
import com.example.sparin.presentation.onboarding.OnboardingScreen
import com.example.sparin.presentation.personalization.PersonalizationScreen
import com.example.sparin.presentation.home.HomeScreen
import com.example.sparin.presentation.community.CommunityScreen
import com.example.sparin.presentation.community.AllMyCommunitiesScreen
import com.example.sparin.presentation.community.FindCommunitiesNearbyScreen
import com.example.sparin.presentation.discover.ModeSelectorScreen
import com.example.sparin.presentation.discover.DiscoverCasualScreen
import com.example.sparin.presentation.discover.DiscoverCompetitiveScreen
import com.example.sparin.presentation.discover.RoomMode
import com.example.sparin.presentation.chat.ChatListScreen
import com.example.sparin.presentation.chat.ChatRoomScreen
import com.example.sparin.presentation.profile.ProfileScreen
import com.example.sparin.presentation.profile.EditProfileScreen
import com.example.sparin.presentation.profile.MatchHistoryScreen
import com.example.sparin.presentation.profile.MatchDetailScreen
import com.example.sparin.presentation.community.feed.CommunityFeedScreen
import com.example.sparin.presentation.community.feed.AllUpcomingEventsScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Navigation Graph untuk aplikasi SparIN
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Onboarding & Auth Flow
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onNavigateToSignIn = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.SignIn.route) {
            SignInScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateToPersonalization = {
                    navController.navigate(Screen.Personalization.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Personalization.route) {
            PersonalizationScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Personalization.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Main Screens dengan Bottom Nav
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.Community.route) {
            CommunityScreen(navController = navController)
        }
        
        // All My Communities Screen
        composable(Screen.AllMyCommunities.route) {
            AllMyCommunitiesScreen(navController = navController)
        }
        
        // Find Communities Nearby Screen
        composable(Screen.FindCommunitiesNearby.route) {
            FindCommunitiesNearbyScreen(navController = navController)
        }
        
        composable(Screen.Discover.route) {
            ModeSelectorScreen(
                navController = navController,
                onModeSelected = { mode ->
                    when (mode) {
                        RoomMode.CASUAL -> navController.navigate(Screen.DiscoverCasual.route)
                        RoomMode.COMPETITIVE -> navController.navigate(Screen.DiscoverCompetitive.route)
                    }
                }
            )
        }

        composable(Screen.DiscoverCasual.route) {
            DiscoverCasualScreen(navController = navController)
        }

        composable(Screen.DiscoverCompetitive.route) {
            DiscoverCompetitiveScreen(navController = navController)
        }
        
        // Competitive Radar Scanning Screen
        composable(
            route = Screen.CompetitiveRadar.route,
            arguments = listOf(
                navArgument("sport") {
                    type = NavType.StringType
                    defaultValue = "All"
                },
                navArgument("matchType") {
                    type = NavType.StringType
                    defaultValue = "1v1"
                }
            )
        ) { backStackEntry ->
            val sport = backStackEntry.arguments?.getString("sport") ?: "All"
            val matchType = backStackEntry.arguments?.getString("matchType") ?: "1v1"
            com.example.sparin.presentation.discover.CompetitiveRadarScreen(
                navController = navController,
                selectedSport = sport,
                matchType = matchType
            )
        }
        
        composable(
            route = Screen.Chat.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            }
        ) {
            ChatListScreen(navController = navController)
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        
        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }
        
        composable(Screen.MatchHistory.route) {
            MatchHistoryScreen(navController = navController)
        }
        
        composable(
            route = Screen.MatchDetail.route,
            arguments = listOf(
                navArgument("matchId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            MatchDetailScreen(
                navController = navController,
                matchId = backStackEntry.arguments?.getString("matchId")
            )
        }
        
        // Detail Screens with arguments
        
        // Chat Room Detail
        composable(
            route = Screen.ChatRoom.route,
            arguments = listOf(
                navArgument("roomId") {
                    type = NavType.StringType
                },
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = "casual"
                },
                navArgument("roomTitle") {
                    type = NavType.StringType
                    defaultValue = "Chat Room"
                },
                navArgument("sport") {
                    type = NavType.StringType
                    defaultValue = "Sport"
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId")
            val mode = backStackEntry.arguments?.getString("mode") ?: "casual"
            val roomTitle = backStackEntry.arguments?.getString("roomTitle") ?: "Chat Room"
            val sport = backStackEntry.arguments?.getString("sport") ?: "Sport"
            ChatRoomScreen(
                navController = navController,
                roomId = roomId,
                mode = mode,
                roomTitle = roomTitle,
                sport = sport
            )
        }
        
        // Create Room Screen
        composable(
            route = "create_room/{mode}",
            arguments = listOf(navArgument("mode") { type = NavType.StringType })
        ) { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "Casual"
            com.example.sparin.presentation.discover.CreateRoomScreen(
                navController = navController,
                mode = mode
            )
        }
        
        // Room Detail Screen
        composable(
            route = "room_detail/{roomId}",
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            com.example.sparin.presentation.discover.RoomDetailScreen(
                navController = navController,
                roomId = roomId
            )
        }
        
        // Community Feed Screen
        composable(
            route = Screen.CommunityFeed.route,
            arguments = listOf(
                navArgument("communityId") {
                    type = NavType.StringType
                },
                navArgument("name") {
                    type = NavType.StringType
                },
                navArgument("emoji") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val communityId = backStackEntry.arguments?.getString("communityId") ?: ""
            val name = URLDecoder.decode(backStackEntry.arguments?.getString("name") ?: "", StandardCharsets.UTF_8.toString())
            val emoji = URLDecoder.decode(backStackEntry.arguments?.getString("emoji") ?: "", StandardCharsets.UTF_8.toString())
            CommunityFeedScreen(
                navController = navController,
                communityId = communityId,
                communityName = name,
                communityEmoji = emoji
            )
        }
        
        // All Upcoming Events Screen
        composable(
            route = Screen.AllUpcomingEvents.route,
            arguments = listOf(
                navArgument("communityId") {
                    type = NavType.StringType
                },
                navArgument("communityName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val communityId = backStackEntry.arguments?.getString("communityId") ?: ""
            val communityName = URLDecoder.decode(backStackEntry.arguments?.getString("communityName") ?: "", StandardCharsets.UTF_8.toString())
            AllUpcomingEventsScreen(
                navController = navController,
                communityId = communityId,
                communityName = communityName
            )
        }
        
        // Room Detail, Campaign Detail, etc. (will be added later)
    }
}
