package com.example.sparin.presentation.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sparin.data.model.Community
import com.example.sparin.ui.theme.*
import com.google.firebase.auth.FirebaseAuth

/**
 * Screen for creating a new community
 * User fills form with community details and submits
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCommunityScreen(
    navController: NavHostController,
    viewModel: CommunityViewModel = org.koin.androidx.compose.koinViewModel()
) {
    var communityName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Badminton") }
    var isPublic by remember { mutableStateOf(true) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isCreating by remember { mutableStateOf(false) }

    val categories = listOf(
        "Badminton", "Futsal", "Basket", "Tennis", 
        "Gym", "Running", "Voli", "Cycling", "Other"
    )

    // Get current user ID
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Community") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(CascadingWhite, SoftLavender)
                    )
                )
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                Text(
                    text = "Create New Community",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Lead
                )

                Text(
                    text = "Fill in the details to create your sports community",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmHaze
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Community Name
                OutlinedTextField(
                    value = communityName,
                    onValueChange = { communityName = it },
                    label = { Text("Community Name") },
                    placeholder = { Text("e.g., Jakarta Badminton Club") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Crunch,
                        unfocusedBorderColor = WarmHaze.copy(alpha = 0.3f)
                    )
                )

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("Describe your community...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Crunch,
                        unfocusedBorderColor = WarmHaze.copy(alpha = 0.3f)
                    )
                )

                // Sport Category
                Text(
                    text = "Sport Category",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Lead
                )

                // Category chips
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Crunch,
                                selectedLabelColor = CascadingWhite
                            )
                        )
                    }
                }

                // Public/Private Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Community Type",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Lead
                        )
                        Text(
                            text = if (isPublic) "Anyone can join" else "Invite only",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmHaze
                        )
                    }
                    Switch(
                        checked = isPublic,
                        onCheckedChange = { isPublic = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Crunch,
                            checkedTrackColor = Crunch.copy(alpha = 0.5f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Create Button
                Button(
                    onClick = {
                        if (communityName.isBlank()) {
                            errorMessage = "Please enter a community name"
                            showErrorDialog = true
                            return@Button
                        }
                        if (description.isBlank()) {
                            errorMessage = "Please enter a description"
                            showErrorDialog = true
                            return@Button
                        }
                        if (currentUserId.isBlank()) {
                            errorMessage = "User not authenticated"
                            showErrorDialog = true
                            return@Button
                        }

                        isCreating = true

                        val newCommunity = Community(
                            id = "", // Will be auto-generated
                            name = communityName.trim(),
                            sportCategory = selectedCategory,
                            description = description.trim(),
                            coverPhoto = "",
                            memberCount = 1, // Creator is first member
                            members = listOf(currentUserId), // Add creator as first member
                            admins = listOf(currentUserId), // Creator is admin
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
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isCreating,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Crunch,
                        contentColor = CascadingWhite
                    )
                ) {
                    if (isCreating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = CascadingWhite,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Create Community",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        navController.popBackStack() // Go back to community screen
                    }
                ) {
                    Text("OK", color = Crunch)
                }
            },
            title = {
                Text("Success!")
            },
            text = {
                Text("Your community has been created successfully!")
            }
        )
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK", color = Crunch)
                }
            },
            title = {
                Text("Error")
            },
            text = {
                Text(errorMessage)
            }
        )
    }
}
