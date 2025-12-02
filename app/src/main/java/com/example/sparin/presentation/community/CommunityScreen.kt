package com.example.sparin.presentation.community

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import java.util.UUID

// ==================== DATA CLASSES ====================

data class SportCategory(
    val name: String,
    val emoji: String,
    val color: Color
)

data class Community(
    val id: String,
    val name: String,
    val emoji: String,
    val bannerColor: Color,
    val memberCount: String,
    val newPosts: Int = 0,
    val isJoined: Boolean = false,
    val description: String = "",
    val category: String = "" // Added category for filtering
)

// ==================== SAMPLE DATA ====================

private val sportCategories = listOf(
    SportCategory("Badminton", "🏸", PeachGlow),
    SportCategory("Futsal", "⚽", MintBreeze),
    SportCategory("Basket", "🏀", SkyMist),
    SportCategory("Volleyball", "🏐", RoseDust),
    SportCategory("Sepeda", "🚴", SoftLavender),
    SportCategory("Lari", "🏃", ChineseSilver),
    SportCategory("Gym", "💪", PeachGlow.copy(alpha = 0.8f)),
    SportCategory("Boxing", "🥊", RoseDust.copy(alpha = 0.9f)),
    SportCategory("Hiking", "🥾", MintBreeze.copy(alpha = 0.9f)),
    SportCategory("Tennis", "🎾", SkyMist.copy(alpha = 0.9f)),
    SportCategory("Renang", "🏊", SoftLavender.copy(alpha = 0.9f)),
    SportCategory("Bowling", "🎳", ChineseSilver.copy(alpha = 0.9f))
)

private val initialMyCommunities = listOf(
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
    )
)

private val initialRecommendedCommunities = listOf(
    Community(
        id = "4",
        name = "Badminton Jogja Community",
        emoji = "🏸",
        bannerColor = PeachGlow,
        memberCount = "1.8k",
        description = "Komunitas bulutangkis Yogyakarta",
        category = "Badminton"
    ),
    Community(
        id = "5",
        name = "Running Club Bali",
        emoji = "🏃",
        bannerColor = MintBreeze,
        memberCount = "980",
        description = "Morning run around beautiful Bali",
        category = "Lari"
    ),
    Community(
        id = "6",
        name = "Gym Bros Jakarta",
        emoji = "💪",
        bannerColor = SkyMist,
        memberCount = "2.5k",
        description = "Fitness enthusiasts unite!",
        category = "Gym"
    ),
    Community(
        id = "7",
        name = "Volleyball Semarang",
        emoji = "🏐",
        bannerColor = RoseDust,
        memberCount = "750",
        description = "Spiker & setter Semarang",
        category = "Volleyball"
    ),
    Community(
        id = "8",
        name = "Cycling Malang",
        emoji = "🚴",
        bannerColor = SoftLavender,
        memberCount = "1.2k",
        description = "Explore Malang on wheels",
        category = "Sepeda"
    ),
    Community(
        id = "9",
        name = "Tennis Club Medan",
        emoji = "🎾",
        bannerColor = SkyMist.copy(alpha = 0.9f),
        memberCount = "650",
        description = "Tennis enthusiasts in Medan",
        category = "Tennis"
    ),
    Community(
        id = "10",
        name = "Boxing Academy Surabaya",
        emoji = "🥊",
        bannerColor = RoseDust.copy(alpha = 0.9f),
        memberCount = "480",
        description = "Learn boxing techniques",
        category = "Boxing"
    )
)

// ==================== GEN-Z COLOR PALETTE ====================

private val GenZBlue = Color(0xFF8CCFFF)
private val GenZTeal = Color(0xFF35C8C3)
private val GenZCyan = Color(0xFF57D3FF)
private val GenZLavender = Color(0xFFB8B5FF)
private val GenZGradientStart = Color(0xFF35C8C3)
private val GenZGradientEnd = Color(0xFF57D3FF)

/**
 * Premium Community Screen for SparIN
 * Gen-Z aesthetic: vibrant colors, soft shadows, frosted cards, modern illustrations
 */
@Composable
fun CommunityScreen(
    navController: NavHostController
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    
    // State for Add Community Dialog
    var showAddCommunityDialog by remember { mutableStateOf(false) }
    
    // State for dynamic community lists
    var myCommunities by remember { mutableStateOf(initialMyCommunities) }
    var recommendedCommunities by remember { mutableStateOf(initialRecommendedCommunities) }
    
    // Filter communities based on search and category
    val filteredMyCommunities = remember(searchQuery, selectedCategory, myCommunities) {
        myCommunities.filter { community ->
            val matchesSearch = searchQuery.isEmpty() ||
                    community.name.contains(searchQuery, ignoreCase = true) ||
                    community.description.contains(searchQuery, ignoreCase = true) ||
                    community.category.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == null || community.category == selectedCategory
            matchesSearch && matchesCategory
        }
    }
    
    val filteredRecommendedCommunities = remember(searchQuery, selectedCategory, recommendedCommunities) {
        recommendedCommunities.filter { community ->
            val matchesSearch = searchQuery.isEmpty() ||
                    community.name.contains(searchQuery, ignoreCase = true) ||
                    community.description.contains(searchQuery, ignoreCase = true) ||
                    community.category.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == null || community.category == selectedCategory
            matchesSearch && matchesCategory
        }
    }
    
    // Get most popular community (highest member count)
    val mostPopularCommunity = remember(myCommunities, recommendedCommunities) {
        (myCommunities + recommendedCommunities)
            .maxByOrNull { 
                it.memberCount.replace("k", "000").replace(".", "").toIntOrNull() ?: 0 
            }
    }

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
        // Animated floating blobs background
        CommunityBackgroundBlobs()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // SECTION 1: Header with Add button
            item {
                CommunityHeader(
                    onAddClick = { showAddCommunityDialog = true }
                )
            }

            // SECTION 2: Search Bar
            item {
                Spacer(modifier = Modifier.height(20.dp))
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            // SECTION 3: Featured Hero Card
            item {
                Spacer(modifier = Modifier.height(24.dp))
                FeaturedHeroCard(
                    onExploreClick = { navController.navigate(Screen.FindCommunitiesNearby.route) }
                )
            }

            // SECTION 4: Category Carousel
            item {
                Spacer(modifier = Modifier.height(28.dp))
                CategoryCarousel(
                    categories = sportCategories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category ->
                        selectedCategory = if (selectedCategory == category) null else category
                    }
                )
            }

            // SECTION 5: My Communities
            item {
                Spacer(modifier = Modifier.height(28.dp))
                MyCommunities(
                    communities = filteredMyCommunities,
                    onViewAllClick = { navController.navigate(Screen.AllMyCommunities.route) },
                    onCommunityClick = { community ->
                        navController.navigate(
                            Screen.CommunityFeed.createRoute(
                                communityId = community.id,
                                name = community.name,
                                emoji = community.emoji
                            )
                        )
                    }
                )
            }

            // SECTION 6: Recommended Communities
            item {
                Spacer(modifier = Modifier.height(28.dp))
                SectionHeader(
                    title = "Recommended for You",
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Recommended Communities List
            items(filteredRecommendedCommunities, key = { it.id }) { community ->
                RecommendedCommunityCard(
                    community = community,
                    onJoinClick = {
                        // Move community from recommended to my communities
                        val joinedCommunity = community.copy(isJoined = true, newPosts = 0)
                        myCommunities = myCommunities + joinedCommunity
                        recommendedCommunities = recommendedCommunities.filter { it.id != community.id }
                    },
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

            // SECTION 7: Most Popular Community Card
            item {
                Spacer(modifier = Modifier.height(28.dp))
                mostPopularCommunity?.let { popular ->
                    MostPopularCommunityCard(
                        community = popular,
                        onClick = {
                            navController.navigate(
                                Screen.CommunityFeed.createRoute(
                                    communityId = popular.id,
                                    name = popular.name,
                                    emoji = popular.emoji
                                )
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
    
    // Add Community Dialog
    if (showAddCommunityDialog) {
        AddCommunityDialog(
            categories = sportCategories,
            onDismiss = { showAddCommunityDialog = false },
            onAddCommunity = { newCommunity ->
                // Add to my communities list
                myCommunities = myCommunities + newCommunity
                showAddCommunityDialog = false
            }
        )
    }
}

// ==================== BACKGROUND BLOBS ====================

@Composable
private fun CommunityBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "community_blobs")

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
                y = size.height * 0.2f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 35f
            )
        )

        // Lavender blob - center
        drawCircle(
            color = GenZLavender.copy(alpha = 0.18f),
            radius = 140f,
            center = Offset(
                x = size.width * 0.5f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 40f,
                y = size.height * 0.45f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 30f
            )
        )

        // Cyan blob - bottom right
        drawCircle(
            color = GenZCyan.copy(alpha = 0.2f),
            radius = 150f,
            center = Offset(
                x = size.width * 0.8f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 25f,
                y = size.height * 0.7f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 35f
            )
        )

        // Peach blob - bottom left
        drawCircle(
            color = PeachGlow.copy(alpha = 0.15f),
            radius = 120f,
            center = Offset(
                x = size.width * 0.15f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 30f,
                y = size.height * 0.85f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 25f
            )
        )
    }
}

// ==================== SECTION 1: HEADER ====================

@Composable
private fun CommunityHeader(
    onAddClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "header_anim")

    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp)
    ) {
        // Subtle gradient header background
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Avatar with floating accent
            Box {
                // Floating accent dot
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .offset(x = (-6).dp, y = (-4 + floatAnim).dp)
                        .background(
                            color = GenZTeal.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                        .blur(3.dp)
                )

                Surface(
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = CircleShape,
                            ambientColor = NeumorphDark.copy(alpha = 0.15f)
                        ),
                    shape = CircleShape,
                    color = ChineseSilver
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(24.dp),
                            tint = WarmHaze
                        )
                    }
                }
            }

            // Title
            Text(
                text = "Communities",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                ),
                color = Lead
            )

            // Add Community Button
            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        ambientColor = GenZTeal.copy(alpha = 0.2f)
                    )
                    .clickable(onClick = onAddClick),
                shape = CircleShape,
                color = GenZTeal
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add Community",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

// ==================== SECTION 2: SEARCH BAR ====================

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.1f),
                spotColor = NeumorphDark.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(28.dp),
        color = NeumorphLight.copy(alpha = 0.95f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            NeumorphLight,
                            ChineseSilver.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(22.dp),
                    tint = GenZBlue
                )

                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(
                        color = Lead,
                        fontSize = 16.sp
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box {
                            if (query.isEmpty()) {
                                Text(
                                    text = "Search communities...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = WarmHaze.copy(alpha = 0.6f)
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Clear",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onQueryChange("") },
                        tint = WarmHaze
                    )
                }
            }
        }
    }
}

// ==================== SECTION 3: FEATURED HERO CARD ====================

@Composable
private fun FeaturedHeroCard(
    onExploreClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_anim")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    val float3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float3"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(220.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = GenZTeal.copy(alpha = 0.25f),
                spotColor = GenZTeal.copy(alpha = 0.25f)
            ),
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GenZGradientStart,
                            GenZGradientEnd,
                            GenZBlue
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        ) {
            // Floating sport icons
            Text(
                text = "🏸",
                fontSize = 36.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-25).dp, y = (20 + float1).dp)
            )

            Text(
                text = "🏀",
                fontSize = 28.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-80).dp, y = (45 + float2).dp)
            )

            Text(
                text = "⚽",
                fontSize = 32.sp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = (-20).dp, y = float3.dp)
            )

            Text(
                text = "🏐",
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-70).dp, y = (-30 + float1).dp)
            )

            Text(
                text = "🎾",
                fontSize = 26.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-20).dp, y = (-55 + float2).dp)
            )

            // Floating decorative circles
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 15.dp, y = (-15 + float1).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = CircleShape
                    )
                    .blur(12.dp)
            )

            Box(
                modifier = Modifier
                    .size(45.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-40).dp, y = (30 + float2).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.12f),
                        shape = CircleShape
                    )
                    .blur(10.dp)
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Welcome to Communities!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Discover sports groups, meet new players,\njoin events.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 22.sp
                    )
                }

                Button(
                    onClick = onExploreClick,
                    modifier = Modifier
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = Color.Black.copy(alpha = 0.15f)
                        ),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Find communities now",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = GenZTeal
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = GenZTeal
                        )
                    }
                }
            }
        }
    }
}

// ==================== SECTION 4: CATEGORY CAROUSEL ====================

@Composable
private fun CategoryCarousel(
    categories: List<SportCategory>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit
) {
    Column {
        SectionHeader(
            title = "Browse by Sport",
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryBubble(
                    category = category,
                    isSelected = selectedCategory == category.name,
                    onClick = {
                        onCategorySelected(
                            if (selectedCategory == category.name) null else category.name
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoryBubble(
    category: SportCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) GenZTeal.copy(alpha = 0.2f) else category.color.copy(alpha = 0.4f),
        animationSpec = tween(200),
        label = "cat_bg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) GenZTeal else Color.Transparent,
        animationSpec = tween(200),
        label = "cat_border"
    )

    Surface(
        modifier = Modifier
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Emoji without background - clean design
            Text(
                text = category.emoji,
                fontSize = 28.sp
            )
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 11.sp
                ),
                color = if (isSelected) GenZTeal else Lead
            )
        }
    }
}

// ==================== SECTION 5: MY COMMUNITIES ====================

@Composable
private fun MyCommunities(
    communities: List<Community>,
    onViewAllClick: () -> Unit,
    onCommunityClick: (Community) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Communities",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Lead
            )

            TextButton(onClick = onViewAllClick) {
                Text(
                    text = "View all",
                    color = GenZTeal,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (communities.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No communities found. Join one below!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmHaze
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(communities, key = { it.id }) { community ->
                    MyCommunityCard(
                        community = community,
                        onClick = { onCommunityClick(community) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MyCommunityCard(
    community: Community,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = NeumorphLight
    ) {
        Column {
            // Banner - clean emoji without extra background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                community.bannerColor,
                                community.bannerColor.copy(alpha = 0.6f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Clean emoji without shadow background
                Text(
                    text = community.emoji,
                    fontSize = 40.sp
                )
            }

            // Content
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = community.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Member count
                    Surface(
                        shape = RoundedCornerShape(10.dp),
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
                            shape = RoundedCornerShape(10.dp),
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
                }
            }
        }
    }
}

// ==================== SECTION 6: RECOMMENDED COMMUNITIES ====================

@Composable
private fun RecommendedCommunityCard(
    community: Community,
    onJoinClick: () -> Unit,
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
            // Thumbnail - clean emoji without extra shadow
            Box(
                modifier = Modifier
                    .size(60.dp)
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
                    fontSize = 30.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = community.name,
                    style = MaterialTheme.typography.titleSmall.copy(
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

                Spacer(modifier = Modifier.height(6.dp))

                // Member badge
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
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmHaze
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Join Button
            Button(
                onClick = onJoinClick,
                modifier = Modifier
                    .height(36.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(18.dp),
                        ambientColor = GenZTeal.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GenZTeal
                ),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(
                    text = "+ Join",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

// ==================== SECTION 7: MOST POPULAR COMMUNITY ====================

@Composable
private fun MostPopularCommunityCard(
    community: Community,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "popular_anim")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        // Section Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🔥",
                    fontSize = 24.sp
                )
                Text(
                    text = "Most Popular",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Lead
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(28.dp),
            color = NeumorphLight
        ) {
            Column {
                // Hero Banner Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    community.bannerColor,
                                    GenZTeal.copy(alpha = 0.7f),
                                    GenZCyan.copy(alpha = 0.8f)
                                )
                            )
                        )
                ) {
                    // Floating emoji - clean without shadow
                    Text(
                        text = community.emoji,
                        fontSize = 56.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(y = float1.dp)
                    )

                    // Popular badge
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.9f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "🏆",
                                fontSize = 14.sp
                            )
                            Text(
                                text = "#1 Popular",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = GenZTeal
                            )
                        }
                    }

                    // Gradient overlay at bottom
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        NeumorphLight.copy(alpha = 0.9f)
                                    )
                                )
                            )
                    )
                }

                // Content
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Title row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = community.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Lead,
                            modifier = Modifier.weight(1f)
                        )

                        // Joined badge if applicable
                        if (community.isJoined) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = GenZTeal.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = GenZTeal
                                    )
                                    Text(
                                        text = "Joined",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = GenZTeal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Stats row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Member count badge
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = GenZBlue.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.People,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = GenZBlue
                                )
                                Text(
                                    text = "${community.memberCount} members",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = GenZBlue
                                )
                            }
                        }

                        // Category badge
                        if (community.category.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = GenZLavender.copy(alpha = 0.3f)
                            ) {
                                Text(
                                    text = community.category,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Lead
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Description
                    Text(
                        text = community.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmHaze,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // CTA Button
                    Button(
                        onClick = onClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(GenZGradientStart, GenZGradientEnd)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "View Community",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== HELPER COMPONENTS ====================

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
        ),
        color = Lead
    )
}

// ==================== ADD COMMUNITY DIALOG ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddCommunityDialog(
    categories: List<SportCategory>,
    onDismiss: () -> Unit,
    onAddCommunity: (Community) -> Unit
) {
    // Form states
    var communityName by remember { mutableStateOf("") }
    var communityDescription by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<SportCategory?>(null) }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    
    // Validation
    val isFormValid = communityName.isNotBlank() && selectedCategory != null
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 24.dp, vertical = 48.dp)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .clickable(enabled = false, onClick = {}),
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                shadowElevation = 24.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header with gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        GenZTeal,
                                        GenZCyan
                                    )
                                )
                            )
                    ) {
                        // Decorative circles
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .offset(x = (-30).dp, y = (-30).dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.1f),
                                    shape = CircleShape
                                )
                        )
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = 20.dp, y = 20.dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.1f),
                                    shape = CircleShape
                                )
                        )
                        
                        // Close button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Close",
                                    modifier = Modifier.padding(8.dp),
                                    tint = Color.White
                                )
                            }
                        }
                        
                        // Title
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(top = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "➕",
                                fontSize = 40.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Create Community",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                        }
                    }
                    
                    // Form Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Community Name Field
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Community Name *",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Lead
                            )
                            
                            OutlinedTextField(
                                value = communityName,
                                onValueChange = { communityName = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        text = "e.g., Badminton Jakarta",
                                        color = WarmHaze
                                    )
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GenZTeal,
                                    unfocusedBorderColor = ChineseSilver.copy(alpha = 0.3f),
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = ChineseSilver.copy(alpha = 0.05f)
                                ),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Group,
                                        contentDescription = null,
                                        tint = GenZTeal
                                    )
                                }
                            )
                        }
                        
                        // Category Selection
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Sport Category *",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Lead
                            )
                            
                            ExposedDropdownMenuBox(
                                expanded = showCategoryDropdown,
                                onExpandedChange = { showCategoryDropdown = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedCategory?.let { "${it.emoji} ${it.name}" } ?: "",
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    placeholder = {
                                        Text(
                                            text = "Select a category",
                                            color = WarmHaze
                                        )
                                    },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GenZTeal,
                                        unfocusedBorderColor = ChineseSilver.copy(alpha = 0.3f),
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = ChineseSilver.copy(alpha = 0.05f)
                                    ),
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryDropdown)
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Rounded.Category,
                                            contentDescription = null,
                                            tint = GenZTeal
                                        )
                                    }
                                )
                                
                                ExposedDropdownMenu(
                                    expanded = showCategoryDropdown,
                                    onDismissRequest = { showCategoryDropdown = false }
                                ) {
                                    categories.forEach { category ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                                ) {
                                                    Text(text = category.emoji, fontSize = 20.sp)
                                                    Text(
                                                        text = category.name,
                                                        style = MaterialTheme.typography.bodyLarge
                                                    )
                                                }
                                            },
                                            onClick = {
                                                selectedCategory = category
                                                showCategoryDropdown = false
                                            },
                                            leadingIcon = {
                                                if (selectedCategory == category) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Check,
                                                        contentDescription = null,
                                                        tint = GenZTeal
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        
                        // Description Field
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Lead
                            )
                            
                            OutlinedTextField(
                                value = communityDescription,
                                onValueChange = { communityDescription = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                placeholder = {
                                    Text(
                                        text = "Tell us about your community...",
                                        color = WarmHaze
                                    )
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GenZTeal,
                                    unfocusedBorderColor = ChineseSilver.copy(alpha = 0.3f),
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = ChineseSilver.copy(alpha = 0.05f)
                                ),
                                maxLines = 4,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Description,
                                        contentDescription = null,
                                        tint = GenZTeal,
                                        modifier = Modifier.padding(bottom = 70.dp)
                                    )
                                }
                            )
                        }
                        
                        // Preview Card
                        if (communityName.isNotBlank() && selectedCategory != null) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Preview",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Lead
                                )
                                
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    color = selectedCategory!!.color.copy(alpha = 0.15f)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // Emoji
                                        Surface(
                                            modifier = Modifier.size(50.dp),
                                            shape = CircleShape,
                                            color = selectedCategory!!.color.copy(alpha = 0.3f)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = selectedCategory!!.emoji,
                                                    fontSize = 24.sp
                                                )
                                            }
                                        }
                                        
                                        // Info
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = communityName,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = Lead,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = if (communityDescription.isNotBlank()) 
                                                    communityDescription 
                                                else "No description",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = WarmHaze,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Person,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.dp),
                                                    tint = GenZTeal
                                                )
                                                Text(
                                                    text = "1 member (You)",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = GenZTeal
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Create Button
                        Button(
                            onClick = {
                                selectedCategory?.let { category ->
                                    val newCommunity = Community(
                                        id = UUID.randomUUID().toString(),
                                        name = communityName.trim(),
                                        emoji = category.emoji,
                                        bannerColor = category.color,
                                        memberCount = "1",
                                        newPosts = 0,
                                        isJoined = true,
                                        description = communityDescription.trim().ifBlank { 
                                            "Welcome to $communityName community!" 
                                        },
                                        category = category.name
                                    )
                                    onAddCommunity(newCommunity)
                                }
                            },
                            enabled = isFormValid,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GenZTeal,
                                disabledContainerColor = ChineseSilver.copy(alpha = 0.3f)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Create Community",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        
                        // Hint text
                        if (!isFormValid) {
                            Text(
                                text = "* Please fill in all required fields",
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmHaze,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
