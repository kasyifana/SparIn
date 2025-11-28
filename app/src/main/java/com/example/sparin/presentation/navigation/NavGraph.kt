package com.example.sparin.presentation.navigation

import androidx.compose.runtime.Composable
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
import com.example.sparin.presentation.discover.DiscoverScreen
import com.example.sparin.presentation.chat.ChatListScreen
import com.example.sparin.presentation.chat.ChatRoomScreen
import com.example.sparin.presentation.profile.ProfileScreen

/**
 * Navigation Graph untuk aplikasi SparIN
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
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
        
        composable(Screen.Discover.route) {
            DiscoverScreen(navController = navController)
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
        
        // Room Detail, Campaign Detail, etc. (will be added later)
    }
}
