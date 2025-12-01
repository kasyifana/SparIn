package com.example.sparin.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Expandable Progress Ring for Winrate
 * Shows Win%, Lose%, Draw% breakdown
 */
@Composable
fun WinrateProgressRing(
    winrate: Float,
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    val loseRate = (100f - winrate) * 0.85f // 85% of losses
    val drawRate = 100f - winrate - loseRate
    
    val ringSize by animateDpAsState(
        targetValue = if (isExpanded) 140.dp else 90.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ring_size"
    )
    
    val strokeWidth = 12.dp
    
    Box(
        modifier = modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val center = this.center
            val radius = (ringSize.toPx() - strokeWidth.toPx()) / 2
            val strokeWidthPx = strokeWidth.toPx()
            val rect = androidx.compose.ui.geometry.Rect(
                center.x - radius,
                center.y - radius,
                center.x + radius,
                center.y + radius
            )
            
            // Background circle
            drawCircle(
                color = Dreamland.copy(alpha = 0.2f),
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
            
            // Win arc (Crunch color)
            drawArc(
                color = Crunch,
                startAngle = -90f,
                sweepAngle = (winrate / 100f) * 360f,
                useCenter = false,
                topLeft = rect.topLeft,
                size = rect.size,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
            
            // Lose arc (Dreamland color)
            val loseStartAngle = -90f + (winrate / 100f) * 360f
            drawArc(
                color = Dreamland.copy(alpha = 0.7f),
                startAngle = loseStartAngle,
                sweepAngle = (loseRate / 100f) * 360f,
                useCenter = false,
                topLeft = rect.topLeft,
                size = rect.size,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
            
            // Draw arc (WarmHaze color) - if exists
            if (drawRate > 0) {
                val drawStartAngle = loseStartAngle + (loseRate / 100f) * 360f
                drawArc(
                    color = WarmHaze.copy(alpha = 0.5f),
                    startAngle = drawStartAngle,
                    sweepAngle = (drawRate / 100f) * 360f,
                    useCenter = false,
                    topLeft = rect.topLeft,
                    size = rect.size,
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
            }
        }
        
        // Center content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${String.format("%.1f", winrate)}%",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Crunch
            )
            if (isExpanded) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Win Rate",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze
                )
            }
        }
    }
    
    // Expanded breakdown
    if (isExpanded) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RingBreakdownItem("Win", "${String.format("%.1f", winrate)}%", Crunch)
            RingBreakdownItem("Loss", "${String.format("%.1f", loseRate)}%", Dreamland.copy(alpha = 0.7f))
            if (drawRate > 0) {
                RingBreakdownItem("Draw", "${String.format("%.1f", drawRate)}%", WarmHaze.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun RingBreakdownItem(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color = color, shape = RoundedCornerShape(50))
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = WarmHaze
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )
    }
}

