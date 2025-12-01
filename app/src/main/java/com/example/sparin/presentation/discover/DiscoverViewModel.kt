package com.example.sparin.presentation.discover

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparin.data.model.Room
import com.example.sparin.data.repository.RoomRepository
import com.example.sparin.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Discover Screen
 * Handles loading casual and competitive rooms with category filtering
 */
class DiscoverViewModel(
    private val roomRepository: RoomRepository
) : ViewModel() {
    
    // Casual Rooms State
    private val _casualRoomsState = MutableStateFlow<RoomsState>(RoomsState.Loading)
    val casualRoomsState: StateFlow<RoomsState> = _casualRoomsState.asStateFlow()
    
    // Competitive Rooms State
    private val _competitiveRoomsState = MutableStateFlow<RoomsState>(RoomsState.Loading)
    val competitiveRoomsState: StateFlow<RoomsState> = _competitiveRoomsState.asStateFlow()
    
    // Selected Category for filtering
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()
    
    init {
        loadCasualRooms()
        loadCompetitiveRooms()
    }
    
    /**
     * Load casual rooms
     */
    fun loadCasualRooms(category: String = "All") {
        Log.d(TAG, "loadCasualRooms called with category: $category")
        viewModelScope.launch {
            _casualRoomsState.value = RoomsState.Loading
            
            val result = if (category == "All") {
                roomRepository.getRooms()
            } else {
                roomRepository.getRoomsByCategory(category)
            }
            
            when (result) {
                is Resource.Success -> {
                    val rooms = result.data ?: emptyList()
                    
                    // Filter only casual mode rooms
                    val casualRooms = rooms.filter { it.mode == "Casual" && it.status == "open" }
                    
                    Log.d(TAG, "Loaded ${casualRooms.size} casual rooms")
                    _casualRoomsState.value = RoomsState.Success(casualRooms)
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to load casual rooms: ${result.message}")
                    _casualRoomsState.value = RoomsState.Error(result.message ?: "Failed to load rooms")
                }
                else -> {
                    _casualRoomsState.value = RoomsState.Error("Unexpected error")
                }
            }
        }
    }
    
    /**
     * Load competitive rooms
     */
    fun loadCompetitiveRooms(category: String = "All") {
        Log.d(TAG, "loadCompetitiveRooms called with category: $category")
        viewModelScope.launch {
            _competitiveRoomsState.value = RoomsState.Loading
            
            val result = if (category == "All") {
                roomRepository.getRooms()
            } else {
                roomRepository.getRoomsByCategory(category)
            }
            
            when (result) {
                is Resource.Success -> {
                    val rooms = result.data ?: emptyList()
                    
                    // Filter only competitive mode rooms
                    val competitiveRooms = rooms.filter { it.mode == "Competitive" && it.status == "open" }
                    
                    Log.d(TAG, "Loaded ${competitiveRooms.size} competitive rooms")
                    _competitiveRoomsState.value = RoomsState.Success(competitiveRooms)
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to load competitive rooms: ${result.message}")
                    _competitiveRoomsState.value = RoomsState.Error(result.message ?: "Failed to load rooms")
                }
                else -> {
                    _competitiveRoomsState.value = RoomsState.Error("Unexpected error")
                }
            }
        }
    }
    
    // Create Room State
    private val _createRoomState = MutableStateFlow<Resource<String>?>(null)
    val createRoomState: StateFlow<Resource<String>?> = _createRoomState.asStateFlow()

    /**
     * Create a new room
     */
    fun createRoom(
        name: String,
        category: String,
        mode: String,
        locationName: String,
        maxPlayers: Int,
        dateTime: Long,
        description: String,
        price: Double? = null
    ) {
        viewModelScope.launch {
            _createRoomState.value = Resource.Loading()
            
            val result = roomRepository.createRoom(
                name = name,
                category = category,
                mode = mode,
                location = null, // Location coordinates can be added later
                locationName = locationName,
                maxPlayers = maxPlayers,
                price = price,
                dateTime = dateTime,
                description = description
            )
            
            _createRoomState.value = result
            
            if (result is Resource.Success) {
                Log.d(TAG, "Room created successfully: ${result.data}")
                // Refresh lists
                refreshRooms()
            } else if (result is Resource.Error) {
                Log.e(TAG, "Failed to create room: ${result.message}")
            }
        }
    }
    
    fun resetCreateRoomState() {
        _createRoomState.value = null
    }

    /**
     * Filter rooms by category
     */
    fun filterByCategory(category: String) {
        _selectedCategory.value = category
        loadCasualRooms(category)
        loadCompetitiveRooms(category)
    }
    
    /**
     * Refresh all rooms
     */
    fun refreshRooms() {
        loadCasualRooms(_selectedCategory.value)
        loadCompetitiveRooms(_selectedCategory.value)
    }
    
    // Room Detail State
    private val _roomDetailState = MutableStateFlow<Resource<Room>?>(null)
    val roomDetailState: StateFlow<Resource<Room>?> = _roomDetailState.asStateFlow()

    /**
     * Load room detail
     */
    fun loadRoomDetail(roomId: String) {
        viewModelScope.launch {
            _roomDetailState.value = Resource.Loading()
            val result = roomRepository.getRoom(roomId)
            _roomDetailState.value = result
        }
    }
    
    fun resetRoomDetailState() {
        _roomDetailState.value = null
    }

    /**
     * Join a room
     */
    fun joinRoom(roomId: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            val result = roomRepository.joinRoom(roomId)
            
            when (result) {
                is Resource.Success -> {
                    Log.d(TAG, "Successfully joined room: $roomId")
                    onSuccess()
                    // Refresh rooms to update member count
                    refreshRooms()
                    // Refresh detail if open
                    loadRoomDetail(roomId)
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to join room: ${result.message}")
                    onError(result.message ?: "Failed to join room")
                }
                else -> {
                    onError("Unexpected error")
                }
            }
        }
    }
    
    companion object {
        private const val TAG = "DiscoverViewModel"
    }
}

// ==================== STATE CLASSES ====================

/**
 * Rooms State
 */
sealed class RoomsState {
    object Loading : RoomsState()
    data class Success(val rooms: List<Room>) : RoomsState()
    data class Error(val message: String) : RoomsState()
}
