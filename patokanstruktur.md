# SparIN - Complete Project Structure & Logic Flow

## 📁 Project Tree Structure

```
SparIN/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/sparin/
│   │   │   │   ├── SparINApplication.kt
│   │   │   │   │
│   │   │   │   ├── di/                          # Dependency Injection
│   │   │   │   │   ├── AppModule.kt
│   │   │   │   │   ├── NetworkModule.kt
│   │   │   │   │   ├── FirebaseModule.kt
│   │   │   │   │   └── RepositoryModule.kt
│   │   │   │   │
│   │   │   │   ├── data/                        # Data Layer
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── User.kt
│   │   │   │   │   │   ├── Room.kt
│   │   │   │   │   │   ├── Community.kt
│   │   │   │   │   │   ├── Campaign.kt
│   │   │   │   │   │   ├── ChatMessage.kt
│   │   │   │   │   │   ├── Match.kt
│   │   │   │   │   │   ├── SportCategory.kt
│   │   │   │   │   │   └── UserStats.kt
│   │   │   │   │   │
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   ├── AuthRepository.kt
│   │   │   │   │   │   ├── UserRepository.kt
│   │   │   │   │   │   ├── RoomRepository.kt
│   │   │   │   │   │   ├── CommunityRepository.kt
│   │   │   │   │   │   ├── CampaignRepository.kt
│   │   │   │   │   │   ├── ChatRepository.kt
│   │   │   │   │   │   ├── MatchRepository.kt
│   │   │   │   │   │   └── AIRepository.kt
│   │   │   │   │   │
│   │   │   │   │   └── remote/
│   │   │   │   │       ├── FirebaseAuthService.kt
│   │   │   │   │       ├── FirestoreService.kt
│   │   │   │   │       └── CloudFunctionsService.kt
│   │   │   │   │
│   │   │   │   ├── domain/                      # Business Logic
│   │   │   │   │   ├── usecase/
│   │   │   │   │   │   ├── auth/
│   │   │   │   │   │   │   ├── SignInWithGoogleUseCase.kt
│   │   │   │   │   │   │   └── SignOutUseCase.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── user/
│   │   │   │   │   │   │   ├── GetUserProfileUseCase.kt
│   │   │   │   │   │   │   ├── UpdateUserProfileUseCase.kt
│   │   │   │   │   │   │   └── CompletePersonalizationUseCase.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── room/
│   │   │   │   │   │   │   ├── CreateRoomUseCase.kt
│   │   │   │   │   │   │   ├── GetRoomsUseCase.kt
│   │   │   │   │   │   │   ├── JoinRoomUseCase.kt
│   │   │   │   │   │   │   └── FilterRoomsByCategoryUseCase.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── community/
│   │   │   │   │   │   │   ├── GetCommunitiesUseCase.kt
│   │   │   │   │   │   │   ├── JoinCommunityUseCase.kt
│   │   │   │   │   │   │   └── FilterCommunitiesBySportUseCase.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── match/
│   │   │   │   │   │   │   ├── GetUpcomingMatchesUseCase.kt
│   │   │   │   │   │   │   ├── UpdateMatchResultUseCase.kt
│   │   │   │   │   │   │   └── GetMatchHistoryUseCase.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── chat/
│   │   │   │   │   │   │   ├── GetChatRoomsUseCase.kt
│   │   │   │   │   │   │   ├── SendMessageUseCase.kt
│   │   │   │   │   │   │   └── ObserveChatMessagesUseCase.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   └── ai/
│   │   │   │   │   │       ├── AnalyzeProfileUseCase.kt
│   │   │   │   │   │       └── GetRecommendationsUseCase.kt
│   │   │   │   │   │
│   │   │   │   │   └── util/
│   │   │   │   │       └── Resource.kt              # Sealed class for API states
│   │   │   │   │
│   │   │   │   ├── presentation/                # UI Layer
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   │
│   │   │   │   │   ├── navigation/
│   │   │   │   │   │   ├── NavGraph.kt
│   │   │   │   │   │   ├── Screen.kt
│   │   │   │   │   │   └── BottomNavItem.kt
│   │   │   │   │   │
│   │   │   │   │   ├── onboarding/
│   │   │   │   │   │   ├── OnboardingScreen.kt
│   │   │   │   │   │   ├── OnboardingViewModel.kt
│   │   │   │   │   │   └── components/
│   │   │   │   │   │       └── OnboardingPager.kt
│   │   │   │   │   │
│   │   │   │   │   ├── auth/
│   │   │   │   │   │   ├── SignInScreen.kt
│   │   │   │   │   │   ├── SignInViewModel.kt
│   │   │   │   │   │   └── components/
│   │   │   │   │   │       └── GoogleSignInButton.kt
│   │   │   │   │   │
│   │   │   │   │   ├── personalization/
│   │   │   │   │   │   ├── PersonalizationScreen.kt
│   │   │   │   │   │   ├── PersonalizationViewModel.kt
│   │   │   │   │   │   └── components/
│   │   │   │   │   │       ├── SportSelectionGrid.kt
│   │   │   │   │   │       ├── SkillLevelPicker.kt
│   │   │   │   │   │       └── LocationPicker.kt
│   │   │   │   │   │
│   │   │   │   │   ├── home/
│   │   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   │   ├── HomeViewModel.kt
│   │   │   │   │   │   └── components/
│   │   │   │   │   │       ├── GreetingHeader.kt
│   │   │   │   │   │       ├── RecommendationSection.kt
│   │   │   │   │   │       ├── QuickActionSection.kt
│   │   │   │   │   │       ├── UpcomingMatchCard.kt
│   │   │   │   │   │       └── CampaignCard.kt
│   │   │   │   │   │
│   │   │   │   │   ├── community/
│   │   │   │   │   │   ├── CommunityScreen.kt
│   │   │   │   │   │   ├── CommunityViewModel.kt
│   │   │   │   │   │   └── components/
│   │   │   │   │   │       ├── CommunityCard.kt
│   │   │   │   │   │       ├── CategoryTabs.kt
│   │   │   │   │   │       └── JoinButton.kt
│   │   │   │   │   │
│   │   │   │   │   ├── discover/
│   │   │   │   │   │   ├── DiscoverScreen.kt
│   │   │   │   │   │   ├── DiscoverViewModel.kt
│   │   │   │   │   │   └── components/
│   │   │   │   │   │       ├── SportCategoryNavbar.kt
│   │   │   │   │   │       ├── ModePicker.kt           # Casual/Competitive
│   │   │   │   │   │       ├── RoomCard.kt
│   │   │   │   │   │       └── CreateRoomFAB.kt
│   │   │   │   │   │
│   │   │   │   │   ├── room/
│   │   │   │   │   │   ├── CreateRoomScreen.kt
│   │   │   │   │   │   ├── CreateRoomViewModel.kt
│   │   │   │   │   │   ├── RoomDetailScreen.kt
│   │   │   │   │   │   ├── RoomDetailViewModel.kt
│   │   │   │   │   │   └── components/
│   │   │   │   │   │       ├── RoomForm.kt
│   │   │   │   │   │       ├── LocationPicker.kt
│   │   │   │   │   │       └── DateTimePicker.kt
│   │   │   │   │   │
│   │   │   │   │   ├── chat/
│   │   │   │   │   │   ├── ChatListScreen.kt
│   │   │   │   │   │   ├── ChatListViewModel.kt
│   │   │   │   │   │   ├── ChatRoomScreen.kt
│   │   │   │   │   │   ├── ChatRoomViewModel.kt
│   │   │   │   │   │   └── components/
│   │   │   │   │   │       ├── ChatItem.kt
│   │   │   │   │   │       ├── MessageBubble.kt
│   │   │   │   │   │       └── MessageInput.kt
│   │   │   │   │   │
│   │   │   │   │   ├── profile/
│   │   │   │   │   │   ├── ProfileScreen.kt
│   │   │   │   │   │   ├── ProfileViewModel.kt
│   │   │   │   │   │   ├── EditProfileScreen.kt
│   │   │   │   │   │   └── components/
│   │   │   │   │   │       ├── ProfileHeader.kt
│   │   │   │   │   │       ├── StatsSection.kt
│   │   │   │   │   │       ├── MatchHistoryList.kt
│   │   │   │   │   │       ├── BadgeDisplay.kt
│   │   │   │   │   │       └── AIInsightCard.kt
│   │   │   │   │   │
│   │   │   │   │   └── components/              # Shared Components
│   │   │   │   │       ├── SparINButton.kt
│   │   │   │   │       ├── SparINTextField.kt
│   │   │   │   │       ├── SparINCard.kt
│   │   │   │   │       ├── LoadingIndicator.kt
│   │   │   │   │       └── ErrorView.kt
│   │   │   │   │
│   │   │   │   └── util/                        # Utilities
│   │   │   │       ├── Constants.kt
│   │   │   │       ├── DateFormatter.kt
│   │   │   │       ├── PermissionHelper.kt
│   │   │   │       └── NotificationHelper.kt
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── mipmap/
│   │   │   │
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   └── test/                                # Unit Tests
│   │       └── java/com/sparin/
│   │           ├── domain/
│   │           │   └── usecase/
│   │           └── presentation/
│   │               └── viewmodel/
│   │
│   └── build.gradle.kts
│
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## 🔄 Logic Flow Documentation

### 1. **User Authentication Flow**
```
START
  ↓
OnboardingScreen (Intro Carousel)
  ↓
SignInScreen
  ↓
[Click Google Sign In]
  ↓
FirebaseAuthService.signInWithGoogle()
  ↓
AuthRepository.authenticateUser()
  ↓
Check: isNewUser?
  ├─ YES → PersonalizationScreen
  │         ↓
  │   Collect: Name, City, Gender, Age, Sports, Skill Level
  │         ↓
  │   UserRepository.createUserProfile()
  │         ↓
  │   Navigate to HomeScreen
  │
  └─ NO → Navigate to HomeScreen
```

### 2. **Home Screen Logic**
```
HomeScreen.onCreate()
  ↓
HomeViewModel.loadHomeData()
  ↓
PARALLEL CALLS:
  ├─ GetUserProfileUseCase() → Display Greeting
  ├─ GetRecommendationsUseCase() → Show Recommended Rooms
  ├─ GetUpcomingMatchesUseCase() → Display Schedule
  └─ CampaignRepository.getActiveCampaigns() → Show Events
  ↓
Render UI with collected data
  ↓
User Actions:
  ├─ Click Recommended Room → Navigate to RoomDetailScreen
  ├─ Click Quick Action → Navigate to ChatRoomScreen
  ├─ Click Upcoming Match → Navigate to RoomDetailScreen
  └─ Click Campaign → Navigate to CampaignDetailScreen
```

### 3. **Discover & Match Flow**
```
DiscoverScreen.onCreate()
  ↓
DiscoverViewModel.initialize()
  ↓
Load: SportCategory List (from Constants)
  ↓
User Selects:
  ├─ Sport Category (Badminton, Futsal, etc.)
  └─ Mode (Casual / Competitive)
  ↓
FilterRoomsByCategoryUseCase(category, mode)
  ↓
RoomRepository.getRoomsByFilter(category, mode, userLocation)
  ↓
Display: List of Available Rooms
  ↓
User Actions:
  ├─ Click Room Card → Navigate to RoomDetailScreen
  │                    ↓
  │              Show: Room Info, Members, Location
  │                    ↓
  │              [Join Room Button]
  │                    ↓
  │              JoinRoomUseCase()
  │                    ↓
  │              Check: Room Capacity
  │                    ├─ Available → Add User to Room
  │                    │              ↓
  │                    │        Create ChatRoom
  │                    │              ↓
  │                    │        Navigate to ChatRoomScreen
  │                    └─ Full → Show Error Toast
  │
  └─ Click FAB [Create Room] → Navigate to CreateRoomScreen
```

### 4. **Create Room Flow**
```
CreateRoomScreen
  ↓
User Fills Form:
  ├─ Room Name
  ├─ Sport Category
  ├─ Number of Players
  ├─ Location (Manual or Map Picker)
  ├─ Price (Optional)
  └─ Date & Time
  ↓
[Submit Button]
  ↓
CreateRoomViewModel.validateInput()
  ├─ Invalid → Show Error Messages
  └─ Valid → Proceed
  ↓
CreateRoomUseCase(roomData)
  ↓
RoomRepository.createRoom()
  ↓
Save to Firestore: /rooms/{roomId}
  ↓
Add Room Creator as First Member
  ↓
Navigate Back to DiscoverScreen
  ↓
Show Success Message
```

### 5. **Community Flow**
```
CommunityScreen.onCreate()
  ↓
CommunityViewModel.loadCommunities()
  ↓
GetCommunitiesUseCase()
  ↓
User Actions:
  ├─ Select Tab: All | Sport Category
  │     ↓
  │ FilterCommunitiesBySportUseCase(category)
  │     ↓
  │ Update Community List
  │
  └─ Click Community Card
        ↓
  Navigate to CommunityDetailScreen
        ↓
  Show: Name, Description, Members Count, Posts
        ↓
  [Join Community Button]
        ↓
  JoinCommunityUseCase(communityId, userId)
        ↓
  CommunityRepository.addMember()
        ↓
  Update UI: Show "Joined" Status
        ↓
  Enable Access to Community Chat
```

### 6. **Chat Flow**
```
ChatListScreen.onCreate()
  ↓
ChatListViewModel.loadChatRooms()
  ↓
GetChatRoomsUseCase(userId)
  ↓
ChatRepository.getUserChatRooms()
  ↓
Display: List of Chat Rooms
  ↓
User Clicks Chat Room
  ↓
Navigate to ChatRoomScreen(roomId)
  ↓
ChatRoomViewModel.initialize(roomId)
  ↓
ObserveChatMessagesUseCase(roomId)
  ↓
ChatRepository.observeMessages()
  ↓
Firestore Realtime Listener: /chats/{roomId}/messages
  ↓
Display Messages in Real-time
  ↓
User Types & Sends Message
  ↓
SendMessageUseCase(roomId, message)
  ↓
ChatRepository.sendMessage()
  ↓
Save to Firestore: /chats/{roomId}/messages/{messageId}
  ↓
Update UI with New Message
  ↓
Send Push Notification to Other Members
```

### 7. **Match & Statistics Update Flow**
```
After Match Completed (Manual Update)
  ↓
RoomDetailScreen → [Update Result Button]
  ↓
Navigate to UpdateMatchResultScreen
  ↓
User Inputs:
  ├─ Winner Selection
  ├─ Score (Optional)
  └─ Notes (Optional)
  ↓
[Submit Button]
  ↓
UpdateMatchResultUseCase(matchData)
  ↓
MatchRepository.updateMatch()
  ↓
Save to Firestore: /matches/{matchId}
  ↓
PARALLEL UPDATES:
  ├─ Update User Stats (Winrate, Total Games)
  │     ↓
  │ UserRepository.updateStats()
  │     ↓
  │ Calculate: New Winrate, Total Matches, Points
  │
  └─ Trigger AI Analysis
        ↓
  Cloud Function: analyzeUserProfile()
        ↓
  Generate Insights based on:
    ├─ Match History
    ├─ Winrate Trends
    ├─ Sport Categories
    └─ Play Frequency
        ↓
  Save to Firestore: /users/{userId}/aiInsights
        ↓
  Display on ProfileScreen
```

### 8. **Profile & AI Insights Flow**
```
ProfileScreen.onCreate()
  ↓
ProfileViewModel.loadProfile()
  ↓
PARALLEL CALLS:
  ├─ GetUserProfileUseCase() → Display User Info
  ├─ GetMatchHistoryUseCase() → Show Match History
  └─ AnalyzeProfileUseCase() → Get AI Insights
  ↓
Render Profile UI:
  ├─ Profile Photo
  ├─ Name, Bio
  ├─ Sport Interests
  ├─ Statistics Card:
  │   ├─ Winrate %
  │   ├─ Total Matches
  │   ├─ Rank/Badge
  │   └─ Points/Elo
  ├─ Match History List
  └─ AI Insights Card:
      ├─ "Your performance improved 15% this week"
      ├─ Recommended Rooms
      └─ Skill Development Tips
  ↓
User Actions:
  ├─ Edit Profile → Navigate to EditProfileScreen
  └─ View Match Detail → Navigate to MatchDetailScreen
```

### 9. **Campaign/Event Flow**
```
HomeScreen: Display Active Campaigns
  ↓
User Clicks Campaign Card
  ↓
Navigate to CampaignDetailScreen(campaignId)
  ↓
CampaignViewModel.loadCampaign(campaignId)
  ↓
CampaignRepository.getCampaignById()
  ↓
Display:
  ├─ Poster Image
  ├─ Event Title
  ├─ Description
  ├─ Date & Time
  ├─ Location
  ├─ Registration Fee
  └─ Organizer Info
  ↓
[Register Button]
  ↓
Check: User Already Registered?
  ├─ YES → Show "Already Registered"
  └─ NO → Proceed to Registration
              ↓
        CampaignRepository.registerUser()
              ↓
        Save to Firestore: /campaigns/{id}/participants
              ↓
        Show Success Message
              ↓
        Send Confirmation Email/Notification
```

---

## 🗂️ Firestore Database Structure

```
/users/{userId}
  ├─ uid: string
  ├─ name: string
  ├─ email: string
  ├─ city: string
  ├─ gender: string
  ├─ age: number
  ├─ sportInterests: [string]
  ├─ skillLevel: string
  ├─ profilePhoto: string
  ├─ bio: string
  ├─ stats:
  │   ├─ winrate: number
  │   ├─ totalMatches: number
  │   ├─ totalWins: number
  │   ├─ points: number
  │   └─ rank: string
  ├─ joinedCommunities: [string]
  ├─ subscription: string
  └─ createdAt: timestamp

/rooms/{roomId}
  ├─ name: string
  ├─ category: string
  ├─ mode: string (casual/competitive)
  ├─ location: geopoint
  ├─ locationName: string
  ├─ maxPlayers: number
  ├─ currentPlayers: number
  ├─ members: [userId]
  ├─ price: number
  ├─ dateTime: timestamp
  ├─ status: string (open/full/completed)
  ├─ createdBy: userId
  └─ createdAt: timestamp

/communities/{communityId}
  ├─ name: string
  ├─ sportCategory: string
  ├─ description: string
  ├─ memberCount: number
  ├─ members: [userId]
  ├─ admins: [userId]
  └─ createdAt: timestamp

/campaigns/{campaignId}
  ├─ title: string
  ├─ description: string
  ├─ posterUrl: string
  ├─ organizerId: userId
  ├─ dateTime: timestamp
  ├─ location: string
  ├─ registrationFee: number
  ├─ participants: [userId]
  └─ status: string (active/completed)

/matches/{matchId}
  ├─ roomId: string
  ├─ participants: [userId]
  ├─ winnerId: userId
  ├─ score: string
  ├─ category: string
  ├─ dateTime: timestamp
  └─ notes: string

/chats/{roomId}/messages/{messageId}
  ├─ senderId: userId
  ├─ senderName: string
  ├─ text: string
  ├─ timestamp: timestamp
  └─ type: string (text/system)

/users/{userId}/aiInsights
  ├─ lastAnalysis: timestamp
  ├─ insights: [string]
  ├─ recommendations: [roomId]
  └─ performanceTrend: string
```

---

## 🎯 Key Logic Patterns

### ViewModel Pattern
```kotlin
class HomeViewModel(
    private val getUserProfile: GetUserProfileUseCase,
    private val getRecommendations: GetRecommendationsUseCase,
    private val getUpcomingMatches: GetUpcomingMatchesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            
            when (val result = getUserProfile()) {
                is Resource.Success -> {
                    // Load other data
                    _uiState.value = HomeUiState.Success(result.data)
                }
                is Resource.Error -> {
                    _uiState.value = HomeUiState.Error(result.message)
                }
            }
        }
    }
}
```

### Repository Pattern
```kotlin
class RoomRepositoryImpl(
    private val firestore: FirestoreService
) : RoomRepository {
    
    override suspend fun createRoom(room: Room): Resource<String> {
        return try {
            val roomId = firestore.createDocument("rooms", room)
            Resource.Success(roomId)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }
    
    override fun observeRooms(category: String): Flow<List<Room>> {
        return firestore.observeCollection("rooms")
            .map { it.filter { room -> room.category == category } }
    }
}
```

### Use Case Pattern
```kotlin
class JoinRoomUseCase(
    private val roomRepository: RoomRepository,
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(roomId: String, userId: String): Resource<Unit> {
        return try {
            // Check room capacity
            val room = roomRepository.getRoom(roomId)
            if (room.currentPlayers >= room.maxPlayers) {
                return Resource.Error("Room is full")
            }
            
            // Add user to room
            roomRepository.addMember(roomId, userId)
            
            // Create or join chat
            chatRepository.createOrJoinChat(roomId, userId)
            
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to join room")
        }
    }
}
```

---

## 📱 Navigation Structure

```kotlin
sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object SignIn : Screen("sign_in")
    object Personalization : Screen("personalization")
    object Home : Screen("home")
    object Community : Screen("community")
    object Discover : Screen("discover")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
    object CreateRoom : Screen("create_room")
    object RoomDetail : Screen("room_detail/{roomId}")
    object ChatRoom : Screen("chat_room/{roomId}")
    object EditProfile : Screen("edit_profile")
    object CampaignDetail : Screen("campaign_detail/{campaignId}")
}
```

---

## 🔧 Dependencies (build.gradle.kts)

```kotlin
dependencies {
    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-functions-ktx")
    
    // Dependency Injection
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Location
    implementation("com.google.android.gms:play-services-location:21.0.1")
}
```

