package com.example.sparinprofile.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparinprofile.presentation.profile.Badge
import com.example.sparinprofile.ui.theme.*

@Composable
fun BadgeDisplay(
    badges: List<Badge>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Section Header
        Text(
            text = "Achievements 🎖️",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkColors.TextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Horizontal scrollable badges
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(badges) { badge ->
                BadgeCard(badge = badge)
            }
        }
    }
}

@Composable
fun BadgeCard(
    badge: Badge,
    accentColor: Color = Crunch
) {
    Surface(
        modifier = Modifier
            .width(140.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = DarkColors.SurfaceDefault,
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    accentColor.copy(alpha = 0.5f),
                    accentColor.copy(alpha = 0.2f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            Text(
                text = badge.icon,
                fontSize = 40.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Title
            Text(
                text = badge.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Description
            Text(
                text = badge.description,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = DarkColors.TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF202022)
@Composable
fun BadgeDisplayPreview() {
    BadgeDisplay(
        badges = listOf(
            Badge("🏆", "Champion", "Won 10 matches"),
            Badge("⚡", "Speed Demon", "Fastest player"),
            Badge("🎯", "Sharpshooter", "95% accuracy"),
            Badge("🔥", "Hot Streak", "5 wins in a row")
        )
    )
}
