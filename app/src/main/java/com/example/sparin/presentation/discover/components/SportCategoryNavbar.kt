package com.example.sparin.presentation.discover.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.*

/**
 * Sport Category Navbar Component
 * Reusable horizontal scrollable category selector with premium styling
 */

data class SportCategoryItem(
    val name: String,
    val emoji: String
)

val defaultSportCategories = listOf(
    SportCategoryItem("All", "🌟"),
    SportCategoryItem("Badminton", "🏸"),
    SportCategoryItem("Basket", "🏀"),
    SportCategoryItem("Futsal", "⚽"),
    SportCategoryItem("Voli", "🏐"),
    SportCategoryItem("Tenis", "🎾"),
    SportCategoryItem("Tenis Meja", "🏓"),
    SportCategoryItem("Padel", "🎾"),
    SportCategoryItem("Sepak Bola", "⚽"),
    SportCategoryItem("Mini Soccer", "⚽"),
    SportCategoryItem("Lari", "🏃"),
    SportCategoryItem("Cycling", "🚴"),
    SportCategoryItem("Gym", "💪"),
    SportCategoryItem("Boxing", "🥊"),
    SportCategoryItem("Muaythai", "🥋"),
    SportCategoryItem("Billiard", "🎱"),
    SportCategoryItem("Catur", "♟️"),
    SportCategoryItem("Hiking", "🥾"),
    SportCategoryItem("Swimming", "🏊")
)

@Composable
fun SportCategoryNavbar(
    categories: List<SportCategoryItem> = defaultSportCategories,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    showTitle: Boolean = true,
    title: String = "Sport Categories"
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (showTitle) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ),
                color = Lead,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))
        }

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            categories.forEach { category ->
                SportCategoryPillItem(
                    category = category,
                    isSelected = selectedCategory == category.name,
                    onClick = { onCategorySelected(category.name) }
                )
            }
        }
    }
}

@Composable
private fun SportCategoryPillItem(
    category: SportCategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Crunch else NeumorphLight,
        animationSpec = tween(250),
        label = "pill_bg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) SunsetOrange else Dreamland.copy(alpha = 0.35f),
        animationSpec = tween(250),
        label = "pill_border"
    )

    val elevation by animateDpAsState(
        targetValue = if (isSelected) 10.dp else 4.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "pill_elevation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "pill_scale"
    )

    Box {
        // Glow effect for active state
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(y = 2.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(
                                Crunch.copy(alpha = 0.4f),
                                Crunch.copy(alpha = 0f)
                            )
                        ),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .blur(12.dp)
            )
        }

        Surface(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .shadow(
                    elevation = elevation,
                    shape = RoundedCornerShape(22.dp),
                    ambientColor = if (isSelected) Crunch.copy(alpha = 0.35f) else NeumorphDark.copy(alpha = 0.1f),
                    spotColor = if (isSelected) Crunch.copy(alpha = 0.35f) else NeumorphDark.copy(alpha = 0.1f)
                )
                .border(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(22.dp)
                )
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(22.dp),
            color = backgroundColor
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = category.emoji, fontSize = 15.sp)
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    ),
                    color = if (isSelected) Lead else WarmHaze
                )

                // Spark particle for active state
                if (isSelected) {
                    Text(
                        text = "✨",
                        fontSize = 10.sp,
                        modifier = Modifier.offset(x = 2.dp, y = (-2).dp)
                    )
                }
            }
        }
    }
}
