package com.example.sparin.presentation.discover

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sparin.domain.util.Resource
import com.example.sparin.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomScreen(
    navController: NavHostController,
    mode: String, // "Casual" or "Competitive"
    viewModel: DiscoverViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val createRoomState by viewModel.createRoomState.collectAsState()
    
    // Form State
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(if (mode == "Casual") casualSportCategories[1].name else competitiveSportCategories[1].name) }
    var locationName by remember { mutableStateOf("") }
    var maxPlayers by remember { mutableStateOf("4") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var dateText by remember { mutableStateOf("") }
    var timeText by remember { mutableStateOf("") }
    
    // Theme colors based on mode
    val primaryColor = if (mode == "Casual") MintBreeze else Color(0xFFFF2D2D)
    val backgroundColor = if (mode == "Casual") CascadingWhite else Color(0xFF0A0A0A)
    val textColor = if (mode == "Casual") Lead else Color.White
    
    // Handle Success
    LaunchedEffect(createRoomState) {
        if (createRoomState is Resource.Success) {
            viewModel.resetCreateRoomState()
            navController.navigateUp()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Create $mode Room",
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Room Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    )
                )
                
                // Category Selection
                Text(
                    "Sport Category",
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold
                )
                
                val categories = if (mode == "Casual") casualSportCategories else competitiveSportCategories
                // Simple dropdown or row of chips (using chips for now, simplified)
                // For MVP, just a text showing selected and a way to cycle or a simple list
                // Let's use a simple row of chips for the first few, or a dropdown if many
                // For simplicity, let's just use a few chips
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.take(4).forEach { category ->
                        if (category.name != "All") {
                            FilterChip(
                                selected = selectedCategory == category.name,
                                onClick = { selectedCategory = category.name },
                                label = { Text(category.name) },
                                leadingIcon = { Text(category.emoji) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = primaryColor.copy(alpha = 0.2f),
                                    selectedLabelColor = primaryColor
                                )
                            )
                        }
                    }
                }
                
                // Location
                OutlinedTextField(
                    value = locationName,
                    onValueChange = { locationName = it },
                    label = { Text("Location Name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Rounded.LocationOn, null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    )
                )
                
                // Date & Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Date Picker
                    OutlinedTextField(
                        value = dateText,
                        onValueChange = {},
                        label = { Text("Date") },
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
                                        selectedDate.set(Calendar.YEAR, year)
                                        selectedDate.set(Calendar.MONTH, month)
                                        selectedDate.set(Calendar.DAY_OF_MONTH, day)
                                        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                                        dateText = sdf.format(selectedDate.time)
                                    },
                                    selectedDate.get(Calendar.YEAR),
                                    selectedDate.get(Calendar.MONTH),
                                    selectedDate.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            },
                        enabled = false, // Disable typing, only click
                        leadingIcon = { Icon(Icons.Rounded.CalendarToday, null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = textColor,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    
                    // Time Picker
                    OutlinedTextField(
                        value = timeText,
                        onValueChange = {},
                        label = { Text("Time") },
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        selectedDate.set(Calendar.HOUR_OF_DAY, hour)
                                        selectedDate.set(Calendar.MINUTE, minute)
                                        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                                        timeText = sdf.format(selectedDate.time)
                                    },
                                    selectedDate.get(Calendar.HOUR_OF_DAY),
                                    selectedDate.get(Calendar.MINUTE),
                                    true
                                ).show()
                            },
                        enabled = false,
                        leadingIcon = { Icon(Icons.Rounded.Schedule, null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = textColor,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                
                // Max Players
                OutlinedTextField(
                    value = maxPlayers,
                    onValueChange = { if (it.all { char -> char.isDigit() }) maxPlayers = it },
                    label = { Text("Max Players") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Icon(Icons.Rounded.People, null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    )
                )
                
                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    )
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Submit Button
                Button(
                    onClick = {
                        if (title.isNotEmpty() && locationName.isNotEmpty() && dateText.isNotEmpty() && timeText.isNotEmpty()) {
                            viewModel.createRoom(
                                name = title,
                                category = selectedCategory,
                                mode = mode,
                                locationName = locationName,
                                maxPlayers = maxPlayers.toIntOrNull() ?: 4,
                                dateTime = selectedDate.timeInMillis,
                                description = description
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor
                    ),
                    enabled = createRoomState !is Resource.Loading
                ) {
                    if (createRoomState is Resource.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Create Room", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                // Error Message
                if (createRoomState is Resource.Error) {
                    Text(
                        text = (createRoomState as Resource.Error).message ?: "Unknown error",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}
