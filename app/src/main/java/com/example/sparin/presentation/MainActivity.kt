package com.example.sparin.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sparin.data.repository.AuthRepository
import com.example.sparin.presentation.navigation.FloatingBottomNavBarPremium
import com.example.sparin.presentation.navigation.NavGraph
import com.example.sparin.presentation.navigation.Screen
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
        
        val showBottomNav = currentRoute in screensWithBottomNav
        
        Box(modifier = Modifier.fillMaxSize()) {
            // Main content
            NavGraph(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.fillMaxSize()
            )
            
            // Floating Bottom Navigation Bar
            if (showBottomNav) {
                FloatingBottomNavBarPremium(
                    currentRoute = currentRoute,
                    onNavigate = { screen ->
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
