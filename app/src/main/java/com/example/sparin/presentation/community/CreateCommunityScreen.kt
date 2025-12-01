package com.example.sparin.presentation.community

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
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

/**
 * CreateCommunityScreen - Sport-Tech Premium Design
 * 
 * Professional, clean form for creating new communities
 * Features:
 * - Clean input fields with subtle borders
 * - Modern category selector
 * - Premium toggle for privacy
 * - Minimal, elegant design
 */

// ==================== CATEGORY DATA ====================

private data class CategoryItem(
    val id: String,
    val name: String,
    val icon: String,
    val color: Color
)

private val categories = listOf(
    CategoryItem("badminton", "Badminton", "🏸", BadmintonAmber),
    CategoryItem("futsal", "Futsal", "⚽", FutsalEmerald),
    CategoryItem("basket", "Basket", "🏀", BasketCoral),
    CategoryItem("tennis", "Tennis", "🎾", TennisTeal),
    CategoryItem("voli", "Voli", "🏐", VolleyOrchid),
    CategoryItem("gym", "Gym", "💪", GymTaupe),
    CategoryItem("running", "Running", "🏃", RunningGold),
    CategoryItem("cycling", "Cycling", "🚴", CyclingAzure),
    CategoryItem("other", "Other", "🏅", TitaniumGray)
)

// ==================== MAIN SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCommunityScreen(
    navController: NavHostController,
    viewModel: CommunityViewModel = org.koin.androidx.compose.koinViewModel()
) {
    var communityName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    var isPublic by remember { mutableStateOf(true) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isCreating by remember { mutableStateOf(false) }
    
    val scrollState = rememberScrollState()
    
    val isFormValid = communityName.isNotBlank() && communityName.length >= 3

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CascadingWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header
            CreateHeader(
                onBackClick = { navController.popBackStack() }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Form content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Community Name
                FormSection(
                    title = "Community Name",
                    subtitle = "Choose a memorable name"
                ) {
                    PremiumTextField(
                        value = communityName,
                        onValueChange = { if (it.length <= 50) communityName = it },
                        placeholder = "Enter community name",
                        leadingIcon = Icons.Outlined.Group
                    )
                }
                
                // Description
                FormSection(
                    title = "Description",
                    subtitle = "Optional"
                ) {
                    PremiumTextField(
                        value = description,
                        onValueChange = { if (it.length <= 200) description = it },
                        placeholder = "Describe your community",
                        leadingIcon = Icons.Outlined.Description,
                        minHeight = 100.dp,
                        singleLine = false
                    )
                }
                
                // Category Selection
                FormSection(
                    title = "Sport Category",
                    subtitle = "Select the main sport"
                ) {
                    CategorySelector(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it }
                    )
                }
                
                // Privacy Toggle
                FormSection(
                    title = "Privacy",
                    subtitle = "Control who can join"
                ) {
                    PrivacyToggle(
                        isPublic = isPublic,
                        onToggle = { isPublic = it }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Create Button
                CreateButton(
                    isEnabled = isFormValid && !isCreating,
                    isLoading = isCreating,
                    onClick = {
                        isCreating = true
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        if (currentUser != null) {
                            val community = Community(
                                name = communityName.trim(),
                                sportCategory = selectedCategory.name,
                                description = description.trim(),
                                createdBy = currentUser.uid,
                                isPublic = isPublic,
                                memberCount = 1,
                                members = listOf(currentUser.uid),
                                admins = listOf(currentUser.uid)
                            )
                            viewModel.createCommunity(
                                community = community,
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
                        } else {
                            isCreating = false
                            errorMessage = "Please sign in to create a community"
                            showErrorDialog = true
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
    
    // Success Dialog
    if (showSuccessDialog) {
        SuccessDialog(
            onDismiss = {
                showSuccessDialog = false
                navController.popBackStack()
            }
        )
    }
    
    // Error Dialog
    if (showErrorDialog) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}

// ==================== HEADER ====================

@Composable
private fun CreateHeader(onBackClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                spotColor = MistGray.copy(alpha = 0.1f)
            ),
        color = IceWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = NeutralInk,
                    modifier = Modifier.size(22.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = "Create Community",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = NeutralInk
            )
        }
    }
}

// ==================== FORM SECTION ====================

@Composable
private fun FormSection(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = NeutralInk
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TitaniumGray
            )
        }
        content()
    }
}

// ==================== PREMIUM TEXT FIELD ====================

@Composable
private fun PremiumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    minHeight: androidx.compose.ui.unit.Dp = 52.dp,
    singleLine: Boolean = true
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight),
        shape = RoundedCornerShape(14.dp),
        color = SmokySilver.copy(alpha = 0.35f),
        border = BorderStroke(1.dp, ShadowMist.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = if (singleLine) 0.dp else 14.dp),
            verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = if (singleLine) 0.dp else 2.dp),
                tint = TitaniumGray
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TitaniumGray
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = NeutralInk
                    ),
                    singleLine = singleLine,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ==================== CATEGORY SELECTOR ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategorySelector(
    selectedCategory: CategoryItem,
    onCategorySelected: (CategoryItem) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            CategoryChip(
                category = category,
                isSelected = selectedCategory.id == category.id,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    category: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "chipScale"
    )
    
    Surface(
        onClick = onClick,
        modifier = Modifier
            .scale(scale),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) category.color.copy(alpha = 0.15f) else SmokySilver.copy(alpha = 0.4f),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) category.color.copy(alpha = 0.5f) else ShadowMist.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = category.icon, fontSize = 16.sp)
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) category.color else TitaniumGray
            )
        }
    }
}

// ==================== PRIVACY TOGGLE ====================

@Composable
private fun PrivacyToggle(
    isPublic: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Public option
        PrivacyOption(
            icon = Icons.Outlined.Public,
            title = "Public",
            subtitle = "Anyone can find & join",
            isSelected = isPublic,
            onClick = { onToggle(true) },
            modifier = Modifier.weight(1f)
        )
        
        // Private option
        PrivacyOption(
            icon = Icons.Outlined.Lock,
            title = "Private",
            subtitle = "Invite only",
            isSelected = !isPublic,
            onClick = { onToggle(false) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PrivacyOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.98f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "privacyScale"
    )
    
    Surface(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
            .height(90.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) NeutralInk else SmokySilver.copy(alpha = 0.4f),
        border = if (!isSelected) BorderStroke(1.dp, ShadowMist.copy(alpha = 0.5f)) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = if (isSelected) IceWhite else TitaniumGray
            )
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) IceWhite else NeutralInk
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) IceWhite.copy(alpha = 0.7f) else TitaniumGray,
                    fontSize = 10.sp
                )
            }
        }
    }
}

// ==================== CREATE BUTTON ====================

@Composable
private fun CreateButton(
    isEnabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(
                elevation = if (isEnabled) 6.dp else 0.dp,
                shape = RoundedCornerShape(14.dp),
                spotColor = if (isEnabled) Crunch.copy(alpha = 0.3f) else Color.Transparent
            ),
        enabled = isEnabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = NeutralInk,
            contentColor = IceWhite,
            disabledContainerColor = ShadowMist,
            disabledContentColor = TitaniumGray
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = IceWhite,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "Create Community",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
    
    // Gold accent line
    if (isEnabled) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(3.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Crunch,
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(1.5.dp)
                    )
            )
        }
    }
}

// ==================== SUCCESS DIALOG ====================

@Composable
private fun SuccessDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            color = IceWhite
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success icon
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            color = FutsalEmerald.copy(alpha = 0.15f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = FutsalEmerald
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "Community Created!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = NeutralInk
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Your community is ready. Start inviting members!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TitaniumGray,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeutralInk,
                        contentColor = IceWhite
                    )
                ) {
                    Text(
                        text = "Done",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// ==================== ERROR DIALOG ====================

@Composable
private fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            color = IceWhite
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Error icon
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            color = BasketCoral.copy(alpha = 0.15f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = BasketCoral
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "Something went wrong",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = NeutralInk
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TitaniumGray,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeutralInk,
                        contentColor = IceWhite
                    )
                ) {
                    Text(
                        text = "Try Again",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
