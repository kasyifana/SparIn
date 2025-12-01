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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

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
    val description: String = ""
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

private val myCommunities = listOf(
    Community(
        id = "1",
        name = "Badminton Jakarta",
        emoji = "🏸",
        bannerColor = PeachGlow,
        memberCount = "3.2k",
        newPosts = 15,
        isJoined = true,
        description = "Komunitas badminton terbesar di Jakarta!"
    ),
    Community(
        id = "2",
        name = "Futsal Bandung",
        emoji = "⚽",
        bannerColor = MintBreeze,
        memberCount = "2.1k",
        newPosts = 8,
        isJoined = true,
        description = "Main futsal bareng di Bandung"
    ),
    Community(
        id = "3",
        name = "Basketball Surabaya",
        emoji = "🏀",
        bannerColor = SkyMist,
        memberCount = "1.5k",
        newPosts = 5,
        isJoined = true,
        description = "Hoops lovers Surabaya!"
    )
)

private val recommendedCommunities = listOf(
    Community(
        id = "4",
        name = "Badminton Jogja Community",
        emoji = "🏸",
        bannerColor = PeachGlow,
        memberCount = "1.8k",
        description = "Komunitas bulutangkis Yogyakarta"
    ),
    Community(
        id = "5",
        name = "Running Club Bali",
        emoji = "🏃",
        bannerColor = MintBreeze,
        memberCount = "980",
        description = "Morning run around beautiful Bali"
    ),
    Community(
        id = "6",
        name = "Gym Bros Jakarta",
        emoji = "💪",
        bannerColor = SkyMist,
        memberCount = "2.5k",
        description = "Fitness enthusiasts unite!"
    ),
    Community(
        id = "7",
        name = "Volleyball Semarang",
        emoji = "🏐",
        bannerColor = RoseDust,
        memberCount = "750",
        description = "Spiker & setter Semarang"
    ),
    Community(
        id = "8",
        name = "Cycling Malang",
        emoji = "🚴",
        bannerColor = SoftLavender,
        memberCount = "1.2k",
        description = "Explore Malang on wheels"
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
            // SECTION 1: Header
            item {
                CommunityHeader()
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
                    onExploreClick = { /* Navigate to explore */ }
                )
            }

            // SECTION 4: Category Carousel
            item {
                Spacer(modifier = Modifier.height(28.dp))
                CategoryCarousel(
                    categories = sportCategories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            // SECTION 5: My Communities
            item {
                Spacer(modifier = Modifier.height(28.dp))
                MyCommunities(
                    communities = myCommunities,
                    onViewAllClick = { /* Navigate */ },
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
            items(recommendedCommunities) { community ->
                RecommendedCommunityCard(
                    community = community,
                    onJoinClick = { /* Handle join */ },
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

            // SECTION 7: Community Detail Preview Card
            item {
                Spacer(modifier = Modifier.height(28.dp))
                CommunityDetailPreviewCard(
                    community = myCommunities.first(),
                    onSwipeClick = { /* Handle swipe */ }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
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
private fun CommunityHeader() {
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
            // Left: Avatar
            Box {
                // Floating accent
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

            // Center: Title
            Text(
                text = "Communities",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                ),
                color = Lead
            )

            // Right: Community Icon
            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        ambientColor = GenZTeal.copy(alpha = 0.2f)
                    ),
                shape = CircleShape,
                color = GenZTeal.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.Groups,
                        contentDescription = "Groups",
                        modifier = Modifier.size(24.dp),
                        tint = GenZTeal
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
            // Decorative floating elements
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

                // CTA Button
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

<<<<<<< HEAD
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 10.dp else 6.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cat_elevation"
=======
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
>>>>>>> 0384ba52251d372fd54afce0be0aa5f9f5e48207
    )

    Surface(
        modifier = Modifier
            .shadow(
<<<<<<< HEAD
                elevation = elevation,
                shape = RoundedCornerShape(20.dp),
                ambientColor = if (isSelected) GenZTeal.copy(alpha = 0.3f) else NeumorphDark.copy(alpha = 0.1f)
            )
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
=======
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
>>>>>>> 0384ba52251d372fd54afce0be0aa5f9f5e48207
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

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(communities) { community ->
                MyCommunityCard(
                    community = community,
                    onClick = { onCommunityClick(community) }
                )
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
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.12f),
                spotColor = NeumorphDark.copy(alpha = 0.12f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = NeumorphLight.copy(alpha = 0.98f)
    ) {
        Column {
            // Banner
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
    var isJoined by remember { mutableStateOf(community.isJoined) }

    Surface(
<<<<<<< HEAD
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.1f),
                spotColor = NeumorphDark.copy(alpha = 0.1f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = NeumorphLight.copy(alpha = 0.98f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                community.bannerColor,
                                community.bannerColor.copy(alpha = 0.5f)
=======
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
>>>>>>> 0384ba52251d372fd54afce0be0aa5f9f5e48207
                            )
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
<<<<<<< HEAD
                Text(
                    text = community.emoji,
                    fontSize = 30.sp
                )
            }
=======
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
>>>>>>> 0384ba52251d372fd54afce0be0aa5f9f5e48207

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
                onClick = {
                    isJoined = !isJoined
                    onJoinClick()
                },
                modifier = Modifier
                    .height(36.dp)
                    .shadow(
                        elevation = if (isJoined) 0.dp else 6.dp,
                        shape = RoundedCornerShape(18.dp),
                        ambientColor = GenZTeal.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isJoined) ChineseSilver.copy(alpha = 0.5f) else GenZTeal
                ),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(
                    text = if (isJoined) "Joined" else "+ Join",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isJoined) WarmHaze else Color.White
                )
            }
        }
    }
}

// ==================== SECTION 7: COMMUNITY DETAIL PREVIEW ====================

@Composable
private fun CommunityDetailPreviewCard(
    community: Community,
    onSwipeClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "preview_anim")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = GenZTeal.copy(alpha = 0.2f),
                spotColor = GenZTeal.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(28.dp),
        color = NeumorphLight.copy(alpha = 0.98f)
    ) {
<<<<<<< HEAD
        Column {
            // Hero Image/Illustration Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                community.bannerColor,
                                GenZTeal.copy(alpha = 0.7f),
                                GenZCyan.copy(alpha = 0.8f)
                            )
=======
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            SlateCharcoal.copy(alpha = 0.95f),
                            NeutralInk.copy(alpha = 0.98f)
>>>>>>> 0384ba52251d372fd54afce0be0aa5f9f5e48207
                        )
                    )
            ) {
<<<<<<< HEAD
                // Floating decorative elements
                Text(
                    text = community.emoji,
                    fontSize = 64.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = float1.dp)
                )

                // Floating bubbles
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-20).dp, y = (20 + float1).dp)
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .blur(6.dp)
                )

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.BottomStart)
                        .offset(x = 30.dp, y = (-20 - float1).dp)
                        .background(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = CircleShape
                        )
                        .blur(5.dp)
                )

                // Gradient overlay at bottom
                Box(
=======
                // Premium Header with close button
                Row(
>>>>>>> 0384ba52251d372fd54afce0be0aa5f9f5e48207
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
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
                // Title and badges row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
<<<<<<< HEAD
                    Text(
                        text = community.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Lead
                    )

                    // Joined badge
                    if (community.isJoined) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GenZTeal.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
=======
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
>>>>>>> 0384ba52251d372fd54afce0be0aa5f9f5e48207
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null,
<<<<<<< HEAD
                                    modifier = Modifier.size(14.dp),
                                    tint = GenZTeal
=======
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
>>>>>>> 0384ba52251d372fd54afce0be0aa5f9f5e48207
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
<<<<<<< HEAD
                                    text = "You Joined",
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

                // Member avatars preview
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy((-10).dp)
                ) {
                    repeat(4) { index ->
                        Surface(
                            modifier = Modifier
                                .size(32.dp)
                                .border(2.dp, NeumorphLight, CircleShape),
                            shape = CircleShape,
                            color = listOf(PeachGlow, MintBreeze, SkyMist, RoseDust)[index]
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = listOf("😊", "🏃", "💪", "🎯")[index],
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "+${community.memberCount} members",
                        style = MaterialTheme.typography.labelMedium,
                        color = WarmHaze
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "${community.description} Connect with fellow ${community.name.split(" ").first().lowercase()} enthusiasts, share your experiences, and find new playing partners!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmHaze,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // CTA Button
                Button(
                    onClick = onSwipeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(27.dp),
                            ambientColor = GenZTeal.copy(alpha = 0.35f),
                            spotColor = GenZTeal.copy(alpha = 0.35f)
                        ),
                    shape = RoundedCornerShape(27.dp),
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
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Swipe & Let's Go",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
=======
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
>>>>>>> 0384ba52251d372fd54afce0be0aa5f9f5e48207
            }
        }
    }
}

<<<<<<< HEAD
// ==================== HELPER COMPONENTS ====================
=======
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
>>>>>>> 0384ba52251d372fd54afce0be0aa5f9f5e48207

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
