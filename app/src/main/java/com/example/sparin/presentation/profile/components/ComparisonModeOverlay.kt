package com.example.sparin.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.*

/**
 * Comparison Mode Overlay
 * Shows this month vs last month comparison
 */
@Composable
fun ComparisonModeOverlay(
    statLabel: String,
    thisMonth: Double,
    lastMonth: Double,
    sportComparison: Map<String, Pair<Double, Double>>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val change = thisMonth - lastMonth
    val changePercent = if (lastMonth > 0) (change / lastMonth * 100) else 0.0
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Dreamland.copy(alpha = 0.5f)
            )
            .clickable(onClick = onDismiss)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Crunch.copy(alpha = 0.3f)
                ),
            shape = RoundedCornerShape(20.dp),
            color = CascadingWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                ChineseSilver.copy(alpha = 0.4f),
                                CascadingWhite
                            )
                        )
                    )
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📊 Comparison Mode",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Crunch
                    )
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Close",
                            color = WarmHaze
                        )
                    }
                }
                
                Divider(color = Dreamland.copy(alpha = 0.3f))
                
                // Month Comparison
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "$statLabel Comparison",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Lead
                    )
                    
                    ComparisonRow(
                        label = "This Month",
                        value = thisMonth,
                        color = Crunch
                    )
                    ComparisonRow(
                        label = "Last Month",
                        value = lastMonth,
                        color = Dreamland.copy(alpha = 0.7f)
                    )
                    
                    // Change indicator
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (change >= 0) Crunch.copy(alpha = 0.2f) else WarmHaze.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Change",
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmHaze
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (change >= 0) "↑" else "↓",
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "${if (change >= 0) "+" else ""}${String.format("%.1f", changePercent)}%",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = if (change >= 0) Crunch else WarmHaze
                                )
                            }
                        }
                    }
                }
                
                Divider(color = Dreamland.copy(alpha = 0.3f))
                
                // Sport Category Comparison
                if (sportComparison.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "By Sport Category",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Lead
                        )
                        
                        sportComparison.forEach { (sport, values) ->
                            SportComparisonRow(
                                sport = sport,
                                thisMonth = values.first,
                                lastMonth = values.second
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComparisonRow(
    label: String,
    value: Double,
    color: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = WarmHaze
        )
        Text(
            text = String.format("%.1f", value),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )
    }
}

@Composable
private fun SportComparisonRow(
    sport: String,
    thisMonth: Double,
    lastMonth: Double
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = ChineseSilver.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sport,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Lead
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "This: ${String.format("%.1f", thisMonth)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Crunch
                )
                Text(
                    text = "Last: ${String.format("%.1f", lastMonth)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Dreamland.copy(alpha = 0.7f)
                )
            }
        }
    }
}

