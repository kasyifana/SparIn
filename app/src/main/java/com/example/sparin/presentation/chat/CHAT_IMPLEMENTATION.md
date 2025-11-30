# Chat Room Implementation - SparIN

## 📁 File Structure

```
presentation/chat/
├── ChatListScreen.kt           # List of chat rooms (existing)
├── ChatRoomScreen.kt           # Main chat room screen (NEW)
├── ChatRoomViewModel.kt        # ViewModel for chat state (NEW)
└── components/
    ├── ChatItem.kt             # Chat list item component (NEW)
    ├── MessageBubble.kt        # Message bubble component (NEW)
    └── MessageInput.kt         # Message input bar component (NEW)
```

## 🎨 Design System Implementation

### Color Palette (SparIN Brand)
- **Cascading White** (#F6F6F6) - Main background
- **Chinese Silver** (#E0DBF3) - TopBar background, soft accents
- **Crunch** (#F3BA60) - Send button, current user message bubbles
- **Dreamland** (#B6B1C0) - Shadows and subtle outlines
- **Warm Haze** (#736A6A) - Body text, timestamps
- **Lead** (#202022) - Titles, strong text

### Visual Style
- ✅ Modern, sporty, Gen-Z friendly aesthetic
- ✅ Rounded corners (20-28dp)
- ✅ Soft pastel gradients for backgrounds
- ✅ Minimal sporty icons (send, emoji, back)
- ✅ Playful but professional message bubbles

## 📱 Components Overview

### 1. ChatRoomScreen.kt
Main screen that displays the chat room interface.

**Features:**
- TopAppBar with room name and participant count
- Auto-scrolling message list
- Message input bar at the bottom
- Loading and error states
- MVVM architecture with ViewModel integration

**Layout Structure:**
```
Column {
    TopAppBar()          // Chinese Silver background
    MessagesList()       // Auto-scroll, reverse layout
    MessageInputBar()    // Floating card design
}
```

### 2. ChatRoomViewModel.kt
Manages chat room state following MVVM pattern.

**State Management:**
- `ChatRoomUiState` - Sealed class (Loading, Success, Error)
- `inputText` - Current message input
- `messages` - List of chat messages
- `roomName` - Chat room name
- `participantsCount` - Number of participants

**Functions:**
- `loadChatRoom()` - Load chat data (mock data for now)
- `updateInputText(text)` - Update input field
- `sendMessage()` - Send new message
- `retry()` - Retry loading on error

### 3. MessageBubble.kt
Individual message bubble component.

**Design Rules:**
- **Current user messages:**
  - Aligned RIGHT
  - Background: Crunch (#F3BA60)
  - Text: Lead (#202022)
  - Rounded corners: top-left 22dp, top-right 6dp, bottom 22dp
  
- **Other user messages:**
  - Aligned LEFT
  - Background: Cascading White (#F6F6F6)
  - Text: Warm Haze (#736A6A)
  - Shadow using Dreamland
  - Rounded corners: top-left 6dp, top-right 22dp, bottom 22dp
  - Shows sender name above bubble

**Features:**
- Timestamp formatting (Just now, Xm ago, HH:mm, MMM dd HH:mm)
- Max width constraint (280dp)
- Soft shadows for depth

### 4. MessageInput.kt
Message input bar at the bottom of the screen.

**Design:**
- Floating card with rounded top corners (24dp)
- Background: Cascading White (#F6F6F6)
- Soft shadow using Dreamland

**Components:**
- Emoji button (left) - Warm Haze color
- Text input field (center) - White background, rounded 20dp
- Send button (right) - Crunch color when active, disabled when empty

**Features:**
- Placeholder text: "Type a message..."
- Dynamic send button (highlights when text is entered)
- Cursor color: Crunch

### 5. ChatItem.kt
Chat room item for the chat list screen.

**Design:**
- Rounded card (20dp) with shadow
- Sport emoji as avatar (56dp circle)
- Room name in Lead color (bold)
- Last message preview in Warm Haze
- Timestamp on the right
- Unread badge in Crunch color (if unreadCount > 0)

## 🔄 Data Flow

### Message Sending Flow
```
User types message
    ↓
updateInputText(text) called
    ↓
User clicks send button
    ↓
sendMessage() called
    ↓
Create new ChatMessage object
    ↓
Add to messages list
    ↓
Update UI state
    ↓
Clear input field
    ↓
Auto-scroll to bottom
```

### Real-time Message Flow (Future Implementation)
```
ChatRoomScreen.onCreate()
    ↓
ChatRoomViewModel.loadChatRoom()
    ↓
ObserveChatMessagesUseCase(roomId)
    ↓
ChatRepository.observeMessages()
    ↓
Firestore Realtime Listener: /chats/{roomId}/messages
    ↓
Display Messages in Real-time
    ↓
Auto-scroll to bottom on new message
```

## 📊 Data Models

### ChatMessage
```kotlin
data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: Long,
    val isCurrentUser: Boolean,
    val senderAvatar: String? = null
)
```

### ChatRoomUiState
```kotlin
sealed class ChatRoomUiState {
    object Loading : ChatRoomUiState()
    data class Success(
        val roomName: String,
        val participantsCount: Int,
        val messages: List<ChatMessage>
    ) : ChatRoomUiState()
    data class Error(val message: String) : ChatRoomUiState()
}
```

## 🎯 Key Features Implemented

✅ **MVVM Architecture** - Clean separation of concerns
✅ **State Management** - Using StateFlow for reactive UI
✅ **Auto-scroll** - Messages list auto-scrolls to bottom
✅ **Loading States** - Loading, Success, Error states
✅ **Premium Design** - Following SparIN design system
✅ **Responsive UI** - Proper padding and spacing
✅ **Preview Functions** - Multiple preview composables for development
✅ **Accessibility** - Content descriptions for icons
✅ **Type Safety** - Sealed classes for state management

## 🚀 Future Enhancements (TODO)

- [ ] Integrate with Firebase Firestore for real-time messaging
- [ ] Implement emoji picker
- [ ] Add image/file sharing
- [ ] Add typing indicators
- [ ] Add read receipts
- [ ] Add message reactions
- [ ] Add voice messages
- [ ] Add message search
- [ ] Add message deletion
- [ ] Add user blocking
- [ ] Add push notifications

## 📝 Usage Example

### Navigation to ChatRoomScreen
```kotlin
// In NavGraph.kt
composable(
    route = "chat_room/{roomId}",
    arguments = listOf(navArgument("roomId") { type = NavType.StringType })
) { backStackEntry ->
    val roomId = backStackEntry.arguments?.getString("roomId")
    ChatRoomScreen(
        navController = navController,
        roomId = roomId
    )
}

// Navigate to chat room
navController.navigate("chat_room/$roomId")
```

### Using ChatItem in ChatListScreen
```kotlin
LazyColumn {
    items(chatRooms) { chatRoom ->
        ChatItem(
            roomName = chatRoom.name,
            lastMessage = chatRoom.lastMessage,
            lastMessageTime = chatRoom.lastMessageTime,
            sportEmoji = chatRoom.sportEmoji,
            unreadCount = chatRoom.unreadCount,
            onClick = {
                navController.navigate("chat_room/${chatRoom.id}")
            }
        )
    }
}
```

## 🎨 Design Highlights

1. **Asymmetric Bubble Corners** - Creates a modern chat app feel
2. **Soft Shadows** - Adds depth without being too heavy
3. **Color Contrast** - Current user vs other users clearly differentiated
4. **Floating Input Bar** - Modern, app-like experience
5. **Dynamic Send Button** - Visual feedback when message is ready to send
6. **Smooth Animations** - Auto-scroll with animation for better UX

## ✨ Premium Gen-Z Experience

- Vibrant Crunch color for CTAs
- Soft pastel backgrounds
- Playful emoji support
- Clean, modern typography
- Smooth interactions
- Professional yet fun aesthetic

---

**Implementation Status:** ✅ Complete
**Design System Compliance:** ✅ 100%
**Architecture Pattern:** ✅ MVVM
**Preview Support:** ✅ Full coverage
