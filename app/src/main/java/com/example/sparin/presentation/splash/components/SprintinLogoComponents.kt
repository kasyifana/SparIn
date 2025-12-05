package com.example.sparin.presentation.splash.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.SprintinColors
import kotlin.math.PI
import kotlin.math.sin

/**
 * Premium Sprintin Logo with iOS-style animations
 * Features a stretchy "I" that mimics sprinting motion
 */
@Composable
fun SprintinAnimatedLogo(
    modifier: Modifier = Modifier,
    revealProgress: Float,
    showTagline: Boolean = true
) {
    // Spring animation for logo appearance
    val logoScale by animateFloatAsState(
        targetValue = if (revealProgress > 0.1f) 1f else 0.7f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_scale"
    )
    
    val logoAlpha by animateFloatAsState(
        targetValue = if (revealProgress > 0.05f) 1f else 0f,
        animationSpec = tween(250, easing = FastOutSlowInEasing),
        label = "logo_alpha"
    )
    
    // Stretchy "I" animation - creates sprint motion effect
    val stretchPhase = (revealProgress * 4f).coerceIn(0f, 1f)
    val iVerticalStretch = calculateStretchFactor(stretchPhase)
    val iHorizontalSqueeze = 1f / (0.7f + iVerticalStretch * 0.3f)
    
    // Letter-by-letter stagger animation
    val letterDelays = listOf(0f, 0.05f, 0.1f, 0.15f, 0.2f, 0.25f, 0.3f, 0.35f) // S,p,r,i,n,t,i,n
    
    Column(
        modifier = modifier
            .scale(logoScale)
            .alpha(logoAlpha),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Main logo row
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            // "S" with slight bounce
            AnimatedLetter(
                letter = "S",
                progress = (revealProgress - letterDelays[0]).coerceIn(0f, 1f),
                fontSize = 52
            )
            
            // "p"
            AnimatedLetter(
                letter = "p",
                progress = (revealProgress - letterDelays[1]).coerceIn(0f, 1f),
                fontSize = 52
            )
            
            // "r"
            AnimatedLetter(
                letter = "r",
                progress = (revealProgress - letterDelays[2]).coerceIn(0f, 1f),
                fontSize = 52
            )
            
            // Stretchy "i" - the sprint letter
            Box(
                modifier = Modifier
                    .scale(scaleX = iHorizontalSqueeze, scaleY = iVerticalStretch)
                    .offset(y = ((1f - iVerticalStretch) * 8).dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedLetter(
                    letter = "i",
                    progress = (revealProgress - letterDelays[3]).coerceIn(0f, 1f),
                    fontSize = 52,
                    isStretchLetter = true
                )
            }
            
            // "n"
            AnimatedLetter(
                letter = "n",
                progress = (revealProgress - letterDelays[4]).coerceIn(0f, 1f),
                fontSize = 52
            )
            
            // "t"
            AnimatedLetter(
                letter = "t",
                progress = (revealProgress - letterDelays[5]).coerceIn(0f, 1f),
                fontSize = 52
            )
            
            // Second "i" - also stretchy
            Box(
                modifier = Modifier
                    .scale(scaleX = iHorizontalSqueeze, scaleY = iVerticalStretch * 0.9f + 0.1f)
                    .offset(y = ((1f - (iVerticalStretch * 0.9f + 0.1f)) * 6).dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedLetter(
                    letter = "i",
                    progress = (revealProgress - letterDelays[6]).coerceIn(0f, 1f),
                    fontSize = 52,
                    isStretchLetter = true
                )
            }
            
            // "n"
            AnimatedLetter(
                letter = "n",
                progress = (revealProgress - letterDelays[7]).coerceIn(0f, 1f),
                fontSize = 52
            )
        }
        
        // Accent underline
        AccentUnderline(
            progress = (revealProgress - 0.5f).coerceIn(0f, 1f) * 2f
        )
        
        // Tagline
        if (showTagline) {
            Spacer(modifier = Modifier.height(12.dp))
            TaglineText(
                progress = (revealProgress - 0.7f).coerceIn(0f, 1f) * 3.33f
            )
        }
    }
}

/**
 * Individual animated letter with bounce effect
 */
@Composable
private fun AnimatedLetter(
    letter: String,
    progress: Float,
    fontSize: Int,
    isStretchLetter: Boolean = false
) {
    val bounceProgress = progress.coerceIn(0f, 1f)
    
    // Overshoot bounce effect
    val yOffset = if (bounceProgress < 1f) {
        val t = bounceProgress
        val overshoot = if (t < 0.7f) {
            -30f * (1f - t / 0.7f)
        } else {
            val bounce = (t - 0.7f) / 0.3f
            sin(bounce * PI.toFloat() * 2) * 5f * (1f - bounce)
        }
        overshoot
    } else 0f
    
    val letterAlpha = (progress * 3f).coerceIn(0f, 1f)
    
    Text(
        text = letter,
        modifier = Modifier
            .offset(y = yOffset.dp)
            .alpha(letterAlpha),
        style = TextStyle(
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            color = if (isStretchLetter) {
                SprintinColors.SportYellow
            } else {
                SprintinColors.SportYellow
            },
            letterSpacing = (-1.5).sp
        )
    )
}

/**
 * Calculate stretch factor with elastic easing
 */
private fun calculateStretchFactor(phase: Float): Float {
    return when {
        phase < 0.3f -> {
            // Initial stretch up
            1f + (phase / 0.3f) * 0.5f
        }
        phase < 0.5f -> {
            // Peak stretch
            1.5f - ((phase - 0.3f) / 0.2f) * 0.3f
        }
        phase < 0.7f -> {
            // Bounce back below normal
            1.2f - ((phase - 0.5f) / 0.2f) * 0.3f
        }
        phase < 0.85f -> {
            // Settle overshoot
            0.9f + ((phase - 0.7f) / 0.15f) * 0.15f
        }
        else -> {
            // Final settle
            1.05f - ((phase - 0.85f) / 0.15f) * 0.05f
        }
    }
}

/**
 * Premium accent underline
 */
@Composable
private fun AccentUnderline(progress: Float) {
    val lineWidth by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "underline_width"
    )
    
    if (lineWidth > 0.01f) {
        Canvas(
            modifier = Modifier
                .padding(top = 6.dp)
                .height(3.dp)
                .width((120 * lineWidth).dp)
        ) {
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        SprintinColors.SoftGoldYellow.copy(alpha = 0.6f),
                        SprintinColors.SportYellow,
                        SprintinColors.SoftGoldYellow.copy(alpha = 0.6f),
                        Color.Transparent
                    )
                ),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}

/**
 * Subtle tagline with iOS-style tracking
 */
@Composable
private fun TaglineText(progress: Float) {
    val taglineAlpha by animateFloatAsState(
        targetValue = (progress).coerceIn(0f, 0.7f),
        animationSpec = tween(300),
        label = "tagline_alpha"
    )
    
    Text(
        text = "RUN YOUR WAY",
        style = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = SprintinColors.DeepCharcoal.copy(alpha = taglineAlpha),
            letterSpacing = 4.sp,
            textAlign = TextAlign.Center
        )
    )
}

/**
 * Sprintin icon mark (abstract runner symbol)
 */
@Composable
fun SprintinIconMark(
    modifier: Modifier = Modifier,
    color: Color = SprintinColors.SportYellow,
    size: Int = 48
) {
    Canvas(modifier = modifier.size(size.dp)) {
        val strokeWidth = size * 0.08f
        
        // Abstract runner shape - dynamic forward motion
        val runnerPath = Path().apply {
            // Head
            moveTo(this@Canvas.size.width * 0.6f, this@Canvas.size.height * 0.15f)
            
            // Body curve (forward lean)
            cubicTo(
                this@Canvas.size.width * 0.7f, this@Canvas.size.height * 0.3f,
                this@Canvas.size.width * 0.65f, this@Canvas.size.height * 0.5f,
                this@Canvas.size.width * 0.5f, this@Canvas.size.height * 0.65f
            )
        }
        
        drawPath(
            path = runnerPath,
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        
        // Head circle
        drawCircle(
            color = color,
            radius = size * 0.1f,
            center = Offset(this.size.width * 0.6f, this.size.height * 0.15f)
        )
        
        // Back arm
        drawLine(
            color = color,
            start = Offset(this.size.width * 0.55f, this.size.height * 0.35f),
            end = Offset(this.size.width * 0.25f, this.size.height * 0.45f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        
        // Front arm (raised)
        drawLine(
            color = color,
            start = Offset(this.size.width * 0.6f, this.size.height * 0.35f),
            end = Offset(this.size.width * 0.85f, this.size.height * 0.2f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        
        // Back leg (extended)
        drawLine(
            color = color,
            start = Offset(this.size.width * 0.5f, this.size.height * 0.65f),
            end = Offset(this.size.width * 0.15f, this.size.height * 0.9f),
            strokeWidth = strokeWidth * 1.1f,
            cap = StrokeCap.Round
        )
        
        // Front leg
        drawLine(
            color = color,
            start = Offset(this.size.width * 0.5f, this.size.height * 0.65f),
            end = Offset(this.size.width * 0.7f, this.size.height * 0.85f),
            strokeWidth = strokeWidth * 1.1f,
            cap = StrokeCap.Round
        )
        
        // Front lower leg (kick forward)
        drawLine(
            color = color,
            start = Offset(this.size.width * 0.7f, this.size.height * 0.85f),
            end = Offset(this.size.width * 0.9f, this.size.height * 0.7f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}
