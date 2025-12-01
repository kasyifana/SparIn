package com.example.sparin.presentation.community

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.sparin.data.model.Community
import com.example.sparin.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.cos
import kotlin.math.sin

/**
 * CreateCommunityScreen - Ultra Premium Gen-Z Aesthetic
 * Modern, aesthetic form for creating new sports communities
 * Features: Glassmorphism, soft gradients, neumorphic elements
 */

// Category data for sports selection
private data class CreateCategoryOption(
    val name: String,
    val emoji: String,
    val gradient: List<Color>,
    val accentColor: Color
)

private val createCategories = listOf(
    CreateCategoryOption("Badminton", "🏸", listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2)), Color(0xFFFFB74D)),
    CreateCategoryOption("Futsal", "⚽", listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9)), Color(0xFF81C784)),
    CreateCategoryOption("Basket", "🏀", listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2)), Color(0xFFE57373)),
    CreateCategoryOption("Tennis", "🎾", listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2)), Color(0xFF4DD0E1)),
    CreateCategoryOption("Voli", "🏐", listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7)), Color(0xFFBA68C8)),
    CreateCategoryOption("Gym", "💪", listOf(Color(0xFFEFEBE9), Color(0xFFD7CCC8)), Color(0xFFA1887F)),
    CreateCategoryOption("Running", "🏃", listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3)), Color(0xFFFFD54F)),
    CreateCategoryOption("Cycling", "🚴", listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC)), Color(0xFF4FC3F7)),
    CreateCategoryOption("Other", "🏅", listOf(Color(0xFFF5F5F5), Color(0xFFE0E0E0)), Dreamland)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCommunityScreen(
    navController: NavHostController,
    viewModel: CommunityViewModel = org.koin.androidx.compose.koinViewModel()
) {
    var communityName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(createCategories.first()) }
    var isPublic by remember { mutableStateOf(true) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isCreating by remember { mutableStateOf(false) }

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CascadingWhite)
    ) {
        // Animated Background Blobs
        CreateCommunityBackgroundBlobs()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Premium Header
            CreateCommunityHeader(onBackClick = { navController.popBackStack() })

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Section Title
                Column {
                    Text(
                        text = "Create Your Community",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Lead,
                        letterSpacing = (-0.3).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Build your sports tribe and connect with athletes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmHaze.copy(alpha = 0.8f)
                    )
                }

                // Community Name Input
                PremiumTextField(
                    value = communityName,
                    onValueChange = { communityName = it },
                    label = "Community Name",
                    placeholder = "e.g., Jakarta Badminton Club",
                    icon = Icons.Rounded.Group,
                    singleLine = true
                )

                // Description Input
                PremiumTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Description",
                    placeholder = "Describe your community and its goals...",
                    icon = Icons.Rounded.Description,
                    singleLine = false,
                    minHeight = 120.dp
                )

                // Sport Category Section
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Crunch, Crunch.copy(alpha = 0.5f))
                                    ),
                                    shape = CircleShape
                                )
                        )
                        Text(
                            text = "Sport Category",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Lead
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Category Grid
                    PremiumCategoryGrid(
                        categories = createCategories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it }
                    )
                }

                // Privacy Toggle
                PremiumPrivacyToggle(
                    isPublic = isPublic,
                    onToggle = { isPublic = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Create Button
                PremiumCreateButton(
                    isCreating = isCreating,
                    enabled = communityName.isNotBlank() && description.isNotBlank(),
                    onClick = {
                        if (communityName.isBlank()) {
                            errorMessage = "Please enter a community name"
                            showErrorDialog = true
                            return@PremiumCreateButton
                        }
                        if (description.isBlank()) {
                            errorMessage = "Please enter a description"
                            showErrorDialog = true
                            return@PremiumCreateButton
                        }
                        if (currentUserId.isBlank()) {
                            errorMessage = "User not authenticated"
                            showErrorDialog = true
                            return@PremiumCreateButton
                        }

                        isCreating = true

                        val newCommunity = Community(
                            id = "",
                            name = communityName.trim(),
                            sportCategory = selectedCategory.name,
                            description = description.trim(),
                            coverPhoto = "",
                            memberCount = 1,
                            members = listOf(currentUserId),
                            admins = listOf(currentUserId),
                            createdBy = currentUserId,
                            createdAt = com.google.firebase.Timestamp.now(),
                            isPublic = isPublic
                        )

                        viewModel.createCommunity(
                            community = newCommunity,
                            onSuccess = {
                                isCreating = false
                                showSuccessDialog = true
                            },
                            onError = { error ->
                                isCreating = false
                                errorMessage = error
                                showErrorDialog = true
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        PremiumSuccessDialog(
            onDismiss = {
                showSuccessDialog = false
                navController.popBackStack()
            }
        )
    }

    // Error Dialog
    if (showErrorDialog) {
        PremiumErrorDialog(
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}

// ==================== BACKGROUND ====================

@Composable
private fun CreateCommunityBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "create_bg")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(32000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset2"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(100.dp)
    ) {
        // Crunch/Gold glow top
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Crunch.copy(alpha = 0.12f),
                    PeachGlow.copy(alpha = 0.06f),
                    Color.Transparent
                )
            ),
            radius = 280f,
            center = Offset(
                x = size.width * 0.15f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 40f,
                y = size.height * 0.12f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f
            )
        )

        // Mint glow center-right
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    MintBreeze.copy(alpha = 0.15f),
                    SkyMist.copy(alpha = 0.08f),
                    Color.Transparent
                )
            ),
            radius = 220f,
            center = Offset(
                x = size.width * 0.85f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 35f,
                y = size.height * 0.45f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 40f
            )
        )

        // Lavender glow bottom
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    SoftLavender.copy(alpha = 0.18f),
                    ChineseSilver.copy(alpha = 0.1f),
                    Color.Transparent
                )
            ),
            radius = 250f,
            center = Offset(
                x = size.width * 0.3f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 30f,
                y = size.height * 0.8f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 35f
            )
        )
    }
}

// ==================== HEADER ====================

@Composable
private fun CreateCommunityHeader(onBackClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                ambientColor = Dreamland.copy(alpha = 0.1f)
            ),
        color = CascadingWhite.copy(alpha = 0.95f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        spotColor = Dreamland.copy(alpha = 0.2f)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onBackClick() },
                shape = CircleShape,
                color = ChineseSilver.copy(alpha = 0.4f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(22.dp),
                        tint = Lead
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title with icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Crunch, SunsetOrange, PeachGlow)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        tint = Lead,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Text(
                    text = "New Community",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Lead,
                    letterSpacing = (-0.3).sp
                )
            }
        }
    }
}

// ==================== PREMIUM TEXT FIELD ====================

@Composable
private fun PremiumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    singleLine: Boolean = true,
    minHeight: androidx.compose.ui.unit.Dp = 60.dp
) {
    Column {
        // Label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Crunch, Crunch.copy(alpha = 0.5f))
                        ),
                        shape = CircleShape
                    )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Lead
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Input field with glassmorphism
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = Dreamland.copy(alpha = 0.15f),
                    ambientColor = Dreamland.copy(alpha = 0.08f)
                ),
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent,
            border = BorderStroke(
                1.5.dp,
                Brush.linearGradient(
                    colors = listOf(
                        ChineseSilver.copy(alpha = 0.6f),
                        Dreamland.copy(alpha = 0.4f),
                        ChineseSilver.copy(alpha = 0.5f)
                    )
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                CascadingWhite.copy(alpha = 0.9f),
                                ChineseSilver.copy(alpha = 0.2f)
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top
                ) {
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = ChineseSilver.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = WarmHaze
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .then(if (!singleLine) Modifier.heightIn(min = 80.dp) else Modifier)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = WarmHaze.copy(alpha = 0.5f)
                            )
                        }
                        BasicTextField(
                            value = value,
                            onValueChange = onValueChange,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = Lead,
                                fontWeight = FontWeight.Medium
                            ),
                            singleLine = singleLine,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// ==================== CATEGORY GRID ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PremiumCategoryGrid(
    categories: List<CreateCategoryOption>,
    selectedCategory: CreateCategoryOption,
    onCategorySelected: (CreateCategoryOption) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        categories.forEach { category ->
            PremiumCategoryChip(
                category = category,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun PremiumCategoryChip(
    category: CreateCategoryOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "chipScale"
    )

    val shadowElevation by animateDpAsState(
        targetValue = if (isSelected) 12.dp else 4.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "chipElevation"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier
            .scale(scale)
            .shadow(
                elevation = shadowElevation,
                shape = RoundedCornerShape(16.dp),
                spotColor = if (isSelected) category.accentColor.copy(alpha = 0.4f) else Dreamland.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = if (!isSelected) BorderStroke(
            1.5.dp,
            Brush.linearGradient(
                colors = listOf(
                    ChineseSilver.copy(alpha = 0.6f),
                    Dreamland.copy(alpha = 0.4f)
                )
            )
        ) else null
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = if (isSelected) {
                        Brush.linearGradient(category.gradient)
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                CascadingWhite,
                                ChineseSilver.copy(alpha = 0.2f)
                            )
                        )
                    }
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Emoji background
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            color = if (isSelected)
                                CascadingWhite.copy(alpha = 0.6f)
                            else
                                ChineseSilver.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = category.emoji, fontSize = 14.sp)
                }

                Text(
                    text = category.name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSelected) Lead else WarmHaze
                )

                // Check mark for selected
                AnimatedVisibility(
                    visible = isSelected,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Lead
                    )
                }
            }
        }
    }
}

// ==================== PRIVACY TOGGLE ====================

@Composable
private fun PremiumPrivacyToggle(
    isPublic: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Dreamland.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(22.dp),
        color = Color.Transparent,
        border = BorderStroke(
            1.5.dp,
            Brush.linearGradient(
                colors = listOf(
                    ChineseSilver.copy(alpha = 0.5f),
                    SoftLavender.copy(alpha = 0.4f),
                    Dreamland.copy(alpha = 0.3f)
                )
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            ChineseSilver.copy(alpha = 0.3f),
                            SoftLavender.copy(alpha = 0.2f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = if (isPublic)
                                        listOf(MintBreeze.copy(alpha = 0.6f), SkyMist.copy(alpha = 0.5f))
                                    else
                                        listOf(ChineseSilver.copy(alpha = 0.5f), Dreamland.copy(alpha = 0.4f))
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPublic) Icons.Rounded.Public else Icons.Rounded.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = if (isPublic) Lead else WarmHaze
                        )
                    }

                    Column {
                        Text(
                            text = if (isPublic) "Public Community" else "Private Community",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Lead
                        )
                        Text(
                            text = if (isPublic) "Anyone can join" else "Invite only",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmHaze.copy(alpha = 0.8f)
                        )
                    }
                }

                // Custom Switch
                Box(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onToggle(!isPublic) }
                ) {
                    val animatedOffset by animateDpAsState(
                        targetValue = if (isPublic) 28.dp else 0.dp,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "switchOffset"
                    )

                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(28.dp)
                            .background(
                                brush = if (isPublic)
                                    Brush.linearGradient(listOf(Crunch, SunsetOrange))
                                else
                                    Brush.linearGradient(listOf(Dreamland, ChineseSilver)),
                                shape = RoundedCornerShape(14.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .offset(x = animatedOffset)
                                .size(24.dp)
                                .shadow(
                                    elevation = 4.dp,
                                    shape = CircleShape
                                )
                                .background(
                                    color = CascadingWhite,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}

// ==================== CREATE BUTTON ====================

@Composable
private fun PremiumCreateButton(
    isCreating: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "createBtn")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btnGlow"
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Glow effect
        if (enabled && !isCreating) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .blur(14.dp)
                    .alpha(glowAlpha * 0.5f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Crunch, Color.Transparent)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .shadow(
                    elevation = if (enabled) 16.dp else 6.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = if (enabled) Crunch.copy(alpha = 0.4f) else Dreamland.copy(alpha = 0.2f)
                )
                .then(
                    if (enabled && !isCreating)
                        Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onClick() }
                    else Modifier
                ),
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = if (enabled)
                            Brush.linearGradient(listOf(Crunch, SunsetOrange, PeachGlow))
                        else
                            Brush.linearGradient(listOf(Dreamland, ChineseSilver))
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCreating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        color = Lead,
                        strokeWidth = 3.dp
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.RocketLaunch,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = if (enabled) Lead else WarmHaze
                        )
                        Text(
                            text = "Create Community",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (enabled) Lead else WarmHaze
                        )
                    }
                }
            }
        }
    }
}

// ==================== SUCCESS DIALOG ====================

@Composable
private fun PremiumSuccessDialog(onDismiss: () -> Unit) {
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            1f,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
    }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .scale(scale.value),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 24.dp,
                        shape = RoundedCornerShape(32.dp),
                        spotColor = MintBreeze.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(32.dp),
                color = Color.Transparent,
                border = BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            MintBreeze.copy(alpha = 0.6f),
                            ChineseSilver.copy(alpha = 0.4f)
                        )
                    )
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    CascadingWhite,
                                    ChineseSilver.copy(alpha = 0.3f)
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Success Icon
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(MintBreeze, Color(0xFF81C784))
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(44.dp),
                                tint = CascadingWhite
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Success! 🎉",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Lead
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Your community has been created successfully!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmHaze,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .shadow(
                                    elevation = 10.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    spotColor = Crunch.copy(alpha = 0.3f)
                                )
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { onDismiss() },
                            shape = RoundedCornerShape(16.dp),
                            color = Color.Transparent
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.linearGradient(
                                            listOf(Crunch, SunsetOrange)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Let's Go!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Lead
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== ERROR DIALOG ====================

@Composable
private fun PremiumErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(0.85f),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(28.dp),
                        spotColor = Color(0xFFEF4444).copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(28.dp),
                color = Color.Transparent,
                border = BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFCA5A5).copy(alpha = 0.6f),
                            Color(0xFFEF4444).copy(alpha = 0.3f)
                        )
                    )
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFFEE2E2),
                                    Color(0xFFFECACA)
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Error Icon
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFEF4444),
                                            Color(0xFFDC2626)
                                        )
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint = CascadingWhite
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Oops!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB91C1C)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFDC2626),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { onDismiss() },
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFDC2626)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Try Again",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = CascadingWhite
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
