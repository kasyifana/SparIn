package com.example.sparin.presentation.community

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sparin.data.model.Community
import com.example.sparin.presentation.community.CommunityViewModel
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.ui.theme.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * CommunityScreen - Displays all communities with real Firestore data
 * Allows users to browse, join, and create communities
 */

// Sport categories for filtering
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
    SportCategory("Tennis", "🎾"),
    SportCategory("Gym", "💪"),
    SportCategory("Running", "🏃"),
    SportCategory("Cycling", "🚴")
)

// Helper: Get emoji from sport category
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

// Helper: Get accent color from sport category
private fun getCommunityColor(sportCategory: String): Color {
    return when (sportCategory.lowercase()) {
        "badminton" -> PeachGlow
        "futsal", "football" -> MintBreeze
        "basket", "basketball" -> SkyMist
        "tennis" -> RoseDust
        "voli", "volleyball" -> SoftLavender
        "gym" -> ChineseSilver
        "running" -> PeachGlow
        "cycling" -> MintBreeze
        else -> Dreamland
    }
}

// ==================== MAIN SCREEN ====================

@Composable
fun CommunityScreen(
    navController: NavHostController,
    viewModel: CommunityViewModel = org.koin.androidx.compose.koinViewModel()
) {
    // ViewModel states
    val allCommunitiesState by viewModel.allCommunitiesState.collectAsState()
    val userCommunitiesState by viewModel.userCommunitiesState.collectAsState()
    
    // Local UI state
    var selectedCategory by remember { mutableStateOf("All") }
    val scrollState = rememberScrollState()
    var showJoinDialog by remember { mutableStateOf(false) }
    var selectedCommunityToJoin by remember { mutableStateOf<Community?>(null) }
    
    // Load data on first composition
    LaunchedEffect(Unit) {
        viewModel.loadAllCommunities()
        viewModel.loadUserCommunities()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create_community") },
                containerColor = Crunch,
                contentColor = CascadingWhite,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Create Community",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(CascadingWhite, SoftLavender.copy(alpha = 0.2f))
                    )
                )
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Header
                CommunityHeader()

                Spacer(modifier = Modifier.height(20.dp))

                // Category Tabs
                CategoryTabsSection(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // My Communities
                MyCommunitiesSection(
                    userCommunitiesState = userCommunitiesState,
                    navController = navController
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Popular Communities
                PopularCommunitiesSection(
                    allCommunitiesState = allCommunitiesState,
                    userCommunitiesState = userCommunitiesState,
                    selectedCategory = selectedCategory,
                    onJoinClick = { community ->
                        selectedCommunityToJoin = community
                        showJoinDialog = true
                    }
                )

                Spacer(modifier = Modifier.height(100.dp))
            }
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
    }
}

// ==================== HEADER ====================

@Composable
private fun CommunityHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Community Hub",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Lead
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "Join groups and find your people",
            style = MaterialTheme.typography.bodyMedium,
            color = WarmHaze
        )
    }
}

// ==================== CATEGORY TABS ====================

@Composable
private fun CategoryTabsSection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        sportCategories.forEach { category ->
            CategoryChip(
                category = category,
                isSelected = selectedCategory == category.name,
                onClick = { onCategorySelected(category.name) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    category: SportCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Crunch else CascadingWhite,
        shadowElevation = if (isSelected) 4.dp else 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = category.emoji, fontSize = 16.sp)
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) Lead else WarmHaze
            )
        }
    }
}

// ==================== MY COMMUNITIES ====================

@Composable
private fun MyCommunitiesSection(
    userCommunitiesState: CommunitiesState,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "My Communities",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Lead
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (userCommunitiesState) {
            is CommunitiesState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Crunch)
                }
            }
            is CommunitiesState.Success -> {
                if (userCommunitiesState.communities.isEmpty()) {
                    EmptyState(message = "You haven't joined any communities yet")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        userCommunitiesState.communities.forEach { community ->
                            JoinedCommunityCard(
                                community = community,
                                onOpenClick = {
                                    if (community.id.isNotEmpty()) {
                                        val encodedName = URLEncoder.encode(community.name, StandardCharsets.UTF_8.toString())
                                        val emoji = getCommunityEmoji(community.sportCategory)
                                        val encodedEmoji = URLEncoder.encode(emoji, StandardCharsets.UTF_8.toString())
                                        navController.navigate(Screen.CommunityFeed.createRoute(community.id, encodedName, encodedEmoji))
                                    } else {
                                        android.util.Log.e("CommunityScreen", "Community ID is empty for ${community.name}")
                                    }
                                }
                            )
                        }
                    }
                }
            }
            is CommunitiesState.Error -> {
                ErrorState(message = userCommunitiesState.message)
            }
        }
    }
}

@Composable
private fun JoinedCommunityCard(
    community: Community,
    onOpenClick: () -> Unit
) {
    val emoji = getCommunityEmoji(community.sportCategory)
    val accentColor = getCommunityColor(community.sportCategory)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenClick),
        shape = RoundedCornerShape(20.dp),
        color = CascadingWhite,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = accentColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = community.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Lead,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Group,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = WarmHaze
                        )
                        Text(
                            text = "${community.memberCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmHaze
                        )
                    }

                    Text(
                        text = "• ${community.sportCategory}",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmHaze
                    )
                }
            }

            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = "Open",
                tint = Crunch,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ==================== POPULAR COMMUNITIES ====================

@Composable
private fun PopularCommunitiesSection(
    allCommunitiesState: CommunitiesState,
    userCommunitiesState: CommunitiesState,
    selectedCategory: String,
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
            Text(
                text = "Popular Communities",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Lead
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
                    CircularProgressIndicator(color = Crunch)
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

                if (filteredCommunities.isEmpty()) {
                    EmptyState(message = "No communities available")
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        filteredCommunities.forEach { community ->
                            PopularCommunityCard(
                                community = community,
                                onJoinClick = { onJoinClick(community) }
                            )
                        }
                    }
                }
            }
            is CommunitiesState.Error -> {
                ErrorState(message = allCommunitiesState.message)
            }
        }
    }
}

@Composable
private fun PopularCommunityCard(
    community: Community,
    onJoinClick: () -> Unit
) {
    val emoji = getCommunityEmoji(community.sportCategory)
    val accentColor = getCommunityColor(community.sportCategory)

    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp),
        shape = RoundedCornerShape(20.dp),
        color = CascadingWhite,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = accentColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = community.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Lead,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.height(40.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Group,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = WarmHaze
                )
                Text(
                    text = "${community.memberCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = WarmHaze
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onJoinClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Crunch,
                    contentColor = CascadingWhite
                ),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text(
                    text = "Join",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ==================== JOIN DIALOG ====================

@Composable
private fun JoinCommunityDialog(
    community: Community,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val emoji = getCommunityEmoji(community.sportCategory)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = emoji,
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Join Community?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = community.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
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
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${community.memberCount} members",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmHaze
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Crunch
                )
            ) {
                Text("Join")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = WarmHaze)
            }
        }
    )
}

// ==================== UTILITY COMPONENTS ====================

@Composable
private fun EmptyState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = WarmHaze,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: $message",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
    }
}
