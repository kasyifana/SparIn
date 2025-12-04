# Logout Feature Implementation

## Overview
Fitur logout telah ditambahkan ke halaman profil dengan desain premium dan Gen-Z aesthetic yang playful namun tetap profesional.

## Components

### 1. LogoutSection.kt
Komponen utama yang menampilkan section logout dengan fitur-fitur berikut:

#### Features:
- **Premium Logout Button**
  - Glassmorphic design dengan gradient background
  - Animated glow effect yang smooth
  - Icon exit dalam circular container
  - Text "Logout" dengan subtitle "See you next game! 👋"
  - Hover dan press states yang responsive

- **Account Action Buttons**
  - Settings button (cyan accent)
  - About button (neon accent)
  - Compact grid layout untuk akses cepat

- **Confirmation Dialog**
  - Full-screen modal dengan backdrop blur
  - Animated emoji (👋) dengan scale animation
  - Clear messaging: "Leaving Already?"
  - Descriptive text tentang data safety
  - Two action buttons:
    - "Stay" button (secondary style)
    - "Logout" button (danger style dengan icon)
  - Smooth enter/exit animations dengan spring physics
  - Click outside to dismiss

#### Design Details:
- **Colors**: 
  - Danger red untuk logout actions
  - Cyan untuk settings
  - Neon yellow untuk about
  - Gradients dan glassmorphism effects
  
- **Animations**:
  - Infinite pulse glow pada logout button
  - Scale dan rotation animations pada dialog
  - Smooth spring transitions
  - Icon pulse animation

- **Spacing & Layout**:
  - Section header dengan proper spacing
  - Card-based layout dengan 20dp border radius
  - Proper padding (20dp internal, 24dp external)
  - Bottom space untuk navigation bar (100dp)

## ViewModel Integration

### ProfileViewModel.kt
Added new state and function:

```kotlin
// State
private val _isLoggingOut = MutableStateFlow(false)
val isLoggingOut: StateFlow<Boolean> = _isLoggingOut.asStateFlow()

// Logout function
fun logout(onLogoutComplete: () -> Unit)
```

#### Logout Flow:
1. Set loading state (`_isLoggingOut = true`)
2. Call `userRepository.signOut()`
3. Optional cache clearing
4. Small delay untuk smooth UX (500ms)
5. Reset loading state
6. Execute callback untuk navigation
7. Error handling dengan fallback navigation

## Repository Integration

### UserRepository.kt
Added `signOut()` function:

```kotlin
suspend fun signOut(): Resource<Unit> {
    return try {
        authService.signOut()
        Log.d(TAG, "User signed out successfully")
        Resource.Success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "Failed to sign out: ${e.message}", e)
        Resource.Error(e.message ?: "Failed to sign out")
    }
}
```

## Screen Integration

### ProfileScreen.kt
Integrated LogoutSection into ProfileContent:

```kotlin
// In ProfileContent
LogoutSection(
    onLogoutClick = {
        viewModel.logout {
            // Navigate to sign in screen and clear back stack
            navController.navigate(Screen.SignIn.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
)
```

#### Navigation:
- Navigate to `Screen.SignIn.route` on logout
- Clear entire back stack (`popUpTo(0)`)
- Prevent back navigation ke profile setelah logout

## User Flow

1. **User clicks Logout button** → Confirmation dialog appears
2. **User confirms (clicks "Logout")** → Dialog animates out
3. **ViewModel processes logout** → Calls repository signOut
4. **Repository signs out** → Calls Firebase Auth signOut
5. **Navigation triggered** → Clear stack dan navigate to sign in
6. **User sees sign in screen** → Cannot go back to logged in state

## Troubleshooting

### Issue: App force closes on logout
**Solution**: 
- Pastikan menggunakan `Screen.SignIn.route` bukan `"auth"`
- Route "auth" tidak terdaftar di NavGraph, yang benar adalah `Screen.SignIn.route`
- Check NavGraph.kt untuk memastikan route SignIn sudah terdaftar

### Issue: Navigation tidak berfungsi
**Solution**:
- Pastikan NavController masih valid saat logout dipanggil
- Gunakan `popUpTo(0) { inclusive = true }` untuk clear semua back stack
- Pastikan Screen object sudah di-import di ProfileScreen.kt

### Issue: User bisa back ke profile screen
**Solution**:
- Pastikan menggunakan `popUpTo(0) { inclusive = true }`
- Ini akan clear seluruh navigation back stack

## Design Philosophy

### Gen-Z Premium Aesthetic:
- ✅ Playful emoji dan friendly copy
- ✅ Smooth animations dan micro-interactions
- ✅ Glassmorphism dan gradient effects
- ✅ Bold colors dengan proper contrast
- ✅ Modern rounded corners
- ✅ Contextual shadows dan glows

### User Experience:
- ✅ Clear confirmation sebelum destructive action
- ✅ Informative messaging (data safety assurance)
- ✅ Easy cancellation (click outside, "Stay" button)
- ✅ Visual feedback untuk semua interactions
- ✅ Smooth transitions tanpa jarring cuts
- ✅ Accessible button sizes dan spacing

## Color Palette Used

```kotlin
// Primary
DarkColors.StatusDanger      // Main logout color
Lead                         // Primary text
WarmHaze                     // Secondary text

// Accents
DarkColors.SurfaceCyan       // Settings button
DarkColors.SurfaceNeon       // About button
Crunch                       // Decorative elements

// Backgrounds
CascadingWhite               // Dialog background
ChineseSilver                // Secondary surfaces
```

## Animations

### Logout Button:
- Glow pulse: 2000ms easeInOutSine, reverse loop
- Alpha range: 0.15 - 0.35

### Dialog:
- Scale: 0.8 - 1.0 dengan spring physics
- Rotation: 0 - 15deg on exit
- Duration: 300ms

### Icon Animation:
- Scale pulse: 0.95 - 1.05
- Duration: 1200ms easeInOutSine

## Accessibility

- ✅ Clear content descriptions untuk icons
- ✅ Sufficient touch target sizes (48dp minimum)
- ✅ Proper color contrast ratios
- ✅ Descriptive labels untuk all interactive elements
- ✅ Keyboard navigation support (dialog dismissal)

## Testing Recommendations

1. **Functional Testing**:
   - Verify logout button shows dialog
   - Verify "Stay" button dismisses dialog
   - Verify "Logout" button navigates to auth
   - Verify back stack is cleared
   - Verify cannot navigate back after logout

2. **UI Testing**:
   - Verify animations play smoothly
   - Verify dialog appears centered
   - Verify backdrop blur works
   - Verify colors match design system
   - Verify spacing is consistent

3. **Error Handling**:
   - Test logout dengan network offline
   - Test logout dengan auth service error
   - Verify error still navigates to auth

## Future Enhancements

Possible improvements:
- [ ] Add loading spinner during logout process
- [ ] Add toast notification "Logged out successfully"
- [ ] Add option to "Remember me" untuk quick login
- [ ] Add analytics tracking untuk logout events
- [ ] Add haptic feedback pada button press
- [ ] Add sound effects (optional)
- [ ] Add logout reason selection (optional feedback)

## Notes

- Logout adalah destructive action, hence the red color dan confirmation
- Copy writing disesuaikan dengan tone SparIN (friendly, sporty)
- Animations dijaga tetap subtle untuk performa
- Component fully reusable dan customizable
- Mengikuti SparIN design system secara konsisten
