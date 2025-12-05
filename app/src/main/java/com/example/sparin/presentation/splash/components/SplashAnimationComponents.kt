package com.example.sparin.presentation.splash.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.dp
import com.example.sparin.ui.theme.SprintinColors
import kotlin.math.*
import kotlin.random.Random

/**
 * Premium iOS-style particle system for sport droplets
 * Creates a rain-like effect with bouncy kinetic motion
 */
@Composable
fun SportDropletParticles(
    modifier: Modifier = Modifier,
    particleCount: Int = 30,
    animationProgress: Float,
    isConverging: Boolean
) {
    val particles = remember {
        List(particleCount) { index ->
            SportParticle(
                id = index,
                initialX = Random.nextFloat(),
                initialY = -Random.nextFloat() * 0.4f - 0.1f,
                size = 6f + Random.nextFloat() * 14f,
                velocity = 0.7f + Random.nextFloat() * 0.5f,
                bounciness = 0.3f + Random.nextFloat() * 0.4f,
                rotationSpeed = Random.nextFloat() * 2f - 1f,
                colorVariant = index % 4
            )
        }
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        
        particles.forEach { particle ->
            drawSportParticle(
                particle = particle,
                canvasSize = size,
                centerX = centerX,
                centerY = centerY,
                animationProgress = animationProgress,
                isConverging = isConverging
            )
        }
    }
}

/**
 * Draw individual sport particle with physics
 */
private fun DrawScope.drawSportParticle(
    particle: SportParticle,
    canvasSize: androidx.compose.ui.geometry.Size,
    centerX: Float,
    centerY: Float,
    animationProgress: Float,
    isConverging: Boolean
) {
    val fallProgress = (animationProgress / 0.3f).coerceIn(0f, 1f)
    val convergeProgress = if (isConverging) {
        ((animationProgress - 0.3f) / 0.2f).coerceIn(0f, 1f)
    } else 0f
    
    // Physics-based position calculation
    val baseX = particle.initialX * canvasSize.width
    val gravity = 1.5f
    val fallDistance = fallProgress * fallProgress * gravity * canvasSize.height * particle.velocity
    val baseY = particle.initialY * canvasSize.height + fallDistance
    
    // Ground collision and bounce
    val groundLevel = canvasSize.height * 0.65f
    val bounceY = if (baseY > groundLevel && !isConverging) {
        val impactVelocity = fallProgress * particle.velocity
        val timeSinceImpact = (baseY - groundLevel) / (canvasSize.height * 0.35f)
        val dampening = particle.bounciness * (1f - timeSinceImpact).coerceAtLeast(0f)
        val bounceHeight = sin(timeSinceImpact * PI.toFloat() * 4) * 30f * dampening * impactVelocity
        -bounceHeight.coerceAtLeast(0f)
    } else 0f
    
    val effectiveY = (baseY + bounceY).coerceAtMost(groundLevel + 20f)
    
    // Converge to center with elastic easing
    val elasticEase = if (convergeProgress > 0f) {
        val c4 = (2 * PI) / 3
        if (convergeProgress == 1f) 1f
        else (2f.pow(-10f * convergeProgress) * sin((convergeProgress * 10f - 0.75f) * c4.toFloat()) + 1f)
            .coerceIn(0f, 1.2f)
    } else 0f
    
    val currentX = baseX + (centerX - baseX) * elasticEase
    val currentY = effectiveY + (centerY - effectiveY) * elasticEase
    
    // Dynamic size during convergence
    val dynamicSize = particle.size * (1f - convergeProgress * 0.85f)
    
    if (dynamicSize > 0.5f && currentY > -50f) {
        // Particle color based on variant
        val particleColor = when (particle.colorVariant) {
            0 -> SprintinColors.SportYellow
            1 -> SprintinColors.SoftGoldYellow
            2 -> SprintinColors.DropletHighlight
            else -> SprintinColors.DropletGold
        }
        
        // Soft shadow
        drawCircle(
            color = Color.Black.copy(alpha = 0.08f * (1f - convergeProgress)),
            radius = dynamicSize * 1.3f,
            center = Offset(currentX + 3f, currentY + 5f)
        )
        
        // Main droplet with glossy effect
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.9f),
                    particleColor,
                    particleColor.copy(alpha = 0.7f)
                ),
                center = Offset(currentX - dynamicSize * 0.25f, currentY - dynamicSize * 0.25f),
                radius = dynamicSize * 2f
            ),
            radius = dynamicSize,
            center = Offset(currentX, currentY)
        )
        
        // Highlight glint (iOS-style)
        if (dynamicSize > 5f) {
            drawCircle(
                color = Color.White.copy(alpha = 0.6f * (1f - convergeProgress)),
                radius = dynamicSize * 0.3f,
                center = Offset(currentX - dynamicSize * 0.3f, currentY - dynamicSize * 0.35f)
            )
        }
    }
}

/**
 * Abstract running silhouette with smooth morph animation
 */
@Composable
fun RunnerMorphSilhouette(
    modifier: Modifier = Modifier,
    morphProgress: Float,
    onMorphComplete: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "runner_motion")
    
    // Subtle running motion
    val runCycle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "run_cycle"
    )
    
    // Scale and fade for morphing
    val appearScale by animateFloatAsState(
        targetValue = if (morphProgress > 0.1f) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "appear_scale"
    )
    
    val fadeOut by animateFloatAsState(
        targetValue = if (morphProgress > 0.7f) 0f else 1f,
        animationSpec = tween(200),
        label = "fade_out"
    )
    
    LaunchedEffect(fadeOut) {
        if (fadeOut == 0f) onMorphComplete()
    }
    
    Canvas(
        modifier = modifier
            .size(140.dp)
            .scale(appearScale)
            .alpha(fadeOut)
    ) {
        val cx = size.width / 2
        val cy = size.height / 2
        
        // Dynamic running pose based on cycle
        val armSwing = sin(runCycle * 2 * PI.toFloat()) * 15f
        val legStride = sin(runCycle * 2 * PI.toFloat()) * 20f
        
        val runnerColor = SprintinColors.SportYellow
        
        // Head
        drawCircle(
            color = runnerColor,
            radius = 14f,
            center = Offset(cx + 5f, cy - 50f)
        )
        
        // Torso (tilted forward for running)
        val torsoPath = Path().apply {
            moveTo(cx + 5f, cy - 35f)
            lineTo(cx + 15f, cy + 10f)
            lineTo(cx - 5f, cy + 10f)
            close()
        }
        drawPath(torsoPath, runnerColor)
        
        // Arms with dynamic swing
        val armWidth = 9f
        // Back arm
        drawLine(
            color = runnerColor,
            start = Offset(cx, cy - 25f),
            end = Offset(cx - 25f - armSwing, cy - 5f + armSwing * 0.5f),
            strokeWidth = armWidth,
            cap = StrokeCap.Round
        )
        // Front arm
        drawLine(
            color = runnerColor,
            start = Offset(cx + 10f, cy - 25f),
            end = Offset(cx + 35f + armSwing, cy - 40f - armSwing * 0.5f),
            strokeWidth = armWidth,
            cap = StrokeCap.Round
        )
        
        // Legs with stride motion
        val legWidth = 11f
        // Back leg
        drawLine(
            color = runnerColor,
            start = Offset(cx, cy + 10f),
            end = Offset(cx - 30f - legStride, cy + 55f),
            strokeWidth = legWidth,
            cap = StrokeCap.Round
        )
        // Back foot
        drawLine(
            color = runnerColor,
            start = Offset(cx - 30f - legStride, cy + 55f),
            end = Offset(cx - 40f - legStride, cy + 45f),
            strokeWidth = legWidth * 0.8f,
            cap = StrokeCap.Round
        )
        
        // Front leg
        drawLine(
            color = runnerColor,
            start = Offset(cx + 10f, cy + 10f),
            end = Offset(cx + 25f + legStride, cy + 50f),
            strokeWidth = legWidth,
            cap = StrokeCap.Round
        )
        // Front lower leg
        drawLine(
            color = runnerColor,
            start = Offset(cx + 25f + legStride, cy + 50f),
            end = Offset(cx + 45f + legStride * 0.5f, cy + 35f),
            strokeWidth = legWidth * 0.9f,
            cap = StrokeCap.Round
        )
    }
}

/**
 * iOS-style blur overlay effect
 */
@Composable
fun iOSBlurOverlay(
    modifier: Modifier = Modifier,
    intensity: Float = 0.5f
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        SprintinColors.YellowOverlay10,
                        Color.Transparent
                    )
                )
            )
            .blur((intensity * 50).dp)
    )
}

/**
 * Ambient glow effect for premium feel
 */
@Composable
fun AmbientGlow(
    modifier: Modifier = Modifier,
    glowColor: Color = SprintinColors.SportYellow,
    intensity: Float = 0.2f
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .blur(80.dp)
    ) {
        // Multiple layered glows for depth
        listOf(0.8f, 0.5f, 0.3f).forEachIndexed { index, scale ->
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = intensity * (1f - index * 0.3f)),
                        Color.Transparent
                    ),
                    radius = size.minDimension * scale
                ),
                radius = size.minDimension * scale,
                center = center
            )
        }
    }
}

/**
 * Data class for particle properties
 */
data class SportParticle(
    val id: Int,
    val initialX: Float,
    val initialY: Float,
    val size: Float,
    val velocity: Float,
    val bounciness: Float,
    val rotationSpeed: Float,
    val colorVariant: Int
)
