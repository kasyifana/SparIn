package com.example.sparin.presentation.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sparin.ui.theme.*
import androidx.compose.ui.geometry.Offset

// Gen-Z vibrant blue matching home page upcoming events
private val GenZBlue = Color(0xFF8CCFFF)
private val GenZGradientStart = Color(0xFF35C8C3)
private val GenZGradientEnd = Color(0xFF57D3FF)

/**
 * EditProfileScreen - Edit user profile with SparIN design system
 * Premium Gen-Z aesthetic with pastel gradients and sporty vibes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    viewModel: EditProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            EditProfileTopBar(
                onBackClick = { navController.navigateUp() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CascadingWhite)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Profile Photo Picker
                ProfilePhotoPicker(
                    currentPhotoUri = uiState.profilePhotoUri,
                    onPhotoSelected = { uri -> viewModel.updateProfilePhoto(uri) }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Name Field
                EditTextField(
                    label = "Name",
                    value = uiState.name,
                    onValueChange = { viewModel.updateName(it) },
                    placeholder = "Enter your name"
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Bio Field
                EditTextField(
                    label = "Bio",
                    value = uiState.bio,
                    onValueChange = { viewModel.updateBio(it) },
                    placeholder = "Tell us about yourself",
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(20.dp))

                // City Field
                EditTextField(
                    label = "City",
                    value = uiState.city,
                    onValueChange = { viewModel.updateCity(it) },
                    placeholder = "Your city"
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Gender Selector
                GenderSelector(
                    selectedGender = uiState.gender,
                    onGenderSelected = { viewModel.updateGender(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Play Frequency Dropdown
                PlayFrequencySelector(
                    selectedFrequency = uiState.playFrequency,
                    onFrequencySelected = { viewModel.updatePlayFrequency(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Skill Level Picker
                SkillLevelPicker(
                    selectedLevel = uiState.skillLevel,
                    onLevelSelected = { viewModel.updateSkillLevel(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sport Interests Selector
                SportInterestSelector(
                    selectedSports = uiState.sportInterests,
                    onSportToggled = { viewModel.toggleSportInterest(it) }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Save Button
                SaveButton(
                    isLoading = uiState.isLoading,
                    onClick = {
                        viewModel.saveProfile {
                            navController.navigateUp()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))
            }

            // Error Message
            uiState.errorMessage?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    containerColor = Color(0xFFE57373),
                    contentColor = Color.White
                ) {
                    Text(error)
                }
            }
        }
    }
}

// ==================== TOP BAR ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Lead
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Lead
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CascadingWhite
        )
    )
}

// ==================== PROFILE PHOTO PICKER ====================

@Composable
private fun ProfilePhotoPicker(
    currentPhotoUri: Uri?,
    onPhotoSelected: (Uri?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onPhotoSelected(uri)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = GenZBlue.copy(alpha = 0.3f),
                    spotColor = GenZBlue.copy(alpha = 0.3f)
                )
                .clip(CircleShape)
                .background(ChineseSilver)
                .border(4.dp, CascadingWhite, CircleShape)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (currentPhotoUri != null) {
                // TODO: Load image from URI using Coil or Glide
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Profile Photo",
                    modifier = Modifier.size(60.dp),
                    tint = Lead.copy(alpha = 0.6f)
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Default Avatar",
                    modifier = Modifier.size(60.dp),
                    tint = Lead.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = GenZBlue.copy(alpha = 0.3f)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GenZGradientStart,
                            GenZGradientEnd,
                            GenZBlue
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .clickable { launcher.launch("image/*") }
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.CameraAlt,
                    contentDescription = "Change Photo",
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
                Text(
                    text = "Change Photo",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

// ==================== EDIT TEXT FIELD ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLines: Int = 1
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Lead
            )
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Lead.copy(alpha = 0.4f)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Dreamland.copy(alpha = 0.2f)
                )
                .clip(RoundedCornerShape(20.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = CascadingWhite,
                unfocusedContainerColor = CascadingWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = GenZBlue,
                focusedTextColor = Lead,
                unfocusedTextColor = Lead
            ),
            maxLines = maxLines,
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }
}

// ==================== GENDER SELECTOR ====================

@Composable
private fun GenderSelector(
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Gender",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Lead
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Gender.values().forEach { gender ->
                GenderChip(
                    gender = gender,
                    isSelected = selectedGender == gender,
                    onClick = { onGenderSelected(gender) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun GenderChip(
    gender: Gender,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = if (isSelected) 8.dp else 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = if (isSelected) GenZBlue.copy(alpha = 0.3f) else Dreamland.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) Brush.linearGradient(
                    colors = listOf(
                        GenZGradientStart,
                        GenZGradientEnd,
                        GenZBlue
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ) else Brush.linearGradient(
                    colors = listOf(ChineseSilver, ChineseSilver)
                )
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = gender.displayName,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Lead
            )
        )
    }
}

// ==================== PLAY FREQUENCY SELECTOR ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayFrequencySelector(
    selectedFrequency: PlayFrequency,
    onFrequencySelected: (PlayFrequency) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Play Frequency",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Lead
            )
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedFrequency.displayName,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Dreamland.copy(alpha = 0.2f)
                    )
                    .clip(RoundedCornerShape(20.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CascadingWhite,
                    unfocusedContainerColor = CascadingWhite,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Lead,
                    unfocusedTextColor = Lead
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(CascadingWhite)
            ) {
                PlayFrequency.values().forEach { frequency ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = frequency.displayName,
                                color = Lead
                            )
                        },
                        onClick = {
                            onFrequencySelected(frequency)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// ==================== SKILL LEVEL PICKER ====================

@Composable
private fun SkillLevelPicker(
    selectedLevel: SkillLevel,
    onLevelSelected: (SkillLevel) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Skill Level",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Lead
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SkillLevel.values().forEach { level ->
                SkillLevelChip(
                    level = level,
                    isSelected = selectedLevel == level,
                    onClick = { onLevelSelected(level) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SkillLevelChip(
    level: SkillLevel,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = if (isSelected) 8.dp else 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = if (isSelected) GenZBlue.copy(alpha = 0.3f) else Dreamland.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) Brush.linearGradient(
                    colors = listOf(
                        GenZGradientStart,
                        GenZGradientEnd,
                        GenZBlue
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ) else Brush.linearGradient(
                    colors = listOf(ChineseSilver, ChineseSilver)
                )
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = level.displayName,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Lead,
                fontSize = 13.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

// ==================== SPORT INTEREST SELECTOR ====================

@Composable
private fun SportInterestSelector(
    selectedSports: List<String>,
    onSportToggled: (String) -> Unit
) {
    val availableSports = listOf(
        "🏸" to "Badminton",
        "⚽" to "Futsal",
        "🏀" to "Basketball",
        "🎾" to "Tennis",
        "🏐" to "Volleyball",
        "🏊" to "Swimming",
        "🏃" to "Running",
        "🚴" to "Cycling"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Sport Interests",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Lead
            )
        )

        // Grid layout for sports
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            availableSports.chunked(2).forEach { rowSports ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowSports.forEach { (icon, name) ->
                        SportChip(
                            icon = icon,
                            name = name,
                            isSelected = selectedSports.contains(name),
                            onClick = { onSportToggled(name) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Fill empty space if odd number
                    if (rowSports.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun SportChip(
    icon: String,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = if (isSelected) 8.dp else 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = if (isSelected) GenZBlue.copy(alpha = 0.3f) else Dreamland.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) Brush.linearGradient(
                    colors = listOf(
                        GenZGradientStart,
                        GenZGradientEnd,
                        GenZBlue
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ) else Brush.linearGradient(
                    colors = listOf(ChineseSilver, ChineseSilver)
                )
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = icon,
                fontSize = 20.sp
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else Lead,
                    fontSize = 12.sp
                )
            )
        }
    }
}

// ==================== SAVE BUTTON ====================

@Composable
private fun SaveButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = GenZBlue.copy(alpha = 0.4f),
                spotColor = GenZBlue.copy(alpha = 0.4f)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        GenZGradientStart,
                        GenZGradientEnd,
                        GenZBlue
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .clickable(enabled = !isLoading, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 3.dp
            )
        } else {
            Text(
                text = "Save Profile",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

// ==================== PREVIEW ====================

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    MaterialTheme {
        EditProfileScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfilePhotoPickerPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CascadingWhite)
                .padding(24.dp)
        ) {
            ProfilePhotoPicker(
                currentPhotoUri = null,
                onPhotoSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenderSelectorPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CascadingWhite)
                .padding(24.dp)
        ) {
            GenderSelector(
                selectedGender = Gender.MALE,
                onGenderSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SkillLevelPickerPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CascadingWhite)
                .padding(24.dp)
        ) {
            SkillLevelPicker(
                selectedLevel = SkillLevel.INTERMEDIATE,
                onLevelSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SportInterestSelectorPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CascadingWhite)
                .padding(24.dp)
        ) {
            SportInterestSelector(
                selectedSports = listOf("Badminton", "Futsal"),
                onSportToggled = {}
            )
        }
    }
}
