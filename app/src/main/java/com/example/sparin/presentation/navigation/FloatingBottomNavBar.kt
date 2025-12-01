package com.example.sparin.presentation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// Premium Gen-Z Color palette for floating nav bar
private val NavBarDark = Color(0xFF1A1A2E)          // Dark navy container
private val NavBarDarkGlossy = Color(0xFF16213E)    // Slightly lighter for glossy effect
private val NavBarActiveWhite = Color(0xFFFFFFFF)   // Pure white active button
private val NavBarInactiveGray = Color(0xFF6B7280)  // Soft gray inactive icons
private val NavBarShadowColor = Color(0xFF0F0F1A)   // Deep shadow

/**
 * Premium Floating Bottom Navigation Bar
 * 
 * A modern, ultra-clean Gen-Z aesthetic navigation bar featuring:
 * - Soft pill-shaped container with glossy dark surface
 * - Sliding circular highlight with smooth animations
 * - Micro-interactions and balanced spacing
 * - Premium drop shadow and elevation
 */
@Composable
fun FloatingBottomNavBarPremium(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(Screen.Home, Icons.Rounded.Home, Icons.Outlined.Home, "Home"),
        NavItem(Screen.Community, Icons.Rounded.Groups, Icons.Outlined.Groups, "Community"),
        NavItem(Screen.Discover, Icons.Rounded.Explore, Icons.Outlined.Explore, "Discover"),
        NavItem(Screen.Chat, Icons.Rounded.ChatBubble, Icons.Outlined.ChatBubbleOutline, "Chat"),
        NavItem(Screen.Profile, Icons.Rounded.Person, Icons.Outlined.PersonOutline, "Profile")
    )
    
    val selectedIndex = items.indexOfFirst { it.screen.route == currentRoute }.coerceAtLeast(0)
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        // Main pill container with premium shadow
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(35.dp),
                    ambientColor = NavBarShadowColor.copy(alpha = 0.4f),
                    spotColor = NavBarShadowColor.copy(alpha = 0.6f)
                )
                .clip(RoundedCornerShape(35.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            NavBarDarkGlossy,
                            NavBarDark
                        )
                    )
                )
                .height(70.dp)
        ) {
            // Glossy top edge highlight for premium feel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.08f),
                                Color.White.copy(alpha = 0.12f),
                                Color.White.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )
                    .align(Alignment.TopCenter)
            )
            
            // Navigation icons row with indicator behind
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp, vertical = 11.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = index == selectedIndex
                    
                    // Each nav item with its own indicator
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Animated white circle indicator
                        val bgAlpha by animateFloatAsState(
                            targetValue = if (isSelected) 1f else 0f,
                            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                            label = "bgAlpha_$index"
                        )
                        
                        val elevation by animateDpAsState(
                            targetValue = if (isSelected) 8.dp else 0.dp,
                            animationSpec = tween(durationMillis = 250),
                            label = "elevation_$index"
                        )
                        
                        // White circle background (perfectly centered)
                        if (bgAlpha > 0f) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .shadow(
                                        elevation = elevation,
                                        shape = CircleShape,
                                        ambientColor = Color.White.copy(alpha = 0.2f),
                                        spotColor = Color.White.copy(alpha = 0.15f)
                                    )
                                    .background(
                                        color = NavBarActiveWhite.copy(alpha = bgAlpha),
                                        shape = CircleShape
                                    )
                            )
                        }
                        
                        // Icon
                        PremiumNavIcon(
                            item = item,
                            isSelected = isSelected,
                            onClick = { onNavigate(item.screen) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PremiumNavIcon(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Icon color animation with smooth transition
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) NavBarDark else NavBarInactiveGray,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "iconColor"
    )
    
    // Scale micro-interaction
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )
    
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.label,
            modifier = Modifier
                .size(24.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            tint = iconColor
        )
    }
}

private data class NavItem(
    val screen: Screen,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val label: String
)

/**
 * Standard Floating Bottom Navigation Bar
 * Simpler version without sliding indicator
 */
@Composable
fun FloatingBottomNavBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(Screen.Home, Icons.Rounded.Home, Icons.Outlined.Home, "Home"),
        NavItem(Screen.Community, Icons.Rounded.Groups, Icons.Outlined.Groups, "Community"),
        NavItem(Screen.Discover, Icons.Rounded.Explore, Icons.Outlined.Explore, "Discover"),
        NavItem(Screen.Chat, Icons.Rounded.ChatBubble, Icons.Outlined.ChatBubbleOutline, "Chat"),
        NavItem(Screen.Profile, Icons.Rounded.Person, Icons.Outlined.PersonOutline, "Profile")
    )
    
    val selectedIndex = items.indexOfFirst { it.screen.route == currentRoute }.coerceAtLeast(0)
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(40.dp),
                    ambientColor = NavBarShadowColor.copy(alpha = 0.3f),
                    spotColor = NavBarShadowColor.copy(alpha = 0.5f)
                ),
            shape = RoundedCornerShape(40.dp),
            color = NavBarDark
        ) {
            Row(
                modifier = Modifier
                    .height(68.dp)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    FloatingNavItem(
                        item = item,
                        isSelected = index == selectedIndex,
                        onClick = { onNavigate(item.screen) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingNavItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedSize by animateDpAsState(
        targetValue = if (isSelected) 48.dp else 44.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "size"
    )
    
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "bgAlpha"
    )
    
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) NavBarDark else NavBarInactiveGray,
        animationSpec = tween(durationMillis = 200),
        label = "iconColor"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "elevation"
    )
    
    Box(
        modifier = Modifier
            .size(animatedSize)
            .then(
                if (isSelected) {
                    Modifier.shadow(
                        elevation = elevation,
                        shape = CircleShape,
                        ambientColor = Color.White.copy(alpha = 0.2f),
                        spotColor = Color.White.copy(alpha = 0.1f)
                    )
                } else Modifier
            )
            .clip(CircleShape)
            .background(
                color = NavBarActiveWhite.copy(alpha = backgroundAlpha)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.label,
            modifier = Modifier.size(22.dp),
            tint = iconColor
        )
    }
}

/**
 * Compact version for smaller screens
 */
@Composable
fun FloatingBottomNavBarCompact(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(Screen.Home, Icons.Rounded.Home, Icons.Outlined.Home, "Home"),
        NavItem(Screen.Community, Icons.Rounded.Groups, Icons.Outlined.Groups, "Community"),
        NavItem(Screen.Discover, Icons.Rounded.Explore, Icons.Outlined.Explore, "Discover"),
        NavItem(Screen.Chat, Icons.Rounded.ChatBubble, Icons.Outlined.ChatBubbleOutline, "Chat"),
        NavItem(Screen.Profile, Icons.Rounded.Person, Icons.Outlined.PersonOutline, "Profile")
    )
    
    val selectedIndex = items.indexOfFirst { it.screen.route == currentRoute }.coerceAtLeast(0)
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = NavBarShadowColor.copy(alpha = 0.3f),
                    spotColor = NavBarShadowColor.copy(alpha = 0.5f)
                )
                .clip(RoundedCornerShape(32.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(NavBarDarkGlossy, NavBarDark)
                    )
                )
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                CompactNavItem(
                    item = item,
                    isSelected = index == selectedIndex,
                    onClick = { onNavigate(item.screen) }
                )
            }
        }
    }
}

@Composable
private fun CompactNavItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) NavBarActiveWhite else Color.Transparent,
        animationSpec = tween(durationMillis = 300),
        label = "bgColor"
    )
    
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) NavBarDark else NavBarInactiveGray,
        animationSpec = tween(durationMillis = 300),
        label = "iconColor"
    )
    
    val size by animateDpAsState(
        targetValue = if (isSelected) 44.dp else 40.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "size"
    )
    
    Box(
        modifier = Modifier
            .size(size)
            .then(
                if (isSelected) {
                    Modifier.shadow(
                        elevation = 6.dp,
                        shape = CircleShape,
                        ambientColor = Color.White.copy(alpha = 0.15f)
                    )
                } else Modifier
            )
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.label,
            modifier = Modifier.size(20.dp),
            tint = iconColor
        )
    }
}
