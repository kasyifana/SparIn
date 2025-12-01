package com.example.sparin.presentation.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
fun RoomDetailScreen(
    navController: NavHostController,
    roomId: String,
    viewModel: DiscoverViewModel = koinViewModel()
) {
    val roomState by viewModel.roomDetailState.collectAsState()
    
    LaunchedEffect(roomId) {
        viewModel.loadRoomDetail(roomId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Room Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Rounded.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = roomState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Error -> {
                    Text(
                        text = state.message ?: "Error loading room",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is Resource.Success -> {
                    val room = state.data
                    if (room != null) {
                        val isCasual = room.mode == "Casual"
                        val primaryColor = if (isCasual) MintBreeze else Color(0xFFFF2D2D)
                        
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            // Header Info
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Emoji Box
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(primaryColor.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = getEmojiForCategory(room.category),
                                        fontSize = 40.sp
                                    )
                                }
                                
                                Column {
                                    Text(
                                        text = room.name,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Surface(
                                        color = primaryColor.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = room.mode,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            color = primaryColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                            
                            Divider(color = Color.Gray.copy(alpha = 0.2f))
                            
                            // Details
                            DetailItem(Icons.Rounded.Category, "Sport", room.category)
                            DetailItem(Icons.Rounded.LocationOn, "Location", room.locationName)
                            
                            val date = Date(room.dateTime)
                            val sdf = SimpleDateFormat("EEEE, dd MMM yyyy • HH:mm", Locale.getDefault())
                            DetailItem(Icons.Rounded.Schedule, "Time", sdf.format(date))
                            
                            DetailItem(
                                Icons.Rounded.People, 
                                "Players", 
                                "${room.currentPlayers} / ${room.maxPlayers} Joined"
                            )
                            
                            if (room.price != null && room.price > 0) {
                                DetailItem(Icons.Rounded.AttachMoney, "Price", "Rp ${room.price.toInt()}")
                            } else {
                                DetailItem(Icons.Rounded.MoneyOff, "Price", "Free")
                            }
                            
                            // Description
                            if (room.description.isNotEmpty()) {
                                Column {
                                    Text(
                                        "Description",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = room.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            // Join Button
                            Button(
                                onClick = { viewModel.joinRoom(room.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                ),
                                enabled = room.status == "open" && room.currentPlayers < room.maxPlayers
                            ) {
                                Text(
                                    text = if (room.currentPlayers >= room.maxPlayers) "Full" else "Join Room",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        Text("Room not found", modifier = Modifier.align(Alignment.Center))
                    }
                }
                null -> {
                    // Initial state
                }
            }
        }
    }
}

@Composable
fun DetailItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// getEmojiForCategory is now in DiscoverUtils.kt
