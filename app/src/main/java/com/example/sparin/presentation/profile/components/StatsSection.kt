package com.example.sparin.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.data.model.UserStats
import com.example.sparin.presentation.profile.*
import com.example.sparin.ui.theme.*

/**
 * Interactive StatsSection Component
 * Features: Tap to expand, Long press for comparison, Flip animation, Swipe to switch clusters
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatsSection(
    stats: UserStats,
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val selectedStatCard by viewModel.selectedStatCard.collectAsState()
    val isComparisonMode by viewModel.isComparisonMode.collectAsState()
    val showBottomSheet by viewModel.showBottomSheet.collectAsState()
    val showPopup by viewModel.showPopup.collectAsState()
    val animatedStatIncrease by viewModel.animatedStatIncrease.collectAsState()
    
    var showTooltip by remember { mutableStateOf<StatCardType?>(null) }
    var expandedWinrateRing by remember { mutableStateOf(false) }
    var expandedTotalMatches by remember { mutableStateOf(false) }
    var expandedTotalWins by remember { mutableStateOf(false) }
    var expandedRank by remember { mutableStateOf(false) }
    var expandedElo by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = NeumorphDark.copy(alpha = 0.12f),
                    spotColor = NeumorphDark.copy(alpha = 0.12f)
                ),
            shape = RoundedCornerShape(24.dp),
            color = ChineseSilver.copy(alpha = 0.5f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                ChineseSilver.copy(alpha = 0.6f),
                                ChineseSilver.copy(alpha = 0.3f)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Section Title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Performance Stats",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Lead
                        )
                        Text(text = "🏆", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Stats Pager for swipe navigation
                    StatsPager(stats = stats) { page, statsData ->
                        when (page) {
                            0 -> StatsCluster1(
                                stats = statsData,
                                viewModel = viewModel,
                                showTooltip = showTooltip,
                                animatedStatIncrease = animatedStatIncrease,
                                isComparisonMode = isComparisonMode,
                                expandedWinrateRing = expandedWinrateRing,
                                expandedTotalMatches = expandedTotalMatches,
                                onTooltipClick = { showTooltip = it },
                                onDismissTooltip = { showTooltip = null },
                                onWinrateExpand = { expandedWinrateRing = !expandedWinrateRing },
                                onTotalMatchesExpand = { expandedTotalMatches = !expandedTotalMatches }
                            )
                            1 -> StatsCluster2(
                                stats = statsData,
                                viewModel = viewModel,
                                showTooltip = showTooltip,
                                animatedStatIncrease = animatedStatIncrease,
                                isComparisonMode = isComparisonMode,
                                expandedTotalWins = expandedTotalWins,
                                expandedRank = expandedRank,
                                onTooltipClick = { statType -> showTooltip = statType },
                                onTotalWinsExpand = { expandedTotalWins = !expandedTotalWins },
                                onRankExpand = { expandedRank = !expandedRank }
                            )
                            2 -> StatsCluster3(
                                stats = statsData,
                                viewModel = viewModel,
                                showTooltip = showTooltip,
                                animatedStatIncrease = animatedStatIncrease,
                                isComparisonMode = isComparisonMode,
                                expandedElo = expandedElo,
                                onTooltipClick = { statType -> showTooltip = statType },
                                onEloExpand = { expandedElo = !expandedElo }
                            )
                        }
                    }
                }
            }
        }

        // Bottom Sheet
        AnimatedVisibility(
            visible = showBottomSheet && selectedStatCard != null,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it }
            ) + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Dreamland.copy(alpha = 0.5f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { viewModel.deselectStatCard() },
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.75f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { /* Prevent dismiss */ },
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    color = CascadingWhite
                ) {
                    if (selectedStatCard != null) {
                        StatCardBottomSheet(
                            statType = selectedStatCard!!,
                            onDismiss = { viewModel.deselectStatCard() }
                        )
                    }
                }
            }
        }

        // Comparison Mode Overlay
        if (isComparisonMode && selectedStatCard != null) {
            ComparisonModeOverlay(
                statLabel = selectedStatCard!!.displayName,
                thisMonth = getCurrentMonthValue(stats, selectedStatCard!!),
                lastMonth = getLastMonthValue(stats, selectedStatCard!!),
                sportComparison = getSportComparison(stats, selectedStatCard!!),
                onDismiss = { viewModel.exitComparisonMode() }
            )
        }

        // Rank Upgrade Popup
        if (showPopup == PopupType.RANK_UPGRADE) {
            RankUpgradePopup(
                currentRank = stats.rank,
                nextRank = getNextRank(stats.rank),
                xpProgress = 0.75f, // Mock data
                onDismiss = { viewModel.hidePopup() }
            )
        }

        // Micro Insight Popup
        if (showPopup == PopupType.MICRO_INSIGHT) {
            MicroInsightPopup(
                onDismiss = { viewModel.hidePopup() }
            )
        }

        // Tooltip
        if (showTooltip != null) {
            TooltipBubble(
                statType = showTooltip!!,
                onDismiss = { showTooltip = null }
            )
        }

        // Achievement Reveal
        if (animatedStatIncrease != null) {
            AchievementReveal(
                message = "New Achievement Unlocked!",
                show = true,
                onDismiss = { viewModel.hidePopup() }
            )
        }
    }
}

@Composable
private fun StatsCluster1(
    stats: UserStats,
    viewModel: ProfileViewModel,
    showTooltip: StatCardType?,
    animatedStatIncrease: StatCardType?,
    isComparisonMode: Boolean,
    expandedWinrateRing: Boolean,
    expandedTotalMatches: Boolean,
    onTooltipClick: (StatCardType) -> Unit,
    onDismissTooltip: () -> Unit,
    onWinrateExpand: () -> Unit,
    onTotalMatchesExpand: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Winrate Card with expandable ring
            Box(modifier = Modifier.weight(1f)) {
                InteractiveStatCard(
                    type = StatCardType.WINRATE,
                    icon = "📊",
                    label = "Winrate",
                    value = "${String.format("%.1f", stats.winrate)}%",
                    accentColor = Crunch,
                    isSelected = viewModel.selectedStatCard.value == StatCardType.WINRATE,
                    isComparisonMode = isComparisonMode,
                    showTooltip = showTooltip == StatCardType.WINRATE,
                    showAchievement = animatedStatIncrease == StatCardType.WINRATE,
                    onTap = { onWinrateExpand() },
                    onLongPress = { viewModel.toggleComparisonMode() },
                    onTooltipClick = { onTooltipClick(StatCardType.WINRATE) },
                    onDismissTooltip = onDismissTooltip
                )
            }

            InteractiveStatCard(
                type = StatCardType.TOTAL_MATCHES,
                icon = "🎯",
                label = "Total Matches",
                value = "${stats.totalMatches}",
                accentColor = MintBreeze,
                modifier = Modifier.weight(1f),
                isSelected = viewModel.selectedStatCard.value == StatCardType.TOTAL_MATCHES,
                isComparisonMode = isComparisonMode,
                showTooltip = showTooltip == StatCardType.TOTAL_MATCHES,
                showAchievement = animatedStatIncrease == StatCardType.TOTAL_MATCHES,
                onTap = { onTotalMatchesExpand() },
                onLongPress = { viewModel.toggleComparisonMode() },
                onTooltipClick = { onTooltipClick(StatCardType.TOTAL_MATCHES) },
                onDismissTooltip = onDismissTooltip
            )
        }

        // Winrate Progress Ring (expandable)
        if (expandedWinrateRing) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WinrateProgressRing(
                    winrate = stats.winrate.toFloat(),
                    isExpanded = true
                )
            }
        }

        // Total Matches Breakdown (expandable)
        if (expandedTotalMatches) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TotalMatchesBreakdown(stats = stats)
            }
        }
    }
}

@Composable
private fun StatsCluster2(
    stats: UserStats,
    viewModel: ProfileViewModel,
    showTooltip: StatCardType?,
    animatedStatIncrease: StatCardType?,
    isComparisonMode: Boolean,
    expandedTotalWins: Boolean,
    expandedRank: Boolean,
    onTooltipClick: (StatCardType) -> Unit,
    onTotalWinsExpand: () -> Unit,
    onRankExpand: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
        InteractiveStatCard(
            type = StatCardType.TOTAL_WINS,
            icon = "✨",
            label = "Total Wins",
            value = "${stats.totalWins}",
            accentColor = PeachGlow,
            modifier = Modifier.weight(1f),
            isSelected = viewModel.selectedStatCard.value == StatCardType.TOTAL_WINS,
            isComparisonMode = isComparisonMode,
            showTooltip = showTooltip == StatCardType.TOTAL_WINS,
            showAchievement = animatedStatIncrease == StatCardType.TOTAL_WINS,
            onTap = { onTotalWinsExpand() },
            onLongPress = { viewModel.toggleComparisonMode() },
            onTooltipClick = { onTooltipClick(StatCardType.TOTAL_WINS) }
        )
        InteractiveStatCard(
            type = StatCardType.RANK,
            icon = "⭐",
            label = "Rank",
            value = stats.rank,
            accentColor = SkyMist,
            modifier = Modifier.weight(1f),
            isSelected = viewModel.selectedStatCard.value == StatCardType.RANK,
            isComparisonMode = isComparisonMode,
            showTooltip = showTooltip == StatCardType.RANK,
            showAchievement = animatedStatIncrease == StatCardType.RANK,
            onTap = { onRankExpand() },
            onLongPress = { viewModel.toggleComparisonMode() },
            onTooltipClick = { onTooltipClick(StatCardType.RANK) }
        )
        }

        // Total Wins Breakdown (expandable)
        if (expandedTotalWins) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TotalWinsBreakdown(stats = stats)
            }
        }

        // Rank Breakdown (expandable)
        if (expandedRank) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RankBreakdown(stats = stats)
            }
        }
    }
}

@Composable
private fun StatsCluster3(
    stats: UserStats,
    viewModel: ProfileViewModel,
    showTooltip: StatCardType?,
    animatedStatIncrease: StatCardType?,
    isComparisonMode: Boolean,
    expandedElo: Boolean,
    onTooltipClick: (StatCardType) -> Unit,
    onEloExpand: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        InteractiveStatCard(
            type = StatCardType.ELO,
            icon = "🔥",
            label = "ELO Rating",
            value = "${stats.elo}",
            accentColor = Crunch,
            modifier = Modifier.fillMaxWidth(),
            isSelected = viewModel.selectedStatCard.value == StatCardType.ELO,
            isComparisonMode = isComparisonMode,
            showTooltip = showTooltip == StatCardType.ELO,
            showAchievement = animatedStatIncrease == StatCardType.ELO,
            onTap = { onEloExpand() },
            onLongPress = { viewModel.toggleComparisonMode() },
            onTooltipClick = { onTooltipClick(StatCardType.ELO) }
        )

        // ELO Breakdown (expandable)
        if (expandedElo) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EloBreakdown(stats = stats)
            }
        }
    }
}

// Helper functions for StatsSection
fun getCurrentMonthValue(stats: UserStats, type: StatCardType): Double {
    return when (type) {
        StatCardType.WINRATE -> stats.winrate.toDouble()
        StatCardType.TOTAL_MATCHES -> stats.totalMatches.toDouble()
        StatCardType.TOTAL_WINS -> stats.totalWins.toDouble()
        StatCardType.ELO -> stats.elo.toDouble()
        else -> 0.0
    }
}

fun getLastMonthValue(stats: UserStats, type: StatCardType): Double {
    // Mock data - in real app, get from historical data
    return when (type) {
        StatCardType.WINRATE -> stats.winrate * 0.95
        StatCardType.TOTAL_MATCHES -> stats.totalMatches * 0.85
        StatCardType.TOTAL_WINS -> stats.totalWins * 0.85
        StatCardType.ELO -> stats.elo - 45.0
        else -> 0.0
    }
}

fun getSportComparison(
    stats: UserStats,
    type: StatCardType
): Map<String, Pair<Double, Double>> {
    // Mock data - in real app, get from historical data
    return mapOf(
        "Badminton" to Pair(
            getCurrentMonthValue(stats, type) * 0.45,
            getLastMonthValue(stats, type) * 0.40
        ),
        "Futsal" to Pair(
            getCurrentMonthValue(stats, type) * 0.30,
            getLastMonthValue(stats, type) * 0.35
        ),
        "Basketball" to Pair(
            getCurrentMonthValue(stats, type) * 0.25,
            getLastMonthValue(stats, type) * 0.25
        )
    )
}

fun getNextRank(currentRank: String): String {
    // Mock logic - in real app, use actual rank progression
    return when {
        currentRank.contains("Gold") -> "Platinum I"
        currentRank.contains("Platinum") -> "Diamond I"
        else -> "Gold I"
    }
}
