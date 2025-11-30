# Discover Feature - Complete Implementation (Part 1 & 2)

## 📋 Overview

Implementasi lengkap fitur Discover dengan 3 screens:
1. **Mode Selector Screen** - Pilih antara Casual dan Competitive
2. **Discover Casual Screen** - Fun, friendly, pastel design
3. **Discover Competitive Screen** - Intense, elite, neon red design ✨ NEW

## 🎯 Part 2: Discover Competitive Mode

### 🔥 Design Philosophy

- **High-energy** competitive atmosphere
- **Red + Dark tones** (Neon Red, Black, Gunmetal)
- **Futuristic** esports aesthetic
- **Sharp design** accents (NOT rounded like casual)
- **Neon red glow** effects
- **Elite/Serious** feel

### 🎨 Color Palette

```kotlin
Primary Colors:
🔴 NeonRed: #FF2D2D
🔴 MetallicRed: #DC143C
🔴 GlowRed: #FF4444

Dark Colors:
⚫ Gunmetal: #0F0F0F
⚫ DarkGraphite: #1A1A1A
⚫ Black: #0A0A0A
```

### 📐 Key Features

#### 1. **Competitive Mode Indicator Banner**
- Prominent top bar
- Red chromatic gradient background
- Glowing border (1.5dp)
- Micro glitch animation (subtle vibration)
- Text: "🔥 COMPETITIVE MODE ⚡"
- Extra bold, letter-spacing: 1.5sp

#### 2. **Dark Header**
- Title: "Competitive Rooms 🔥"
- Subtitle: "Find real challenges near you."
- White text on dark background
- Minimal but bold design

#### 3. **Sharp Sport Category Chips**
- **Sharp corners** (6dp radius, NOT 24dp like casual)
- **Selected state**:
  - Neon red border (2dp)
  - Red glow effect
  - Metallic texture
  - Lightning bolt ⚡ indicator
  - Extra bold text
- **Unselected state**:
  - Dark graphite background
  - Subtle white outline (0.2 alpha)
  - Medium weight text

#### 4. **Elite Competitive Match Cards**

**Card Design**:
- Dark background (#0F0F0F)
- Sharp corners (10dp)
- Red neon glow (animated pulsing)
- Metallic highlights
- Dynamic shadows

**Card Components**:
- **Sport Badge**:
  - 60dp size
  - Sharp corners (8dp)
  - Red gradient background
  - Neon border
  - Large emoji (30sp)

- **Title**: Extra bold, white, 18sp
- **Location + Distance**: Red pin icon
- **Difficulty Indicator**: 1-5 flame emojis 🔥
- **Tags**: "Ranked", "High Skill", "Serious Only", "Elite Only"
  - Sharp corners (6dp)
  - Red border + background
  - Bold text

- **Skill Level Requirement**:
  - Star icon
  - "Skill Level Required: Intermediate+"
  - Prominent display

- **Schedule**: Red highlight, bold
- **Player Count**: White text
- **Join Button**:
  - Red neon gradient
  - Glowing animation
  - "Join Match" text
  - Arrow icon
  - Sharp corners (8dp)

#### 5. **Hexagonal Create Room FAB**
- Top-right position
- Circular shape with hexagonal feel
- Black + red neon border (2dp)
- Rotating glow effect
- Plus icon in neon red
- 64dp size

### 🎭 Animations

#### Glitch Effect
- Mode indicator vibrates subtly
- 0-2dp offset
- 100ms cycle
- Linear easing

#### Neon Glow Pulsing
- Alpha: 0.3 → 0.7
- 1500ms cycle
- EaseInOutSine
- Applied to:
  - Selected chips
  - Room cards
  - Join buttons
  - FAB

#### Rotating Glow (FAB)
- 360° rotation
- 20000ms (20 seconds)
- Continuous
- Radial gradient

#### Background Red Spots
- 3 red glow circles
- Slow movement
- Heavy blur (120dp)
- Alpha animation: 0.1 → 0.25

### 📊 Sample Data

```kotlin
4 Competitive Rooms:

1. Badminton Ranked Match
   - Skill: Intermediate+
   - Difficulty: 4/5 🔥🔥🔥🔥
   - Tags: Ranked, High Skill
   
2. Futsal Tournament Qualifier
   - Skill: Advanced
   - Difficulty: 5/5 🔥🔥🔥🔥🔥
   - Tags: Tournament, Serious Only
   
3. Basketball Pro League
   - Skill: Pro
   - Difficulty: 5/5 🔥🔥🔥🔥🔥
   - Tags: Ranked, Elite Only
   
4. Muaythai Sparring Session
   - Skill: Intermediate+
   - Difficulty: 4/5 🔥🔥🔥🔥
   - Tags: Serious Only, High Skill
```

### 🎨 Design Specs

#### Typography
```kotlin
Mode Banner: 16sp, ExtraBold, 1.5sp letter-spacing
Header Title: 32sp, ExtraBold, -0.5sp letter-spacing
Card Title: 18sp, ExtraBold, 0.3sp letter-spacing
Tags: 12sp, Bold, 0.3sp letter-spacing
Body: 13-15sp, Medium/Bold
```

#### Spacing
```kotlin
Section Gaps: 20-24dp
Card Gaps: 16dp
Internal Padding: 20dp
Chip Spacing: 10dp
```

#### Shadows
```kotlin
Mode Banner: 16dp (red tint)
Cards: 16dp (red tint)
FAB: 16dp (red tint)
Chips (selected): 12dp (red tint)
Join Button: 8dp (red tint)
```

#### Border Radius
```kotlin
Mode Banner: 8dp (sharp)
Cards: 10dp (sharp)
Chips: 6dp (sharp)
Buttons: 8dp (sharp)
Sport Badge: 8dp (sharp)
Tags: 6dp (sharp)
```

#### Borders
```kotlin
Mode Banner: 1.5dp neon red
Cards: 1.5dp red gradient
Chips (selected): 2dp neon red
Chips (unselected): 1dp white (0.2 alpha)
Join Button: 1dp glow red
FAB: 2dp neon red
```

## 📊 Comparison: Casual vs Competitive

| Aspect | Casual 🎮 | Competitive 🔥 |
|--------|-----------|----------------|
| **Background** | Pastel gradients | Dark + red glow |
| **Primary Color** | Mint green | Neon red |
| **Corners** | Ultra-rounded (24-32dp) | Sharp (6-10dp) |
| **Tone** | Fun, playful | Intense, elite |
| **Tags** | "Chill", "Fun Only" | "Ranked", "Elite Only" |
| **Emoji** | 🎉 ✨ 🎮 | 🔥 ⚡ 🏆 |
| **Shadows** | Soft, subtle | Sharp, dramatic |
| **Glow** | Soft pastel | Neon red |
| **Text Weight** | Medium/SemiBold | Bold/ExtraBold |
| **Letter Spacing** | Normal | Increased |
| **Borders** | 1-2dp subtle | 1.5-2dp prominent |
| **Animations** | Gentle breathing | Pulsing, glitching |
| **FAB Position** | Bottom-right | Top-right |
| **FAB Shape** | Circle | Hexagonal feel |
| **Search Bar** | Pastel gradient | Dark + red border |
| **Difficulty** | Not shown | 1-5 flames 🔥 |
| **Skill Level** | Not prominent | Required level shown |

## 🎯 Visual Identity

### Casual Mode
- **Feeling**: "Let's have fun together!"
- **Target**: Casual players, beginners, social
- **Atmosphere**: Friendly, inviting, relaxed
- **Design**: Soft, airy, pastel, rounded

### Competitive Mode
- **Feeling**: "Prove yourself!"
- **Target**: Serious players, ranked, competitive
- **Atmosphere**: Intense, elite, challenging
- **Design**: Sharp, dark, neon, futuristic

## 🔄 Complete Navigation Flow

```
App Launch
    ↓
Navigate to Discover
    ↓
ModeSelectorScreen
    ↓
    ├─ (Tap Casual) → DiscoverCasualScreen
    │                     ↓
    │                 Browse fun matches
    │                     ↓
    │                 Filter by sport
    │                     ↓
    │                 Join casual room
    │
    └─ (Tap Competitive) → DiscoverCompetitiveScreen
                              ↓
                          Browse elite matches
                              ↓
                          Check skill requirements
                              ↓
                          Join competitive room
```

## 📁 Complete File Structure

```
presentation/discover/
├── ModeSelectorScreen.kt           (Part 1)
├── DiscoverCasualScreen.kt         (Part 1)
├── DiscoverCompetitiveScreen.kt    (Part 2) ✨ NEW
├── DiscoverScreen.kt               (Old - can be removed)
├── DiscoverViewModel.kt            (Empty - TBD)
├── DISCOVER_IMPLEMENTATION.md      (This file)
└── components/
    ├── SportCategoryNavbar.kt
    ├── RoomCard.kt
    ├── ModePicker.kt
    └── CreateRoomFAB.kt
```

## 🚀 Integration Steps

### 1. Update NavGraph
```kotlin
// Add routes
composable("mode_selector") {
    ModeSelectorScreen(
        navController = navController,
        onModeSelected = { mode ->
            when (mode) {
                RoomMode.CASUAL -> navController.navigate("discover_casual")
                RoomMode.COMPETITIVE -> navController.navigate("discover_competitive")
            }
        }
    )
}

composable("discover_casual") {
    DiscoverCasualScreen(navController = navController)
}

composable("discover_competitive") {
    DiscoverCompetitiveScreen(navController = navController)
}
```

### 2. Update Bottom Navigation
```kotlin
// Change Discover nav item to navigate to mode_selector
BottomNavItem(
    route = "mode_selector",
    icon = Icons.Rounded.Explore,
    label = "Discover"
)
```

### 3. Add ViewModel (Optional)
```kotlin
class DiscoverViewModel : ViewModel() {
    private val _selectedMode = MutableStateFlow(RoomMode.CASUAL)
    val selectedMode = _selectedMode.asStateFlow()
    
    private val _rooms = MutableStateFlow<List<RoomItem>>(emptyList())
    val rooms = _rooms.asStateFlow()
    
    fun setMode(mode: RoomMode) {
        _selectedMode.value = mode
        loadRooms(mode)
    }
    
    private fun loadRooms(mode: RoomMode) {
        // Load from repository
    }
}
```

## ✨ Premium Features Summary

### Casual Mode
✅ Pastel gradients  
✅ Soft neumorphism  
✅ Floating animations  
✅ Sparkle effects  
✅ Glassmorphism  
✅ Breathing animations  
✅ Ultra-rounded corners  
✅ Mint green accents  

### Competitive Mode
✅ Neon red glow  
✅ Dark theme  
✅ Sharp edges  
✅ Glitch effects  
✅ Metallic textures  
✅ Pulsing animations  
✅ Difficulty indicators  
✅ Skill requirements  
✅ Elite atmosphere  
✅ Esports aesthetic  

## 🎯 Next Steps

### Phase 3: Room Details & Actions
1. **Room Detail Screen**
   - Full room information
   - Member list with avatars
   - Map location
   - Chat preview
   - Join/Leave functionality
   - Share room

2. **Create Room Screen**
   - Sport selection
   - Mode selection (Casual/Competitive)
   - Date/time picker
   - Location picker with map
   - Player limit
   - Skill level requirement (for competitive)
   - Price (optional)

3. **Filter & Search**
   - Advanced filters
   - Location radius
   - Skill level filter
   - Date range
   - Price range
   - Sort options

### Phase 4: Backend Integration
1. **Firebase/Firestore**
   - Room collection
   - Real-time updates
   - User matching
   - Distance calculation

2. **State Management**
   - ViewModel implementation
   - Repository pattern
   - Use cases

3. **Location Services**
   - GPS integration
   - Distance calculation
   - Map integration

## 📱 User Experience Flow

### Casual Mode Journey
1. User selects "Casual" mode
2. Sees fun, colorful interface
3. Browses sport categories
4. Filters by sport
5. Reads tags: "Chill", "Fun Only"
6. Checks player count
7. Taps "Join" button
8. Joins casual match

### Competitive Mode Journey
1. User selects "Competitive" mode
2. Sees intense, dark interface
3. Checks skill requirements
4. Browses elite matches
5. Reads tags: "Ranked", "Elite Only"
6. Sees difficulty level (flames)
7. Verifies skill level requirement
8. Taps "Join Match" button
9. Joins competitive match

## 🎨 Design Consistency

Both modes maintain SparIN design system:
- ✅ Premium quality
- ✅ Gen-Z aesthetic
- ✅ Smooth animations
- ✅ Attention to detail
- ✅ Consistent spacing
- ✅ Typography hierarchy
- ✅ Icon usage
- ✅ Shadow depth

But with **completely different tone**:
- Casual: Soft, friendly, inviting
- Competitive: Sharp, intense, elite

---

**Created**: 2025-11-30  
**Version**: 2.0 (Complete)  
**Status**: ✅ Ready for Integration  
**Screens**: 3/3 Complete  
**Next**: Backend Integration & Room Details
