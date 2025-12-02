package com.example.sparin.presentation.community

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

// ==================== GEN-Z COLOR PALETTE ====================
private val GenZBlue = Color(0xFF8CCFFF)
private val GenZTeal = Color(0xFF35C8C3)
private val GenZCyan = Color(0xFF57D3FF)
private val GenZLavender = Color(0xFFB8B5FF)

// ==================== SAMPLE DATA ====================
// This would typically come from a ViewModel in production
private val allMyCommunities = listOf(
    Community(
        id = "1",
        name = "Badminton Jakarta",
        emoji = "🏸",
        bannerColor = PeachGlow,
        memberCount = "3.2k",
        newPosts = 15,
        isJoined = true,
        description = "Komunitas badminton terbesar di Jakarta!",
        category = "Badminton"
    ),
    Community(
        id = "2",
        name = "Futsal Bandung",
        emoji = "⚽",
        bannerColor = MintBreeze,
        memberCount = "2.1k",
        newPosts = 8,
        isJoined = true,
        description = "Main futsal bareng di Bandung",
        category = "Futsal"
    ),
    Community(
        id = "3",
        name = "Basketball Surabaya",
        emoji = "🏀",
        bannerColor = SkyMist,
        memberCount = "1.5k",
        newPosts = 5,
        isJoined = true,
        description = "Hoops lovers Surabaya!",
        category = "Basket"
    ),
    Community(
        id = "11",
        name = "Volleyball Champions",
        emoji = "🏐",
        bannerColor = RoseDust,
        memberCount = "890",
        newPosts = 3,
        isJoined = true,
        description = "Komunitas voli se-Indonesia",
        category = "Volleyball"
    ),
    Community(
        id = "12",
        name = "Running Squad Jakarta",
        emoji = "🏃",
        bannerColor = MintBreeze,
        memberCount = "1.2k",
        newPosts = 12,
        isJoined = true,
        description = "Morning run dan marathon training",
        category = "Lari"
    )
)

/**
 * All My Communities Screen
 * Shows a list of all communities the user has joined
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllMyCommunitiesScreen(
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CascadingWhite,
                        GenZBlue.copy(alpha = 0.08f),
                        SoftLavender.copy(alpha = 0.15f)
                    )
                )
            )
    ) {
        // Background blobs
        AllCommunitiesBackgroundBlobs()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header
            item {
                AllCommunitiesHeader(
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Stats Card
            item {
                Spacer(modifier = Modifier.height(20.dp))
                CommunitiesStatsCard(
                    totalCommunities = allMyCommunities.size,
                    totalNewPosts = allMyCommunities.sumOf { it.newPosts }
                )
            }

            // Section Title
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "All Communities",
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Lead
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Community List
            items(allMyCommunities, key = { it.id }) { community ->
                MyCommunityListCard(
                    community = community,
                    onClick = {
                        navController.navigate(
                            Screen.CommunityFeed.createRoute(
                                communityId = community.id,
                                name = community.name,
                                emoji = community.emoji
                            )
                        )
                    },
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                )
            }
        }
    }
}

// ==================== BACKGROUND ====================

@Composable
private fun AllCommunitiesBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "all_communities_blobs")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset2"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(70.dp)
    ) {
        // Teal blob - top right
        drawCircle(
            color = GenZTeal.copy(alpha = 0.2f),
            radius = 180f,
            center = Offset(
                x = size.width * 0.85f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 35f,
                y = size.height * 0.08f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 25f
            )
        )

        // Blue blob - left side
        drawCircle(
            color = GenZBlue.copy(alpha = 0.25f),
            radius = 160f,
            center = Offset(
                x = size.width * 0.1f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 30f,
                y = size.height * 0.3f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 35f
            )
        )

        // Lavender blob - center
        drawCircle(
            color = GenZLavender.copy(alpha = 0.18f),
            radius = 140f,
            center = Offset(
                x = size.width * 0.5f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 40f,
                y = size.height * 0.6f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 30f
            )
        )
    }
}

// ==================== HEADER ====================

@Composable
private fun AllCommunitiesHeader(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp)
    ) {
        // Gradient header background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            GenZTeal.copy(alpha = 0.1f),
                            GenZBlue.copy(alpha = 0.08f),
                            GenZCyan.copy(alpha = 0.1f)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .clickable(onClick = onBackClick),
                shape = CircleShape,
                color = NeumorphLight
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp),
                        tint = Lead
                    )
                }
            }

            // Title
            Text(
                text = "My Communities",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                ),
                color = Lead
            )
        }
    }
}

// ==================== STATS CARD ====================

@Composable
private fun CommunitiesStatsCard(
    totalCommunities: Int,
    totalNewPosts: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GenZTeal,
                            GenZCyan,
                            GenZBlue
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Total Communities
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = totalCommunities.toString(),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "Communities",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                // Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(50.dp)
                        .background(Color.White.copy(alpha = 0.3f))
                )

                // New Posts
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = totalNewPosts.toString(),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "New Posts",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

// ==================== COMMUNITY LIST CARD ====================

@Composable
private fun MyCommunityListCard(
    community: Community,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = NeumorphLight
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail with emoji
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                community.bannerColor,
                                community.bannerColor.copy(alpha = 0.5f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = community.emoji,
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = community.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = community.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = WarmHaze,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Stats Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Member count badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GenZBlue.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.People,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = GenZBlue
                            )
                            Text(
                                text = community.memberCount,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = GenZBlue
                            )
                        }
                    }

                    // New posts indicator
                    if (community.newPosts > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GenZTeal.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${community.newPosts} new",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = GenZTeal
                            )
                        }
                    }

                    // Category badge
                    if (community.category.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GenZLavender.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = community.category,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Lead.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Arrow indicator
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = "View",
                modifier = Modifier.size(24.dp),
                tint = WarmHaze
            )
        }
    }
}
