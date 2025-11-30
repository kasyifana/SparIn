package com.example.sparin.presentation.community

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sparin.ui.theme.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.math.cos
import kotlin.math.sin

/**
 * Premium Community Screen for SparIN
 * Gen-Z aesthetic: soft-neumorphism, frosted glass cards, pastel gradients
 */

// Sport categories data
data class SportCategory(
    val name: String,
    val emoji: String
)

val sportCategories = listOf(
    SportCategory("All", "🌟"),
    SportCategory("Badminton", "🏸"),
    SportCategory("Futsal", "⚽"),
    SportCategory("Basket", "🏀"),
    SportCategory("Voli", "🏐"),
    SportCategory("Tenis", "🎾"),
    SportCategory("Gym", "💪"),
    SportCategory("Boxing", "🥊"),
    SportCategory("Lari", "🏃"),
    SportCategory("Hiking", "🥾"),
    SportCategory("Renang", "🏊"),
    SportCategory("Sepeda", "🚴")
)

// Community data
data class CommunityItem(
    val name: String,
    val sport: String,
    val emoji: String,
    val memberCount: Int,
    val tags: List<String>,
    val accentColor: Color
)

val communityList = listOf(
    CommunityItem(
        name = "Jakarta Badminton Club",
        sport = "Badminton",
        emoji = "🏸",
        memberCount = 1250,
        tags = listOf("Beginner Friendly", "Weekly Meetups"),
        accentColor = PeachGlow
    ),
    CommunityItem(
        name = "Futsal Warriors BSD",
        sport = "Futsal",
        emoji = "⚽",
        memberCount = 890,
        tags = listOf("Competitive", "City Area"),
        accentColor = MintBreeze
    ),
    CommunityItem(
        name = "Streetball Jakarta",
        sport = "Basketball",
        emoji = "🏀",
        memberCount = 2100,
        tags = listOf("All Levels", "Weekend Games"),
        accentColor = SkyMist
    ),
    CommunityItem(
        name = "Tennis Enthusiasts",
        sport = "Tennis",
        emoji = "🎾",
        memberCount = 560,
        tags = listOf("Semi-Pro", "Morning Sessions"),
        accentColor = RoseDust
    ),
    CommunityItem(
        name = "Gym Bros Community",
        sport = "Gym",
        emoji = "💪",
        memberCount = 3400,
        tags = listOf("Fitness Tips", "Workout Buddy"),
        accentColor = ChineseSilver
    ),
    CommunityItem(
        name = "Running Jakarta",
        sport = "Running",
        emoji = "🏃",
        memberCount = 4200,
        tags = listOf("Casual", "City Routes"),
        accentColor = PeachGlow
    )
)

val suggestedCommunities = listOf(
    CommunityItem("Voli Pantai", "Volleyball", "🏐", 320, listOf(), MintBreeze),
    CommunityItem("Boxing Club", "Boxing", "🥊", 180, listOf(), RoseDust),
    CommunityItem("Cycling ID", "Cycling", "🚴", 890, listOf(), SkyMist),
    CommunityItem("Swim Squad", "Swimming", "🏊", 450, listOf(), PeachGlow),
    CommunityItem("Hiking Indo", "Hiking", "🥾", 1200, listOf(), ChineseSilver)
)

@Composable
fun CommunityScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("All") }
    val scrollState = rememberScrollState()
    
    // State untuk menyimpan komunitas yang sudah di-join
    var joinedCommunities by remember { mutableStateOf<Set<String>>(emptySet()) }
    
    // State untuk dialog konfirmasi join
    var showJoinDialog by remember { mutableStateOf(false) }
    var selectedCommunityToJoin by remember { mutableStateOf<CommunityItem?>(null) }
    
    // Fungsi untuk join komunitas
    val onJoinCommunity: (String) -> Unit = { communityName ->
        joinedCommunities = joinedCommunities + communityName
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CascadingWhite, SoftLavender.copy(alpha = 0.2f))
                )
            )
    ) {
        // Background blobs
        CommunityBackgroundBlobs()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header Section
            CommunityHeader()

            Spacer(modifier = Modifier.height(20.dp))

            // Category Tabs
            CategoryTabsSection(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // My Communities (yang sudah di-join)
            MyCommunitiesSection(
                joinedCommunities = joinedCommunities,
                navController = navController
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Popular Communities (yang belum di-join)
            PopularCommunitiesSection(
                joinedCommunities = joinedCommunities,
                onJoinClick = { community ->
                    selectedCommunityToJoin = community
                    showJoinDialog = true
                }
            )

            Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for nav bar
        }
        
        // Join Confirmation Dialog
        if (showJoinDialog && selectedCommunityToJoin != null) {
            JoinCommunityDialog(
                community = selectedCommunityToJoin!!,
                onConfirm = {
                    onJoinCommunity(selectedCommunityToJoin!!.name)
                    showJoinDialog = false
                    selectedCommunityToJoin = null
                },
                onDismiss = {
                    showJoinDialog = false
                    selectedCommunityToJoin = null
                }
            )
        }
    }
}

// ==================== BACKGROUND ====================

@Composable
private fun CommunityBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "community_blobs")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset2"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(80.dp)
    ) {
        drawCircle(
            color = ChineseSilver.copy(alpha = 0.25f),
            radius = 180f,
            center = Offset(
                x = size.width * 0.1f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 30f,
                y = size.height * 0.08f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 20f
            )
        )

        drawCircle(
            color = PeachGlow.copy(alpha = 0.15f),
            radius = 140f,
            center = Offset(
                x = size.width * 0.85f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 25f,
                y = size.height * 0.2f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 30f
            )
        )

        drawCircle(
            color = MintBreeze.copy(alpha = 0.15f),
            radius = 160f,
            center = Offset(
                x = size.width * 0.25f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 35f,
                y = size.height * 0.5f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 25f
            )
        )

        drawCircle(
            color = SkyMist.copy(alpha = 0.12f),
            radius = 120f,
            center = Offset(
                x = size.width * 0.8f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 20f,
                y = size.height * 0.75f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f
            )
        )
    }
}

// ==================== HEADER SECTION ====================

@Composable
private fun CommunityHeader() {
    val infiniteTransition = rememberInfiniteTransition(label = "header_float")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp)
    ) {
        // Floating 3D accents
        Box(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-60).dp, y = (20 + float1).dp)
                .background(
                    color = PeachGlow.copy(alpha = 0.4f),
                    shape = CircleShape
                )
                .blur(8.dp)
        )

        Box(
            modifier = Modifier
                .size(25.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-100).dp, y = (40 + float2).dp)
                .background(
                    color = MintBreeze.copy(alpha = 0.5f),
                    shape = CircleShape
                )
                .blur(5.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Community Hub",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = Lead
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Join groups and find your people",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmHaze
                )
            }

            // Search Icon with glow
            Box {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Crunch.copy(alpha = 0.2f),
                                    Crunch.copy(alpha = 0f)
                                )
                            ),
                            shape = CircleShape
                        )
                )

                Surface(
                    modifier = Modifier
                        .size(44.dp)
                        .align(Alignment.Center)
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            ambientColor = NeumorphDark.copy(alpha = 0.15f)
                        ),
                    shape = CircleShape,
                    color = NeumorphLight
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                            modifier = Modifier.size(22.dp),
                            tint = WarmHaze
                        )
                    }
                }
            }
        }
    }
}

// ==================== CATEGORY TABS ====================

@Composable
private fun CategoryTabsSection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Browse by Sport",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Lead,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            sportCategories.forEach { category ->
                CategoryPill(
                    category = category,
                    isSelected = selectedCategory == category.name,
                    onClick = { onCategorySelected(category.name) }
                )
            }
        }
    }
}

@Composable
private fun CategoryPill(
    category: SportCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Crunch else NeumorphLight,
        animationSpec = tween(200),
        label = "bg_color"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Crunch else Dreamland.copy(alpha = 0.4f),
        animationSpec = tween(200),
        label = "border_color"
    )

    val elevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 4.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "elevation"
    )

    Surface(
        modifier = Modifier
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(20.dp),
                ambientColor = if (isSelected) Crunch.copy(alpha = 0.3f) else NeumorphDark.copy(alpha = 0.1f),
                spotColor = if (isSelected) Crunch.copy(alpha = 0.3f) else NeumorphDark.copy(alpha = 0.1f)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = category.emoji, fontSize = 16.sp)
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                ),
                color = if (isSelected) Lead else WarmHaze
            )
        }
    }
}

// ==================== MY COMMUNITIES (JOINED) ====================

@Composable
private fun MyCommunitiesSection(
    joinedCommunities: Set<String>,
    navController: NavHostController
) {
    // Filter komunitas yang sudah di-join
    val myJoinedCommunities = communityList.filter { it.name in joinedCommunities }
    
    if (myJoinedCommunities.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "My Communities",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Lead
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                myJoinedCommunities.forEach { community ->
                    JoinedCommunityCard(
                        community = community,
                        onOpenClick = {
                            // Encode emoji dan nama untuk menghindari error navigasi
                            val encodedName = URLEncoder.encode(community.name, StandardCharsets.UTF_8.toString())
                            val encodedEmoji = URLEncoder.encode(community.emoji, StandardCharsets.UTF_8.toString())
                            navController.navigate("chat/$encodedName/$encodedEmoji")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun JoinedCommunityCard(
    community: CommunityItem,
    onOpenClick: () -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isPressed) 6.dp else 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.12f),
                spotColor = NeumorphDark.copy(alpha = 0.12f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = NeumorphLight.copy(alpha = 0.97f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Dreamland.copy(alpha = 0.2f),
                            community.accentColor.copy(alpha = 0.15f),
                            Dreamland.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            // Subtle background gradient swirl
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                community.accentColor.copy(alpha = 0.2f),
                                community.accentColor.copy(alpha = 0f)
                            )
                        ),
                        shape = CircleShape
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sport Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    community.accentColor.copy(alpha = 0.4f),
                                    community.accentColor.copy(alpha = 0.2f)
                                )
                            ),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = community.emoji, fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Community Details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = community.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Lead,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.People,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = WarmHaze
                        )
                        Text(
                            text = "${community.memberCount} members",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmHaze
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tags
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        community.tags.take(2).forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ChineseSilver.copy(alpha = 0.5f)
                            ) {
                                Text(
                                    text = tag,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = WarmHaze,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Open Button
                Surface(
                    modifier = Modifier
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(14.dp),
                            ambientColor = Crunch.copy(alpha = 0.3f),
                            spotColor = Crunch.copy(alpha = 0.3f)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { 
                            isPressed = !isPressed
                            onOpenClick()
                        },
                    shape = RoundedCornerShape(14.dp),
                    color = Crunch
                ) {
                    Text(
                        text = "Open",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Lead
                    )
                }
            }
        }
    }
}

// ==================== POPULAR COMMUNITIES (NOT JOINED) ====================

@Composable
private fun PopularCommunitiesSection(
    joinedCommunities: Set<String>,
    onJoinClick: (CommunityItem) -> Unit
) {
    // Gabungkan semua komunitas yang belum di-join
    val allCommunities = communityList + suggestedCommunities
    val notJoinedCommunities = allCommunities.filter { it.name !in joinedCommunities }
    
    if (notJoinedCommunities.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Popular Communities",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Lead
                )
                TextButton(onClick = { }) {
                    Text(
                        text = "See All",
                        color = Crunch,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                notJoinedCommunities.forEach { community ->
                    PopularCommunityCard(
                        community = community,
                        onJoinClick = { onJoinClick(community) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PopularCommunityCard(
    community: CommunityItem,
    onJoinClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "popular_float")

    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Surface(
        modifier = Modifier
            .width(130.dp)
            .height(180.dp)
            .offset(y = floatAnim.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.12f),
                spotColor = NeumorphDark.copy(alpha = 0.12f)
            ),
        shape = RoundedCornerShape(22.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            community.accentColor.copy(alpha = 0.5f),
                            community.accentColor.copy(alpha = 0.2f),
                            NeumorphLight
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            NeumorphLight.copy(alpha = 0.8f),
                            community.accentColor.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Sport emoji with 3D effect
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = CircleShape,
                            ambientColor = NeumorphDark.copy(alpha = 0.15f)
                        )
                        .background(
                            color = NeumorphLight.copy(alpha = 0.9f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = community.emoji, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = community.name,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.People,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = WarmHaze
                    )
                    Text(
                        text = "${community.memberCount}",
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmHaze
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Join Button
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = Crunch.copy(alpha = 0.3f)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onJoinClick
                        ),
                    shape = RoundedCornerShape(12.dp),
                    color = Crunch
                ) {
                    Text(
                        text = "Join",
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Lead,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

// ==================== JOIN CONFIRMATION DIALOG ====================

@Composable
private fun JoinCommunityDialog(
    community: CommunityItem,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NeumorphLight,
        shape = RoundedCornerShape(24.dp),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Community Icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    community.accentColor.copy(alpha = 0.4f),
                                    community.accentColor.copy(alpha = 0.2f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = community.emoji, fontSize = 32.sp)
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Join Community?",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Lead
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = community.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.People,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = WarmHaze
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${community.memberCount} members",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmHaze
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "You will be able to chat and connect with other members.",
                    style = MaterialTheme.typography.bodySmall,
                    color = WarmHaze,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        },
        confirmButton = {
            Surface(
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Crunch.copy(alpha = 0.3f)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onConfirm
                    ),
                shape = RoundedCornerShape(14.dp),
                color = Crunch
            ) {
                Text(
                    text = "Join",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead
                )
            }
        },
        dismissButton = {
            Surface(
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = NeumorphDark.copy(alpha = 0.1f)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss
                    ),
                shape = RoundedCornerShape(14.dp),
                color = Dreamland.copy(alpha = 0.5f)
            ) {
                Text(
                    text = "Cancel",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = WarmHaze
                )
            }
        }
    )
}
