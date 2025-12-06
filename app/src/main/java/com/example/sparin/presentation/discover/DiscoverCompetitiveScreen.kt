package com.example.sparin.presentation.discover

import android.app.DatePickerDialog
import androidx.compose.animation.*
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.ui.theme.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * PHASE 1 — COMPETITIVE MODE DISCOVER PAGE
 * ═══════════════════════════════════════════════════════════════════════════
 * 
 * Ultra-premium, Gen-Z iOS-style Competitive Mode landing page
 * Features:
 * - Dark mode iOS premium aesthetic
 * - Intense crimson/neon red accents
 * - Glassmorphism panels with soft inner shadows
 * - Micro-animations and particle effects
 * - Apple-grade typography and spacing
 * 
 * Color Palette:
 * - Primary Background: #0F0F12, #1A1A1D
 * - Accent: #FF3344, #FF5560, #D60026
 * - Glass: rgba(255,255,255,0.05-0.1)
 */

// ═══════════════════════════════════════════════════════════════════════════
// COLOR PALETTE — COMPETITIVE ESPORTS THEME
// ═══════════════════════════════════════════════════════════════════════════

// Primary Dark Backgrounds
val CompetitiveDarkBase = Color(0xFF0F0F12)
val CompetitiveDarkSurface = Color(0xFF1A1A1D)
val CompetitiveDarkCard = Color(0xFF141418)
val CompetitiveDarkElevated = Color(0xFF1E1E22)

// Crimson Red Accents (Primary)
val CrimsonPrimary = Color(0xFFFF3344)
val CrimsonBright = Color(0xFFFF5560)
val CrimsonDeep = Color(0xFFD60026)
val CrimsonNeon = Color(0xFFFF2D3D)
val CrimsonGlow = Color(0xFFFF6B7A)

// Orange/Gold Accents (Secondary)
val CompetitiveOrange = Color(0xFFFF6B35)
val CompetitiveGold = Color(0xFFFFAA33)
val CompetitiveAmber = Color(0xFFFF8C42)

// Glassmorphism Colors
val GlassWhiteLight = Color(0x0DFFFFFF)  // 5%
val GlassWhiteMedium = Color(0x1AFFFFFF) // 10%
val GlassWhiteStrong = Color(0x33FFFFFF) // 20%
val GlassBorderLight = Color(0x1AFFFFFF)
val GlassBorderMedium = Color(0x33FFFFFF)

// Text Colors
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB8B8BC)
val TextMuted = Color(0xFF6E6E73)
val TextAccent = CrimsonBright

// Tier Colors
val TierBronze = Color(0xFFCD7F32)
val TierSilver = Color(0xFFC0C0C0)
val TierGold = Color(0xFFFFD700)
val TierPlatinum = Color(0xFFE5E4E2)
val TierDiamond = Color(0xFFB9F2FF)

// Competitive Sport Categories
val competitiveSportCategories = listOf(
    DiscoverSportCategory("All", "🔥"),
    DiscoverSportCategory("Badminton", "🏸"),
    DiscoverSportCategory("Futsal", "⚽"),
    DiscoverSportCategory("Basket", "🏀"),
    DiscoverSportCategory("Muaythai", "🥋"),
    DiscoverSportCategory("Boxing", "🥊"),
    DiscoverSportCategory("Tennis", "🎾"),
    DiscoverSportCategory("Voli", "🏐")
)

// ═══════════════════════════════════════════════════════════════════════════
// DATA MODELS
// ═══════════════════════════════════════════════════════════════════════════

data class CompetitiveRoomItem(
    val id: String,
    val title: String,
    val sport: String,
    val emoji: String,
    val location: String,
    val distance: String,
    val schedule: String,
    val date: String = "",
    val description: String = "",
    val hostName: String = "Host",
    val hostRating: Float = 4.5f,
    val currentPlayers: Int,
    val maxPlayers: Int,
    val skillLevel: String,
    val tier: String, // Bronze, Silver, Gold, Platinum, Diamond
    val tags: List<String>,
    val difficulty: Int, // 1-5
    val entryFee: String? = null,
    val prize: String? = null,
    val xpReward: Int = 25,
    val isRanked: Boolean = true,
    val requirements: List<String> = emptyList()
)

// Sample Rooms Data
val sampleCompetitiveRooms = listOf(
    CompetitiveRoomItem(
        id = "1",
        title = "Badminton Ranked — Tier 3",
        sport = "Badminton",
        emoji = "🏸",
        location = "Elite Sports Center",
        distance = "0.8 km",
        schedule = "Today · 19:00",
        date = "Dec 2, 2025",
        description = "High-intensity ranked match for serious players.",
        hostName = "ProPlayer99",
        hostRating = 4.8f,
        currentPlayers = 1,
        maxPlayers = 2,
        skillLevel = "Intermediate+",
        tier = "Silver",
        tags = listOf("Ranked", "1v1"),
        difficulty = 4,
        xpReward = 35,
        isRanked = true
    ),
    CompetitiveRoomItem(
        id = "2",
        title = "Futsal Championship Qualifier",
        sport = "Futsal",
        emoji = "⚽",
        location = "Champion Arena",
        distance = "2.1 km",
        schedule = "Tomorrow · 20:00",
        date = "Dec 3, 2025",
        description = "Official tournament qualifier match.",
        hostName = "TournamentOrg",
        hostRating = 5.0f,
        currentPlayers = 8,
        maxPlayers = 10,
        skillLevel = "Advanced",
        tier = "Gold",
        tags = listOf("Tournament", "Team"),
        difficulty = 5,
        entryFee = "100K",
        prize = "2M Pool",
        xpReward = 50,
        isRanked = true
    ),
    CompetitiveRoomItem(
        id = "3",
        title = "Basketball Pro League",
        sport = "Basket",
        emoji = "🏀",
        location = "Senayan Pro Court",
        distance = "1.5 km",
        schedule = "Today · 18:00",
        date = "Dec 2, 2025",
        description = "Elite basketball league match. Pro-level only.",
        hostName = "EliteHoops",
        hostRating = 4.9f,
        currentPlayers = 4,
        maxPlayers = 5,
        skillLevel = "Pro",
        tier = "Platinum",
        tags = listOf("League", "5v5"),
        difficulty = 5,
        xpReward = 45,
        isRanked = true
    ),
    CompetitiveRoomItem(
        id = "4",
        title = "Boxing Sparring — Open",
        sport = "Boxing",
        emoji = "🥊",
        location = "Fight Club Elite",
        distance = "3.2 km",
        schedule = "Sat · 17:00",
        date = "Dec 7, 2025",
        description = "Intense sparring session for fighters.",
        hostName = "FightMaster",
        hostRating = 4.7f,
        currentPlayers = 2,
        maxPlayers = 4,
        skillLevel = "Intermediate",
        tier = "Bronze",
        tags = listOf("Sparring", "Open"),
        difficulty = 3,
        xpReward = 25,
        isRanked = false
    ),
    CompetitiveRoomItem(
        id = "5",
        title = "Tennis Singles — Ranked",
        sport = "Tennis",
        emoji = "🎾",
        location = "Grand Slam Courts",
        distance = "4.0 km",
        schedule = "Sun · 09:00",
        date = "Dec 8, 2025",
        description = "Competitive singles match for ranking.",
        hostName = "AcePlayer",
        hostRating = 4.6f,
        currentPlayers = 1,
        maxPlayers = 2,
        skillLevel = "Advanced",
        tier = "Gold",
        tags = listOf("Ranked", "Singles"),
        difficulty = 4,
        xpReward = 40,
        isRanked = true
    )
)

// ═══════════════════════════════════════════════════════════════════════════
// MAIN SCREEN COMPOSABLE
// ═══════════════════════════════════════════════════════════════════════════

@Composable
fun DiscoverCompetitiveScreen(navController: NavHostController) {
    var selectedSportFilter by remember { mutableStateOf<String?>("All") }
    var showFindMatchModal by remember { mutableStateOf(false) }
    var isTransitioningToRadar by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CompetitiveDarkBase)
    ) {
        // Animated Background with Particles
        CompetitiveBackgroundEffects()
        
        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            // 1) TOP HEADER
            CompetitiveHeader(
                onBackClick = { navController.popBackStack() }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 2) HERO — MAIN CTA
            HeroFindMatchButton(
                onClick = { showFindMatchModal = true }
            )
            
            Spacer(modifier = Modifier.height(28.dp))
            
            // 3) COMPETITIVE INFO PANEL
            CompetitiveInfoPanel()
            
            Spacer(modifier = Modifier.height(28.dp))
            
            // 5) SPORT FILTER ROW
            QuickFilterRow(
                selectedFilter = selectedSportFilter,
                onFilterSelected = { selectedSportFilter = it ?: "All" }
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // 4) ACTIVE ROOMS PREVIEW (Observation Only)
            ActiveRoomsSection(
                rooms = sampleCompetitiveRooms,
                selectedCategory = selectedSportFilter ?: "All"
            )
            
            Spacer(modifier = Modifier.height(100.dp))
        }
        
        // PHASE 2: Find Match Modal
        if (showFindMatchModal) {
            FindMatchModal(
                onDismiss = { showFindMatchModal = false },
                onStartFinding = { selectedSport, selectedMatchType ->
                    isTransitioningToRadar = true
                    showFindMatchModal = false
                    // Navigate to Radar Screen with params
                    navController.navigate(
                        Screen.CompetitiveRadar.createRoute(
                            sport = selectedSport,
                            matchType = selectedMatchType
                        )
                    )
                }
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// BACKGROUND EFFECTS — PARTICLES & AMBIENT GLOW
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun CompetitiveBackgroundEffects() {
    val infiniteTransition = rememberInfiniteTransition(label = "bg_effects")
    
    // Slow rotation for ambient glow
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    // Pulsing glow intensity
    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_intensity"
    )
    
    // Particle animation offset
    val particleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particles"
    )
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Base gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CompetitiveDarkBase,
                            CompetitiveDarkSurface.copy(alpha = 0.8f),
                            CompetitiveDarkBase,
                            CrimsonDeep.copy(alpha = 0.03f),
                            CompetitiveDarkBase
                        )
                    )
                )
        )
        
        // Ambient red glow blobs
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(120.dp)
        ) {
            // Top-left crimson glow
            drawCircle(
                color = CrimsonPrimary.copy(alpha = glowIntensity * 0.5f),
                radius = 250f,
                center = Offset(
                    x = size.width * 0.1f + cos(Math.toRadians(rotation.toDouble())).toFloat() * 50f,
                    y = size.height * 0.08f + sin(Math.toRadians(rotation.toDouble())).toFloat() * 30f
                )
            )
            
            // Right-side orange accent
            drawCircle(
                color = CompetitiveOrange.copy(alpha = glowIntensity * 0.3f),
                radius = 180f,
                center = Offset(
                    x = size.width * 0.9f,
                    y = size.height * 0.35f + sin(Math.toRadians(rotation.toDouble() * 0.5)).toFloat() * 40f
                )
            )
            
            // Bottom crimson glow
            drawCircle(
                color = CrimsonDeep.copy(alpha = glowIntensity * 0.4f),
                radius = 300f,
                center = Offset(
                    x = size.width * 0.5f + cos(Math.toRadians(rotation.toDouble() * 0.7)).toFloat() * 60f,
                    y = size.height * 0.85f
                )
            )
        }
        
        // Micro spark particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val particleCount = 15
            for (i in 0 until particleCount) {
                val seed = i * 137.5f
                val x = ((seed + particleOffset * (0.3f + i * 0.05f)) % size.width)
                val y = ((seed * 2.3f + particleOffset * (0.2f + i * 0.03f)) % size.height)
                val alpha = (sin((particleOffset + seed) * 0.01) * 0.3f + 0.2f).toFloat()
                val radius = (2f + sin((particleOffset + seed) * 0.02) * 1.5f).toFloat()
                
                drawCircle(
                    color = CrimsonBright.copy(alpha = alpha.coerceIn(0.1f, 0.5f)),
                    radius = radius,
                    center = Offset(x, y)
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 1) TOP HEADER — Title, Subtitle, Flame Icon
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun CompetitiveHeader(onBackClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "header_anim")
    
    // Glowing underline animation
    val underlineGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "underline_glow"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Back Button
        Surface(
            onClick = onBackClick,
            modifier = Modifier
                .size(44.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = CrimsonPrimary.copy(alpha = 0.2f)
                ),
            shape = CircleShape,
            color = CompetitiveDarkElevated
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                GlassBorderLight,
                                GlassBorderMedium,
                                GlassBorderLight
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(22.dp),
                    tint = TextPrimary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Title Row with Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Animated flame icon
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                CrimsonPrimary.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚔️",
                    fontSize = 24.sp
                )
            }
            
            Column {
                // Main Title with glowing underline
                Text(
                    text = "Competitive Mode",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        letterSpacing = (-1).sp
                    ),
                    color = TextPrimary
                )
                
                // Animated glowing underline
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(3.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    CrimsonPrimary.copy(alpha = underlineGlow),
                                    CrimsonBright.copy(alpha = underlineGlow * 0.8f),
                                    CrimsonPrimary.copy(alpha = underlineGlow * 0.5f),
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Subtitle
        Text(
            text = "Find opponents, earn XP, climb the leaderboard.",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 15.sp,
                letterSpacing = 0.3.sp
            ),
            color = TextSecondary
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 2) HERO — MAIN CTA BUTTON (FIND NEARBY MATCH)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun HeroFindMatchButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_cta")
    
    // Floating animation
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )
    
    // Glow pulse
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_pulse"
    )
    
    // Scale pulse
    val scalePulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_pulse"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = -floatOffset
                    scaleX = scalePulse
                    scaleY = scalePulse
                }
        ) {
            // Outer glow layer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .blur(24.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                CrimsonDeep.copy(alpha = glowPulse * 0.6f),
                                CrimsonPrimary.copy(alpha = glowPulse * 0.7f),
                                CrimsonDeep.copy(alpha = glowPulse * 0.6f)
                            )
                        ),
                        shape = RoundedCornerShape(36.dp)
                    )
            )
            
            // Main button
            Surface(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(36.dp),
                        ambientColor = CrimsonPrimary.copy(alpha = 0.5f),
                        spotColor = CrimsonBright.copy(alpha = 0.5f)
                    ),
                shape = RoundedCornerShape(36.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    CrimsonDeep,
                                    CrimsonPrimary,
                                    CrimsonDeep
                                )
                            )
                        )
                        // Inner highlight (glossy effect)
                        .drawWithContent {
                            drawContent()
                            drawRoundRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.2f),
                                        Color.Transparent
                                    ),
                                    startY = 0f,
                                    endY = size.height * 0.5f
                                ),
                                cornerRadius = CornerRadius(36.dp.toPx())
                            )
                        }
                        .border(
                            width = 1.5.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    CrimsonGlow.copy(alpha = 0.6f),
                                    CrimsonPrimary.copy(alpha = 0.3f),
                                    CrimsonDeep.copy(alpha = 0.2f)
                                )
                            ),
                            shape = RoundedCornerShape(36.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.NearMe,
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Find Nearby Match",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                letterSpacing = 0.5.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Subtext
        Text(
            text = "Search for players around your location",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp
            ),
            color = TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 3) COMPETITIVE INFO PANEL — Benefits & Rules Explanation
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun CompetitiveInfoPanel() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Section Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "⚔️",
                fontSize = 18.sp
            )
            Text(
                text = "HOW COMPETITIVE MODE WORKS",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 2.sp
                ),
                color = TextSecondary
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Glass Panel Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = CrimsonPrimary.copy(alpha = 0.15f)
                ),
            shape = RoundedCornerShape(24.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                GlassWhiteMedium,
                                GlassWhiteLight,
                                CompetitiveDarkCard.copy(alpha = 0.9f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                GlassBorderMedium,
                                GlassBorderLight,
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // IF YOU WIN
                    BenefitInfoCard(
                        icon = "🏆",
                        title = "IF YOU WIN",
                        items = listOf(
                            "✓ Earn +25 to +50 XP points",
                            "✓ Gain +15 Rank Points (RP)",
                            "✓ Increase your Win Rate %",
                            "✓ Unlock Season Rewards faster"
                        ),
                        accentColor = CompetitiveGold,
                        isPositive = true
                    )
                    
                    // IF YOU LOSE
                    BenefitInfoCard(
                        icon = "💔",
                        title = "IF YOU LOSE",
                        items = listOf(
                            "• Lose -10 Rank Points (RP)",
                            "• Still earn +10 XP for participating",
                            "• Learn from the match replay",
                            "• Keep trying — next win is closer!"
                        ),
                        accentColor = CrimsonPrimary,
                        isPositive = false
                    )
                    
                    // Divider
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        GlassBorderLight,
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    
                    // RANK TIERS INFO
                    RankTiersInfo()
                    
                    // FAIR PLAY RULES
                    FairPlayRules()
                }
            }
        }
    }
}

@Composable
private fun BenefitInfoCard(
    icon: String,
    title: String,
    items: List<String>,
    accentColor: Color,
    isPositive: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "benefit_$title")
    
    val iconGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isPositive) CompetitiveGold.copy(alpha = 0.08f) else CrimsonPrimary.copy(alpha = 0.08f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = accentColor.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = iconGlow * 0.4f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(icon, fontSize = 20.sp)
                    }
                    
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp
                        ),
                        color = accentColor
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items.forEach { item ->
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp
                            ),
                            color = if (isPositive) TextPrimary else TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RankTiersInfo() {
    Column {
        Text(
            text = "🎖️ RANK TIERS",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp
            ),
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(10.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(
                Triple("🥉", "Bronze", TierBronze),
                Triple("🥈", "Silver", TierSilver),
                Triple("🥇", "Gold", TierGold),
                Triple("💎", "Platinum", TierPlatinum),
                Triple("👑", "Diamond", TierDiamond)
            ).forEach { (emoji, name, color) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(emoji, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = color
                    )
                }
            }
        }
    }
}

@Composable
private fun FairPlayRules() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CompetitiveDarkElevated.copy(alpha = 0.6f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = GlassBorderLight,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(14.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("📋", fontSize = 14.sp)
                    Text(
                        text = "FAIR PLAY RULES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        ),
                        color = TextMuted
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "• No-show = -20 RP penalty\n• Report unsportsmanlike behavior\n• Match must be verified by both players\n• Disputes resolved within 24 hours",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        lineHeight = 18.sp
                    ),
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
private fun InfoChip(
    modifier: Modifier = Modifier,
    icon: String,
    title: String,
    value: String,
    accentColor: Color,
    showProgress: Boolean = false,
    progress: Float = 0f,
    showTrend: Boolean = false,
    trendUp: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "chip_$title")
    
    val iconGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_glow"
    )
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = CompetitiveDarkElevated.copy(alpha = 0.8f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = accentColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(14.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Icon with glow
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = iconGlow * 0.3f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(icon, fontSize = 16.sp)
                    }
                    
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = TextMuted
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = accentColor
                    )
                    
                    if (showTrend) {
                        Icon(
                            imageVector = if (trendUp) Icons.Rounded.TrendingUp else Icons.Rounded.TrendingDown,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = if (trendUp) MintBreeze else CrimsonPrimary
                        )
                    }
                }
                
                // Progress bar for season rewards
                if (showProgress) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(
                                CompetitiveDarkBase,
                                RoundedCornerShape(2.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .height(4.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(accentColor, accentColor.copy(alpha = 0.6f))
                                    ),
                                    RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DifficultyBar() {
    val infiniteTransition = rememberInfiniteTransition(label = "difficulty")
    
    val barAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bar_anim"
    )
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = CompetitiveDarkElevated.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = CrimsonPrimary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🔥", fontSize = 16.sp)
                Text(
                    text = "Match Difficulty",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = TextSecondary
                )
            }
            
            // Difficulty bars with animation
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(5) { index ->
                    val isActive = index < 3
                    val delay = index * 0.15f
                    val animatedAlpha = if (isActive) {
                        val phase = (barAnimation + delay) % 1f
                        0.5f + 0.5f * sin(phase * Math.PI.toFloat() * 2)
                    } else 0.2f
                    
                    Box(
                        modifier = Modifier
                            .width(8.dp)
                            .height(16.dp)
                            .background(
                                color = if (isActive) {
                                    CrimsonPrimary.copy(alpha = animatedAlpha)
                                } else {
                                    TextMuted.copy(alpha = 0.3f)
                                },
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 5) SPORT FILTER ROW — Only Sport Category Filter
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun QuickFilterRow(
    selectedFilter: String?,
    onFilterSelected: (String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("🏅", fontSize = 16.sp)
            Text(
                text = "BROWSE BY SPORT",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.5.sp
                ),
                color = TextMuted
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            competitiveSportCategories.forEach { category ->
                SportFilterChip(
                    emoji = category.emoji,
                    label = category.name,
                    isSelected = selectedFilter == category.name,
                    onClick = {
                        onFilterSelected(if (selectedFilter == category.name) null else category.name)
                    }
                )
            }
        }
    }
}

@Composable
private fun SportFilterChip(
    emoji: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) CrimsonPrimary.copy(alpha = 0.2f) else CompetitiveDarkElevated,
        animationSpec = tween(200),
        label = "chip_bg"
    )
    
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) CrimsonPrimary else GlassBorderLight,
        animationSpec = tween(200),
        label = "chip_border"
    )
    
    Surface(
        onClick = onClick,
        modifier = Modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(emoji, fontSize = 16.sp)
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    ),
                    color = if (isSelected) CrimsonBright else TextSecondary
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 4) ACTIVE ROOMS PREVIEW — Observation Only (Dimmed Cards)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun ActiveRoomsSection(
    rooms: List<CompetitiveRoomItem>,
    selectedCategory: String
) {
    // Filter rooms by selected sport category
    val filteredRooms = when {
        selectedCategory == null || selectedCategory == "All" -> rooms
        else -> rooms.filter { it.sport == selectedCategory }
    }
    
    // Animation for section appearing
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(selectedCategory) {
        isVisible = false
        delay(100)
        isVisible = true
    }
    
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("👁️", fontSize = 18.sp)
                Column {
                    Text(
                        text = "ACTIVE ROOMS NEARBY",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 2.sp
                        ),
                        color = TextSecondary
                    )
                    Text(
                        text = "View only — Use \"Find Match\" to join",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp
                        ),
                        color = TextMuted
                    )
                }
            }
            
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CompetitiveDarkElevated
            ) {
                Text(
                    text = "${filteredRooms.size} found",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = TextMuted
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Show message when no rooms match filter
        if (filteredRooms.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = CompetitiveDarkCard.copy(alpha = 0.5f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔍", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No active rooms for this sport",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Be the first to create one!",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            // Room Cards (Observation Only - Dimmed to show it's just info)
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(300)) + slideInVertically(
                        initialOffsetY = { 50 },
                        animationSpec = tween(300)
                    )
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        filteredRooms.forEachIndexed { index, room ->
                            ObservationRoomCard(
                                room = room,
                                animationDelay = index * 80
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ObservationRoomCard(
    room: CompetitiveRoomItem,
    animationDelay: Int = 0
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(animationDelay.toLong())
        isVisible = true
    }
    
    val tierColor = when (room.tier) {
        "Bronze" -> TierBronze
        "Silver" -> TierSilver
        "Gold" -> TierGold
        "Platinum" -> TierPlatinum
        "Diamond" -> TierDiamond
        else -> TierSilver
    }
    
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(300)) + slideInHorizontally(
            initialOffsetX = { 100 },
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
    ) {
        // Dimmed card with "observation only" overlay
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    // Make the card look dimmed/faded
                    .graphicsLayer {
                        alpha = 0.65f // Dimmed effect
                    },
                shape = RoundedCornerShape(20.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    CompetitiveDarkCard.copy(alpha = 0.7f),
                                    CompetitiveDarkElevated.copy(alpha = 0.5f)
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    GlassBorderLight.copy(alpha = 0.5f),
                                    GlassBorderLight.copy(alpha = 0.2f)
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        // Top Row - Icon, Title, Tier
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Sport Icon
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                tierColor.copy(alpha = 0.15f),
                                                Color.Transparent
                                            )
                                        ),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = tierColor.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(14.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(room.emoji, fontSize = 24.sp)
                            }
                            
                            Spacer(modifier = Modifier.width(14.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = room.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    color = TextPrimary.copy(alpha = 0.85f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Tier badge
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = tierColor.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = room.tier,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            ),
                                            color = tierColor
                                        )
                                    }
                                    
                                    // Skill level
                                    Text(
                                        text = room.skillLevel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted
                                    )
                                }
                            }
                            
                            // Players count
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (room.currentPlayers >= room.maxPlayers - 1) {
                                        CrimsonPrimary.copy(alpha = 0.1f)
                                    } else {
                                        CompetitiveDarkElevated.copy(alpha = 0.5f)
                                    }
                                ) {
                                    Text(
                                        text = "${room.currentPlayers}/${room.maxPlayers}",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = if (room.currentPlayers >= room.maxPlayers - 1) {
                                            CrimsonBright.copy(alpha = 0.8f)
                                        } else {
                                            TextSecondary.copy(alpha = 0.7f)
                                        }
                                    )
                                }
                                
                                if (room.currentPlayers >= room.maxPlayers - 1) {
                                    Text(
                                        text = "Almost full",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp
                                        ),
                                        color = CrimsonBright.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Middle Row - Location & Schedule
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("📍", fontSize = 12.sp)
                                Text(
                                    text = "${room.location} • ${room.distance}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted.copy(alpha = 0.8f)
                                )
                            }
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("🕐", fontSize = 12.sp)
                                Text(
                                    text = room.schedule,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted.copy(alpha = 0.8f)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Tags row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            room.tags.take(3).forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = CompetitiveDarkElevated.copy(alpha = 0.4f)
                                ) {
                                    Text(
                                        text = tag,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp
                                        ),
                                        color = TextMuted.copy(alpha = 0.7f)
                                    )
                                }
                            }
                            
                            if (room.isRanked) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = CrimsonPrimary.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = "⚔️ Ranked",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp
                                        ),
                                        color = CrimsonBright.copy(alpha = 0.6f)
                                    )
                                }
                            }
                            
                            // XP reward
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CompetitiveGold.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "+${room.xpReward} XP",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp
                                    ),
                                    color = CompetitiveGold.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
            
            // "View Only" overlay badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        color = CompetitiveDarkBase.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = TextMuted.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("👁️", fontSize = 10.sp)
                    Text(
                        text = "View Only",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = TextMuted
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// PHASE 2 — FIND MATCH MODAL (3D Animated Pop-up)
// ═══════════════════════════════════════════════════════════════════════════

// Sport options for modal
private val modalSportOptions = listOf(
    DiscoverSportCategory("Badminton", "🏸"),
    DiscoverSportCategory("Futsal", "⚽"),
    DiscoverSportCategory("Basket", "🏀"),
    DiscoverSportCategory("Boxing", "🥊"),
    DiscoverSportCategory("Tennis", "🎾"),
    DiscoverSportCategory("Muaythai", "🥋"),
    DiscoverSportCategory("Running", "🏃"),
    DiscoverSportCategory("Gym", "🏋️"),
    DiscoverSportCategory("Voli", "🏐")
)

// Match type options based on sport
private data class MatchTypeOption(
    val label: String,
    val icon: String,
    val players: Int
)

// Function to get match types based on selected sport
private fun getMatchTypesForSport(sport: String): List<MatchTypeOption> {
    return when (sport) {
        "Badminton" -> listOf(
            MatchTypeOption("Single", "👤", 2),
            MatchTypeOption("Double", "👥", 4),
            MatchTypeOption("Mixed Double", "👫", 4)
        )
        "Tennis" -> listOf(
            MatchTypeOption("Single", "👤", 2),
            MatchTypeOption("Double", "👥", 4),
            MatchTypeOption("Mixed Double", "👫", 4)
        )
        "Futsal" -> listOf(
            MatchTypeOption("5 vs 5", "🏟️", 10),
            MatchTypeOption("3 vs 3", "👥", 6)
        )
        "Basket" -> listOf(
            MatchTypeOption("5 vs 5", "🏟️", 10),
            MatchTypeOption("3 vs 3", "👥", 6),
            MatchTypeOption("1 vs 1", "👤", 2)
        )
        "Voli" -> listOf(
            MatchTypeOption("6 vs 6", "🏟️", 12),
            MatchTypeOption("4 vs 4", "👥", 8),
            MatchTypeOption("2 vs 2", "👫", 4)
        )
        "Boxing" -> listOf(
            MatchTypeOption("1 vs 1", "🥊", 2),
            MatchTypeOption("Sparring", "🤼", 2)
        )
        "Muaythai" -> listOf(
            MatchTypeOption("1 vs 1", "🥋", 2),
            MatchTypeOption("Sparring", "🤼", 2)
        )
        "Running" -> listOf(
            MatchTypeOption("Solo Race", "🏃", 2),
            MatchTypeOption("Group Race", "🏃‍♂️", 10),
            MatchTypeOption("Relay", "🔄", 8)
        )
        "Gym" -> listOf(
            MatchTypeOption("Workout Buddy", "💪", 2),
            MatchTypeOption("Group Session", "🏋️", 6),
            MatchTypeOption("Challenge", "🎯", 2)
        )
        else -> listOf(
            MatchTypeOption("1 vs 1", "👤", 2),
            MatchTypeOption("2 vs 2", "👥", 4)
        )
    }
}

@Composable
private fun FindMatchModal(
    onDismiss: () -> Unit,
    onStartFinding: (String, String) -> Unit // (sport, matchType)
) {
    val context = LocalContext.current
    
    var visible by remember { mutableStateOf(false) }
    var selectedSport by remember { mutableStateOf(modalSportOptions[0]) }
    
    // Match types depend on selected sport
    val matchTypesForSport = remember(selectedSport) {
        getMatchTypesForSport(selectedSport.name)
    }
    var selectedMatchType by remember(matchTypesForSport) { 
        mutableStateOf(matchTypesForSport.firstOrNull() ?: MatchTypeOption("1 vs 1", "👤", 2))
    }
    
    // Date state
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var dateText by remember { mutableStateOf("") }
    
    // Time state (start time only)
    var startHour by remember { mutableStateOf(19) }
    var startMinute by remember { mutableStateOf(0) }
    
    var locationText by remember { mutableStateOf("") }
    var notesText by remember { mutableStateOf("") }
    var isStartingSearch by remember { mutableStateOf(false) }
    
    // Particle burst state
    var showParticleBurst by remember { mutableStateOf(false) }
    
    // Update match type when sport changes
    LaunchedEffect(selectedSport) {
        val newMatchTypes = getMatchTypesForSport(selectedSport.name)
        selectedMatchType = newMatchTypes.firstOrNull() ?: MatchTypeOption("1 vs 1", "👤", 2)
    }
    
    LaunchedEffect(Unit) {
        visible = true
        showParticleBurst = true
        kotlinx.coroutines.delay(800)
        showParticleBurst = false
    }
    
    // Modal animation specs
    val modalScale by animateFloatAsState(
        targetValue = if (visible && !isStartingSearch) 1f else if (isStartingSearch) 0.3f else 0.8f,
        animationSpec = spring(
            dampingRatio = if (isStartingSearch) Spring.DampingRatioMediumBouncy else Spring.DampingRatioLowBouncy,
            stiffness = if (isStartingSearch) Spring.StiffnessMedium else Spring.StiffnessLow
        ),
        label = "modal_scale"
    )
    
    val modalAlpha by animateFloatAsState(
        targetValue = if (visible && !isStartingSearch) 1f else 0f,
        animationSpec = tween(if (isStartingSearch) 400 else 300),
        label = "modal_alpha"
    )
    
    val modalOffsetY by animateFloatAsState(
        targetValue = if (visible && !isStartingSearch) 0f else if (isStartingSearch) -200f else 100f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "modal_offset"
    )
    
    // 3D rotation effect
    val modalRotationX by animateFloatAsState(
        targetValue = if (visible && !isStartingSearch) 0f else if (isStartingSearch) -15f else 10f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "modal_rotation"
    )
    
    Dialog(
        onDismissRequest = {
            visible = false
            onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f * modalAlpha))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    visible = false
                    onDismiss()
                },
            contentAlignment = Alignment.Center
        ) {
            // Particle Burst Effect on Open
            if (showParticleBurst) {
                ParticleBurstEffect()
            }
            
            // Main Modal Content
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight(0.85f)
                    .graphicsLayer {
                        scaleX = modalScale
                        scaleY = modalScale
                        alpha = modalAlpha
                        translationY = modalOffsetY
                        rotationX = modalRotationX
                        cameraDistance = 12f * density
                    }
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { /* Prevent dismiss */ }
            ) {
                // Neon glow behind modal
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(40.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    CrimsonPrimary.copy(alpha = 0.4f),
                                    CrimsonDeep.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(32.dp)
                        )
                )
                
                // Modal Surface
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(
                            elevation = 32.dp,
                            shape = RoundedCornerShape(32.dp),
                            ambientColor = CrimsonPrimary.copy(alpha = 0.3f),
                            spotColor = CrimsonDeep.copy(alpha = 0.4f)
                        ),
                    shape = RoundedCornerShape(32.dp),
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        CompetitiveDarkCard,
                                        CompetitiveDarkBase.copy(alpha = 0.98f),
                                        CompetitiveDarkCard.copy(alpha = 0.95f)
                                    )
                                )
                            )
                            // Crimson neon rim
                            .border(
                                width = 2.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        CrimsonPrimary,
                                        CrimsonDeep,
                                        CrimsonPrimary.copy(alpha = 0.6f),
                                        CrimsonDeep,
                                        CrimsonPrimary
                                    )
                                ),
                                shape = RoundedCornerShape(32.dp)
                            )
                            // Inner glossy highlight
                            .drawWithContent {
                                drawContent()
                                drawRoundRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.08f),
                                            Color.Transparent
                                        ),
                                        startY = 0f,
                                        endY = size.height * 0.3f
                                    ),
                                    cornerRadius = CornerRadius(32.dp.toPx())
                                )
                            }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Modal Header
                            ModalHeader()
                            
                            // Scrollable Form Body
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = 24.dp)
                            ) {
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Sport Selection
                                SportSelectionSection(
                                    selectedSport = selectedSport,
                                    onSportSelected = { selectedSport = it }
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                // Match Type Selection (based on sport)
                                MatchTypeSection(
                                    matchTypes = matchTypesForSport,
                                    selectedType = selectedMatchType,
                                    onTypeSelected = { selectedMatchType = it },
                                    sportName = selectedSport.name
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                // Date Selection (Calendar Picker)
                                DatePickerSection(
                                    selectedDate = selectedDate,
                                    dateText = dateText,
                                    onDateSelected = { date, text ->
                                        selectedDate = date
                                        dateText = text
                                    }
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                // Time Selection (Start Time Only)
                                StartTimeSection(
                                    startHour = startHour,
                                    startMinute = startMinute,
                                    onStartTimeChanged = { h, m ->
                                        startHour = h
                                        startMinute = m
                                    }
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                // Location Section
                                LocationSection(
                                    locationText = locationText,
                                    onLocationChange = { locationText = it }
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                // Notes/Rules Section
                                NotesSection(
                                    notesText = notesText,
                                    onNotesChange = { notesText = it }
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                            
                            // Footer Actions
                            ModalFooter(
                                onCancel = {
                                    visible = false
                                    onDismiss()
                                },
                                onStartFinding = {
                                    isStartingSearch = true
                                    onStartFinding(selectedSport.name, selectedMatchType.label)
                                },
                                isSearching = isStartingSearch
                            )
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// PARTICLE BURST EFFECT
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun ParticleBurstEffect() {
    val infiniteTransition = rememberInfiniteTransition(label = "particle_burst")
    
    val burstProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseOut),
            repeatMode = RepeatMode.Restart
        ),
        label = "burst"
    )
    
    val emojis = listOf("⚔️", "🔥", "🏅", "💪", "⚡", "🎯")
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        emojis.forEachIndexed { index, emoji ->
            val angle = (index * 60f) + (burstProgress * 30f)
            val distance = burstProgress * 150f
            val alpha = (1f - burstProgress).coerceIn(0f, 1f)
            
            Text(
                text = emoji,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = cos(Math.toRadians(angle.toDouble())).toFloat() * distance
                        translationY = sin(Math.toRadians(angle.toDouble())).toFloat() * distance
                        this.alpha = alpha
                        scaleX = 1f + burstProgress * 0.5f
                        scaleY = 1f + burstProgress * 0.5f
                    },
                fontSize = 28.sp
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// MODAL HEADER
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun ModalHeader() {
    val infiniteTransition = rememberInfiniteTransition(label = "header_pulse")
    
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_pulse"
    )
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CrimsonDeep.copy(alpha = 0.15f),
                        Color.Transparent
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Floating emoji badges
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left badge - Fire
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    CompetitiveOrange.copy(alpha = glowAlpha),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔥", fontSize = 18.sp)
                }
                
                // Title with sword emoji
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Set Match Details",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        color = TextPrimary
                    )
                    
                    // Animated sword icon
                    Text(
                        text = "⚔️",
                        modifier = Modifier.graphicsLayer {
                            scaleX = iconScale
                            scaleY = iconScale
                        },
                        fontSize = 22.sp
                    )
                }
                
                // Right badge - Trophy
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    CompetitiveGold.copy(alpha = glowAlpha),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏆", fontSize = 18.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Subtitle
            Text(
                text = "Configure your competitive match",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Decorative line
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(2.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                CrimsonPrimary.copy(alpha = glowAlpha),
                                CrimsonBright.copy(alpha = glowAlpha),
                                CrimsonPrimary.copy(alpha = glowAlpha),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(1.dp)
                    )
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// SPORT SELECTION SECTION
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun SportSelectionSection(
    selectedSport: DiscoverSportCategory,
    onSportSelected: (DiscoverSportCategory) -> Unit
) {
    Column {
        SectionLabel(icon = "🎯", title = "SELECT SPORT")
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            modalSportOptions.forEach { sport ->
                SportChip(
                    sport = sport,
                    isSelected = selectedSport == sport,
                    onClick = { onSportSelected(sport) }
                )
            }
        }
    }
}

@Composable
private fun SportChip(
    sport: DiscoverSportCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "chip_scale"
    )
    
    val glowAlpha by animateFloatAsState(
        targetValue = if (isSelected) 0.6f else 0f,
        animationSpec = tween(200),
        label = "chip_glow"
    )
    
    Box {
        // Glow behind selected chip
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .blur(12.dp)
                    .background(
                        CrimsonPrimary.copy(alpha = glowAlpha),
                        RoundedCornerShape(16.dp)
                    )
            )
        }
        
        Surface(
            onClick = onClick,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            shape = RoundedCornerShape(16.dp),
            color = if (isSelected) CrimsonPrimary.copy(alpha = 0.2f) else CompetitiveDarkElevated
        ) {
            Box(
                modifier = Modifier
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        brush = if (isSelected) {
                            Brush.linearGradient(listOf(CrimsonPrimary, CrimsonBright))
                        } else {
                            Brush.linearGradient(listOf(GlassBorderLight, GlassBorderLight))
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(sport.emoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = sport.name,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        ),
                        color = if (isSelected) CrimsonBright else TextSecondary
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// MATCH TYPE SECTION (Dynamic based on Sport)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun MatchTypeSection(
    matchTypes: List<MatchTypeOption>,
    selectedType: MatchTypeOption,
    onTypeSelected: (MatchTypeOption) -> Unit,
    sportName: String
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SectionLabel(icon = "👥", title = "MATCH TYPE")
            
            // Sport badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CrimsonPrimary.copy(alpha = 0.15f)
            ) {
                Text(
                    text = sportName,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp
                    ),
                    color = CrimsonBright
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Match type chips (horizontal scroll for many options)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            matchTypes.forEach { type ->
                MatchTypeChip(
                    type = type,
                    isSelected = selectedType == type,
                    onClick = { onTypeSelected(type) }
                )
            }
        }
    }
}

@Composable
private fun MatchTypeChip(
    type: MatchTypeOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "chip_scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) CrimsonPrimary.copy(alpha = 0.2f) else CompetitiveDarkElevated,
        animationSpec = tween(200),
        label = "chip_bg"
    )
    
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) CrimsonPrimary else GlassBorderLight,
        animationSpec = tween(200),
        label = "chip_border"
    )
    
    Surface(
        onClick = onClick,
        modifier = Modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(type.icon, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = type.label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    ),
                    color = if (isSelected) CrimsonBright else TextSecondary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${type.players} players",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp
                    ),
                    color = TextMuted
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// DATE PICKER SECTION (Calendar)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun DatePickerSection(
    selectedDate: Calendar,
    dateText: String,
    onDateSelected: (Calendar, String) -> Unit
) {
    val context = LocalContext.current
    val dateFormat = remember { SimpleDateFormat("EEEE, dd MMMM yyyy", java.util.Locale.getDefault()) }
    
    Column {
        SectionLabel(icon = "📅", title = "SELECT DATE")
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Date picker button with calendar icon
        Surface(
            onClick = {
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val newCalendar = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }
                        val formattedDate = dateFormat.format(newCalendar.time)
                        onDateSelected(newCalendar, formattedDate)
                    },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH)
                )
                // Set minimum date to today
                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = CompetitiveDarkElevated
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = CrimsonPrimary.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Calendar icon
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        CrimsonPrimary.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📆", fontSize = 24.sp)
                    }
                    
                    Column {
                        Text(
                            text = "Tanggal Pertandingan",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp
                            ),
                            color = TextMuted
                        )
                        Text(
                            text = dateText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            ),
                            color = Color.White
                        )
                    }
                }
                
                // Chevron icon
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Select date",
                    tint = CrimsonBright,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// START TIME SECTION (Single Time Picker)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun StartTimeSection(
    startHour: Int,
    startMinute: Int,
    onStartTimeChanged: (Int, Int) -> Unit
) {
    Column {
        SectionLabel(icon = "⏰", title = "WAKTU MULAI")
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Single time picker card
        TimePickerCard(
            title = "Jam Mulai Sparing",
            hour = startHour,
            minute = startMinute,
            onTimeChanged = onStartTimeChanged,
            modifier = Modifier.fillMaxWidth(),
            icon = "🏁"
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Helpful tip
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = CrimsonPrimary.copy(alpha = 0.1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("💡", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pilih waktu yang kamu available untuk sparring",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = CrimsonBright
                )
            }
        }
    }
}

@Composable
private fun TimePickerCard(
    title: String,
    hour: Int,
    minute: Int,
    onTimeChanged: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    icon: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = CompetitiveDarkElevated
    ) {
        Column(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = GlassBorderLight,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(icon, fontSize = 14.sp)
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    ),
                    color = TextMuted
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Hour and minute spinners
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Hour column
                TimeSpinner(
                    value = hour,
                    range = 0..23,
                    onValueChange = { onTimeChanged(it, minute) }
                )
                
                // Separator
                Text(
                    text = ":",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = CrimsonBright,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                
                // Minute column
                TimeSpinner(
                    value = minute,
                    range = 0..59,
                    step = 5,
                    onValueChange = { onTimeChanged(hour, it) }
                )
            }
        }
    }
}

@Composable
private fun TimeSpinner(
    value: Int,
    range: IntRange,
    step: Int = 1,
    onValueChange: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Up button
        IconButton(
            onClick = {
                var newValue = value + step
                if (newValue > range.last) newValue = range.first
                onValueChange(newValue)
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Increase",
                tint = CrimsonBright,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Value display
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = CrimsonPrimary.copy(alpha = 0.15f)
        ) {
            Text(
                text = String.format("%02d", value),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                ),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
        
        // Down button
        IconButton(
            onClick = {
                var newValue = value - step
                if (newValue < range.first) newValue = range.last
                onValueChange(newValue)
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Decrease",
                tint = CrimsonBright,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// LOCATION SECTION
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun LocationSection(
    locationText: String,
    onLocationChange: (String) -> Unit
) {
    Column {
        SectionLabel(icon = "📍", title = "LOCATION")
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Mini map preview placeholder
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(16.dp),
            color = CompetitiveDarkElevated
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = GlassBorderLight,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                CrimsonPrimary.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📍", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap to select location",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Search input
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = CompetitiveDarkElevated
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = GlassBorderLight,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = TextMuted
                )
                
                TextField(
                    value = locationText,
                    onValueChange = onLocationChange,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = "Search venue or address...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = CrimsonPrimary
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                )
                
                if (locationText.isNotEmpty()) {
                    IconButton(
                        onClick = { onLocationChange("") },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Clear",
                            modifier = Modifier.size(18.dp),
                            tint = TextMuted
                        )
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// NOTES SECTION
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun NotesSection(
    notesText: String,
    onNotesChange: (String) -> Unit
) {
    Column {
        SectionLabel(icon = "📝", title = "NOTES / RULES (Optional)")
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = CompetitiveDarkElevated
        ) {
            TextField(
                value = notesText,
                onValueChange = onNotesChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .border(
                        width = 1.dp,
                        color = GlassBorderLight,
                        shape = RoundedCornerShape(14.dp)
                    ),
                placeholder = {
                    Text(
                        text = "e.g., \"Best of 3\", \"No timeouts\", \"Standard rules\"...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = CrimsonPrimary
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// MODAL FOOTER
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun ModalFooter(
    onCancel: () -> Unit,
    onStartFinding: () -> Unit,
    isSearching: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "footer_anim")
    
    val buttonGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_glow"
    )
    
    val buttonScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_scale"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        CompetitiveDarkBase.copy(alpha = 0.9f),
                        CompetitiveDarkBase
                    )
                )
            )
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cancel button (ghost)
            Surface(
                onClick = onCancel,
                modifier = Modifier.weight(0.35f),
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.5.dp,
                            color = TextMuted,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = TextSecondary
                    )
                }
            }
            
            // Start Finding button (main CTA)
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .graphicsLayer {
                        scaleX = if (!isSearching) buttonScale else 0.95f
                        scaleY = if (!isSearching) buttonScale else 0.95f
                    }
            ) {
                // Glow behind button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .blur(16.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    CrimsonDeep.copy(alpha = buttonGlow * 0.5f),
                                    CrimsonPrimary.copy(alpha = buttonGlow * 0.6f),
                                    CrimsonDeep.copy(alpha = buttonGlow * 0.5f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
                
                Surface(
                    onClick = onStartFinding,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = CrimsonPrimary.copy(alpha = 0.4f),
                            spotColor = CrimsonDeep.copy(alpha = 0.4f)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Transparent,
                    enabled = !isSearching
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        CrimsonDeep,
                                        CrimsonPrimary,
                                        CrimsonDeep
                                    )
                                )
                            )
                            .drawWithContent {
                                drawContent()
                                // Glossy highlight
                                drawRoundRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.2f),
                                            Color.Transparent
                                        ),
                                        startY = 0f,
                                        endY = size.height * 0.5f
                                    ),
                                    cornerRadius = CornerRadius(16.dp.toPx())
                                )
                            }
                            .border(
                                width = 1.5.dp,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        CrimsonGlow.copy(alpha = 0.6f),
                                        CrimsonPrimary.copy(alpha = 0.3f)
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSearching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Radar,
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Start Finding",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    ),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("🔥", fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// HELPER COMPONENTS
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun SectionLabel(icon: String, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(icon, fontSize = 16.sp)
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 1.5.sp
            ),
            color = TextSecondary
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Legacy color aliases for backward compatibility with Phase 2
// ═══════════════════════════════════════════════════════════════════════════

val DeepRed = CrimsonPrimary
val Crimson = CrimsonDeep
val CharcoalBlack = CompetitiveDarkBase
val DarkGrey = CompetitiveDarkSurface
val NeonRed = CrimsonNeon
val GlowRed = CrimsonGlow
val MetallicDark = CompetitiveDarkElevated
val SteelGrey = Color(0xFF3A3A3A)
