package com.example.sparin.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.sparin.data.model.UserStats
import com.example.sparin.ui.theme.*

/**
 * Horizontal Pager for Stats Clusters
 * Allows swiping left/right to switch between stat clusters
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatsPager(
    stats: UserStats,
    modifier: Modifier = Modifier,
    content: @Composable (page: Int, stats: UserStats) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    var pageChangeKey by remember { mutableStateOf(0) }
    
    // Trigger page change animation when page changes
    LaunchedEffect(pagerState.currentPage) {
        pageChangeKey = pagerState.currentPage
    }
    
    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .key(pageChangeKey),
                contentAlignment = Alignment.Center
            ) {
                content(page, stats)
            }
        }
        
        // Page indicator with animated transitions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                val isSelected = pagerState.currentPage == index
                val animatedWidth by animateDpAsState(
                    targetValue = if (isSelected) 24.dp else 8.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "IndicatorWidth"
                )
                val animatedAlpha by animateFloatAsState(
                    targetValue = if (isSelected) 1f else 0.4f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "IndicatorAlpha"
                )
                Box(
                    modifier = Modifier
                        .width(animatedWidth)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Crunch.copy(alpha = animatedAlpha)
                        )
                )
                if (index < 2) Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

