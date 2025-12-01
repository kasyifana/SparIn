package com.example.sparin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.ui.Modifier
import com.example.sparin.presentation.auth.SignInScreen
import com.example.sparin.presentation.onboarding.OnboardingScreen
import com.example.sparin.presentation.personalization.PersonalizationScreen
import com.example.sparin.presentation.home.HomeScreen
import com.example.sparin.presentation.community.CommunityScreen
import com.example.sparin.presentation.discover.ModeSelectorScreen
import com.example.sparin.presentation.discover.DiscoverCasualScreen
import com.example.sparin.presentation.discover.DiscoverCompetitiveScreen
import com.example.sparin.presentation.discover.RoomMode
import com.example.sparin.presentation.chat.ChatListScreen
import com.example.sparin.presentation.chat.ChatRoomScreen
import com.example.sparin.presentation.community.feed.CommunityFeedScreen
import com.example.sparin.presentation.community.CreateCommunityScreen
import com.example.sparin.presentation.profile.ProfileScreen

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
        
        // Create Community Screen
        composable("create_community") {
            CreateCommunityScreen(navController = navController)
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
        
        composable(Screen.Chat.route) {
            ChatListScreen(navController = navController)
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        
        // Detail Screens with arguments
        
        // Chat Room Detail
        composable(
            route = Screen.ChatRoom.route,
            arguments = listOf(
                navArgument("roomId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId")
            ChatRoomScreen(
                navController = navController,
                roomId = roomId
            )
        }
        
        // Community Feed (Posts & Comments)
        // Community Feed (Posts & Comments)
        composable(
            route = Screen.CommunityFeed.route,
            arguments = listOf(
                navArgument("communityId") { type = NavType.StringType },
                navArgument("communityName") { 
                    type = NavType.StringType
                    defaultValue = "Community"
                },
                navArgument("communityEmoji") { 
                    type = NavType.StringType
                    defaultValue = "🏸"
                }
            )
        ) { backStackEntry ->
            val communityId = backStackEntry.arguments?.getString("communityId") ?: ""
            val communityName = backStackEntry.arguments?.getString("communityName") ?: "Community"
            val communityEmoji = backStackEntry.arguments?.getString("communityEmoji") ?: "🏸"
            
            CommunityFeedScreen(
                navController = navController,
                communityId = communityId,
                communityName = communityName,
                communityEmoji = communityEmoji
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
        
        // Room Detail, Campaign Detail, etc. (will be added later)
    }
}
