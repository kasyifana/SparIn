package com.example.sparin.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sparin.data.repository.AuthRepository
import com.example.sparin.presentation.navigation.NavGraph
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.presentation.navigation.bottomNavItems
import com.example.sparin.ui.theme.SparInTheme
import org.koin.android.ext.android.inject

/**
 * Main Activity untuk SparIN
 */
class MainActivity : ComponentActivity() {
    
    private val authRepository: AuthRepository by inject()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            SparInTheme {
                MainScreen()
            }
        }
    }
    
    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        
        // Determine start destination based on auth and personalization status
        val startDestination = remember {
            when {
                !authRepository.isUserSignedIn() -> {
                    // Not signed in -> Onboarding
                    Screen.Onboarding.route
                }
                else -> {
                    // Signed in - check if personalization is complete
                    // For now, assume they need personalization (will be updated by ViewModel)
                    // This will be overridden by navigation logic after user data loads
                    Screen.Home.route
                }
            }
        }
        
        // Screens yang menampilkan bottom nav
        val screensWithBottomNav = listOf(
            Screen.Home.route,
            Screen.Community.route,
            Screen.Discover.route,
            Screen.Chat.route,
            Screen.Profile.route
        )
        
        Scaffold(
            bottomBar = {
                if (currentRoute in screensWithBottomNav) {
                    NavigationBar {
                        bottomNavItems.forEach { item ->
                            NavigationBarItem(
                                icon = { Icon(item.icon, contentDescription = item.title) },
                                label = { Text(item.title) },
                                selected = currentRoute == item.screen.route,
                                onClick = {
                                    if (currentRoute != item.screen.route) {
                                        navController.navigate(item.screen.route) {
                                            // Pop up to start destination
                                            popUpTo(Screen.Home.route) {
                                                saveState = true
                                            }
                                            // Avoid multiple copies
                                            launchSingleTop = true
                                            // Restore state when reselecting
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            NavGraph(
                navController = navController,
                startDestination = startDestination
            )
        }
    }
}
