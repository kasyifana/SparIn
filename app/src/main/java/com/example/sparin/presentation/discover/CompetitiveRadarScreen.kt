package com.example.sparin.presentation.discover

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * PHASE 3 — COMPETITIVE RADAR SCANNING PAGE
 * ═══════════════════════════════════════════════════════════════════════════
 * 
 * Ultra-futuristic radar scanning screen with:
 * - Animated radar sweep with comet tail
 * - Glowing competitor pins with skill tiers
 * - Floating ember particles
 * - iOS dark glass bottom sheet
 * - Premium neon red aesthetic
 */

// ═══════════════════════════════════════════════════════════════════════════
// RADAR COLOR PALETTE
// ═══════════════════════════════════════════════════════════════════════════

private val RadarDarkBase = Color(0xFF0C0C10)
private val RadarDarkSurface = Color(0xFF121214)
private val RadarDarkCard = Color(0xFF0E0E12)
private val RadarDarkElevated = Color(0xFF1A1A1E)

private val RadarCrimsonPrimary = Color(0xFFFF3344)
private val RadarCrimsonBright = Color(0xFFFF5560)
private val RadarCrimsonDeep = Color(0xFFD60026)
private val RadarCrimsonGlow = Color(0xFFFF6B7A)
private val RadarNeonRed = Color(0xFFFF1A1A)

private val RadarGold = Color(0xFFFFD700)
private val RadarSilver = Color(0xFFC0C0C0)
private val RadarBronze = Color(0xFFCD7F32)
private val RadarPlatinum = Color(0xFFE5E4E2)
private val RadarElite = Color(0xFFFF4500)

private val RadarTextPrimary = Color(0xFFFAFAFA)
private val RadarTextSecondary = Color(0xFFB0B0B5)
private val RadarTextMuted = Color(0xFF6A6A70)

private val RadarGlassBg = Color(0xFF161618)
private val RadarGlassBorder = Color(0xFF2A2A2E)

// ═══════════════════════════════════════════════════════════════════════════
// DATA CLASSES
// ═══════════════════════════════════════════════════════════════════════════

private data class CompetitorPin(
    val id: String,
    val name: String,
    val sport: String,
    val sportEmoji: String,
    val tier: SkillTier,
    val distance: Float, // in km
    val angle: Float, // position on radar in degrees
    val radiusPercent: Float, // 0-1 from center
    val status: CompetitorStatus
)

private enum class SkillTier(val label: String, val color: Color, val icon: String) {
    BRONZE("Bronze", RadarBronze, "🥉"),
    SILVER("Silver", RadarSilver, "🥈"),
    GOLD("Gold", RadarGold, "🥇"),
    PLATINUM("Platinum", RadarPlatinum, "💎"),
    ELITE("Elite", RadarElite, "👑")
}

private enum class CompetitorStatus(val label: String, val color: Color) {
    ACTIVE("Active", RadarCrimsonBright),
    SEARCHING("Searching", Color(0xFFFFAA33)),
    RANKED("Ranked Mode", RadarCrimsonPrimary)
}

// ═══════════════════════════════════════════════════════════════════════════
// MATCH DETECTION STATE & PHRASES
// ═══════════════════════════════════════════════════════════════════════════

private enum class RadarPhase {
    SCANNING,           // Initial scanning animation
    DETECTING,          // Found potential matches
    MATCH_FOUND,        // Perfect match popup!
    NO_SPORT_MATCH,     // No match for selected sport, show nearby
    SHOWING_RESULTS     // Bottom sheet with results
}

// Gen-Z Hype Phrases for different scenarios
private object GenZPhrases {
    val scanningPhrases = listOf(
        "Hunting for your next rival... 🎯",
        "Scanning the arena... 👀",
        "Looking for worthy opponents... ⚔️",
        "Radar going brr... 📡",
        "Finding your match energy... ✨"
    )
    
    val matchFoundPhrases = listOf(
        "YO WE GOT ONE! 🔥",
        "MATCH DETECTED! 🎯",
        "FOUND YOUR RIVAL! ⚔️",
        "IT'S GIVING... COMPETITION! 💀",
        "OPPONENT LOCKED IN! 🔒",
        "SLAY INCOMING! ✨",
        "NO CAP, PERFECT MATCH! 🎯"
    )
    
    val noMatchPhrases = listOf(
        "No exact vibes rn... 😅",
        "Your sport's lowkey quiet... 🤫", 
        "No matches? That's rough buddy 💀",
        "Crickets for now... 🦗"
    )
    
    val nearbyDetectedPhrases = listOf(
        "BUT WAIT— 👀",
        "PLOT TWIST! 🔄",
        "HOLD UP THO... ✋",
        "NOT ALL HOPE IS LOST! 💫"
    )
    
    val nearbyAvailablePhrases = listOf(
        "Nearby Competitors Detected! 🎯",
        "Other Warriors Around You! ⚔️",
        "Check These Challengers! 👊",
        "More Opponents Unlocked! 🔓"
    )
    
    val challengePhrases = listOf(
        "Think you can handle it? 💪",
        "Ready to throw down? 🥊",
        "Show them what you got! 🔥",
        "Time to prove yourself! ⭐",
        "Let's get this W! 🏆"
    )
    
    fun getRandomPhrase(list: List<String>): String = list.random()
}

// Match Result Data
private data class MatchResult(
    val hasExactMatch: Boolean,
    val exactMatches: List<CompetitorPin>,
    val nearbyCompetitors: List<CompetitorPin>,
    val selectedSport: String
)

// Mock detected competitors
private val mockCompetitors = listOf(
    CompetitorPin("1", "BadmintonPro11", "Badminton", "🏸", SkillTier.GOLD, 0.4f, 45f, 0.35f, CompetitorStatus.ACTIVE),
    CompetitorPin("2", "FutsalAce", "Futsal", "⚽", SkillTier.PLATINUM, 0.7f, 120f, 0.55f, CompetitorStatus.SEARCHING),
    CompetitorPin("3", "BoxerKing99", "Boxing", "🥊", SkillTier.ELITE, 1.2f, 200f, 0.75f, CompetitorStatus.RANKED),
    CompetitorPin("4", "HoopMaster", "Basketball", "🏀", SkillTier.SILVER, 0.6f, 280f, 0.45f, CompetitorStatus.ACTIVE),
    CompetitorPin("5", "RunnerX", "Running", "🏃", SkillTier.BRONZE, 1.5f, 340f, 0.85f, CompetitorStatus.SEARCHING),
    CompetitorPin("6", "TennisAce", "Tennis", "🎾", SkillTier.GOLD, 0.9f, 160f, 0.6f, CompetitorStatus.ACTIVE),
    CompetitorPin("7", "MuayFighter", "Muaythai", "🥋", SkillTier.PLATINUM, 0.5f, 80f, 0.4f, CompetitorStatus.RANKED)
)

// ═══════════════════════════════════════════════════════════════════════════
// MAIN SCREEN
// ═══════════════════════════════════════════════════════════════════════════

@Composable
fun CompetitiveRadarScreen(
    navController: NavHostController,
    selectedSport: String = "All",
    matchType: String = "1v1"
) {
    // Core states
    var radarPhase by remember { mutableStateOf(RadarPhase.SCANNING) }
    var detectedCompetitors by remember { mutableStateOf<List<CompetitorPin>>(emptyList()) }
    var showBottomSheet by remember { mutableStateOf(false) }
    
    // Match detection states
    var showMatchPopup by remember { mutableStateOf(false) }
    var matchResult by remember { mutableStateOf<MatchResult?>(null) }
    var currentScanPhrase by remember { mutableStateOf(GenZPhrases.getRandomPhrase(GenZPhrases.scanningPhrases)) }
    var scanProgress by remember { mutableStateOf(0f) }
    
    // View mode for bottom sheet
    var viewMode by remember { mutableStateOf("category") } // "category" or "all"
    
    // Phrase rotation during scanning
    LaunchedEffect(radarPhase) { 
        if (radarPhase == RadarPhase.SCANNING) {
            while (radarPhase == RadarPhase.SCANNING) {
                delay(2500)
                currentScanPhrase = GenZPhrases.getRandomPhrase(GenZPhrases.scanningPhrases)
            }
        }
    }
    
    // Main detection flow - SIMPLIFIED, NO MULTIPLE POPUPS
    LaunchedEffect(Unit) {
        // Phase 1: Scanning animation (smooth, no interruptions)
        radarPhase = RadarPhase.SCANNING
        
        // Smooth scanning with progress
        val scanDuration = 2500L
        val steps = 25
        for (i in 1..steps) {
            delay(scanDuration / steps)
            scanProgress = i.toFloat() / steps
        }
        
        // Phase 2: Silently detect competitors (no popup spam)
        radarPhase = RadarPhase.DETECTING
        
        // Add all competitors quietly
        mockCompetitors.forEach { competitor ->
            delay(100)
            detectedCompetitors = detectedCompetitors + competitor
        }
        
        delay(500)
        
        // Phase 3: Analyze matches
        val exactMatches = if (selectedSport == "All") {
            detectedCompetitors
        } else {
            detectedCompetitors.filter { it.sport.equals(selectedSport, ignoreCase = true) }
        }
        
        val nearbyCompetitors = if (selectedSport != "All") {
            detectedCompetitors.filter { !it.sport.equals(selectedSport, ignoreCase = true) }
        } else {
            emptyList()
        }
        
        matchResult = MatchResult(
            hasExactMatch = exactMatches.isNotEmpty(),
            exactMatches = exactMatches,
            nearbyCompetitors = nearbyCompetitors,
            selectedSport = selectedSport
        )
        
        // Phase 4: Show SINGLE popup with result summary
        radarPhase = if (exactMatches.isNotEmpty()) RadarPhase.MATCH_FOUND else RadarPhase.NO_SPORT_MATCH
        delay(300)
        showMatchPopup = true
        
        // Popup will be dismissed by user interaction, no auto-dismiss
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        RadarDarkBase,
                        RadarDarkSurface,
                        RadarDarkBase
                    )
                )
            )
    ) {
        // Background ember particles
        EmberParticlesBackground()
        
        // Main content
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Floating Header with dynamic text
            RadarHeader(
                onBackClick = { navController.navigateUp() },
                radarPhase = radarPhase,
                scanPhrase = currentScanPhrase,
                scanProgress = scanProgress,
                selectedSport = selectedSport
            )
            
            // Radar Visual (Hero)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                RadarVisual(
                    competitors = detectedCompetitors,
                    isScanning = radarPhase == RadarPhase.SCANNING || radarPhase == RadarPhase.DETECTING,
                    radarPhase = radarPhase,
                    selectedSport = selectedSport
                )
            }
        }
        
        // MATCH FOUND POPUP! 🔥 - Single popup after scan complete
        AnimatedVisibility(
            visible = showMatchPopup && matchResult != null,
            enter = scaleIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            matchResult?.let { result ->
                MatchResultPopup(
                    matchResult = result,
                    selectedSport = selectedSport,
                    onViewCategory = {
                        // View matches sesuai kategori yang dipilih
                        viewMode = "category"
                        showMatchPopup = false
                        radarPhase = RadarPhase.SHOWING_RESULTS
                        showBottomSheet = true
                    },
                    onViewAllSports = {
                        // View semua olahraga yang tersedia
                        viewMode = "all"
                        showMatchPopup = false
                        radarPhase = RadarPhase.SHOWING_RESULTS
                        showBottomSheet = true
                    }
                )
            }
        }
        
        // Bottom Sheet with categorized results
        AnimatedVisibility(
            visible = showBottomSheet,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            CompetitorsBottomSheet(
                competitors = detectedCompetitors,
                matchResult = matchResult,
                selectedSport = selectedSport,
                viewMode = viewMode,
                onViewModeChange = { viewMode = it },
                onCompetitorClick = { /* Handle selection */ }
            )
        }
        
        // Auto-matching footer indicator (only during scanning)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (showBottomSheet) 340.dp else 32.dp)
        ) {
            if (!showBottomSheet && !showMatchPopup && (radarPhase == RadarPhase.SCANNING || radarPhase == RadarPhase.DETECTING)) {
                AutoMatchingIndicator(radarPhase = radarPhase)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EMBER PARTICLES BACKGROUND
// ═══════════════════════════════════════════════════════════════════════════

private data class EmberParticle(
    var x: Float,
    var y: Float,
    var size: Float,
    var alpha: Float,
    var speed: Float,
    var drift: Float
)

@Composable
private fun EmberParticlesBackground() {
    val particles = remember {
        List(40) {
            EmberParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 4f + 2f,
                alpha = Random.nextFloat() * 0.6f + 0.2f,
                speed = Random.nextFloat() * 0.0015f + 0.0005f,
                drift = Random.nextFloat() * 0.001f - 0.0005f
            )
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "embers")
    
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )
    
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        particles.forEach { particle ->
            // Update position (moving upward)
            val newY = (particle.y - time * particle.speed * 100) % 1f
            val adjustedY = if (newY < 0) newY + 1f else newY
            
            val newX = particle.x + sin(time * PI * 2 + particle.drift * 1000).toFloat() * 0.02f
            
            // Flicker effect
            val flickerAlpha = particle.alpha * (0.5f + 0.5f * sin(time * PI * 8 + particle.x * 10).toFloat())
            
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        RadarCrimsonBright.copy(alpha = flickerAlpha),
                        RadarCrimsonPrimary.copy(alpha = flickerAlpha * 0.5f),
                        Color.Transparent
                    ),
                    center = Offset(newX * size.width, adjustedY * size.height),
                    radius = particle.size * 3
                ),
                center = Offset(newX * size.width, adjustedY * size.height),
                radius = particle.size * 3
            )
            
            // Core ember
            drawCircle(
                color = RadarCrimsonGlow.copy(alpha = flickerAlpha),
                center = Offset(newX * size.width, adjustedY * size.height),
                radius = particle.size
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// HEADER
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun RadarHeader(
    onBackClick: () -> Unit,
    radarPhase: RadarPhase,
    scanPhrase: String,
    scanProgress: Float,
    selectedSport: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "header")
    
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_pulse"
    )
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    val dotOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dots"
    )
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        color = RadarGlassBg.copy(alpha = 0.85f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            RadarCrimsonPrimary.copy(alpha = 0.3f),
                            RadarGlassBorder,
                            RadarCrimsonPrimary.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            RadarDarkElevated,
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = RadarTextPrimary
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    // Dynamic title based on phase
                    val titleText = when (radarPhase) {
                        RadarPhase.SCANNING -> "Scanning Area"
                        RadarPhase.DETECTING -> "Detecting Players"
                        RadarPhase.MATCH_FOUND -> "MATCH FOUND!"
                        RadarPhase.NO_SPORT_MATCH -> "Searching..."
                        RadarPhase.SHOWING_RESULTS -> "Results Ready"
                    }
                    
                    val titleEmoji = when (radarPhase) {
                        RadarPhase.SCANNING -> "📡"
                        RadarPhase.DETECTING -> "👀"
                        RadarPhase.MATCH_FOUND -> "🔥"
                        RadarPhase.NO_SPORT_MATCH -> "🔍"
                        RadarPhase.SHOWING_RESULTS -> "✅"
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Pulsing icon
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .graphicsLayer {
                                    scaleX = iconScale
                                    scaleY = iconScale
                                }
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            if (radarPhase == RadarPhase.MATCH_FOUND) 
                                                RadarGold.copy(alpha = glowAlpha)
                                            else RadarCrimsonPrimary.copy(alpha = glowAlpha),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(titleEmoji, fontSize = 16.sp)
                        }
                        
                        Text(
                            text = titleText,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = if (radarPhase == RadarPhase.MATCH_FOUND) 
                                RadarGold else RadarTextPrimary
                        )
                        
                        // Animated dots (only during scanning/detecting)
                        if (radarPhase == RadarPhase.SCANNING || radarPhase == RadarPhase.DETECTING) {
                            Row {
                                repeat(3) { index ->
                                    val alpha = if ((dotOffset * 100).toInt() % 3 == index) 1f else 0.3f
                                    Text(
                                        text = ".",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = RadarCrimsonBright.copy(alpha = alpha)
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Dynamic subtitle with scan phrase
                    Text(
                        text = if (radarPhase == RadarPhase.SCANNING || radarPhase == RadarPhase.DETECTING) 
                            scanPhrase 
                        else if (selectedSport != "All") 
                            "Looking for $selectedSport players nearby"
                        else 
                            "All sports • Competitive Mode",
                        style = MaterialTheme.typography.bodySmall,
                        color = RadarTextMuted
                    )
                    
                    // Progress bar during scanning
                    if (radarPhase == RadarPhase.SCANNING) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(RadarDarkElevated)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(scanProgress)
                                    .fillMaxHeight()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                RadarCrimsonPrimary,
                                                RadarCrimsonBright
                                            )
                                        )
                                    )
                            )
                        }
                    }
                }
                
                // Live indicator
                Box(
                    modifier = Modifier
                        .background(
                            RadarCrimsonPrimary.copy(alpha = 0.15f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .graphicsLayer { alpha = glowAlpha }
                                .background(RadarCrimsonBright, CircleShape)
                        )
                        Text(
                            text = "LIVE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = RadarCrimsonBright
                        )
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// RADAR VISUAL (HERO ELEMENT)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun RadarVisual(
    competitors: List<CompetitorPin>,
    isScanning: Boolean,
    radarPhase: RadarPhase,
    selectedSport: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    
    // Radar sweep rotation
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )
    
    // Pulse animation for rings
    val ringPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    // Center glow pulse
    val centerGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "center_glow"
    )
    
    // Energy wave
    val waveProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseOut),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow
        Box(
            modifier = Modifier
                .fillMaxSize(0.95f)
                .blur(60.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            RadarCrimsonDeep.copy(alpha = 0.3f),
                            RadarCrimsonPrimary.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        
        // Main radar canvas
        Canvas(
            modifier = Modifier.fillMaxSize(0.9f)
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val maxRadius = size.minDimension / 2
            
            // Frosted glass background
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        RadarDarkCard.copy(alpha = 0.9f),
                        RadarDarkBase.copy(alpha = 0.95f),
                        RadarDarkCard.copy(alpha = 0.85f)
                    ),
                    center = center,
                    radius = maxRadius
                ),
                center = center,
                radius = maxRadius
            )
            
            // Outer ring border
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        RadarCrimsonPrimary.copy(alpha = 0.6f),
                        RadarCrimsonDeep.copy(alpha = 0.3f),
                        RadarCrimsonPrimary.copy(alpha = 0.6f),
                        RadarCrimsonDeep.copy(alpha = 0.3f),
                        RadarCrimsonPrimary.copy(alpha = 0.6f)
                    ),
                    center = center
                ),
                center = center,
                radius = maxRadius,
                style = Stroke(width = 3.dp.toPx())
            )
            
            // Concentric rings
            val ringCount = 4
            for (i in 1..ringCount) {
                val ringRadius = maxRadius * (i.toFloat() / ringCount)
                val ringAlpha = 0.15f + ringPulse * 0.1f * (ringCount - i) / ringCount
                
                drawCircle(
                    color = RadarCrimsonPrimary.copy(alpha = ringAlpha),
                    center = center,
                    radius = ringRadius,
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = if (i == ringCount) null else PathEffect.dashPathEffect(
                            floatArrayOf(10f, 10f),
                            phase = sweepAngle
                        )
                    )
                )
            }
            
            // Cross lines
            val crossAlpha = 0.1f
            drawLine(
                color = RadarCrimsonPrimary.copy(alpha = crossAlpha),
                start = Offset(center.x, center.y - maxRadius),
                end = Offset(center.x, center.y + maxRadius),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = RadarCrimsonPrimary.copy(alpha = crossAlpha),
                start = Offset(center.x - maxRadius, center.y),
                end = Offset(center.x + maxRadius, center.y),
                strokeWidth = 1.dp.toPx()
            )
            
            // Energy wave effect
            val waveRadius = maxRadius * waveProgress
            val waveAlpha = (1f - waveProgress) * 0.4f
            drawCircle(
                color = RadarCrimsonBright.copy(alpha = waveAlpha),
                center = center,
                radius = waveRadius,
                style = Stroke(width = 2.dp.toPx())
            )
            
            // Radar sweep line with comet tail
            drawRadarSweep(center, maxRadius, sweepAngle)
            
            // Distance labels
            drawDistanceLabels(center, maxRadius)
        }
        
        // Center "You" marker
        CenterUserMarker(glowAlpha = centerGlow)
        
        // Competitor pins with staggered premium appearance
        competitors.forEachIndexed { index, competitor ->
            CompetitorPinMarker(
                competitor = competitor,
                radarSize = 0.9f,
                sweepAngle = sweepAngle,
                appearDelay = 200 + (index * 150) // Staggered iOS-like appearance
            )
        }
        
        // Spark particles around radar
        RadarSparkParticles()
    }
}

private fun DrawScope.drawRadarSweep(
    center: Offset,
    maxRadius: Float,
    angle: Float
) {
    // Comet tail gradient (sweep arc)
    val tailLength = 60f // degrees
    
    for (i in 0..30) {
        val tailAngle = angle - (i * tailLength / 30)
        val tailAlpha = (1f - i / 30f) * 0.5f
        val radians = Math.toRadians(tailAngle.toDouble())
        
        val endX = center.x + cos(radians).toFloat() * maxRadius
        val endY = center.y + sin(radians).toFloat() * maxRadius
        
        drawLine(
            brush = Brush.linearGradient(
                colors = listOf(
                    RadarCrimsonBright.copy(alpha = tailAlpha),
                    RadarCrimsonPrimary.copy(alpha = tailAlpha * 0.5f),
                    Color.Transparent
                ),
                start = center,
                end = Offset(endX, endY)
            ),
            start = center,
            end = Offset(endX, endY),
            strokeWidth = (3 - i * 0.08f).dp.toPx(),
            cap = StrokeCap.Round
        )
    }
    
    // Main sweep line (bright)
    val mainRadians = Math.toRadians(angle.toDouble())
    val mainEndX = center.x + cos(mainRadians).toFloat() * maxRadius
    val mainEndY = center.y + sin(mainRadians).toFloat() * maxRadius
    
    drawLine(
        brush = Brush.linearGradient(
            colors = listOf(
                RadarCrimsonGlow,
                RadarCrimsonBright,
                RadarCrimsonPrimary.copy(alpha = 0.8f)
            ),
            start = center,
            end = Offset(mainEndX, mainEndY)
        ),
        start = center,
        end = Offset(mainEndX, mainEndY),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )
    
    // Bright tip
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.White,
                RadarCrimsonGlow,
                Color.Transparent
            ),
            center = Offset(mainEndX, mainEndY),
            radius = 12.dp.toPx()
        ),
        center = Offset(mainEndX, mainEndY),
        radius = 12.dp.toPx()
    )
}

private fun DrawScope.drawDistanceLabels(
    center: Offset,
    maxRadius: Float
) {
    val distances = listOf("0.5km", "1km", "1.5km", "2km")
    val ringCount = 4
    
    // Note: In actual implementation, you'd use drawContext.canvas.nativeCanvas
    // to draw text. For simplicity, we'll skip text drawing in Canvas
    // and overlay with Compose Text elements instead
}

@Composable
private fun CenterUserMarker(glowAlpha: Float) {
    Box(
        modifier = Modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow rings
        Box(
            modifier = Modifier
                .size(80.dp)
                .graphicsLayer { alpha = glowAlpha * 0.3f }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            RadarCrimsonPrimary.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        
        Box(
            modifier = Modifier
                .size(50.dp)
                .graphicsLayer { alpha = glowAlpha * 0.5f }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            RadarCrimsonBright.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        
        // Core dot
        Box(
            modifier = Modifier
                .size(24.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = RadarCrimsonPrimary,
                    spotColor = RadarCrimsonDeep
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            RadarCrimsonGlow,
                            RadarCrimsonBright,
                            RadarCrimsonPrimary
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.8f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "👤",
                fontSize = 10.sp
            )
        }
        
        // "You" label
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 8.dp)
                .background(
                    RadarDarkBase.copy(alpha = 0.9f),
                    RoundedCornerShape(6.dp)
                )
                .border(
                    width = 1.dp,
                    color = RadarCrimsonPrimary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = "YOU",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    letterSpacing = 1.sp
                ),
                color = RadarCrimsonBright
            )
        }
    }
}

@Composable
private fun BoxScope.CompetitorPinMarker(
    competitor: CompetitorPin,
    radarSize: Float,
    sweepAngle: Float,
    appearDelay: Int = 0
) {
    // Premium iOS-like appearance animation
    var isVisible by remember { mutableStateOf(false) }
    val appearProgress by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "appear"
    )
    
    LaunchedEffect(Unit) {
        delay(appearDelay.toLong())
        isVisible = true
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "pin_${competitor.id}")
    
    // Subtle breathing glow
    val pinGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200 + (competitor.angle.toInt() % 400), easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    // Smooth orbit rotation
    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit"
    )
    
    // Check if sweep is near this pin (for highlight effect)
    val angleDiff = kotlin.math.abs(sweepAngle - competitor.angle)
    val isHighlighted = angleDiff < 15 || angleDiff > 345
    
    val highlightScale by animateFloatAsState(
        targetValue = if (isHighlighted) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "highlight"
    )
    
    // Calculate position
    val radians = Math.toRadians(competitor.angle.toDouble())
    val offsetPercent = competitor.radiusPercent * 0.42f
    
    // Only show if visible
    if (appearProgress > 0.01f) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    translationX = (cos(radians) * offsetPercent * 800).toFloat()
                    translationY = (sin(radians) * offsetPercent * 800).toFloat()
                    scaleX = appearProgress * highlightScale
                    scaleY = appearProgress * highlightScale
                    alpha = appearProgress
                }
        ) {
            // Subtle orbiting particle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .graphicsLayer { 
                        rotationZ = orbitAngle 
                        alpha = pinGlow * 0.6f
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .offset(y = (-16).dp)
                        .background(
                            competitor.tier.color.copy(alpha = 0.8f),
                            CircleShape
                        )
                )
            }
            
            // Soft glow behind pin
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .graphicsLayer { alpha = pinGlow * 0.4f }
                    .blur(10.dp)
                    .background(
                        competitor.tier.color.copy(alpha = 0.6f),
                        CircleShape
                    )
            )
            
            // Pin background
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = CircleShape,
                        ambientColor = competitor.tier.color.copy(alpha = 0.3f),
                        spotColor = competitor.tier.color.copy(alpha = 0.2f)
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                RadarDarkElevated,
                                RadarDarkCard
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                competitor.tier.color.copy(alpha = 0.9f),
                                competitor.tier.color.copy(alpha = 0.4f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(competitor.sportEmoji, fontSize = 14.sp)
            }
            
            // Tier indicator (small dot)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp)
                    .size(12.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                competitor.tier.color,
                                competitor.tier.color.copy(alpha = 0.7f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(1.dp, RadarDarkBase, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when(competitor.tier) {
                        SkillTier.ELITE -> "👑"
                        SkillTier.PLATINUM -> "💎"
                        SkillTier.GOLD -> "🥇"
                        SkillTier.SILVER -> "🥈"
                        SkillTier.BRONZE -> "🥉"
                    },
                    fontSize = 6.sp
                )
            }
        }
    }
}

@Composable
private fun RadarSparkParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "sparks")
    
    val sparkProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spark"
    )
    
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2 * 0.9f
        
        // Draw small sparks around the radar edge
        for (i in 0..7) {
            val sparkAngle = (i * 45f + sparkProgress * 360f) % 360f
            val sparkRadians = Math.toRadians(sparkAngle.toDouble())
            val sparkX = center.x + cos(sparkRadians).toFloat() * (radius + 5)
            val sparkY = center.y + sin(sparkRadians).toFloat() * (radius + 5)
            
            val sparkAlpha = ((sin(sparkProgress * PI * 2 + i).toFloat() + 1f) / 2f) * 0.8f
            
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        RadarCrimsonGlow.copy(alpha = sparkAlpha),
                        RadarCrimsonPrimary.copy(alpha = sparkAlpha * 0.5f),
                        Color.Transparent
                    ),
                    center = Offset(sparkX, sparkY),
                    radius = 6.dp.toPx()
                ),
                center = Offset(sparkX, sparkY),
                radius = 6.dp.toPx()
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// MATCH RESULT POPUP — CLEAN, COMPACT & PREMIUM! 🎯
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun MatchResultPopup(
    matchResult: MatchResult,
    selectedSport: String,
    onViewCategory: () -> Unit,
    onViewAllSports: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "result_popup")
    
    // Subtle glow animation
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    val hasMatch = matchResult.hasExactMatch
    val matchCount = matchResult.exactMatches.size
    val nearbyCount = matchResult.nearbyCompetitors.size
    val totalCount = matchCount + nearbyCount
    
    // Group by sport - max 5 to keep compact
    val sportGroups = (matchResult.exactMatches + matchResult.nearbyCompetitors)
        .groupBy { it.sport }
        .mapValues { it.value.size }
        .entries
        .take(5)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        // Soft glow behind popup
        Box(
            modifier = Modifier
                .size(280.dp)
                .blur(60.dp)
                .graphicsLayer { alpha = glowPulse * 0.4f }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (hasMatch) RadarGold.copy(alpha = 0.5f) else RadarCrimsonPrimary.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        
        // Main popup card - compact design
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = RadarDarkCard.copy(alpha = 0.97f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = if (hasMatch) listOf(
                                RadarGold.copy(alpha = 0.6f),
                                RadarGold.copy(alpha = 0.2f)
                            ) else listOf(
                                RadarCrimsonBright.copy(alpha = 0.5f),
                                RadarCrimsonPrimary.copy(alpha = 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Compact Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (hasMatch) "🎯" else "👀",
                            fontSize = 36.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (hasMatch) "WE GOT A HIT!" else "No $selectedSport Yet",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black
                                ),
                                color = if (hasMatch) RadarGold else RadarTextSecondary
                            )
                            Text(
                                text = if (hasMatch) 
                                    "$matchCount players match your vibe" 
                                else 
                                    "$nearbyCount other athletes nearby",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (hasMatch) RadarTextSecondary else RadarCrimsonBright
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Compact sport chips - horizontal scroll if needed
                    if (sportGroups.isNotEmpty()) {
                        Text(
                            text = "Available nearby:",
                            style = MaterialTheme.typography.labelSmall,
                            color = RadarTextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Compact chips row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            sportGroups.forEach { (sport, count) ->
                                val isSelected = sport.equals(selectedSport, ignoreCase = true)
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) 
                                        RadarGold.copy(alpha = 0.2f) 
                                    else 
                                        RadarDarkElevated.copy(alpha = 0.8f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(getSportEmoji(sport), fontSize = 12.sp)
                                        Text(
                                            text = "$count",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = if (isSelected) RadarGold else RadarTextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Compact Action Buttons - side by side
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // All Sports Button
                        Surface(
                            onClick = onViewAllSports,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = RadarDarkElevated
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        width = 1.dp,
                                        color = RadarGlassBorder.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("🏅", fontSize = 14.sp)
                                    Text(
                                        text = "All Sports",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = RadarTextSecondary
                                    )
                                }
                            }
                        }
                        
                        // Primary Action Button
                        Surface(
                            onClick = onViewCategory,
                            modifier = Modifier
                                .weight(1.2f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Transparent
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = if (hasMatch) listOf(
                                                RadarGold,
                                                RadarCrimsonBright
                                            ) else listOf(
                                                RadarCrimsonPrimary,
                                                RadarCrimsonBright
                                            )
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = if (hasMatch) "View Match" else "Explore",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.White
                                    )
                                    Text("→", color = Color.White, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    // Challenge text
                    Text(
                        text = GenZPhrases.getRandomPhrase(GenZPhrases.challengePhrases),
                        style = MaterialTheme.typography.bodySmall,
                        color = RadarTextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// Helper function to get sport emoji
private fun getSportEmoji(sport: String): String {
    return when (sport.lowercase()) {
        "badminton" -> "🏸"
        "futsal" -> "⚽"
        "boxing" -> "🥊"
        "basketball" -> "🏀"
        "running" -> "🏃"
        "tennis" -> "🎾"
        "muaythai" -> "🥋"
        "swimming" -> "🏊"
        "cycling" -> "🚴"
        "volleyball" -> "🏐"
        else -> "🏅"
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// MATCH FOUND POPUP — THE EPIC GEN-Z MOMENT! 🔥 (Legacy - kept for reference)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun MatchFoundPopup(
    matchResult: MatchResult,
    radarPhase: RadarPhase,
    onDismiss: () -> Unit,
    onAcceptMatch: (CompetitorPin) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "popup_anim")
    
    // Epic pulsing glow
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    // Rotation for decorative elements
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    // Scale bounce
    val scaleBounce by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    // Shake animation for hype
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shake"
    )
    
    val hasMatch = matchResult.hasExactMatch
    val displayCompetitors = if (hasMatch) matchResult.exactMatches else matchResult.nearbyCompetitors
    val firstMatch = displayCompetitors.firstOrNull()
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow effect
        Box(
            modifier = Modifier
                .size(320.dp)
                .graphicsLayer { 
                    alpha = glowPulse * 0.4f 
                    rotationZ = rotation
                }
                .blur(40.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (hasMatch) RadarGold.copy(alpha = 0.6f) else RadarCrimsonPrimary.copy(alpha = 0.5f),
                            if (hasMatch) RadarCrimsonBright.copy(alpha = 0.3f) else Color(0xFFFFAA33).copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        
        // Main popup card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = scaleBounce
                    scaleY = scaleBounce
                    translationX = if (hasMatch) shakeOffset else 0f
                },
            shape = RoundedCornerShape(32.dp),
            color = RadarDarkCard.copy(alpha = 0.95f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = if (hasMatch) listOf(
                                RadarGold,
                                RadarCrimsonBright,
                                RadarGold
                            ) else listOf(
                                Color(0xFFFFAA33),
                                RadarCrimsonPrimary,
                                Color(0xFFFFAA33)
                            )
                        ),
                        shape = RoundedCornerShape(32.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Epic header with animated emojis
                    if (hasMatch) {
                        MatchFoundHeader(glowPulse = glowPulse)
                    } else {
                        NoMatchHeader(glowPulse = glowPulse, matchResult = matchResult)
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Matched competitor card (if exists)
                    if (firstMatch != null) {
                        MatchedCompetitorCard(
                            competitor = firstMatch,
                            isExactMatch = hasMatch,
                            glowPulse = glowPulse
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // More matches indicator
                        if (displayCompetitors.size > 1) {
                            Text(
                                text = "+${displayCompetitors.size - 1} more ${if (hasMatch) "matches" else "nearby players"} found!",
                                style = MaterialTheme.typography.bodySmall,
                                color = RadarTextSecondary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    
                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Skip button
                        Surface(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = RadarDarkElevated
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        width = 1.dp,
                                        color = RadarGlassBorder,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "View All",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = RadarTextSecondary
                                )
                            }
                        }
                        
                        // Accept/Challenge button
                        Surface(
                            onClick = { firstMatch?.let { onAcceptMatch(it) } },
                            modifier = Modifier
                                .weight(1.5f)
                                .height(52.dp)
                                .graphicsLayer {
                                    scaleX = 1f + (glowPulse - 0.6f) * 0.1f
                                    scaleY = 1f + (glowPulse - 0.6f) * 0.1f
                                },
                            shape = RoundedCornerShape(16.dp),
                            color = Color.Transparent
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = if (hasMatch) listOf(
                                                RadarCrimsonPrimary,
                                                RadarCrimsonBright
                                            ) else listOf(
                                                Color(0xFFFFAA33),
                                                RadarCrimsonBright
                                            )
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
                                        text = if (hasMatch) "LET'S GO!" else "EXPLORE",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.White
                                    )
                                    Text("🔥", fontSize = 16.sp)
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Challenge phrase
                    Text(
                        text = GenZPhrases.getRandomPhrase(GenZPhrases.challengePhrases),
                        style = MaterialTheme.typography.bodySmall,
                        color = RadarTextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchFoundHeader(glowPulse: Float) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated emoji row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚔️",
                fontSize = 28.sp,
                modifier = Modifier.graphicsLayer {
                    scaleX = glowPulse
                    scaleY = glowPulse
                }
            )
            Text(
                text = "🔥",
                fontSize = 36.sp,
                modifier = Modifier.graphicsLayer {
                    scaleX = 1f + (glowPulse - 0.6f) * 0.5f
                    scaleY = 1f + (glowPulse - 0.6f) * 0.5f
                }
            )
            Text(
                text = "⚔️",
                fontSize = 28.sp,
                modifier = Modifier.graphicsLayer {
                    scaleX = glowPulse
                    scaleY = glowPulse
                }
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Main text with glow
        Text(
            text = GenZPhrases.getRandomPhrase(GenZPhrases.matchFoundPhrases),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Black,
                fontSize = 24.sp
            ),
            color = RadarGold,
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer { alpha = glowPulse }
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "Perfect match for your vibe ✨",
            style = MaterialTheme.typography.bodyMedium,
            color = RadarTextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NoMatchHeader(glowPulse: Float, matchResult: MatchResult) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Thinking emoji
        Text(
            text = "🤔",
            fontSize = 40.sp,
            modifier = Modifier.graphicsLayer {
                scaleX = glowPulse
                scaleY = glowPulse
            }
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // No match message
        Text(
            text = GenZPhrases.getRandomPhrase(GenZPhrases.noMatchPhrases),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = RadarTextSecondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "No ${matchResult.selectedSport} players nearby rn",
            style = MaterialTheme.typography.bodySmall,
            color = RadarTextMuted,
            textAlign = TextAlign.Center
        )
        
        if (matchResult.nearbyCompetitors.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Plot twist animation
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(1.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color(0xFFFFAA33))
                            )
                        )
                )
                
                Text(
                    text = GenZPhrases.getRandomPhrase(GenZPhrases.nearbyDetectedPhrases),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFFFFAA33),
                    modifier = Modifier.graphicsLayer { alpha = glowPulse }
                )
                
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(1.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFFAA33), Color.Transparent)
                            )
                        )
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = GenZPhrases.getRandomPhrase(GenZPhrases.nearbyAvailablePhrases),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = RadarTextPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MatchedCompetitorCard(
    competitor: CompetitorPin,
    isExactMatch: Boolean,
    glowPulse: Float
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = RadarDarkElevated.copy(alpha = 0.8f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            competitor.tier.color.copy(alpha = glowPulse),
                            competitor.tier.color.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sport emoji with glow
                Box(
                    modifier = Modifier.size(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Glow
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .blur(12.dp)
                            .graphicsLayer { alpha = glowPulse * 0.6f }
                            .background(
                                competitor.tier.color.copy(alpha = 0.4f),
                                CircleShape
                            )
                    )
                    
                    // Icon container
                    Surface(
                        shape = CircleShape,
                        color = RadarDarkCard
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .border(
                                    width = 2.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            competitor.tier.color,
                                            competitor.tier.color.copy(alpha = 0.5f)
                                        )
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(competitor.sportEmoji, fontSize = 26.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = competitor.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = RadarTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        if (isExactMatch) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = RadarGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "MATCH",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    ),
                                    color = RadarGold
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sport badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = RadarCrimsonPrimary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = competitor.sport,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = RadarCrimsonBright
                            )
                        }
                        
                        // Tier badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = competitor.tier.color.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(competitor.tier.icon, fontSize = 10.sp)
                                Text(
                                    text = competitor.tier.label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = competitor.tier.color
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    // Distance and status
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("📍", fontSize = 12.sp)
                            Text(
                                text = "${String.format("%.1f", competitor.distance)} km away",
                                style = MaterialTheme.typography.bodySmall,
                                color = RadarTextMuted
                            )
                        }
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .graphicsLayer { alpha = glowPulse }
                                    .background(competitor.status.color, CircleShape)
                            )
                            Text(
                                text = competitor.status.label,
                                style = MaterialTheme.typography.bodySmall,
                                color = competitor.status.color
                            )
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// BOTTOM SHEET
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun CompetitorsBottomSheet(
    competitors: List<CompetitorPin>,
    matchResult: MatchResult?,
    selectedSport: String,
    viewMode: String, // "category" or "all"
    onViewModeChange: (String) -> Unit,
    onCompetitorClick: (CompetitorPin) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sheet")
    
    val borderGlow by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border"
    )
    
    // Group competitors by sport
    val sportGroups = competitors.groupBy { it.sport }
    val displayCompetitors = if (viewMode == "category" && selectedSport != "All") {
        competitors.filter { it.sport.equals(selectedSport, ignoreCase = true) }
    } else {
        competitors
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            RadarGlassBg.copy(alpha = 0.95f),
                            RadarDarkBase.copy(alpha = 0.98f)
                        )
                    ),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            RadarCrimsonPrimary.copy(alpha = borderGlow),
                            RadarGlassBorder.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Handle bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(
                                RadarTextMuted.copy(alpha = 0.5f),
                                RoundedCornerShape(2.dp)
                            )
                    )
                }
                
                // Header with view mode toggle
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = if (viewMode == "category" && selectedSport != "All") "🎯" else "🏅",
                                fontSize = 20.sp
                            )
                            Column {
                                Text(
                                    text = if (viewMode == "category" && selectedSport != "All") 
                                        "$selectedSport Players" 
                                    else 
                                        "All Sports",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = RadarTextPrimary
                                )
                                Text(
                                    text = "${displayCompetitors.size} players found",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = RadarTextMuted
                                )
                            }
                        }
                        
                        // View mode toggle chips
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ViewModeChip(
                                text = selectedSport,
                                emoji = getSportEmoji(selectedSport),
                                isSelected = viewMode == "category",
                                onClick = { onViewModeChange("category") }
                            )
                            ViewModeChip(
                                text = "All",
                                emoji = "🏅",
                                isSelected = viewMode == "all",
                                onClick = { onViewModeChange("all") }
                            )
                        }
                    }
                }
                
                // Sport filter chips (when viewing all)
                if (viewMode == "all") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sportGroups.forEach { (sport, list) ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = RadarDarkElevated
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(getSportEmoji(sport), fontSize = 12.sp)
                                    Text(
                                        text = "${list.size}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = RadarTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(horizontal = 24.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    RadarGlassBorder,
                                    Color.Transparent
                                )
                            )
                        )
                )
                
                // Competitor list (grouped by sport when viewing all)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (viewMode == "all") {
                        // Group by sport with headers
                        sportGroups.forEach { (sport, sportCompetitors) ->
                            item {
                                SportSectionHeader(sport = sport, count = sportCompetitors.size)
                            }
                            itemsIndexed(sportCompetitors) { index, competitor ->
                                CompetitorListItem(
                                    competitor = competitor,
                                    onClick = { onCompetitorClick(competitor) },
                                    animationDelay = index * 30,
                                    isMatch = competitor.sport.equals(selectedSport, ignoreCase = true)
                                )
                            }
                        }
                    } else {
                        // Just show filtered list
                        itemsIndexed(displayCompetitors) { index, competitor ->
                            CompetitorListItem(
                                competitor = competitor,
                                onClick = { onCompetitorClick(competitor) },
                                animationDelay = index * 50,
                                isMatch = true
                            )
                        }
                        
                        // Empty state
                        if (displayCompetitors.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("😢", fontSize = 40.sp)
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = "No $selectedSport players nearby rn",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = RadarTextSecondary,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Surface(
                                            onClick = { onViewModeChange("all") },
                                            shape = RoundedCornerShape(12.dp),
                                            color = RadarCrimsonPrimary.copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = "View All Sports →",
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                                color = RadarCrimsonBright
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

@Composable
private fun ViewModeChip(
    text: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) RadarCrimsonPrimary.copy(alpha = 0.2f) else RadarDarkElevated
    ) {
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (isSelected) RadarCrimsonPrimary.copy(alpha = 0.5f) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(emoji, fontSize = 12.sp)
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isSelected) RadarCrimsonBright else RadarTextSecondary
            )
        }
    }
}

@Composable
private fun SportSectionHeader(sport: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(getSportEmoji(sport), fontSize = 18.sp)
        Text(
            text = sport,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = RadarTextPrimary
        )
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = RadarCrimsonPrimary.copy(alpha = 0.15f)
        ) {
            Text(
                text = "$count",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = RadarCrimsonBright
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(RadarGlassBorder.copy(alpha = 0.5f))
        )
    }
}

@Composable
private fun CompetitorListItem(
    competitor: CompetitorPin,
    onClick: () -> Unit,
    animationDelay: Int,
    isMatch: Boolean = false
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(animationDelay.toLong())
        isVisible = true
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "item_${competitor.id}")
    
    val statusGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "status"
    )
    
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(300)) + slideInHorizontally(
            initialOffsetX = { 100 },
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = RadarDarkCard.copy(alpha = 0.8f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                RadarCrimsonPrimary.copy(alpha = 0.3f),
                                RadarGlassBorder,
                                RadarCrimsonPrimary.copy(alpha = 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sport icon with glow
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .blur(8.dp)
                                .background(
                                    RadarCrimsonPrimary.copy(alpha = 0.3f),
                                    CircleShape
                                )
                        )
                        Surface(
                            shape = CircleShape,
                            color = RadarDarkElevated
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .border(
                                        width = 1.5.dp,
                                        color = RadarCrimsonPrimary.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(competitor.sportEmoji, fontSize = 22.sp)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(14.dp))
                    
                    // Info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = competitor.name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = RadarTextPrimary,
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
                                color = competitor.tier.color.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(competitor.tier.icon, fontSize = 10.sp)
                                    Text(
                                        text = competitor.tier.label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 10.sp
                                        ),
                                        color = competitor.tier.color
                                    )
                                }
                            }
                            
                            // Distance
                            Text(
                                text = "${String.format("%.1f", competitor.distance)} km away",
                                style = MaterialTheme.typography.labelSmall,
                                color = RadarTextMuted
                            )
                        }
                    }
                    
                    // Status badge
                    Box(
                        modifier = Modifier
                            .background(
                                competitor.status.color.copy(alpha = 0.15f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .graphicsLayer { alpha = statusGlow }
                                    .background(competitor.status.color, CircleShape)
                            )
                            Text(
                                text = competitor.status.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 10.sp
                                ),
                                color = competitor.status.color
                            )
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// AUTO-MATCHING INDICATOR
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun AutoMatchingIndicator(radarPhase: RadarPhase) {
    val infiniteTransition = rememberInfiniteTransition(label = "auto_match")
    
    val dotScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot"
    )
    
    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring"
    )
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    // Dynamic text based on phase
    val indicatorText = when (radarPhase) {
        RadarPhase.SCANNING -> "Scanning..."
        RadarPhase.DETECTING -> "Detecting players..."
        RadarPhase.MATCH_FOUND -> "Match found!"
        RadarPhase.NO_SPORT_MATCH -> "Searching nearby..."
        RadarPhase.SHOWING_RESULTS -> "Ready"
    }
    
    val indicatorEmoji = when (radarPhase) {
        RadarPhase.SCANNING -> "📡"
        RadarPhase.DETECTING -> "👀"
        RadarPhase.MATCH_FOUND -> "🔥"
        RadarPhase.NO_SPORT_MATCH -> "🔍"
        RadarPhase.SHOWING_RESULTS -> "✅"
    }
    
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = RadarDarkCard.copy(alpha = 0.9f)
    ) {
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            RadarCrimsonPrimary.copy(alpha = 0.3f),
                            RadarGlassBorder,
                            RadarCrimsonPrimary.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Rotating ring with dot
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                // Ring
                Canvas(
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer { rotationZ = ringRotation }
                ) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                RadarCrimsonBright,
                                RadarCrimsonPrimary.copy(alpha = 0.5f),
                                Color.Transparent,
                                Color.Transparent,
                                Color.Transparent
                            )
                        ),
                        startAngle = 0f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                
                // Center dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .graphicsLayer {
                            scaleX = dotScale
                            scaleY = dotScale
                        }
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    RadarCrimsonGlow,
                                    RadarCrimsonBright
                                )
                            ),
                            shape = CircleShape
                        )
                )
            }
            
            Text(indicatorEmoji, fontSize = 14.sp)
            
            Text(
                text = indicatorText,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = RadarCrimsonBright.copy(alpha = glowAlpha)
            )
            
            // Three animated dots
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                repeat(3) { index ->
                    val dotAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, delayMillis = index * 200, easing = EaseInOutSine),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "dot_$index"
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .graphicsLayer { alpha = dotAlpha }
                            .background(RadarCrimsonBright, CircleShape)
                    )
                }
            }
        }
    }
}
