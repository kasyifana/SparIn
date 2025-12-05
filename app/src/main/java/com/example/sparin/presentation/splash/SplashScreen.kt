package com.example.sparin.presentation.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * SparIN Premium Splash Screen
 * With Tennis Player Animation
 * 
 * Animation Flow:
 * 1. Logo appears with zoom
 * 2. Tennis player slides in from right with dynamic effect
 * 3. Ball animation flies across
 * 4. Transition to next screen
 */

// SparIN Brand Colors
object SparINColors {
    val SportYellow = Color(0xFFFFC928)
    val DeepBlue = Color(0xFF003C8F)
    val White = Color(0xFFFFFFFF)
    val LightYellow = Color(0xFFFFE082)
    val OrangeAccent = Color(0xFFFF6B35)
    val DarkText = Color(0xFF1A1A1A)
}

@Composable
fun SprintinSplashScreen(
    onSplashComplete: () -> Unit = {}
) {
    // Animation states
    var currentPhase by remember { mutableIntStateOf(0) }
    
    // Logo animations
    val logoScale = remember { Animatable(0.5f) }
    val logoAlpha = remember { Animatable(0f) }
    
    // Player animations
    val playerSlideX = remember { Animatable(300f) }
    val playerAlpha = remember { Animatable(0f) }
    val playerScale = remember { Animatable(0.8f) }
    
    // Ball animation
    val ballProgress = remember { Animatable(0f) }
    val ballScale = remember { Animatable(0f) }
    
    // Ball zoom to fullscreen (after bounce)
    val ballZoomScale = remember { Animatable(1f) }
    val ballZoomAlpha = remember { Animatable(0f) }
    
    // Background circle expansion
    val circleScale = remember { Animatable(0f) }
    
    // Exit animation
    val exitAlpha = remember { Animatable(1f) }
    
    // Floating effect for player
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatY"
    )
    
    // Glow pulse
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    // Animation sequence
    LaunchedEffect(Unit) {
        // Phase 1: Logo appears
        launch {
            logoAlpha.animateTo(1f, tween(400))
        }
        logoScale.animateTo(
            1f,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = 400f)
        )
        
        delay(200)
        
        // Phase 2: Background circle expands
        currentPhase = 1
        circleScale.animateTo(
            1f,
            tween(600, easing = FastOutSlowInEasing)
        )
        
        // Phase 3: Player slides in
        currentPhase = 2
        launch {
            playerAlpha.animateTo(1f, tween(300))
        }
        launch {
            playerScale.animateTo(1f, spring(dampingRatio = 0.6f, stiffness = 300f))
        }
        playerSlideX.animateTo(
            0f,
            spring(dampingRatio = 0.7f, stiffness = 200f)
        )
        
        delay(200)
        
        // Phase 4: Ball animation (approach + hit + small bounce)
        currentPhase = 3
        launch {
            ballScale.animateTo(1f, spring(dampingRatio = 0.5f, stiffness = 500f))
        }
        ballProgress.animateTo(
            1f,
            tween(1000, easing = FastOutSlowInEasing)
        )
        
        delay(100)
        
        // Phase 5: Ball zooms toward screen (fullscreen transition)
        currentPhase = 4
        launch {
            // Fade out other elements
            logoAlpha.animateTo(0f, tween(400))
            playerAlpha.animateTo(0f, tween(400))
        }
        launch {
            ballZoomAlpha.animateTo(1f, tween(200))
        }
        ballZoomScale.animateTo(
            25f,  // Zoom to cover entire screen
            tween(600, easing = FastOutSlowInEasing)
        )
        
        delay(100)
        
        // Complete - transition to onboarding
        currentPhase = 5
        onSplashComplete()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SparINColors.White)
            .alpha(exitAlpha.value)
    ) {
        // Background decorative elements
        BackgroundElements(
            circleScale = circleScale.value,
            glowPulse = glowPulse
        )
        
        // Main content
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
            // Logo Section
            LogoSection(
                logoScale = logoScale.value,
                logoAlpha = logoAlpha.value
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Player + Ball Section (overlapping)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                // Player Section
                if (currentPhase >= 2) {
                    PlayerSection(
                        slideX = playerSlideX.value,
                        alpha = playerAlpha.value,
                        scale = playerScale.value,
                        floatY = floatY
                    )
                }
                
                // Ball Animation - overlaps with player
                if (currentPhase >= 3) {
                    BallAnimation(
                        progress = ballProgress.value,
                        scale = ballScale.value
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
        
        // Bottom tagline
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "FIND YOUR SPORT PARTNER",
                modifier = Modifier.alpha(logoAlpha.value),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SparINColors.DarkText.copy(alpha = 0.5f),
                    letterSpacing = 3.sp
                )
            )
        }
        
        // Ball Zoom Fullscreen Overlay (Phase 4+)
        if (currentPhase >= 4) {
            BallZoomTransition(
                zoomScale = ballZoomScale.value,
                alpha = ballZoomAlpha.value
            )
        }
    }
}

/**
 * Ball Zoom Transition - Ball flies toward screen and fills it
 * Ball keeps spinning and moving, not stuck
 */
@Composable
private fun BallZoomTransition(
    zoomScale: Float,
    alpha: Float
) {
    // Continuous rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "ballZoom")
    
    // Ball keeps spinning
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    // Subtle pulse effect
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(150, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    // Slight wobble/shake for energy
    val wobbleX by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wobbleX"
    )
    
    val wobbleY by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(80, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wobbleY"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        // The zooming ball - keeps moving! Using realistic image
        Box(
            modifier = Modifier
                .size(80.dp)
                .offset(x = wobbleX.dp, y = wobbleY.dp)
                .scale(zoomScale * pulse)
                .rotate(rotation)
        ) {
            // Realistic tennis ball image
            Image(
                painter = painterResource(id = R.drawable.bolatenis),
                contentDescription = "Tennis Ball",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        
        // Speed/energy lines radiating outward as ball zooms
        if (zoomScale > 3f && zoomScale < 20f) {
            val lineAlpha = ((zoomScale - 3f) / 17f).coerceIn(0f, 0.5f)
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(lineAlpha)
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                
                // Radial speed lines
                for (i in 0..11) {
                    val angle = (i * 30f) + rotation
                    val startRadius = size.minDimension * 0.1f
                    val endRadius = size.minDimension * 0.5f
                    
                    val startX = centerX + kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat() * startRadius
                    val startY = centerY + kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat() * startRadius
                    val endX = centerX + kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat() * endRadius
                    val endY = centerY + kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat() * endRadius
                    
                    drawLine(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                SparINColors.SportYellow.copy(alpha = 0.8f),
                                Color.Transparent
                            ),
                            start = Offset(startX, startY),
                            end = Offset(endX, endY)
                        ),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 4f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Composable
private fun BackgroundElements(
    circleScale: Float,
    glowPulse: Float
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        // Large yellow circle - top right
        drawCircle(
            color = SparINColors.SportYellow.copy(alpha = 0.15f),
            radius = width * 0.6f * circleScale,
            center = Offset(width * 0.85f, height * 0.15f)
        )
        
        // Blue accent circle - bottom left
        drawCircle(
            color = SparINColors.DeepBlue.copy(alpha = 0.08f),
            radius = width * 0.4f * circleScale,
            center = Offset(width * 0.1f, height * 0.85f)
        )
        
        // Glowing yellow orb
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    SparINColors.SportYellow.copy(alpha = glowPulse),
                    SparINColors.LightYellow.copy(alpha = glowPulse * 0.5f),
                    Color.Transparent
                ),
                center = Offset(width * 0.7f, height * 0.3f)
            ),
            radius = 150f * circleScale
        )
        
        // Small decorative dots
        drawCircle(
            color = SparINColors.OrangeAccent.copy(alpha = 0.6f),
            radius = 8f,
            center = Offset(width * 0.15f, height * 0.25f)
        )
        drawCircle(
            color = SparINColors.DeepBlue.copy(alpha = 0.4f),
            radius = 6f,
            center = Offset(width * 0.9f, height * 0.6f)
        )
        drawCircle(
            color = SparINColors.SportYellow,
            radius = 10f,
            center = Offset(width * 0.25f, height * 0.7f)
        )
    }
}

@Composable
private fun LogoSection(
    logoScale: Float,
    logoAlpha: Float
) {
    Column(
        modifier = Modifier
            .scale(logoScale)
            .alpha(logoAlpha),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo text with gradient
        Text(
            text = "SparIN",
            style = TextStyle(
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = SparINColors.SportYellow,
                letterSpacing = (-2).sp
            )
        )
        
        // Underline
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            SparINColors.SportYellow,
                            SparINColors.OrangeAccent
                        )
                    )
                )
        )
        
        // Tagline
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Sport Partner Finder",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = SparINColors.DarkText.copy(alpha = 0.6f),
                letterSpacing = 1.sp
            )
        )
    }
}

@Composable
private fun PlayerSection(
    slideX: Float,
    alpha: Float,
    scale: Float,
    floatY: Float
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(550.dp)  // Lebih besar lagi
            .offset(x = slideX.dp, y = floatY.dp)
            .alpha(alpha)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        // Glow behind player
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        SparINColors.SportYellow.copy(alpha = 0.3f),
                        SparINColors.LightYellow.copy(alpha = 0.1f),
                        Color.Transparent
                    )
                ),
                radius = 400f,
                center = Offset(size.width / 2, size.height / 2)
            )
        }
        
        // Tennis Player Image - EVEN LARGER
        Image(
            painter = painterResource(id = R.drawable.tennis_player),
            contentDescription = "Tennis Player",
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(0.55f),  // Lebih lebar lagi
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun BallAnimation(
    progress: Float,
    scale: Float
) {
    // Ball animation: comes from left side, hits racket, then bounces TOWARD screen (center)
    // This creates the effect of ball coming at the viewer
    
    // Key positions - ADJUSTED to hit racket more precisely
    val racketX = 55f      // Raket position X (lebih ke kiri, sesuai posisi raket di gambar)
    val racketY = -140f    // Raket height (lebih ke atas, di area raket)
    
    // Ball comes from LEFT side of screen, at racket height
    val startX = -180f     // Start from left outside screen
    val startY = racketY + 10f  // At racket level
    
    // After hitting racket, ball bounces TOWARD CENTER (toward viewer)
    val bounceX = 0f       // Center X
    val bounceY = 80f      // Center-ish (coming toward viewer)
    
    // Split animation: approach (0-0.35), hit (0.35), bounce toward center (0.35-1)
    val hitPoint = 0.35f
    
    val ballX: Float
    val ballY: Float
    val ballRotation: Float
    val currentScale: Float
    
    if (progress <= hitPoint) {
        // Phase 1: Ball approaching racket from left
        val approachProgress = progress / hitPoint
        
        // Curved approach toward racket
        ballX = startX + (approachProgress * (racketX - startX))
        // Slight upward curve to meet racket
        val approachCurve = -30f * kotlin.math.sin(approachProgress * Math.PI).toFloat()
        ballY = startY + approachCurve
        
        // Ball spinning as it approaches
        ballRotation = approachProgress * 360f * 3
        currentScale = scale * (0.6f + (approachProgress * 0.4f))
    } else {
        // Phase 2: Ball bouncing TOWARD viewer (center of screen)
        val bounceProgress = (progress - hitPoint) / (1f - hitPoint)
        
        // Move toward center
        ballX = racketX + (bounceProgress * (bounceX - racketX))
        ballY = racketY + (bounceProgress * (bounceY - racketY))
        
        // Ball gets BIGGER as it comes toward viewer (3D effect)
        val scaleBoost = 1f + (bounceProgress * 2f)  // Gets 3x bigger
        currentScale = scale * scaleBoost
        
        // Slower spin after hit
        ballRotation = 720f + (bounceProgress * 180f)
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Motion trail before hit
        if (progress > 0.05f && progress < hitPoint) {
            val trailCount = 6
            for (i in 1..trailCount) {
                val trailProgress = ((progress - (i * 0.025f)) / hitPoint).coerceAtLeast(0f)
                val trailX = startX + (trailProgress * (racketX - startX))
                val trailY = startY + (kotlin.math.sin(trailProgress * Math.PI * 2).toFloat() * 8f)
                val trailAlpha = (0.4f - (i * 0.06f)).coerceAtLeast(0f)
                val trailSize = (24 - (i * 3)).coerceAtLeast(6)
                
                Canvas(
                    modifier = Modifier
                        .offset(x = trailX.dp, y = trailY.dp)
                        .size(trailSize.dp)
                        .alpha(trailAlpha)
                ) {
                    drawCircle(
                        color = SparINColors.OrangeAccent,
                        radius = size.minDimension / 2
                    )
                }
            }
        }
        
        // Radial speed lines after hit (showing ball coming toward viewer)
        if (progress > hitPoint && progress < 0.7f) {
            val lineAlpha = (1f - ((progress - hitPoint) / 0.3f)).coerceIn(0f, 0.7f)
            Canvas(
                modifier = Modifier
                    .offset(x = ballX.dp, y = ballY.dp)
                    .size(100.dp)
                    .alpha(lineAlpha)
            ) {
                // Radial lines showing ball coming at viewer
                for (i in 0..7) {
                    val angle = i * 45f
                    val lineLength = size.width * 0.4f
                    val startRadius = size.width * 0.25f
                    val startOffset = Offset(
                        size.width / 2 + kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat() * startRadius,
                        size.height / 2 + kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat() * startRadius
                    )
                    val endOffset = Offset(
                        size.width / 2 + kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat() * lineLength,
                        size.height / 2 + kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat() * lineLength
                    )
                    drawLine(
                        color = SparINColors.SportYellow.copy(alpha = 0.6f),
                        start = startOffset,
                        end = endOffset,
                        strokeWidth = 2f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
        
        // Impact flash when ball hits racket
        if (progress > (hitPoint - 0.05f) && progress < (hitPoint + 0.15f)) {
            val impactProgress = ((progress - (hitPoint - 0.05f)) / 0.2f).coerceIn(0f, 1f)
            val impactAlpha = kotlin.math.sin(impactProgress * Math.PI).toFloat() * 0.9f
            val impactSize = 50f + (impactProgress * 60f)
            
            Canvas(
                modifier = Modifier
                    .offset(x = racketX.dp, y = racketY.dp)
                    .size(impactSize.dp)
                    .alpha(impactAlpha)
            ) {
                // Bright impact burst
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White,
                            SparINColors.SportYellow,
                            SparINColors.OrangeAccent.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    radius = size.minDimension / 2
                )
            }
            
            // Spark particles
            if (impactProgress > 0.2f && impactProgress < 0.8f) {
                for (i in 0..5) {
                    val angle = (i * 60f) + (impactProgress * 30f)
                    val sparkDist = 30f + (impactProgress * 40f)
                    val sparkX = racketX + (kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat() * sparkDist)
                    val sparkY = racketY + (kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat() * sparkDist)
                    
                    Canvas(
                        modifier = Modifier
                            .offset(x = sparkX.dp, y = sparkY.dp)
                            .size(8.dp)
                            .alpha(impactAlpha * 0.8f)
                    ) {
                        drawCircle(
                            color = SparINColors.SportYellow,
                            radius = size.minDimension / 2
                        )
                    }
                }
            }
        }
        
        // The Ball - Using realistic tennis ball image (clean, no shadow)
        Image(
            painter = painterResource(id = R.drawable.bolatenis),
            contentDescription = "Tennis Ball",
            modifier = Modifier
                .offset(x = ballX.dp, y = ballY.dp)
                .size(50.dp)
                .scale(currentScale)
                .rotate(ballRotation),
            contentScale = ContentScale.Fit
        )
        
        // Shrinking trail when bouncing toward viewer (creates depth illusion)
        if (progress > hitPoint + 0.05f && progress < 0.9f) {
            val bounceProgress = (progress - hitPoint) / (1f - hitPoint)
            val trailCount = 5
            for (i in 1..trailCount) {
                val trailBounceProgress = (bounceProgress - (i * 0.06f)).coerceAtLeast(0f)
                val trailX = racketX + (trailBounceProgress * (bounceX - racketX))
                val trailY = racketY + (trailBounceProgress * (bounceY - racketY))
                val trailAlpha = (0.4f - (i * 0.08f)).coerceAtLeast(0f)
                // Trail gets smaller (further away = smaller, creating depth)
                val trailScale = (1f + (trailBounceProgress * 1.5f)) * (1f - (i * 0.15f))
                val trailSize = (25 * trailScale).coerceAtLeast(5f)
                
                Canvas(
                    modifier = Modifier
                        .offset(x = trailX.dp, y = trailY.dp)
                        .size(trailSize.dp)
                        .alpha(trailAlpha)
                ) {
                    drawCircle(
                        color = SparINColors.OrangeAccent,
                        radius = size.minDimension / 2
                    )
                }
            }
        }
    }
}

/**
 * Minimal fallback version
 */

@Composable
fun SprintinSplashMinimal(
    onSplashComplete: () -> Unit = {}
) {
    val scale = remember { Animatable(0.8f) }
    
    LaunchedEffect(Unit) {
        scale.animateTo(
            1f,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
        )
        delay(800)
        onSplashComplete()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SparINColors.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "SparIN",
            modifier = Modifier.scale(scale.value),
            style = TextStyle(
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = SparINColors.SportYellow,
                letterSpacing = (-1.5).sp
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SprintinSplashScreenPreview() {
    SprintinSplashScreen()
}

@Preview(showBackground = true)
@Composable
private fun SprintinSplashMinimalPreview() {
    SprintinSplashMinimal()
}