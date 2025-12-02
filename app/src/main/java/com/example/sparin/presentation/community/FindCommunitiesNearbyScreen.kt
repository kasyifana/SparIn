package com.example.sparin.presentation.community

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// ==================== GEN-Z COLOR PALETTE ====================

private val GenZBlue = Color(0xFF8CCFFF)
private val GenZTeal = Color(0xFF35C8C3)
private val GenZCyan = Color(0xFF57D3FF)
private val GenZLavender = Color(0xFFB8B5FF)
private val GenZMint = Color(0xFF7FDBDA)
private val GenZPeach = Color(0xFFFFB5A7)
private val GenZSky = Color(0xFFA8DADC)
private val GenZGradientStart = Color(0xFF35C8C3)
private val GenZGradientEnd = Color(0xFF57D3FF)

// Map colors
private val MapBackground = Color(0xFFF8FBFD)
private val MapWater = Color(0xFFCCE5FF)
private val MapLand = Color(0xFFE8F4EC)
private val MapRoad = Color(0xFFF5F5F5)
private val RadarGlow = Color(0xFF35C8C3)
private val UserLocationGlow = Color(0xFF57D3FF)

// ==================== DATA CLASSES ====================

data class NearbyCommunity(
    val id: String,
    val name: String,
    val sport: String,
    val emoji: String,
    val distance: String,
    val memberCount: Int,
    val memberAvatars: List<String>,
    val accentColor: Color,
    val latitude: Float,
    val longitude: Float
)

data class MapPin(
    val community: NearbyCommunity,
    val x: Float,
    val y: Float
)

// ==================== SAMPLE DATA ====================

private val nearbyCommunities = listOf(
    NearbyCommunity(
        id = "1",
        name = "Badminton Jogja Players",
        sport = "Badminton",
        emoji = "🏸",
        distance = "1.2 km",
        memberCount = 23,
        memberAvatars = listOf("👤", "👤", "👤", "👤"),
        accentColor = PeachGlow,
        latitude = 0.3f,
        longitude = 0.6f
    ),
    NearbyCommunity(
        id = "2",
        name = "Futsal United Jakarta",
        sport = "Futsal",
        emoji = "⚽",
        distance = "2.5 km",
        memberCount = 47,
        memberAvatars = listOf("👤", "👤", "👤"),
        accentColor = MintBreeze,
        latitude = 0.7f,
        longitude = 0.3f
    ),
    NearbyCommunity(
        id = "3",
        name = "Basketball Stars BSD",
        sport = "Basketball",
        emoji = "🏀",
        distance = "3.1 km",
        memberCount = 35,
        memberAvatars = listOf("👤", "👤", "👤", "👤"),
        accentColor = SkyMist,
        latitude = 0.2f,
        longitude = 0.25f
    ),
    NearbyCommunity(
        id = "4",
        name = "Volleyball Squad",
        sport = "Volleyball",
        emoji = "🏐",
        distance = "1.8 km",
        memberCount = 19,
        memberAvatars = listOf("👤", "👤", "👤"),
        accentColor = RoseDust,
        latitude = 0.55f,
        longitude = 0.75f
    ),
    NearbyCommunity(
        id = "5",
        name = "Running Club Senayan",
        sport = "Running",
        emoji = "🏃",
        distance = "4.2 km",
        memberCount = 62,
        memberAvatars = listOf("👤", "👤", "👤", "👤"),
        accentColor = GenZMint,
        latitude = 0.8f,
        longitude = 0.6f
    ),
    NearbyCommunity(
        id = "6",
        name = "Gym Fitness Pro",
        sport = "Gym",
        emoji = "💪",
        distance = "0.8 km",
        memberCount = 28,
        memberAvatars = listOf("👤", "👤", "👤"),
        accentColor = GenZPeach,
        latitude = 0.4f,
        longitude = 0.4f
    ),
    NearbyCommunity(
        id = "7",
        name = "Hiking Adventures ID",
        sport = "Hiking",
        emoji = "🥾",
        distance = "5.5 km",
        memberCount = 41,
        memberAvatars = listOf("👤", "👤", "👤", "👤"),
        accentColor = SoftLavender,
        latitude = 0.15f,
        longitude = 0.8f
    )
)

/**
 * Find Communities Nearby Screen
 * Premium Gen-Z aesthetic with interactive map and radar scanning animation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindCommunitiesNearbyScreen(
    navController: NavHostController
) {
    var isScanning by remember { mutableStateOf(true) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedCommunity by remember { mutableStateOf<NearbyCommunity?>(null) }
    var detectedCommunities by remember { mutableStateOf<List<NearbyCommunity>>(emptyList()) }

    // Simulate community detection
    LaunchedEffect(Unit) {
        delay(1500)
        nearbyCommunities.forEachIndexed { index, community ->
            delay(400)
            detectedCommunities = detectedCommunities + community
        }
        delay(500)
        isScanning = false
        showBottomSheet = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CascadingWhite,
                        GenZBlue.copy(alpha = 0.06f),
                        GenZMint.copy(alpha = 0.08f)
                    )
                )
            )
    ) {
        // Animated background blobs
        NearbyBackgroundBlobs()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // TOP HEADER SECTION
            NearbyHeader(
                isScanning = isScanning,
                onBackClick = { navController.popBackStack() }
            )

            // INTERACTIVE MAP WITH RADAR
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                InteractiveRadarMap(
                    communities = detectedCommunities,
                    isScanning = isScanning,
                    onCommunityClick = { community ->
                        selectedCommunity = community
                    }
                )
            }
        }

        // BOTTOM FLOATING RESULT CARD
        AnimatedVisibility(
            visible = showBottomSheet,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            NearbyCommunitiesBottomSheet(
                communities = detectedCommunities,
                onCommunityClick = { community ->
                    navController.navigate(
                        Screen.CommunityFeed.createRoute(
                            communityId = community.id,
                            name = community.name,
                            emoji = community.emoji
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.45f)
            )
        }

        // Community Detail Popup
        selectedCommunity?.let { community ->
            CommunityQuickView(
                community = community,
                onDismiss = { selectedCommunity = null },
                onViewClick = {
                    navController.navigate(
                        Screen.CommunityFeed.createRoute(
                            communityId = community.id,
                            name = community.name,
                            emoji = community.emoji
                        )
                    )
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp)
            )
        }
    }
}

// ==================== BACKGROUND BLOBS ====================

@Composable
private fun NearbyBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "nearby_blobs")

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
        val radius1 = size.minDimension * 0.4f
        val radius2 = size.minDimension * 0.35f

        // Teal blob
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    GenZTeal.copy(alpha = 0.15f),
                    GenZTeal.copy(alpha = 0.05f),
                    Color.Transparent
                )
            ),
            radius = radius1,
            center = Offset(
                x = size.width * 0.8f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 50f,
                y = size.height * 0.15f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f
            )
        )

        // Blue blob
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    GenZBlue.copy(alpha = 0.12f),
                    GenZBlue.copy(alpha = 0.04f),
                    Color.Transparent
                )
            ),
            radius = radius2,
            center = Offset(
                x = size.width * 0.2f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 40f,
                y = size.height * 0.7f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 35f
            )
        )
    }
}

// ==================== HEADER SECTION ====================

@Composable
private fun NearbyHeader(
    isScanning: Boolean,
    onBackClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "header_anim")
    
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .padding(top = 32.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = GenZTeal.copy(alpha = 0.15f),
                spotColor = GenZTeal.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(28.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                GenZTeal.copy(alpha = 0.1f),
                                GenZBlue.copy(alpha = 0.1f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onBackClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = GenZTeal,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title and subtitle
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Find Nearby Communities",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        letterSpacing = (-0.3).sp
                    ),
                    color = NeutralInk
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isScanning) {
                        Text(
                            text = "Detecting sports communities around you",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp
                            ),
                            color = WarmHaze
                        )
                        // Animated dots
                        Text(
                            text = "...",
                            style = MaterialTheme.typography.bodySmall,
                            color = GenZTeal,
                            modifier = Modifier.alpha(dotAlpha)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = GenZTeal,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Found ${nearbyCommunities.size} communities nearby",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp
                            ),
                            color = GenZTeal
                        )
                    }
                }
            }

            // Search icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = GenZBlue.copy(alpha = 0.12f),
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { /* Search action */ }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    tint = GenZTeal,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

// ==================== INTERACTIVE RADAR MAP ====================

@Composable
private fun InteractiveRadarMap(
    communities: List<NearbyCommunity>,
    isScanning: Boolean,
    onCommunityClick: (NearbyCommunity) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar_anim")

    // Radar rotation
    val radarRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarRotation"
    )

    // Radar pulse scales
    val pulse1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse1"
    )

    val pulse2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing, delayMillis = 666),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse2"
    )

    val pulse3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing, delayMillis = 1333),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse3"
    )

    // User location pulse
    val userPulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "userPulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Map background with soft styling
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = GenZTeal.copy(alpha = 0.12f),
                    spotColor = GenZBlue.copy(alpha = 0.12f)
                ),
            shape = RoundedCornerShape(32.dp),
            color = MapBackground
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Stylized map canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val maxRadius = minOf(size.width, size.height) * 0.45f

                    // Draw stylized map elements (abstract roads and areas)
                    drawMapElements(this, size)

                    // Draw radar circles (static guide rings)
                    for (i in 1..4) {
                        drawCircle(
                            color = GenZTeal.copy(alpha = 0.08f),
                            radius = maxRadius * (i / 4f),
                            center = Offset(centerX, centerY),
                            style = Stroke(width = 1.5f)
                        )
                    }

                    // Draw animated radar pulses (only when scanning)
                    if (isScanning) {
                        // Pulse 1 - ensure radius is always > 0
                        val pulse1Radius = (maxRadius * pulse1).coerceAtLeast(1f)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    RadarGlow.copy(alpha = 0.4f * (1f - pulse1)),
                                    RadarGlow.copy(alpha = 0.1f * (1f - pulse1)),
                                    Color.Transparent
                                ),
                                center = Offset(centerX, centerY),
                                radius = pulse1Radius
                            ),
                            radius = pulse1Radius,
                            center = Offset(centerX, centerY)
                        )

                        // Pulse 2 - ensure radius is always > 0
                        val pulse2Radius = (maxRadius * pulse2).coerceAtLeast(1f)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    RadarGlow.copy(alpha = 0.4f * (1f - pulse2)),
                                    RadarGlow.copy(alpha = 0.1f * (1f - pulse2)),
                                    Color.Transparent
                                ),
                                center = Offset(centerX, centerY),
                                radius = pulse2Radius
                            ),
                            radius = pulse2Radius,
                            center = Offset(centerX, centerY)
                        )

                        // Pulse 3 - ensure radius is always > 0
                        val pulse3Radius = (maxRadius * pulse3).coerceAtLeast(1f)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    RadarGlow.copy(alpha = 0.4f * (1f - pulse3)),
                                    RadarGlow.copy(alpha = 0.1f * (1f - pulse3)),
                                    Color.Transparent
                                ),
                                center = Offset(centerX, centerY),
                                radius = pulse3Radius
                            ),
                            radius = pulse3Radius,
                            center = Offset(centerX, centerY)
                        )

                        // Rotating radar sweep
                        rotate(degrees = radarRotation, pivot = Offset(centerX, centerY)) {
                            drawArc(
                                brush = Brush.sweepGradient(
                                    0f to Color.Transparent,
                                    0.1f to RadarGlow.copy(alpha = 0.25f),
                                    0.2f to Color.Transparent
                                ),
                                startAngle = 0f,
                                sweepAngle = 60f,
                                useCenter = true,
                                topLeft = Offset(centerX - maxRadius, centerY - maxRadius),
                                size = Size(maxRadius * 2, maxRadius * 2)
                            )
                        }
                    }

                    // User location center glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                UserLocationGlow.copy(alpha = 0.4f),
                                UserLocationGlow.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        ),
                        radius = 50f * userPulse,
                        center = Offset(centerX, centerY)
                    )

                    // User location pin
                    drawCircle(
                        color = Color.White,
                        radius = 20f,
                        center = Offset(centerX, centerY)
                    )
                    drawCircle(
                        brush = Brush.linearGradient(
                            colors = listOf(GenZTeal, GenZCyan)
                        ),
                        radius = 14f,
                        center = Offset(centerX, centerY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 5f,
                        center = Offset(centerX, centerY)
                    )
                }

                // Community pins - use single BoxWithConstraints outside the loop
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val containerWidth = maxWidth
                    val containerHeight = maxHeight
                    val pinSize = 56.dp
                    
                    communities.forEach { community ->
                        val offsetX = (containerWidth.value * community.longitude - pinSize.value / 2).dp
                        val offsetY = (containerHeight.value * community.latitude - pinSize.value / 2).dp

                        CommunityMapPin(
                            community = community,
                            onClick = { onCommunityClick(community) },
                            modifier = Modifier.offset(x = offsetX, y = offsetY)
                        )
                    }
                }
            }
        }

        // Legend
        MapLegend(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
        )
    }
}

// Helper function to draw stylized map elements
private fun drawMapElements(drawScope: DrawScope, size: Size) {
    with(drawScope) {
        // Abstract land masses (soft green areas)
        drawRoundRect(
            color = MapLand,
            topLeft = Offset(size.width * 0.1f, size.height * 0.15f),
            size = Size(size.width * 0.35f, size.height * 0.25f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(40f, 40f)
        )
        
        drawRoundRect(
            color = MapLand.copy(alpha = 0.8f),
            topLeft = Offset(size.width * 0.55f, size.height * 0.4f),
            size = Size(size.width * 0.4f, size.height * 0.3f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(50f, 50f)
        )
        
        drawRoundRect(
            color = MapLand.copy(alpha = 0.6f),
            topLeft = Offset(size.width * 0.05f, size.height * 0.55f),
            size = Size(size.width * 0.3f, size.height * 0.35f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(45f, 45f)
        )

        // Abstract water body
        drawCircle(
            color = MapWater.copy(alpha = 0.5f),
            radius = size.width * 0.12f,
            center = Offset(size.width * 0.7f, size.height * 0.2f)
        )

        // Abstract roads
        val roadColor = MapRoad.copy(alpha = 0.8f)
        
        // Horizontal road
        drawRoundRect(
            color = roadColor,
            topLeft = Offset(0f, size.height * 0.48f),
            size = Size(size.width, 8f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
        )
        
        // Vertical road
        drawRoundRect(
            color = roadColor,
            topLeft = Offset(size.width * 0.48f, 0f),
            size = Size(8f, size.height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
        )
        
        // Diagonal road
        drawLine(
            color = roadColor,
            start = Offset(0f, size.height * 0.8f),
            end = Offset(size.width * 0.6f, size.height * 0.2f),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )
    }
}

// ==================== COMMUNITY MAP PIN ====================

@Composable
private fun CommunityMapPin(
    community: NearbyCommunity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pin_${community.id}")
    
    val pinScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pinScale"
    )

    // Entry animation
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    val entryScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "entryScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .scale(entryScale * pinScale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        // Pin with emoji and member count
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Glow effect
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                community.accentColor.copy(alpha = 0.4f),
                                community.accentColor.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // Pin body
            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = community.accentColor.copy(alpha = 0.3f)
                    ),
                shape = CircleShape,
                color = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.White,
                                    community.accentColor.copy(alpha = 0.15f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = community.emoji,
                        fontSize = 22.sp
                    )
                }
            }

            // Member count badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-4).dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(GenZTeal, GenZCyan)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "+${community.memberCount}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.sp
                    ),
                    color = Color.White
                )
            }
        }

        // Pin tail
        Canvas(modifier = Modifier.size(12.dp)) {
            val path = Path().apply {
                moveTo(size.width / 2, size.height)
                lineTo(0f, 0f)
                lineTo(size.width, 0f)
                close()
            }
            drawPath(
                path = path,
                color = Color.White,
                style = androidx.compose.ui.graphics.drawscope.Fill
            )
        }
    }
}

// ==================== MAP LEGEND ====================

@Composable
private fun MapLegend(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.95f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(GenZTeal, GenZCyan)
                            ),
                            shape = CircleShape
                        )
                )
                Text(
                    text = "You",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            color = PeachGlow,
                            shape = CircleShape
                        )
                )
                Text(
                    text = "Community",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze
                )
            }
        }
    }
}

// ==================== BOTTOM SHEET ====================

@Composable
private fun NearbyCommunitiesBottomSheet(
    communities: List<NearbyCommunity>,
    onCommunityClick: (NearbyCommunity) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 32.dp,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                ambientColor = Color.Black.copy(alpha = 0.15f),
                spotColor = Color.Black.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Handle bar
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally)
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        color = MistGray.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            // Header
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nearby Communities",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = NeutralInk
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GenZTeal.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "${communities.size} found",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = GenZTeal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Communities around you based on your sports preference",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp
                    ),
                    color = WarmHaze
                )
            }

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MistGray.copy(alpha = 0.3f),
                                MistGray.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Community list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(communities, key = { it.id }) { community ->
                    NearbyCommunityCard(
                        community = community,
                        onClick = { onCommunityClick(community) }
                    )
                }
            }
        }
    }
}

// ==================== NEARBY COMMUNITY CARD ====================

@Composable
private fun NearbyCommunityCard(
    community: NearbyCommunity,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = community.accentColor.copy(alpha = 0.1f),
                spotColor = community.accentColor.copy(alpha = 0.1f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sport emoji icon
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                community.accentColor.copy(alpha = 0.2f),
                                community.accentColor.copy(alpha = 0.1f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = community.emoji,
                    fontSize = 26.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Community info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = community.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        lineHeight = 18.sp
                    ),
                    color = NeutralInk,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Distance
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = null,
                            tint = GenZTeal,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = community.distance,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            ),
                            color = WarmHaze
                        )
                    }

                    // Dot separator
                    Box(
                        modifier = Modifier
                            .size(3.dp)
                            .background(
                                color = MistGray,
                                shape = CircleShape
                            )
                    )

                    // Sport type
                    Text(
                        text = community.sport,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp
                        ),
                        color = WarmHaze
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Member avatars and count
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar stack
                    Box {
                        community.memberAvatars.take(4).forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .offset(x = (index * 14).dp)
                                    .size(24.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                GenZBlue.copy(alpha = 0.3f + index * 0.1f),
                                                GenZTeal.copy(alpha = 0.3f + index * 0.1f)
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = Color.White,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width((community.memberAvatars.take(4).size * 14 + 8).dp))

                    // Member count badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GenZTeal.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "${community.memberCount}+",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = GenZTeal,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Chevron
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                GenZTeal.copy(alpha = 0.1f),
                                GenZBlue.copy(alpha = 0.1f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "See more",
                    tint = GenZTeal,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ==================== COMMUNITY QUICK VIEW POPUP ====================

@Composable
private fun CommunityQuickView(
    community: NearbyCommunity,
    onDismiss: () -> Unit,
    onViewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Clean popup without dark background overlay
    Surface(
        modifier = modifier
            .shadow(
                elevation = 32.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = community.accentColor.copy(alpha = 0.3f),
                spotColor = community.accentColor.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(28.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Close button
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .size(32.dp)
                    .background(
                        color = MistGray.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = WarmHaze,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Large emoji
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                community.accentColor.copy(alpha = 0.25f),
                                community.accentColor.copy(alpha = 0.1f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = community.emoji,
                    fontSize = 40.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Community name - with proper text handling
            Text(
                text = community.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    lineHeight = 24.sp
                ),
                color = NeutralInk,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Stats row
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Distance
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        tint = GenZTeal,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = community.distance,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmHaze
                    )
                }

                // Members
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.People,
                        contentDescription = null,
                        tint = GenZBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${community.memberCount} members",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmHaze
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // View button
            Button(
                onClick = onViewClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(GenZTeal, GenZCyan)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "View Community",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
