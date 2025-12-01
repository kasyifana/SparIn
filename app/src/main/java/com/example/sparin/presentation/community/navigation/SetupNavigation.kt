package com.example.sparin.presentation.community.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sparin.presentation.community.CommunityScreen
import com.example.sparin.presentation.community.feed.CommunityFeedScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Navigation setup untuk Community feature
 * 
 * Cara menggunakan:
 * 1. Import function ini ke MainActivity atau root composable
 * 2. Panggil SetupCommunityNavigation dengan NavController
 * 
 * Contoh:
 * ```
 * val navController = rememberNavController()
 * SetupCommunityNavigation(navController = navController)
 * ```
 */
@Composable
fun SetupCommunityNavigation(
    navController: NavHostController,
    startDestination: String = "community"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Community List Screen
        composable(route = "community") {
            CommunityScreen(navController = navController)
        }

        // Chat Screen dengan arguments
        // Chat Screen dengan arguments
        composable(
            route = "chat/{communityId}/{communityName}/{communityEmoji}",
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
    }
}

/**
 * Extension function untuk navigate ke chat dengan type-safe arguments
 */
fun NavHostController.navigateToChat(communityId: String, communityName: String, communityEmoji: String) {
    val encodedName = URLEncoder.encode(communityName, StandardCharsets.UTF_8.toString())
    val encodedEmoji = URLEncoder.encode(communityEmoji, StandardCharsets.UTF_8.toString())
    this.navigate("chat/$communityId/$encodedName/$encodedEmoji")
}
