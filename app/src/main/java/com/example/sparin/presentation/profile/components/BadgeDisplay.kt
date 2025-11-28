package com.example.sparin.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.presentation.profile.Badge
import com.example.sparin.ui.theme.*

/**
 * BadgeDisplay Component
 * Horizontal scrollable row of achievement badges
 */
@Composable
fun BadgeDisplay(
    badges: List<Badge>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Section Header
        Text(
            text = "Achievements",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = Lead
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Horizontal Scrollable Badges
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            badges.forEach { badge ->
                BadgeCard(badge = badge)
            }
        }
    }
}

@Composable
private fun BadgeCard(
    badge: Badge
) {
    // Cycle through accent colors for variety
    val accentColor = when (badge.icon) {
        "🏆" -> Crunch
        "🔥" -> SunsetOrange
        "⭐" -> PeachGlow
        "💪" -> MintBreeze
        "🎯" -> SkyMist
        else -> ChineseSilver
    }

    Surface(
        modifier = Modifier
            .width(140.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = accentColor.copy(alpha = 0.15f),
                spotColor = accentColor.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = NeumorphLight.copy(alpha = 0.95f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.15f),
                            accentColor.copy(alpha = 0.03f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.4f),
                            accentColor.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Badge Icon Container
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = accentColor.copy(alpha = 0.25f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = badge.icon,
                            fontSize = 32.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Badge Title
                Text(
                    text = badge.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    color = Lead,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Badge Description
                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze,
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    maxLines = 2
                )
            }
        }
    }
}
