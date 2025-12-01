package com.example.sparin.presentation.community

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.sparin.data.model.Community
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.ui.theme.*
import com.google.firebase.Timestamp
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

/**
 * CommunityScreen - Ultra Modern Gen-Z Aesthetic
 * Features: Glassmorphism, Floating Search, Premium Cards, Member Stack Preview
 */

// Sport categories for filtering
data class SportCategory(
    val name: String,
    val emoji: String,
    val gradient: List<Color>
)

val sportCategories = listOf(
    SportCategory("All", "✨", listOf(Color(0xFFF8D5A3), Color(0xFFF3BA60))),
    SportCategory("Badminton", "🏸", listOf(Color(0xFFFFE0B2), Color(0xFFFFCC80))),
    SportCategory("Futsal", "⚽", listOf(Color(0xFFC8E6C9), Color(0xFFA5D6A7))),
    SportCategory("Basket", "🏀", listOf(Color(0xFFFFCDD2), Color(0xFFEF9A9A))),
    SportCategory("Voli", "🏐", listOf(Color(0xFFE1BEE7), Color(0xFFCE93D8))),
    SportCategory("Tennis", "🎾", listOf(Color(0xFFB2EBF2), Color(0xFF80DEEA))),
    SportCategory("Gym", "💪", listOf(Color(0xFFD7CCC8), Color(0xFFBCAAA4))),
    SportCategory("Running", "🏃", listOf(Color(0xFFFFE0B2), Color(0xFFFFCC80))),
    SportCategory("Cycling", "🚴", listOf(Color(0xFFB3E5FC), Color(0xFF81D4FA)))
)

// Helper functions
private fun getCommunityEmoji(sportCategory: String): String {
    return when (sportCategory.lowercase()) {
        "badminton" -> "🏸"
        "futsal", "football" -> "⚽"
        "basket", "basketball" -> "🏀"
        "tennis" -> "🎾"
        "voli", "volleyball" -> "🏐"
        "gym" -> "💪"
        "running" -> "🏃"
        "cycling" -> "🚴"
        else -> "🏅"
    }
}

private fun getCommunityGradient(sportCategory: String): List<Color> {
    return when (sportCategory.lowercase()) {
        "badminton" -> listOf(Color(0xFFFFE0B2), Color(0xFFFFCC80))
        "futsal", "football" -> listOf(Color(0xFFC8E6C9), Color(0xFFA5D6A7))
        "basket", "basketball" -> listOf(Color(0xFFFFCDD2), Color(0xFFEF9A9A))
        "tennis" -> listOf(Color(0xFFB2EBF2), Color(0xFF80DEEA))
        "voli", "volleyball" -> listOf(Color(0xFFE1BEE7), Color(0xFFCE93D8))
        "gym" -> listOf(Color(0xFFD7CCC8), Color(0xFFBCAAA4))
        "running" -> listOf(Color(0xFFFFE0B2), Color(0xFFFFCC80))
        "cycling" -> listOf(Color(0xFFB3E5FC), Color(0xFF81D4FA))
        else -> listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD))
    }
}

private fun getCommunityColor(sportCategory: String): Color {
    return when (sportCategory.lowercase()) {
        "badminton" -> PeachGlow
        "futsal", "football" -> MintBreeze
        "basket", "basketball" -> SkyMist
        "tennis" -> Color(0xFF80DEEA)
        "voli", "volleyball" -> SoftLavender
        "gym" -> ChineseSilver
        "running" -> PeachGlow
        "cycling" -> MintBreeze
        else -> Dreamland
    }
}

private fun formatDate(timestamp: Timestamp): String {
    return try {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        sdf.format(timestamp.toDate())
    } catch (e: Exception) {
        "Unknown"
    }
}

// ==================== MAIN SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    navController: NavHostController,
    viewModel: CommunityViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val allCommunitiesState by viewModel.allCommunitiesState.collectAsState()
    val userCommunitiesState by viewModel.userCommunitiesState.collectAsState()
    
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var showJoinDialog by remember { mutableStateOf(false) }
    var selectedCommunityToJoin by remember { mutableStateOf<Community?>(null) }
    var showFullscreenImage by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(Unit) {
        viewModel.loadAllCommunities()
        viewModel.loadUserCommunities()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CascadingWhite,
                        SoftLavender.copy(alpha = 0.3f),
                        ChineseSilver.copy(alpha = 0.2f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Premium Header
            PremiumHeader()
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Floating Search Bar
            FloatingSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Category Pills (Scrollable)
            CategoryPillsSection(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
            
            Spacer(modifier = Modifier.height(28.dp))
            
            // My Communities Section
            MyCommunitySection(
                userCommunitiesState = userCommunitiesState,
                navController = navController,
                searchQuery = searchQuery
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Discover Communities Section
            DiscoverCommunitiesSection(
                allCommunitiesState = allCommunitiesState,
                userCommunitiesState = userCommunitiesState,
                selectedCategory = selectedCategory,
                searchQuery = searchQuery,
                onJoinClick = { community ->
                    selectedCommunityToJoin = community
                    showJoinDialog = true
                }
            )
            
            Spacer(modifier = Modifier.height(120.dp))
        }
        
        // Premium Floating Create Button with Lock Badge
        PremiumFloatingButton(
            onClick = { navController.navigate("create_community") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        )
    }

    // Join Dialog
    if (showJoinDialog && selectedCommunityToJoin != null) {
        PremiumJoinDialog(
            community = selectedCommunityToJoin!!,
            onConfirm = {
                val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                if (currentUserId != null) {
                    viewModel.joinCommunity(
                        communityId = selectedCommunityToJoin!!.id,
                        userId = currentUserId,
                        onSuccess = {
                            showJoinDialog = false
                            selectedCommunityToJoin = null
                        },
                        onError = {
                            showJoinDialog = false
                            selectedCommunityToJoin = null
                        }
                    )
                }
            },
            onDismiss = {
                showJoinDialog = false
                selectedCommunityToJoin = null
            }
        )
    }
    
    // Fullscreen Image Viewer
    showFullscreenImage?.let { imageUrl ->
        FullscreenImageViewer(
            imageUrl = imageUrl,
            onDismiss = { showFullscreenImage = null }
        )
    }
}

// ==================== PREMIUM HEADER ====================

@Composable
private fun PremiumHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Animated gradient icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Crunch, SunsetOrange)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🌟", fontSize = 24.sp)
            }
            
            Column {
                Text(
                    text = "Community",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Lead
                )
                Text(
                    text = "Connect with your sports tribe",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmHaze.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// ==================== FLOATING SEARCH BAR ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FloatingSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Glassmorphism container
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = Lead.copy(alpha = 0.1f),
                    ambientColor = Lead.copy(alpha = 0.05f)
                ),
            shape = RoundedCornerShape(20.dp),
            color = CascadingWhite.copy(alpha = 0.95f),
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    tint = WarmHaze.copy(alpha = 0.6f),
                    modifier = Modifier.size(22.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search communities...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = WarmHaze.copy(alpha = 0.5f)
                        )
                    }
                    androidx.compose.foundation.text.BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = Lead
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = { onQueryChange("") },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Clear",
                            tint = WarmHaze,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// ==================== CATEGORY PILLS ====================

@Composable
private fun CategoryPillsSection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(sportCategories) { category ->
            CategoryPill(
                category = category,
                isSelected = selectedCategory == category.name,
                onClick = { onCategorySelected(category.name) }
            )
        }
    }
}

@Composable
private fun CategoryPill(
    category: SportCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 12.dp else 4.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "elevation"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier
            .scale(scale)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(16.dp),
                spotColor = if (isSelected) Crunch.copy(alpha = 0.3f) else Lead.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = if (isSelected) {
                        Brush.linearGradient(category.gradient)
                    } else {
                        Brush.linearGradient(listOf(CascadingWhite, CascadingWhite))
                    }
                )
                .border(
                    width = if (isSelected) 0.dp else 1.dp,
                    color = if (isSelected) Color.Transparent else ChineseSilver.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = category.emoji, fontSize = 18.sp)
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Lead else WarmHaze
                )
            }
        }
    }
}

// ==================== MY COMMUNITIES SECTION ====================

@Composable
private fun MyCommunitySection(
    userCommunitiesState: CommunitiesState,
    navController: NavHostController,
    searchQuery: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Crunch, CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "My Communities",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Lead
                )
            }
            
            when (userCommunitiesState) {
                is CommunitiesState.Success -> {
                    Text(
                        text = "${userCommunitiesState.communities.size} joined",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmHaze.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                }
                else -> {}
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (userCommunitiesState) {
            is CommunitiesState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Crunch,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            is CommunitiesState.Success -> {
                val filteredCommunities = userCommunitiesState.communities.filter {
                    searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true)
                }
                
                if (filteredCommunities.isEmpty()) {
                    EmptyStateCard(
                        emoji = "🔍",
                        message = if (searchQuery.isNotEmpty()) 
                            "No communities found" 
                        else 
                            "You haven't joined any communities yet",
                        subtitle = if (searchQuery.isEmpty()) "Explore and join some!" else null
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        filteredCommunities.forEach { community ->
                            PremiumCommunityCard(
                                community = community,
                                onClick = {
                                    if (community.id.isNotEmpty()) {
                                        val encodedName = URLEncoder.encode(community.name, StandardCharsets.UTF_8.toString())
                                        val emoji = getCommunityEmoji(community.sportCategory)
                                        val encodedEmoji = URLEncoder.encode(emoji, StandardCharsets.UTF_8.toString())
                                        navController.navigate(Screen.CommunityFeed.createRoute(community.id, encodedName, encodedEmoji))
                                    }
                                }
                            )
                        }
                    }
                }
            }
            is CommunitiesState.Error -> {
                ErrorStateCard(message = userCommunitiesState.message)
            }
        }
    }
}

// ==================== PREMIUM COMMUNITY CARD ====================

@Composable
private fun PremiumCommunityCard(
    community: Community,
    onClick: () -> Unit
) {
    val emoji = getCommunityEmoji(community.sportCategory)
    val gradient = getCommunityGradient(community.sportCategory)
    val accentColor = getCommunityColor(community.sportCategory)
    
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "cardScale"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Lead.copy(alpha = 0.08f),
                ambientColor = Lead.copy(alpha = 0.04f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = CascadingWhite
    ) {
        Box {
            // Subtle gradient overlay at top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Emoji Icon with gradient background
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                brush = Brush.linearGradient(gradient),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp),
                                spotColor = gradient.first().copy(alpha = 0.4f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        // Community Name
                        Text(
                            text = community.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Lead,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Category Tag
                        Box(
                            modifier = Modifier
                                .background(
                                    color = accentColor.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = community.sportCategory,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Lead.copy(alpha = 0.8f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Creator info & Creation date
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = WarmHaze.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = community.createdBy.take(12) + if (community.createdBy.length > 12) "..." else "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = WarmHaze.copy(alpha = 0.8f)
                                )
                            }
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.DateRange,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = WarmHaze.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = formatDate(community.createdAt),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = WarmHaze.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    // Arrow indicator
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = Crunch.copy(alpha = 0.1f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ChevronRight,
                            contentDescription = "Open",
                            tint = Crunch,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom row: Member count, Member stack, Share button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Member count + stack
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Member count badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .background(
                                    color = SoftLavender.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Group,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Lead.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "${community.memberCount} members",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Lead.copy(alpha = 0.8f)
                            )
                        }

                        // Mini profile stack preview
                        MemberStackPreview(
                            memberCount = community.memberCount,
                            accentColor = accentColor
                        )
                    }

                    // Disabled Share button
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ChineseSilver.copy(alpha = 0.4f),
                        modifier = Modifier.alpha(0.5f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Share,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = WarmHaze.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "Share",
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmHaze.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== MEMBER STACK PREVIEW ====================

@Composable
private fun MemberStackPreview(
    memberCount: Int,
    accentColor: Color
) {
    val displayCount = minOf(memberCount, 4)
    val remainingCount = memberCount - displayCount
    
    if (memberCount == 0) return
    
    Row(
        modifier = Modifier.height(28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            (0 until displayCount).forEach { index ->
                Box(
                    modifier = Modifier
                        .offset(x = (index * 14).dp)
                        .size(28.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.8f),
                                    accentColor
                                )
                            ),
                            shape = CircleShape
                        )
                        .border(2.dp, CascadingWhite, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👤",
                        fontSize = 12.sp
                    )
                }
            }
            
            if (remainingCount > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = (displayCount * 14).dp)
                        .size(28.dp)
                        .background(Lead.copy(alpha = 0.8f), CircleShape)
                        .border(2.dp, CascadingWhite, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+$remainingCount",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CascadingWhite
                    )
                }
            }
        }
    }
}

// ==================== DISCOVER COMMUNITIES SECTION ====================

@Composable
private fun DiscoverCommunitiesSection(
    allCommunitiesState: CommunitiesState,
    userCommunitiesState: CommunitiesState,
    selectedCategory: String,
    searchQuery: String,
    onJoinClick: (Community) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(MintBreeze, CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Lead
                )
            }
            
            Text(
                text = if (selectedCategory == "All") "All sports" else selectedCategory,
                style = MaterialTheme.typography.bodySmall,
                color = WarmHaze.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (allCommunitiesState) {
            is CommunitiesState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Crunch,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            is CommunitiesState.Success -> {
                val joinedIds = when (userCommunitiesState) {
                    is CommunitiesState.Success ->
                        userCommunitiesState.communities.map { it.id }.toSet()
                    else -> emptySet()
                }

                val filteredCommunities = allCommunitiesState.communities
                    .filter { it.id !in joinedIds }
                    .filter { selectedCategory == "All" || it.sportCategory.equals(selectedCategory, ignoreCase = true) }
                    .filter { searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true) }

                if (filteredCommunities.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        EmptyStateCard(
                            emoji = "🔭",
                            message = "No communities to discover",
                            subtitle = "Try a different category"
                        )
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredCommunities) { community ->
                            DiscoverCommunityCard(
                                community = community,
                                onJoinClick = { onJoinClick(community) }
                            )
                        }
                    }
                }
            }
            is CommunitiesState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    ErrorStateCard(message = allCommunitiesState.message)
                }
            }
        }
    }
}

// ==================== DISCOVER COMMUNITY CARD ====================

@Composable
private fun DiscoverCommunityCard(
    community: Community,
    onJoinClick: () -> Unit
) {
    val emoji = getCommunityEmoji(community.sportCategory)
    val gradient = getCommunityGradient(community.sportCategory)
    val accentColor = getCommunityColor(community.sportCategory)

    Surface(
        modifier = Modifier
            .width(180.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Lead.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = CascadingWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Gradient background icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        brush = Brush.linearGradient(gradient),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = gradient.first().copy(alpha = 0.4f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 36.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Community Name
            Text(
                text = community.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Lead,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.height(42.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category tag
            Box(
                modifier = Modifier
                    .background(
                        color = accentColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = community.sportCategory,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = Lead.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Member count
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Group,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = WarmHaze.copy(alpha = 0.7f)
                )
                Text(
                    text = "${community.memberCount} members",
                    style = MaterialTheme.typography.bodySmall,
                    color = WarmHaze.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Join Button
            Button(
                onClick = onJoinClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Crunch,
                    contentColor = Lead
                ),
                contentPadding = PaddingValues(vertical = 12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = "Join",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ==================== PREMIUM FLOATING BUTTON ====================

@Composable
private fun PremiumFloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "fab")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(modifier = modifier) {
        // Glow effect
        Box(
            modifier = Modifier
                .size(64.dp)
                .scale(pulseScale)
                .blur(12.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Crunch.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        
        // Main FAB
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .size(60.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    spotColor = Crunch.copy(alpha = 0.4f)
                ),
            shape = CircleShape,
            containerColor = Crunch,
            contentColor = Lead,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Create Community",
                modifier = Modifier.size(28.dp)
            )
        }
        
        // Premium Lock Badge (optional - uncomment to show)
        // Box(
        //     modifier = Modifier
        //         .align(Alignment.TopEnd)
        //         .offset(x = 4.dp, y = (-4).dp)
        //         .size(22.dp)
        //         .background(Lead, CircleShape)
        //         .border(2.dp, CascadingWhite, CircleShape),
        //     contentAlignment = Alignment.Center
        // ) {
        //     Icon(
        //         imageVector = Icons.Rounded.Lock,
        //         contentDescription = "Premium",
        //         modifier = Modifier.size(12.dp),
        //         tint = Crunch
        //     )
        // }
    }
}

// ==================== JOIN DIALOG ====================

@Composable
private fun PremiumJoinDialog(
    community: Community,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val emoji = getCommunityEmoji(community.sportCategory)
    val gradient = getCommunityGradient(community.sportCategory)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(28.dp)
                ),
            shape = RoundedCornerShape(28.dp),
            color = CascadingWhite
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Emoji with gradient background
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            brush = Brush.linearGradient(gradient),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 40.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Join Community?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Lead
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = community.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = Lead.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Group,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = WarmHaze
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${community.memberCount} members",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmHaze
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Cancel button
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, ChineseSilver),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            color = WarmHaze,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Join button
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Crunch,
                            contentColor = Lead
                        ),
                        contentPadding = PaddingValues(vertical = 14.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Text(
                            text = "Join",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ==================== FULLSCREEN IMAGE VIEWER ====================

@Composable
private fun FullscreenImageViewer(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = CascadingWhite,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Image
            AsyncImage(
                model = imageUrl,
                contentDescription = "Full image",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )
        }
    }
}

// ==================== UTILITY COMPONENTS ====================

@Composable
private fun EmptyStateCard(
    emoji: String,
    message: String,
    subtitle: String? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = SoftLavender.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = WarmHaze,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
            subtitle?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = WarmHaze.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ErrorStateCard(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFEE2E2)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Error,
                contentDescription = null,
                tint = Color(0xFFDC2626),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFDC2626)
            )
        }
    }
}
