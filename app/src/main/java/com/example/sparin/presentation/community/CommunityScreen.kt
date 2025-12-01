package com.example.sparin.presentation.community

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import kotlin.math.cos
import kotlin.math.sin

/**
 * CommunityScreen - Ultra Premium Gen-Z Aesthetic
 * Features: Glassmorphism, Floating Search, Neumorphic Cards, Member Stack Preview
 * Design: Soft-sport pastel gradients, dreamy shadows, floating elements
 */

// Sport categories for filtering with premium gradients
data class SportCategory(
    val name: String,
    val emoji: String,
    val gradient: List<Color>,
    val accentColor: Color
)

val sportCategories = listOf(
    SportCategory("All", "✨", listOf(Color(0xFFF8D5A3), Color(0xFFF3BA60)), Crunch),
    SportCategory("Badminton", "🏸", listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2)), Color(0xFFFFB74D)),
    SportCategory("Futsal", "⚽", listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9)), Color(0xFF81C784)),
    SportCategory("Basket", "🏀", listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2)), Color(0xFFE57373)),
    SportCategory("Voli", "🏐", listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7)), Color(0xFFBA68C8)),
    SportCategory("Tennis", "🎾", listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2)), Color(0xFF4DD0E1)),
    SportCategory("Gym", "💪", listOf(Color(0xFFEFEBE9), Color(0xFFD7CCC8)), Color(0xFFA1887F)),
    SportCategory("Running", "🏃", listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3)), Color(0xFFFFD54F)),
    SportCategory("Cycling", "🚴", listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC)), Color(0xFF4FC3F7))
)

// Helper functions with enhanced gradients
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
        "swimming" -> "🏊"
        else -> "🏅"
    }
}

private fun getCommunityGradient(sportCategory: String): List<Color> {
    return when (sportCategory.lowercase()) {
        "badminton" -> listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2), Color(0xFFFFCC80))
        "futsal", "football" -> listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9), Color(0xFFA5D6A7))
        "basket", "basketball" -> listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2), Color(0xFFEF9A9A))
        "tennis" -> listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2), Color(0xFF80DEEA))
        "voli", "volleyball" -> listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7), Color(0xFFCE93D8))
        "gym" -> listOf(Color(0xFFEFEBE9), Color(0xFFD7CCC8), Color(0xFFBCAAA4))
        "running" -> listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3), Color(0xFFFFE082))
        "cycling" -> listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC), Color(0xFF81D4FA))
        else -> listOf(Color(0xFFF5F5F5), Color(0xFFE0E0E0), Color(0xFFBDBDBD))
    }
}

private fun getCommunityColor(sportCategory: String): Color {
    return when (sportCategory.lowercase()) {
        "badminton" -> Color(0xFFFFB74D)
        "futsal", "football" -> Color(0xFF81C784)
        "basket", "basketball" -> Color(0xFFE57373)
        "tennis" -> Color(0xFF4DD0E1)
        "voli", "volleyball" -> Color(0xFFBA68C8)
        "gym" -> Color(0xFFA1887F)
        "running" -> Color(0xFFFFD54F)
        "cycling" -> Color(0xFF4FC3F7)
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

private fun formatRelativeDate(timestamp: Timestamp): String {
    return try {
        val now = System.currentTimeMillis()
        val diff = now - timestamp.toDate().time
        when {
            diff < 60000 -> "Just now"
            diff < 3600000 -> "${diff / 60000}m ago"
            diff < 86400000 -> "${diff / 3600000}h ago"
            diff < 604800000 -> "${diff / 86400000}d ago"
            else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(timestamp.toDate())
        }
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
    var showCommunityDetail by remember { mutableStateOf<Community?>(null) }
    
    LaunchedEffect(Unit) {
        viewModel.loadAllCommunities()
        viewModel.loadUserCommunities()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CascadingWhite)
    ) {
        // Animated Background Blobs
        PremiumBackgroundBlobs()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Premium Header with Glow
            PremiumHeader()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Floating Glassmorphism Search Bar
            GlassmorphicSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Premium Category Pills with Inner Glow
            PremiumCategoryPills(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // My Communities Section with Premium Cards
            MyCommunitySection(
                userCommunitiesState = userCommunitiesState,
                navController = navController,
                searchQuery = searchQuery,
                onCommunityClick = { community ->
                    if (community.id.isNotEmpty()) {
                        val encodedName = URLEncoder.encode(community.name, StandardCharsets.UTF_8.toString())
                        val emoji = getCommunityEmoji(community.sportCategory)
                        val encodedEmoji = URLEncoder.encode(emoji, StandardCharsets.UTF_8.toString())
                        navController.navigate(Screen.CommunityFeed.createRoute(community.id, encodedName, encodedEmoji))
                    }
                },
                onImageClick = { url -> showFullscreenImage = url }
            )
            
            Spacer(modifier = Modifier.height(36.dp))
            
            // Discover Communities Section
            DiscoverCommunitiesSection(
                allCommunitiesState = allCommunitiesState,
                userCommunitiesState = userCommunitiesState,
                selectedCategory = selectedCategory,
                searchQuery = searchQuery,
                onJoinClick = { community ->
                    selectedCommunityToJoin = community
                    showJoinDialog = true
                },
                onImageClick = { url -> showFullscreenImage = url }
            )
            
            Spacer(modifier = Modifier.height(140.dp))
        }
        
        // Premium Floating Create Button with Lock Badge
        PremiumFloatingCreateButton(
            onClick = { navController.navigate("create_community") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp)
        )
    }

    // Premium Join Dialog
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
    
    // Community Detail Dialog
    showCommunityDetail?.let { community ->
        CommunityDetailDialog(
            community = community,
            onDismiss = { showCommunityDetail = null },
            onImageClick = { url -> showFullscreenImage = url }
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

// ==================== ANIMATED BACKGROUND BLOBS ====================

@Composable
private fun PremiumBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "bg_blobs")
    
    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset1"
    )
    
    val offset2 by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset2"
    )
    
    val offset3 by infiniteTransition.animateFloat(
        initialValue = 90f,
        targetValue = 450f,
        animationSpec = infiniteRepeatable(
            animation = tween(35000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset3"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(100.dp)
    ) {
        // Peach/Gold glow - top area
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Crunch.copy(alpha = 0.15f),
                    PeachGlow.copy(alpha = 0.08f),
                    Color.Transparent
                )
            ),
            radius = 300f,
            center = Offset(
                x = size.width * 0.2f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 50f,
                y = size.height * 0.1f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 40f
            )
        )

        // Soft lavender glow - center right
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    SoftLavender.copy(alpha = 0.2f),
                    ChineseSilver.copy(alpha = 0.1f),
                    Color.Transparent
                )
            ),
            radius = 280f,
            center = Offset(
                x = size.width * 0.85f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 40f,
                y = size.height * 0.35f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 50f
            )
        )
        
        // Mint breeze glow - bottom left
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    MintBreeze.copy(alpha = 0.12f),
                    SkyMist.copy(alpha = 0.06f),
                    Color.Transparent
                )
            ),
            radius = 250f,
            center = Offset(
                x = size.width * 0.15f + cos(Math.toRadians(offset3.toDouble())).toFloat() * 35f,
                y = size.height * 0.7f + sin(Math.toRadians(offset3.toDouble())).toFloat() * 45f
            )
        )
        
        // Sky mist glow - bottom right
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    SkyMist.copy(alpha = 0.1f),
                    Color.Transparent
                )
            ),
            radius = 200f,
            center = Offset(
                x = size.width * 0.8f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f,
                y = size.height * 0.85f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 35f
            )
        )
    }
}

// ==================== PREMIUM HEADER ====================

@Composable
private fun PremiumHeader() {
    val infiniteTransition = rememberInfiniteTransition(label = "header")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Animated gradient icon with glow
            Box(contentAlignment = Alignment.Center) {
                // Glow effect
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .blur(12.dp)
                        .alpha(glowAlpha * 0.5f)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Crunch, Crunch.copy(alpha = 0f))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
                
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = Crunch.copy(alpha = 0.4f),
                            ambientColor = Crunch.copy(alpha = 0.2f)
                        )
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Crunch, SunsetOrange, PeachGlow)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌟", fontSize = 26.sp)
                }
            }
            
            Column {
                Text(
                    text = "Community",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Lead,
                    letterSpacing = (-0.5).sp
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(Crunch, Crunch.copy(alpha = 0.5f))
                                ),
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = "Connect with your sports tribe",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmHaze.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ==================== GLASSMORPHIC SEARCH BAR ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GlassmorphicSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Outer glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .blur(16.dp)
                .alpha(0.3f)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Crunch.copy(alpha = 0.2f),
                            SoftLavender.copy(alpha = 0.3f),
                            MintBreeze.copy(alpha = 0.2f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        )
        
        // Glassmorphism container
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = Lead.copy(alpha = 0.08f),
                    ambientColor = Lead.copy(alpha = 0.04f)
                ),
            shape = RoundedCornerShape(24.dp),
            color = CascadingWhite.copy(alpha = 0.92f),
            tonalElevation = 0.dp,
            border = BorderStroke(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        ChineseSilver.copy(alpha = 0.5f),
                        Dreamland.copy(alpha = 0.3f),
                        ChineseSilver.copy(alpha = 0.5f)
                    )
                )
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search icon with subtle glow
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    ChineseSilver.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search",
                        tint = WarmHaze,
                        modifier = Modifier.size(22.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search communities...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = WarmHaze.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    androidx.compose.foundation.text.BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = Lead,
                            fontWeight = FontWeight.Medium
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                AnimatedVisibility(
                    visible = query.isNotEmpty(),
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Surface(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onQueryChange("") },
                        shape = CircleShape,
                        color = ChineseSilver.copy(alpha = 0.5f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Clear",
                                tint = WarmHaze,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== PREMIUM CATEGORY PILLS ====================

@Composable
private fun PremiumCategoryPills(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sportCategories) { category ->
            PremiumCategoryPill(
                category = category,
                isSelected = selectedCategory == category.name,
                onClick = { onCategorySelected(category.name) }
            )
        }
    }
}

@Composable
private fun PremiumCategoryPill(
    category: SportCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "scale"
    )
    
    val shadowElevation by animateDpAsState(
        targetValue = if (isSelected) 16.dp else 4.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "elevation"
    )
    
    val infiniteTransition = rememberInfiniteTransition(label = "pill_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pill_glow_alpha"
    )

    Box(contentAlignment = Alignment.Center) {
        // Inner glow for selected state
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .scale(1.1f)
                    .blur(10.dp)
                    .alpha(glowAlpha)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                category.accentColor.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(18.dp)
                    )
            )
        }
        
        Surface(
            onClick = onClick,
            modifier = Modifier
                .scale(scale)
                .shadow(
                    elevation = shadowElevation,
                    shape = RoundedCornerShape(18.dp),
                    spotColor = if (isSelected) category.accentColor.copy(alpha = 0.4f) else Lead.copy(alpha = 0.08f),
                    ambientColor = if (isSelected) category.accentColor.copy(alpha = 0.2f) else Lead.copy(alpha = 0.04f)
                ),
            shape = RoundedCornerShape(18.dp),
            color = Color.Transparent,
            border = if (!isSelected) BorderStroke(
                1.5.dp,
                Brush.linearGradient(
                    colors = listOf(
                        ChineseSilver.copy(alpha = 0.6f),
                        Dreamland.copy(alpha = 0.4f)
                    )
                )
            ) else null
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = if (isSelected) {
                            Brush.linearGradient(
                                colors = category.gradient + listOf(category.gradient.last().copy(alpha = 0.9f))
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    CascadingWhite,
                                    ChineseSilver.copy(alpha = 0.2f)
                                )
                            )
                        }
                    )
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Emoji with mini background
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                color = if (isSelected) 
                                    CascadingWhite.copy(alpha = 0.6f) 
                                else 
                                    ChineseSilver.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = category.emoji, fontSize = 16.sp)
                    }
                    
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isSelected) Lead else WarmHaze,
                        letterSpacing = 0.3.sp
                    )
                }
            }
        }
    }
}

// ==================== MY COMMUNITIES SECTION ====================

@Composable
private fun MyCommunitySection(
    userCommunitiesState: CommunitiesState,
    navController: NavHostController,
    searchQuery: String,
    onCommunityClick: (Community) -> Unit,
    onImageClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Section Header with accent dot
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Glowing accent dot
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .blur(4.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(Crunch, Crunch.copy(alpha = 0f))
                                ),
                                shape = CircleShape
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(Crunch, SunsetOrange)
                                ),
                                shape = CircleShape
                            )
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "My Communities",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Lead,
                    letterSpacing = (-0.3).sp
                )
            }
            
            when (userCommunitiesState) {
                is CommunitiesState.Success -> {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ChineseSilver.copy(alpha = 0.4f)
                    ) {
                        Text(
                            text = "${userCommunitiesState.communities.size} joined",
                            style = MaterialTheme.typography.labelMedium,
                            color = WarmHaze,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
                else -> {}
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (userCommunitiesState) {
            is CommunitiesState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Premium loading indicator
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = Crunch,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(44.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .blur(12.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Crunch.copy(alpha = 0.3f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
            is CommunitiesState.Success -> {
                val filteredCommunities = userCommunitiesState.communities.filter {
                    searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true)
                }
                
                if (filteredCommunities.isEmpty()) {
                    PremiumEmptyStateCard(
                        emoji = if (searchQuery.isNotEmpty()) "🔍" else "🌟",
                        message = if (searchQuery.isNotEmpty()) 
                            "No communities found" 
                        else 
                            "You haven't joined any communities yet",
                        subtitle = if (searchQuery.isEmpty()) "Explore and join some!" else null
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        filteredCommunities.forEach { community ->
                            PremiumCommunityCard(
                                community = community,
                                onClick = { onCommunityClick(community) },
                                onImageClick = onImageClick
                            )
                        }
                    }
                }
            }
            is CommunitiesState.Error -> {
                PremiumErrorStateCard(message = userCommunitiesState.message)
            }
        }
    }
}

// ==================== PREMIUM COMMUNITY CARD ====================

@Composable
private fun PremiumCommunityCard(
    community: Community,
    onClick: () -> Unit,
    onImageClick: (String) -> Unit = {}
) {
    val emoji = getCommunityEmoji(community.sportCategory)
    val gradient = getCommunityGradient(community.sportCategory)
    val accentColor = getCommunityColor(community.sportCategory)
    
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "cardScale"
    )

    Box {
        // Soft shadow bloom
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 4.dp)
                .blur(20.dp)
                .alpha(0.15f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(accentColor, Dreamland)
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
        )
        
        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale),
            shape = RoundedCornerShape(28.dp),
            color = Color.Transparent,
            border = BorderStroke(
                1.5.dp,
                Brush.linearGradient(
                    colors = listOf(
                        ChineseSilver.copy(alpha = 0.5f),
                        accentColor.copy(alpha = 0.3f),
                        Dreamland.copy(alpha = 0.4f)
                    )
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                ChineseSilver.copy(alpha = 0.35f),
                                SoftLavender.copy(alpha = 0.25f),
                                gradient.first().copy(alpha = 0.2f)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
            ) {
                // Decorative gradient overlay at corner
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 40.dp, y = (-40).dp)
                        .blur(40.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Premium Emoji Icon with gradient background
                        Box(contentAlignment = Alignment.Center) {
                            // Glow effect
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .blur(12.dp)
                                    .alpha(0.4f)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                gradient.first(),
                                                Color.Transparent
                                            )
                                        ),
                                        shape = RoundedCornerShape(18.dp)
                                    )
                            )
                            
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .shadow(
                                        elevation = 12.dp,
                                        shape = RoundedCornerShape(18.dp),
                                        spotColor = gradient.first().copy(alpha = 0.5f)
                                    )
                                    .background(
                                        brush = Brush.linearGradient(gradient),
                                        shape = RoundedCornerShape(18.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 30.sp)
                            }
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
                                overflow = TextOverflow.Ellipsis,
                                letterSpacing = (-0.2).sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Category Tag with gradient border
                            Box(
                                modifier = Modifier
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                accentColor.copy(alpha = 0.2f),
                                                gradient.first().copy(alpha = 0.15f)
                                            )
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                accentColor.copy(alpha = 0.4f),
                                                gradient.first().copy(alpha = 0.3f)
                                            )
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = community.sportCategory,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Lead.copy(alpha = 0.85f)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Creator info & Creation date
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                // Creator
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .background(
                                                color = ChineseSilver.copy(alpha = 0.5f),
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Person,
                                            contentDescription = null,
                                            modifier = Modifier.size(12.dp),
                                            tint = WarmHaze
                                        )
                                    }
                                    Text(
                                        text = community.createdBy.take(15) + if (community.createdBy.length > 15) "..." else "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = WarmHaze,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                
                                // Date
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Schedule,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = WarmHaze.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = formatRelativeDate(community.createdAt),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = WarmHaze.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }

                        // Arrow indicator with neumorphic style
                        Surface(
                            modifier = Modifier
                                .size(40.dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = CircleShape,
                                    spotColor = Crunch.copy(alpha = 0.3f)
                                ),
                            shape = CircleShape,
                            color = Crunch.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Crunch.copy(alpha = 0.3f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.ChevronRight,
                                    contentDescription = "Open",
                                    tint = Crunch,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    
                    // Divider with gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Dreamland.copy(alpha = 0.5f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    // Bottom row: Cover photo, Member count, Member stack, Share button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Cover Photo Thumbnail (if available)
                            if (community.coverPhoto.isNotEmpty()) {
                                Surface(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .shadow(
                                            elevation = 6.dp,
                                            shape = RoundedCornerShape(12.dp),
                                            spotColor = Dreamland.copy(alpha = 0.3f)
                                        )
                                        .clickable { onImageClick(community.coverPhoto) },
                                    shape = RoundedCornerShape(12.dp),
                                    color = ChineseSilver.copy(alpha = 0.3f)
                                ) {
                                    AsyncImage(
                                        model = community.coverPhoto,
                                        contentDescription = "Cover",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            
                            // Member count badge with gradient
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Transparent,
                                border = BorderStroke(
                                    1.dp,
                                    Brush.linearGradient(
                                        colors = listOf(
                                            SoftLavender.copy(alpha = 0.6f),
                                            ChineseSilver.copy(alpha = 0.5f)
                                        )
                                    )
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    SoftLavender.copy(alpha = 0.5f),
                                                    ChineseSilver.copy(alpha = 0.3f)
                                                )
                                            )
                                        )
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Group,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = Lead.copy(alpha = 0.75f)
                                        )
                                        Text(
                                            text = "${community.memberCount} members",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Lead.copy(alpha = 0.85f)
                                        )
                                    }
                                }
                            }

                            // Mini profile stack preview
                            PremiumMemberStackPreview(
                                memberCount = community.memberCount,
                                accentColor = accentColor,
                                gradient = gradient
                            )
                        }

                        // Disabled Share button with soft styling
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Dreamland.copy(alpha = 0.3f),
                            modifier = Modifier.alpha(0.5f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
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
                                    color = WarmHaze.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== PREMIUM MEMBER STACK PREVIEW ====================

@Composable
private fun PremiumMemberStackPreview(
    memberCount: Int,
    accentColor: Color,
    gradient: List<Color>
) {
    val displayCount = minOf(memberCount, 5)
    val remainingCount = memberCount - displayCount
    
    if (memberCount == 0) return
    
    Row(
        modifier = Modifier.height(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            (0 until displayCount).forEach { index ->
                Box(
                    modifier = Modifier
                        .offset(x = (index * 16).dp)
                        .size(32.dp)
                        .zIndex((displayCount - index).toFloat())
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            spotColor = gradient.first().copy(alpha = 0.3f)
                        )
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    gradient.getOrElse(index % gradient.size) { accentColor }.copy(alpha = 0.9f),
                                    gradient.getOrElse((index + 1) % gradient.size) { accentColor }
                                )
                            ),
                            shape = CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = CascadingWhite,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val avatarEmojis = listOf("😊", "🏃", "💪", "🎯", "⭐")
                    Text(
                        text = avatarEmojis[index % avatarEmojis.size],
                        fontSize = 14.sp
                    )
                }
            }
            
            if (remainingCount > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = (displayCount * 16).dp)
                        .size(32.dp)
                        .zIndex(0f)
                        .shadow(
                            elevation = 6.dp,
                            shape = CircleShape,
                            spotColor = Lead.copy(alpha = 0.2f)
                        )
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Lead.copy(alpha = 0.85f), Lead)
                            ),
                            shape = CircleShape
                        )
                        .border(2.dp, CascadingWhite, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+$remainingCount",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
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
    onJoinClick: (Community) -> Unit,
    onImageClick: (String) -> Unit
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
                // Glowing accent dot with mint color
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .blur(4.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(MintBreeze, MintBreeze.copy(alpha = 0f))
                                ),
                                shape = CircleShape
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(MintBreeze, Color(0xFF81C784))
                                ),
                                shape = CircleShape
                            )
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Lead,
                    letterSpacing = (-0.3).sp
                )
            }
            
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MintBreeze.copy(alpha = 0.3f)
            ) {
                Text(
                    text = if (selectedCategory == "All") "All sports" else selectedCategory,
                    style = MaterialTheme.typography.labelMedium,
                    color = Lead.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (allCommunitiesState) {
            is CommunitiesState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = MintBreeze,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(44.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .blur(12.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            MintBreeze.copy(alpha = 0.3f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )
                    }
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
                        PremiumEmptyStateCard(
                            emoji = "🔭",
                            message = "No communities to discover",
                            subtitle = "Try a different category"
                        )
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        items(filteredCommunities) { community ->
                            PremiumDiscoverCard(
                                community = community,
                                onJoinClick = { onJoinClick(community) },
                                onImageClick = onImageClick
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
                    PremiumErrorStateCard(message = allCommunitiesState.message)
                }
            }
        }
    }
}

// ==================== PREMIUM DISCOVER CARD ====================

@Composable
private fun PremiumDiscoverCard(
    community: Community,
    onJoinClick: () -> Unit,
    onImageClick: (String) -> Unit = {}
) {
    val emoji = getCommunityEmoji(community.sportCategory)
    val gradient = getCommunityGradient(community.sportCategory)
    val accentColor = getCommunityColor(community.sportCategory)
    
    var isHovered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.02f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "discoverScale"
    )

    Box(
        modifier = Modifier
            .width(200.dp)
            .scale(scale)
    ) {
        // Shadow bloom
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 6.dp)
                .blur(20.dp)
                .alpha(0.12f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(accentColor, Dreamland)
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
        )
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.Transparent,
            border = BorderStroke(
                1.5.dp,
                Brush.linearGradient(
                    colors = listOf(
                        ChineseSilver.copy(alpha = 0.5f),
                        gradient.first().copy(alpha = 0.4f),
                        Dreamland.copy(alpha = 0.3f)
                    )
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                ChineseSilver.copy(alpha = 0.35f),
                                SoftLavender.copy(alpha = 0.3f),
                                gradient.first().copy(alpha = 0.15f)
                            )
                        )
                    )
            ) {
                // Decorative blob
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 30.dp, y = (-30).dp)
                        .blur(35.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    gradient.first().copy(alpha = 0.35f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Premium Emoji Icon with glow
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(82.dp)
                                .blur(16.dp)
                                .alpha(0.45f)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            gradient.first(),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = RoundedCornerShape(22.dp)
                                )
                        )
                        
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .shadow(
                                    elevation = 14.dp,
                                    shape = RoundedCornerShape(22.dp),
                                    spotColor = gradient.first().copy(alpha = 0.5f)
                                )
                                .background(
                                    brush = Brush.linearGradient(gradient),
                                    shape = RoundedCornerShape(22.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 38.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Community Name
                    Text(
                        text = community.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Lead,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.height(44.dp),
                        letterSpacing = (-0.2).sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Category tag with gradient border
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = 0.2f),
                                        gradient.first().copy(alpha = 0.15f)
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                width = 1.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = 0.4f),
                                        gradient.first().copy(alpha = 0.3f)
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = community.sportCategory,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Lead.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Member count with icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(
                                    color = ChineseSilver.copy(alpha = 0.5f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Group,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = WarmHaze
                            )
                        }
                        Text(
                            text = "${community.memberCount} members",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmHaze,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Creator info
                    Text(
                        text = "by ${community.createdBy.take(12)}${if (community.createdBy.length > 12) "..." else ""}",
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmHaze.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Premium Join Button with glow
                    Box(contentAlignment = Alignment.Center) {
                        // Glow effect
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .blur(10.dp)
                                .alpha(0.4f)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Crunch, Color.Transparent)
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                        )
                        
                        Button(
                            onClick = onJoinClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .shadow(
                                    elevation = 10.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    spotColor = Crunch.copy(alpha = 0.4f)
                                ),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Lead
                            ),
                            contentPadding = PaddingValues(0.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 0.dp
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(Crunch, SunsetOrange, PeachGlow)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.PersonAdd,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = Lead
                                    )
                                    Text(
                                        text = "Join",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Lead
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== PREMIUM FLOATING CREATE BUTTON ====================

@Composable
private fun PremiumFloatingCreateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "fab")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Outer rotating glow ring
        Box(
            modifier = Modifier
                .size(80.dp)
                .rotate(rotationAngle)
                .blur(16.dp)
                .alpha(glowAlpha * 0.5f)
                .background(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Crunch.copy(alpha = 0.6f),
                            SunsetOrange.copy(alpha = 0.3f),
                            PeachGlow.copy(alpha = 0.5f),
                            Crunch.copy(alpha = 0.6f)
                        )
                    ),
                    shape = CircleShape
                )
        )
        
        // Pulsing glow effect
        Box(
            modifier = Modifier
                .size(72.dp)
                .scale(pulseScale)
                .blur(14.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Crunch.copy(alpha = glowAlpha),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        
        // Main FAB with premium styling
        Surface(
            modifier = Modifier
                .size(64.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = CircleShape,
                    spotColor = Crunch.copy(alpha = 0.5f),
                    ambientColor = Crunch.copy(alpha = 0.3f)
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() },
            shape = CircleShape,
            color = Color.Transparent,
            border = BorderStroke(
                2.dp,
                Brush.linearGradient(
                    colors = listOf(
                        Crunch,
                        SunsetOrange,
                        PeachGlow
                    )
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Crunch, SunsetOrange, PeachGlow)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Create Community",
                    modifier = Modifier.size(30.dp),
                    tint = Lead
                )
            }
        }
        
        // Premium Lock Badge
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 4.dp, y = (-4).dp)
                .size(26.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = Lead.copy(alpha = 0.3f)
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Lead, Lead.copy(alpha = 0.9f))
                    ),
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = CascadingWhite,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = "Premium",
                modifier = Modifier.size(13.dp),
                tint = Crunch
            )
        }
    }
}

// ==================== PREMIUM JOIN DIALOG ====================

@Composable
private fun PremiumJoinDialog(
    community: Community,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val emoji = getCommunityEmoji(community.sportCategory)
    val gradient = getCommunityGradient(community.sportCategory)
    val accentColor = getCommunityColor(community.sportCategory)
    
    val infiniteTransition = rememberInfiniteTransition(label = "dialog")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dialog_glow"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer glow
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .blur(30.dp)
                    .alpha(glowAlpha * 0.4f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                gradient.first(),
                                accentColor.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(32.dp)
                    )
            )
            
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 28.dp,
                        shape = RoundedCornerShape(32.dp),
                        spotColor = accentColor.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(32.dp),
                color = Color.Transparent,
                border = BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            ChineseSilver.copy(alpha = 0.6f),
                            gradient.first().copy(alpha = 0.4f),
                            Dreamland.copy(alpha = 0.5f)
                        )
                    )
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    CascadingWhite,
                                    ChineseSilver.copy(alpha = 0.3f),
                                    SoftLavender.copy(alpha = 0.2f)
                                )
                            )
                        )
                ) {
                    // Decorative corner blob
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 50.dp, y = (-50).dp)
                            .blur(50.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        gradient.first().copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )
                    
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Emoji with premium glow
                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .blur(20.dp)
                                    .alpha(0.5f)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                gradient.first(),
                                                Color.Transparent
                                            )
                                        ),
                                        shape = RoundedCornerShape(28.dp)
                                    )
                            )
                            
                            Box(
                                modifier = Modifier
                                    .size(88.dp)
                                    .shadow(
                                        elevation = 16.dp,
                                        shape = RoundedCornerShape(26.dp),
                                        spotColor = gradient.first().copy(alpha = 0.5f)
                                    )
                                    .background(
                                        brush = Brush.linearGradient(gradient),
                                        shape = RoundedCornerShape(26.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 44.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Join Community?",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Lead,
                            letterSpacing = (-0.3).sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = community.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = Lead.copy(alpha = 0.85f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Member count badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ChineseSilver.copy(alpha = 0.4f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Group,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = WarmHaze
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${community.memberCount} members",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = WarmHaze,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Cancel button with neumorphic style
                            Surface(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(16.dp),
                                        spotColor = Dreamland.copy(alpha = 0.3f)
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                color = Color.Transparent,
                                border = BorderStroke(1.5.dp, Dreamland.copy(alpha = 0.6f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    ChineseSilver.copy(alpha = 0.4f),
                                                    Dreamland.copy(alpha = 0.3f)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Cancel",
                                        color = WarmHaze,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }

                            // Join button with premium glow
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .blur(10.dp)
                                        .alpha(0.5f)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(Crunch, Color.Transparent)
                                            ),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                )
                                
                                Surface(
                                    onClick = onConfirm,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .shadow(
                                            elevation = 12.dp,
                                            shape = RoundedCornerShape(16.dp),
                                            spotColor = Crunch.copy(alpha = 0.4f)
                                        ),
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.Transparent
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(Crunch, SunsetOrange, PeachGlow)
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp),
                                                tint = Lead
                                            )
                                            Text(
                                                text = "Join",
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.labelLarge,
                                                color = Lead
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== COMMUNITY DETAIL DIALOG ====================

@Composable
private fun CommunityDetailDialog(
    community: Community,
    onDismiss: () -> Unit,
    onImageClick: (String) -> Unit
) {
    val emoji = getCommunityEmoji(community.sportCategory)
    val gradient = getCommunityGradient(community.sportCategory)
    val accentColor = getCommunityColor(community.sportCategory)

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
                            Lead.copy(alpha = 0.95f),
                            Lead.copy(alpha = 0.98f)
                        )
                    )
                )
        ) {
            // Background decorative blobs
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(80.dp)
            ) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    ),
                    radius = 300f,
                    center = Offset(size.width * 0.2f, size.height * 0.15f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            gradient.first().copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    radius = 250f,
                    center = Offset(size.width * 0.8f, size.height * 0.4f)
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with back button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .size(44.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                                spotColor = CascadingWhite.copy(alpha = 0.2f)
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onDismiss() },
                        shape = CircleShape,
                        color = CascadingWhite.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, CascadingWhite.copy(alpha = 0.2f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Back",
                                tint = CascadingWhite,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Text(
                        text = "Community Details",
                        style = MaterialTheme.typography.titleMedium,
                        color = CascadingWhite,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.size(44.dp))
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Large floating header card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 24.dp,
                                shape = RoundedCornerShape(32.dp),
                                spotColor = accentColor.copy(alpha = 0.3f)
                            ),
                        shape = RoundedCornerShape(32.dp),
                        color = Color.Transparent,
                        border = BorderStroke(
                            1.5.dp,
                            Brush.linearGradient(
                                colors = listOf(
                                    CascadingWhite.copy(alpha = 0.3f),
                                    accentColor.copy(alpha = 0.3f),
                                    CascadingWhite.copy(alpha = 0.2f)
                                )
                            )
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            CascadingWhite.copy(alpha = 0.12f),
                                            ChineseSilver.copy(alpha = 0.08f)
                                        )
                                    )
                                )
                        ) {
                            // Decorative blob
                            Box(
                                modifier = Modifier
                                    .size(180.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = 60.dp, y = (-60).dp)
                                    .blur(60.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                gradient.first().copy(alpha = 0.4f),
                                                Color.Transparent
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                            )
                            
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Large emoji icon
                                Box(contentAlignment = Alignment.Center) {
                                    Box(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .blur(25.dp)
                                            .alpha(0.5f)
                                            .background(
                                                brush = Brush.radialGradient(
                                                    colors = listOf(
                                                        gradient.first(),
                                                        Color.Transparent
                                                    )
                                                ),
                                                shape = RoundedCornerShape(32.dp)
                                            )
                                    )
                                    
                                    Box(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .shadow(
                                                elevation = 20.dp,
                                                shape = RoundedCornerShape(30.dp),
                                                spotColor = gradient.first().copy(alpha = 0.5f)
                                            )
                                            .background(
                                                brush = Brush.linearGradient(gradient),
                                                shape = RoundedCornerShape(30.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = emoji, fontSize = 52.sp)
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                // Community name
                                Text(
                                    text = community.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = CascadingWhite,
                                    textAlign = TextAlign.Center,
                                    letterSpacing = (-0.3).sp
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Category tag
                                Box(
                                    modifier = Modifier
                                        .background(
                                            brush = Brush.linearGradient(gradient),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = community.sportCategory,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Lead
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(20.dp))
                                
                                // Stats row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    // Members stat
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "${community.memberCount}",
                                            style = MaterialTheme.typography.headlineMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = CascadingWhite
                                        )
                                        Text(
                                            text = "Members",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = CascadingWhite.copy(alpha = 0.7f)
                                        )
                                    }
                                    
                                    // Divider
                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .height(40.dp)
                                            .background(CascadingWhite.copy(alpha = 0.2f))
                                    )
                                    
                                    // Created stat
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = formatDate(community.createdAt),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = CascadingWhite
                                        )
                                        Text(
                                            text = "Created",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = CascadingWhite.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(20.dp))
                                
                                // Creator info
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = CascadingWhite.copy(alpha = 0.1f),
                                    border = BorderStroke(1.dp, CascadingWhite.copy(alpha = 0.15f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(
                                                    brush = Brush.linearGradient(
                                                        colors = listOf(
                                                            accentColor.copy(alpha = 0.5f),
                                                            gradient.first().copy(alpha = 0.5f)
                                                        )
                                                    ),
                                                    shape = CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Person,
                                                contentDescription = null,
                                                tint = CascadingWhite,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        
                                        Column {
                                            Text(
                                                text = "Created by",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = CascadingWhite.copy(alpha = 0.6f)
                                            )
                                            Text(
                                                text = community.createdBy,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                color = CascadingWhite
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(28.dp))
                
                // Description section
                if (community.description.isNotEmpty()) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "About",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CascadingWhite
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = CascadingWhite.copy(alpha = 0.08f),
                            border = BorderStroke(1.dp, CascadingWhite.copy(alpha = 0.1f))
                        ) {
                            Text(
                                text = community.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = CascadingWhite.copy(alpha = 0.85f),
                                modifier = Modifier.padding(18.dp),
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(28.dp))
                
                // Member avatars grid section
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Members",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CascadingWhite
                        )
                        
                        Text(
                            text = "View all",
                            style = MaterialTheme.typography.labelMedium,
                            color = Crunch,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Member avatars in a flowing layout
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val avatarEmojis = listOf("😊", "🏃", "💪", "🎯", "⭐", "🔥")
                        val displayCount = minOf(community.memberCount, 6)
                        
                        (0 until displayCount).forEach { index ->
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = CircleShape,
                                        spotColor = gradient.getOrElse(index % gradient.size) { accentColor }.copy(alpha = 0.4f)
                                    )
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                gradient.getOrElse(index % gradient.size) { accentColor },
                                                gradient.getOrElse((index + 1) % gradient.size) { accentColor.copy(alpha = 0.8f) }
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = CascadingWhite.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = avatarEmojis[index % avatarEmojis.size],
                                    fontSize = 24.sp
                                )
                            }
                        }
                        
                        if (community.memberCount > 6) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = CircleShape,
                                        spotColor = Lead.copy(alpha = 0.3f)
                                    )
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(Lead.copy(alpha = 0.8f), Lead)
                                        ),
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = CascadingWhite.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+${community.memberCount - 6}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = CascadingWhite
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(100.dp))
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
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    }
    LaunchedEffect(Unit) {
        alpha.animateTo(1f, tween(300))
    }

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
                .background(Lead.copy(alpha = alpha.value * 0.98f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            // Close button with premium styling
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(20.dp)
                    .size(48.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = CircleShape,
                        spotColor = CascadingWhite.copy(alpha = 0.2f)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDismiss() },
                shape = CircleShape,
                color = CascadingWhite.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, CascadingWhite.copy(alpha = 0.3f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = CascadingWhite,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // Image with premium shadow
            Box(
                modifier = Modifier
                    .scale(scale.value)
                    .alpha(alpha.value)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .shadow(
                            elevation = 32.dp,
                            shape = RoundedCornerShape(24.dp),
                            spotColor = Crunch.copy(alpha = 0.2f)
                        ),
                    shape = RoundedCornerShape(24.dp),
                    color = ChineseSilver.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, CascadingWhite.copy(alpha = 0.2f))
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Full image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

// ==================== PREMIUM EMPTY STATE CARD ====================

@Composable
private fun PremiumEmptyStateCard(
    emoji: String,
    message: String,
    subtitle: String? = null
) {
    Box {
        // Soft shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 4.dp)
                .blur(16.dp)
                .alpha(0.1f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(SoftLavender, Dreamland)
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        )
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.Transparent,
            border = BorderStroke(
                1.5.dp,
                Brush.linearGradient(
                    colors = listOf(
                        ChineseSilver.copy(alpha = 0.5f),
                        SoftLavender.copy(alpha = 0.4f),
                        Dreamland.copy(alpha = 0.3f)
                    )
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                SoftLavender.copy(alpha = 0.35f),
                                ChineseSilver.copy(alpha = 0.25f)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Emoji with glow
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .blur(20.dp)
                                .alpha(0.4f)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Crunch.copy(alpha = 0.5f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )
                        Text(text = emoji, fontSize = 48.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Lead.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    subtitle?.let {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmHaze.copy(alpha = 0.75f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// ==================== PREMIUM ERROR STATE CARD ====================

@Composable
private fun PremiumErrorStateCard(message: String) {
    val errorGradient = listOf(Color(0xFFFEE2E2), Color(0xFFFECACA))
    
    Box {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 4.dp)
                .blur(16.dp)
                .alpha(0.15f)
                .background(
                    color = Color(0xFFEF4444),
                    shape = RoundedCornerShape(24.dp)
                )
        )
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.Transparent,
            border = BorderStroke(
                1.5.dp,
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFCA5A5).copy(alpha = 0.6f),
                        Color(0xFFEF4444).copy(alpha = 0.3f)
                    )
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(errorGradient)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Error icon with glow
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .blur(10.dp)
                                .alpha(0.4f)
                                .background(
                                    color = Color(0xFFEF4444),
                                    shape = CircleShape
                                )
                        )
                        
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFEF4444),
                                            Color(0xFFDC2626)
                                        )
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Error,
                                contentDescription = null,
                                tint = CascadingWhite,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = "Something went wrong",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB91C1C)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFDC2626)
                        )
                    }
                }
            }
        }
    }
}
