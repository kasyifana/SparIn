package com.example.sparin.presentation.community

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
 * CommunityScreen - Sport-Tech Premium Design
 * 
 * Professional, modern, ultra-clean Community Page for SparIN
 * Design Style: Sporty, Premium, Gen-Z Mature
 * 
 * Inspiration: Apple Fitness + Strava Clubs + Nike Training Club
 * 
 * Features:
 * - Clean geometry with premium spacing
 * - Micro-shadows with monochrome accents
 * - Subtle gradients (sport-tech aesthetic)
 * - Professional floating cards
 * - Modern segmented control tabs
 */

// ==================== SPORT CATEGORY DATA ====================

private data class SportTab(
    val id: String,
    val label: String,
    val icon: String
)

private val sportTabs = listOf(
    SportTab("all", "All", "✨"),
    SportTab("badminton", "Badminton", "🏸"),
    SportTab("futsal", "Futsal", "⚽"),
    SportTab("basket", "Basket", "🏀"),
    SportTab("running", "Running", "🏃"),
    SportTab("gym", "Gym", "💪"),
    SportTab("tennis", "Tennis", "🎾"),
    SportTab("voli", "Voli", "🏐"),
    SportTab("cycling", "Cycling", "🚴")
)

// ==================== HELPER FUNCTIONS ====================

private fun getSportAccentColor(category: String): Color {
    return when (category.lowercase()) {
        "badminton" -> BadmintonAmber
        "futsal", "football" -> FutsalEmerald
        "basket", "basketball" -> BasketCoral
        "tennis" -> TennisTeal
        "voli", "volleyball" -> VolleyOrchid
        "gym" -> GymTaupe
        "running" -> RunningGold
        "cycling" -> CyclingAzure
        else -> TitaniumGray
    }
}

private fun getSportIcon(category: String): String {
    return when (category.lowercase()) {
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

private fun formatCreatedDate(timestamp: Timestamp): String {
    return try {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        sdf.format(timestamp.toDate())
    } catch (e: Exception) {
        "Unknown"
    }
}

private fun formatMemberCount(count: Int): String {
    return when {
        count >= 1000 -> "${count / 1000}k"
        else -> count.toString()
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
    val userNamesCache by viewModel.userNamesCache.collectAsState()
    
    var selectedTab by remember { mutableStateOf("all") }
    var searchQuery by remember { mutableStateOf("") }
    var showJoinDialog by remember { mutableStateOf(false) }
    var selectedCommunityToJoin by remember { mutableStateOf<Community?>(null) }
    var showCommunityDetail by remember { mutableStateOf<Community?>(null) }
    
    val scrollState = rememberScrollState()
    
    val getCreatorName: (String) -> String = { userId ->
        viewModel.getUserDisplayName(userId)
    }
    
    LaunchedEffect(Unit) {
        viewModel.loadAllCommunities()
        viewModel.loadUserCommunities()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CascadingWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Premium Floating Header
            PremiumHeader(
                onFilterClick = { /* Handle filter */ }
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Modern Segmented Control Tabs
            SportTabBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
            
            Spacer(modifier = Modifier.height(28.dp))
            
            // My Communities Section
            MyCommunitySection(
                userCommunitiesState = userCommunitiesState,
                searchQuery = searchQuery,
                selectedTab = selectedTab,
                onCommunityClick = { community ->
                    if (community.id.isNotEmpty()) {
                        val encodedName = URLEncoder.encode(community.name, StandardCharsets.UTF_8.toString())
                        val icon = getSportIcon(community.sportCategory)
                        val encodedIcon = URLEncoder.encode(icon, StandardCharsets.UTF_8.toString())
                        navController.navigate(Screen.CommunityFeed.createRoute(community.id, encodedName, encodedIcon))
                    }
                },
                onDetailClick = { community -> showCommunityDetail = community },
                getCreatorName = getCreatorName
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Discover Communities Section
            DiscoverSection(
                allCommunitiesState = allCommunitiesState,
                userCommunitiesState = userCommunitiesState,
                selectedTab = selectedTab,
                searchQuery = searchQuery,
                onJoinClick = { community ->
                    selectedCommunityToJoin = community
                    showJoinDialog = true
                },
                getCreatorName = getCreatorName
            )
            
            Spacer(modifier = Modifier.height(120.dp))
        }
        
        // Premium Floating Create Button
        FloatingCreateButton(
            onClick = { navController.navigate("create_community") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 96.dp)
        )
    }
    
    // Join Dialog
    if (showJoinDialog && selectedCommunityToJoin != null) {
        JoinCommunityDialog(
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
    
    // Community Detail Dialog
    showCommunityDetail?.let { community ->
        CommunityDetailSheet(
            community = community,
            creatorName = getCreatorName(community.createdBy),
            onDismiss = { showCommunityDetail = null }
        )
    }
}

// ==================== PREMIUM HEADER ====================

@Composable
private fun PremiumHeader(
    onFilterClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp),
                spotColor = MistGray.copy(alpha = 0.1f)
            ),
        color = CascadingWhite.copy(alpha = 0.95f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Back/Profile icon (subtle)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = SmokySilver.copy(alpha = 0.5f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Profile",
                    tint = TitaniumGray,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            // Center: Title
            Text(
                text = "Community",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = NeutralInk,
                letterSpacing = (-0.5).sp
            )
            
            // Right: Filter icon (iOS-style thin line)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onFilterClick() }
                    .background(
                        color = SmokySilver.copy(alpha = 0.5f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.FilterList,
                    contentDescription = "Filter",
                    tint = TitaniumGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ==================== SPORT TAB BAR ====================

@Composable
private fun SportTabBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(sportTabs) { tab ->
            SportTabPill(
                tab = tab,
                isSelected = selectedTab == tab.id,
                onClick = { onTabSelected(tab.id) }
            )
        }
    }
}

@Composable
private fun SportTabPill(
    tab: SportTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.98f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "tabScale"
    )
    
    Surface(
        onClick = onClick,
        modifier = Modifier
            .scale(animatedScale)
            .height(40.dp),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) NeutralInk else CoolSteel.copy(alpha = 0.6f),
        border = if (!isSelected) BorderStroke(1.dp, ShadowMist) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = tab.icon,
                fontSize = 14.sp
            )
            Text(
                text = tab.label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) IceWhite else TitaniumGray,
                letterSpacing = 0.2.sp
            )
        }
        
        // Gold underline glow for selected
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 2.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(2.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Crunch.copy(alpha = 0f),
                                    Crunch,
                                    Crunch.copy(alpha = 0f)
                                )
                            ),
                            shape = RoundedCornerShape(1.dp)
                        )
                )
            }
        }
    }
}

// ==================== MY COMMUNITIES SECTION ====================

@Composable
private fun MyCommunitySection(
    userCommunitiesState: CommunitiesState,
    searchQuery: String,
    selectedTab: String,
    onCommunityClick: (Community) -> Unit,
    onDetailClick: (Community) -> Unit,
    getCreatorName: (String) -> String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Accent stripe
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(20.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Crunch, GoldenAmber)
                            ),
                            shape = RoundedCornerShape(1.5.dp)
                        )
                )
                Text(
                    text = "My Communities",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NeutralInk,
                    letterSpacing = (-0.3).sp
                )
            }
            
            when (userCommunitiesState) {
                is CommunitiesState.Success -> {
                    Text(
                        text = "${userCommunitiesState.communities.size} joined",
                        style = MaterialTheme.typography.labelSmall,
                        color = TitaniumGray,
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
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Crunch,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            is CommunitiesState.Success -> {
                val filteredCommunities = userCommunitiesState.communities.filter {
                    (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true)) &&
                    (selectedTab == "all" || it.sportCategory.equals(selectedTab, ignoreCase = true))
                }
                
                if (filteredCommunities.isEmpty()) {
                    EmptyStateCard(
                        message = if (searchQuery.isNotEmpty()) "No communities found" else "Join a community to get started",
                        icon = Icons.Outlined.Groups
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        filteredCommunities.forEach { community ->
                            PremiumCommunityCard(
                                community = community,
                                creatorName = getCreatorName(community.createdBy),
                                onClick = { onCommunityClick(community) },
                                onDetailClick = { onDetailClick(community) }
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
    creatorName: String,
    onClick: () -> Unit,
    onDetailClick: () -> Unit
) {
    val accentColor = getSportAccentColor(community.sportCategory)
    val sportIcon = getSportIcon(community.sportCategory)
    
    var isPressed by remember { mutableStateOf(false) }
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "cardPress"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = accentColor.copy(alpha = 0.12f),
                ambientColor = MistGray.copy(alpha = 0.06f)
            ),
        shape = RoundedCornerShape(16.dp),
        color = IceWhite,
        border = BorderStroke(1.5.dp, accentColor.copy(alpha = 0.08f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Premium Header Section with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.06f),
                                accentColor.copy(alpha = 0.02f)
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Community Name - Premium Typography
                        Text(
                            text = community.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = NeutralInk,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            letterSpacing = (-0.3).sp
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Category + Creator in elegant row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Category pill - Enhanced
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = accentColor.copy(alpha = 0.14f),
                                border = BorderStroke(0.8.dp, accentColor.copy(alpha = 0.2f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(text = sportIcon, fontSize = 12.sp)
                                    Text(
                                        text = community.sportCategory,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = accentColor,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            
                            // Creator info - Minimal
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = TitaniumGray
                                )
                                Text(
                                    text = creatorName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TitaniumGray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                    
                    // Premium Sport Icon Badge
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = 0.18f),
                                        accentColor.copy(alpha = 0.06f)
                                    )
                                ),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .border(
                                width = 1.2.dp,
                                color = accentColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = sportIcon, fontSize = 26.sp)
                    }
                }
            }
            
            // Subtle divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.8.dp)
                    .background(ShadowMist.copy(alpha = 0.3f))
            )
            
            // Bottom Content Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Members Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Member count badge - Premium variant
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = accentColor.copy(alpha = 0.08f),
                        border = BorderStroke(0.8.dp, accentColor.copy(alpha = 0.12f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Group,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = accentColor
                            )
                            Text(
                                text = "${formatMemberCount(community.memberCount)} Members",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = NeutralInk,
                                fontSize = 12.sp
                            )
                        }
                    }
                    
                    // Created date
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = TitaniumGray
                        )
                        Text(
                            text = formatCreatedDate(community.createdAt),
                            style = MaterialTheme.typography.labelSmall,
                            color = TitaniumGray,
                            fontSize = 10.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Right: Avatar Stack + Action
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Avatar preview
                    MemberAvatarStack(
                        memberCount = community.memberCount,
                        accentColor = accentColor
                    )
                    
                    // Premium Action Button
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = NeutralInk
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = "Open",
                                tint = IceWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== MEMBER AVATAR STACK ====================

@Composable
private fun MemberAvatarStack(
    memberCount: Int,
    accentColor: Color
) {
    val displayCount = minOf(memberCount, 4)
    if (memberCount == 0) return
    
    Row {
        (0 until displayCount).forEach { index ->
            Box(
                modifier = Modifier
                    .offset(x = (-(index * 8)).dp)
                    .size(24.dp)
                    .zIndex((displayCount - index).toFloat())
                    .border(
                        width = 1.5.dp,
                        color = IceWhite,
                        shape = CircleShape
                    )
                    .background(
                        color = accentColor.copy(alpha = 0.15f + (index * 0.1f)),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = accentColor.copy(alpha = 0.7f)
                )
            }
        }
        
        if (memberCount > 4) {
            Box(
                modifier = Modifier
                    .offset(x = (-(displayCount * 8)).dp)
                    .size(24.dp)
                    .zIndex(0f)
                    .border(1.5.dp, IceWhite, CircleShape)
                    .background(NeutralInk, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+${memberCount - 4}",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = IceWhite
                )
            }
        }
    }
}

// ==================== DISCOVER SECTION ====================

@Composable
private fun DiscoverSection(
    allCommunitiesState: CommunitiesState,
    userCommunitiesState: CommunitiesState,
    selectedTab: String,
    searchQuery: String,
    onJoinClick: (Community) -> Unit,
    getCreatorName: (String) -> String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(20.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(FutsalEmerald, TennisTeal)
                            ),
                            shape = RoundedCornerShape(1.5.dp)
                        )
                )
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NeutralInk,
                    letterSpacing = (-0.3).sp
                )
            }
            
            Text(
                text = if (selectedTab == "all") "All sports" else selectedTab.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.labelSmall,
                color = TitaniumGray,
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (allCommunitiesState) {
            is CommunitiesState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Crunch,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            is CommunitiesState.Success -> {
                val joinedIds = when (userCommunitiesState) {
                    is CommunitiesState.Success -> userCommunitiesState.communities.map { it.id }.toSet()
                    else -> emptySet()
                }
                
                val filteredCommunities = allCommunitiesState.communities
                    .filter { it.id !in joinedIds }
                    .filter { 
                        selectedTab == "all" || it.sportCategory.equals(selectedTab, ignoreCase = true)
                    }
                    .filter { 
                        searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true)
                    }
                
                if (filteredCommunities.isEmpty()) {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        EmptyStateCard(
                            message = "No communities to discover",
                            icon = Icons.Outlined.Explore
                        )
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(filteredCommunities) { community ->
                            DiscoverCard(
                                community = community,
                                creatorName = getCreatorName(community.createdBy),
                                onJoinClick = { onJoinClick(community) }
                            )
                        }
                    }
                }
            }
            is CommunitiesState.Error -> {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ErrorStateCard(message = allCommunitiesState.message)
                }
            }
        }
    }
}

// ==================== DISCOVER CARD ====================

@Composable
private fun DiscoverCard(
    community: Community,
    creatorName: String,
    onJoinClick: () -> Unit
) {
    val accentColor = getSportAccentColor(community.sportCategory)
    val sportIcon = getSportIcon(community.sportCategory)
    
    Surface(
        modifier = Modifier
            .width(220.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = accentColor.copy(alpha = 0.13f),
                ambientColor = MistGray.copy(alpha = 0.06f)
            ),
        shape = RoundedCornerShape(18.dp),
        color = IceWhite,
        border = BorderStroke(1.5.dp, accentColor.copy(alpha = 0.09f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Premium Header with gradient background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.08f),
                                accentColor.copy(alpha = 0.02f)
                            )
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Premium Sport Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.2f),
                                    accentColor.copy(alpha = 0.08f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.2.dp,
                            color = accentColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = sportIcon, fontSize = 32.sp)
                }
            }
            
            // Subtle divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.8.dp)
                    .background(ShadowMist.copy(alpha = 0.25f))
            )
            
            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Community Name - Premium Typography
                Text(
                    text = community.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = NeutralInk,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    letterSpacing = (-0.2).sp,
                    modifier = Modifier.height(44.dp)
                )
                
                // Category tag - Enhanced with border
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor.copy(alpha = 0.14f),
                    border = BorderStroke(0.8.dp, accentColor.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = sportIcon, fontSize = 11.sp)
                        Text(
                            text = community.sportCategory,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = accentColor,
                            fontSize = 10.sp
                        )
                    }
                }
                
                // Stats Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = SmokySilver.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Group,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = accentColor
                        )
                        Text(
                            text = "${formatMemberCount(community.memberCount)}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = NeutralInk,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "members",
                            style = MaterialTheme.typography.labelSmall,
                            color = TitaniumGray,
                            fontSize = 9.sp
                        )
                    }
                }
                
                // Creator info
                Text(
                    text = "by $creatorName",
                    style = MaterialTheme.typography.labelSmall,
                    color = TitaniumGray,
                    fontSize = 9.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Premium Join Button
                Button(
                    onClick = onJoinClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(11.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeutralInk,
                        contentColor = IceWhite
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 3.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PersonAdd,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )
                        Text(
                            text = "Join Now",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

// ==================== FLOATING CREATE BUTTON ====================

@Composable
private fun FloatingCreateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Soft glow
        Box(
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    alpha = 0.3f
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Crunch, Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )
        
        // Main button
        Surface(
            onClick = onClick,
            modifier = Modifier
                .size(56.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = Crunch.copy(alpha = 0.4f)
                ),
            shape = CircleShape,
            color = NeutralInk
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Create Community",
                    modifier = Modifier.size(26.dp),
                    tint = IceWhite
                )
            }
        }
        
        // Gold accent ring
        Box(
            modifier = Modifier
                .size(56.dp)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Crunch, GoldenAmber)
                    ),
                    shape = CircleShape
                )
        )
    }
}

// ==================== JOIN DIALOG ====================

@Composable
private fun JoinCommunityDialog(
    community: Community,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val accentColor = getSportAccentColor(community.sportCategory)
    val sportIcon = getSportIcon(community.sportCategory)
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = MistGray.copy(alpha = 0.2f)
                ),
            shape = RoundedCornerShape(24.dp),
            color = IceWhite
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sport Icon
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.2f),
                                    accentColor.copy(alpha = 0.08f)
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = sportIcon, fontSize = 36.sp)
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "Join Community?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = NeutralInk
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = community.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = TitaniumGray,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Member count badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SmokySilver.copy(alpha = 0.6f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Group,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = TitaniumGray
                        )
                        Text(
                            text = "${community.memberCount} members",
                            style = MaterialTheme.typography.labelMedium,
                            color = NeutralInk
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(28.dp))
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Cancel
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, ShadowMist),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TitaniumGray
                        )
                    ) {
                        Text(
                            text = "Cancel",
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // Join
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeutralInk,
                            contentColor = IceWhite
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Join",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== COMMUNITY DETAIL SHEET ====================

@Composable
private fun CommunityDetailSheet(
    community: Community,
    creatorName: String,
    onDismiss: () -> Unit
) {
    val accentColor = getSportAccentColor(community.sportCategory)
    val sportIcon = getSportIcon(community.sportCategory)
    
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
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            SlateCharcoal.copy(alpha = 0.95f),
                            NeutralInk.copy(alpha = 0.98f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Premium Header with close button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(40.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                                spotColor = accentColor.copy(alpha = 0.2f)
                            ),
                        shape = CircleShape,
                        color = IceWhite.copy(alpha = 0.12f),
                        border = BorderStroke(1.2.dp, IceWhite.copy(alpha = 0.25f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close",
                                tint = IceWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    
                    Text(
                        text = "Community Info",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = IceWhite,
                        letterSpacing = (-0.2).sp
                    )
                    
                    Spacer(modifier = Modifier.size(40.dp))
                }
                
                Spacer(modifier = Modifier.height(28.dp))
                
                // Main Premium Content Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .shadow(
                            elevation = 14.dp,
                            shape = RoundedCornerShape(26.dp),
                            spotColor = accentColor.copy(alpha = 0.18f),
                            ambientColor = NeutralInk.copy(alpha = 0.1f)
                        ),
                    shape = RoundedCornerShape(26.dp),
                    color = IceWhite.copy(alpha = 0.09f),
                    border = BorderStroke(1.2.dp, IceWhite.copy(alpha = 0.16f))
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Premium Sport Icon with gradient background
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            accentColor.copy(alpha = 0.32f),
                                            accentColor.copy(alpha = 0.12f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(28.dp)
                                )
                                .border(
                                    width = 1.5.dp,
                                    color = accentColor.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(28.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = sportIcon, fontSize = 48.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Community Name - Premium Typography
                        Text(
                            text = community.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = IceWhite,
                            textAlign = TextAlign.Center,
                            letterSpacing = (-0.3).sp
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Category tag - Premium variant
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = accentColor.copy(alpha = 0.28f),
                            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = sportIcon, fontSize = 12.sp)
                                Text(
                                    text = community.sportCategory,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = IceWhite,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Premium Stats Row with enhanced design
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            IceWhite.copy(alpha = 0.08f),
                                            IceWhite.copy(alpha = 0.03f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                    width = 0.8.dp,
                                    color = IceWhite.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 20.dp, vertical = 20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem(
                                value = "${community.memberCount}",
                                label = "Members",
                                icon = Icons.Outlined.Group,
                                color = accentColor
                            )
                            
                            Box(
                                modifier = Modifier
                                    .width(0.8.dp)
                                    .height(50.dp)
                                    .background(IceWhite.copy(alpha = 0.12f))
                            )
                            
                            StatItem(
                                value = formatCreatedDate(community.createdAt),
                                label = "Created",
                                icon = Icons.Outlined.CalendarToday,
                                color = accentColor
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(28.dp))
                
                // Premium Info Section - Creator & Description
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(20.dp),
                            spotColor = accentColor.copy(alpha = 0.12f)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    color = IceWhite.copy(alpha = 0.07f),
                    border = BorderStroke(1.dp, IceWhite.copy(alpha = 0.12f))
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        // Creator Section
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                accentColor.copy(alpha = 0.3f),
                                                accentColor.copy(alpha = 0.12f)
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 1.2.dp,
                                        color = accentColor.copy(alpha = 0.2f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    tint = IceWhite.copy(alpha = 0.8f),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            
                            Column {
                                Text(
                                    text = "Created by",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = IceWhite.copy(alpha = 0.6f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = creatorName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = IceWhite,
                                    letterSpacing = (-0.1).sp
                                )
                            }
                        }
                        
                        if (community.description.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.8.dp)
                                    .background(IceWhite.copy(alpha = 0.1f))
                            )
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Info,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = accentColor
                                    )
                                    Text(
                                        text = "About",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = IceWhite.copy(alpha = 0.85f),
                                        fontSize = 12.sp
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(10.dp))
                                
                                Text(
                                    text = community.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = IceWhite.copy(alpha = 0.8f),
                                    lineHeight = 24.sp,
                                    letterSpacing = (0.2).sp
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

// Helper composable for stats
@Composable
fun StatItem(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = color
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = IceWhite,
                fontSize = 13.sp
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = IceWhite.copy(alpha = 0.6f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ==================== EMPTY & ERROR STATES ====================

@Composable
private fun EmptyStateCard(
    message: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SmokySilver.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, ShadowMist.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = TitaniumGray
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = TitaniumGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ErrorStateCard(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = BasketCoral.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, BasketCoral.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Error,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = BasketCoral
            )
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = BasketCoral
            )
        }
    }
}
