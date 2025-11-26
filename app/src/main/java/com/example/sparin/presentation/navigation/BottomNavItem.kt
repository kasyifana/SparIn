package com.example.sparin.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Bottom Navigation Items
 */
data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)

val bottomNavItems = listOf(
    BottomNavItem(
        title = "Home",
        icon = Icons.Filled.Home,
        screen = Screen.Home
    ),
    BottomNavItem(
        title = "Community",
        icon = Icons.Filled.Groups,
        screen = Screen.Community
    ),
    BottomNavItem(
        title = "Discover",
        icon = Icons.Filled.Explore,
        screen = Screen.Discover
    ),
    BottomNavItem(
        title = "Chat",
        icon = Icons.Filled.Message,
        screen = Screen.Chat
    ),
    BottomNavItem(
        title = "Profile",
        icon = Icons.Filled.Person,
        screen = Screen.Profile
    )
)
