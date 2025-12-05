package com.example.sparin.presentation.profile.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.sparin.data.model.UserStats
import com.example.sparin.ui.theme.*
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Horizontal Pager for Stats Clusters
 * Allows swiping left/right to switch between stat clusters
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatsPager(
    stats: UserStats,
    modifier: Modifier = Modifier,
    userScrollEnabled: Boolean = true,
    onPageChanged: () -> Unit = {},
    content: @Composable (page: Int, stats: UserStats) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    
    // Detect page changes and call onPageChanged
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect {
                onPageChanged()
            }
    }
    
    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = userScrollEnabled
        ) { page ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                content(page, stats)
            }
        }
        
        // Page indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .width(if (isSelected) 24.dp else 8.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (isSelected) Crunch else Dreamland.copy(alpha = 0.4f)
                        )
                )
                if (index < 2) Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

