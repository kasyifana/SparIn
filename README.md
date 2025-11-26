# SparIN - Sports Partner Matching App

<div align="center">

![SparIN Logo](https://via.placeholder.com/150x150/4CAF50/FFFFFF?text=SparIN)

**Temukan Partner Olahraga Anda dengan Mudah!**

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-orange.svg)](https://firebase.google.com/)

</div>

---

## 📱 About SparIN

SparIN adalah aplikasi mobile Android yang memudahkan para pecinta olahraga untuk:
- 🏸 Menemukan partner sparring
- 🏠 Membuat dan bergabung room olahraga
- 👥 Join komunitas olahraga
- 📊 Tracking statistik performa
- 🎯 Ikut campaign/event olahraga
- 💬 Chat dengan member room
- 🤖 Mendapatkan AI insights

---

## 🏗️ Tech Stack

### Frontend
- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material 3** - Design system
- **Navigation Compose** - Navigation

### Backend
- **Firebase Authentication** - Google Sign-In
- **Cloud Firestore** - Database
- **Firebase Storage** - File storage
- **Cloud Functions** - Serverless backend

### Architecture
- **Clean Architecture** - Separation of concerns
- **MVVM Pattern** - Presentation layer
- **Repository Pattern** - Data layer
- **Use Cases** - Business logic

### Dependencies
- **Koin** - Dependency injection
- **Coroutines & Flow** - Asynchronous programming
- **Coil** - Image loading
- **Google Maps** - Location services

---

## 📂 Project Structure

```
app/src/main/java/com/example/sparin/
├── SparINApplication.kt           # Application class
│
├── data/                          # Data Layer
│   ├── model/                     # Data models
│   ├── remote/                    # Firebase services
│   └── repository/                # Repository implementations
│
├── domain/                        # Business Logic
│   ├── usecase/                   # Use cases
│   └── util/                      # Utilities
│
├── presentation/                  # UI Layer
│   ├── MainActivity.kt
│   ├── navigation/                # Navigation setup
│   ├── auth/                      # Authentication screens
│   ├── home/                      # Home screen
│   ├── discover/                  # Discover rooms
│   ├── community/                 # Communities
│   ├── chat/                      # Chat features
│   └── profile/                   # User profile
│
├── di/                            # Dependency Injection
│   ├── AppModule.kt
│   ├── FirebaseModule.kt
│   └── RepositoryModule.kt
│
└── util/                          # Utilities
    └── Constants.kt
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 11+
- Android SDK 24+ (Android 7.0)
- Firebase project

### Installation

1. **Clone repository** (or open existing project)
   ```bash
   cd "/Users/user/Campuss/Semester 5/SparIn"
   ```

2. **Add Firebase configuration**
   
   Download `google-services.json` dari Firebase Console dan letakkan di:
   ```
   app/google-services.json
   ```

3. **Generate SHA-1 Fingerprint** (untuk Google Sign-In)
   ```bash
   ./gradlew signingReport
   ```
   
   Copy SHA-1 fingerprint dan tambahkan ke Firebase Console:
   - Project Settings → Your apps → SparIn
   - Add fingerprint

4. **Sync & Build**
   
   Di Android Studio:
   - File → Sync Project with Gradle Files
   - Build → Make Project

5. **Run**
   
   - Run → Run 'app'
   - Pilih emulator atau device

---

## 🎯 Features

### MVP Features (Current)

#### ✅ Implemented (Foundation)
- [x] Firebase Authentication (Google Sign-In)
- [x] User profile management
- [x] Room creation & management
- [x] Community system
- [x] Real-time chat
- [x] Match history tracking
- [x] Campaign/Event system
- [x] Navigation system
- [x] Bottom navigation

#### 🚧 In Progress
- [ ] Use Cases layer
- [ ] ViewModels
- [ ] Full UI implementation
- [ ] Google Maps integration
- [ ] AI profile analysis
- [ ] Image upload

### Supported Sports (24 Categories)

**Racket & Ball Sports:**
Badminton, Futsal, Basket, Voli, Tenis Meja, Tenis, Padel, Golf, Sepak Bola, Mini Soccer

**Endurance Sports:**
Jogging, Lari, Sepeda, Renang

**Strength & Combat:**
Gym, Boxing, Muay Thai, Taekwondo

**Recreation:**
Billiard, Catur, Hiking, Bowling

---

## 📊 Data Models

### User
```kotlin
data class User(
    val uid: String,
    val name: String,
    val email: String,
    val city: String,
    val sportInterests: List<String>,
    val skillLevel: String,
    val stats: UserStats,
    ...
)
```

### Room
```kotlin
data class Room(
    val id: String,
    val name: String,
    val category: String,
    val mode: String, // Casual/Competitive
    val location: GeoPoint,
    val maxPlayers: Int,
    val members: List<String>,
    ...
)
```

### Community
```kotlin
data class Community(
    val id: String,
    val name: String,
    val sportCategory: String,
    val members: List<String>,
    ...
)
```

---

## 🔐 Firebase Configuration

### Required Services

1. **Authentication**
   - Enable Google Sign-In
   - Add Web Client ID to app

2. **Firestore Database**
   - Region: `asia-southeast2` (Jakarta)
   - Collections:
     - `users`
     - `rooms`
     - `communities`
     - `campaigns`
     - `matches`
     - `chats/{roomId}/messages`

3. **Storage**
   - For profile photos
   - For campaign posters

4. **Functions** (Optional for MVP)
   - AI profile analysis
   - Notifications

---

## 🛠️ Development Roadmap

### Phase 1: Foundation ✅ (Current)
- [x] Project setup
- [x] Data layer
- [x] Repository layer
- [x] Dependency injection
- [x] Navigation

### Phase 2: Domain Layer
- [ ] Use cases implementation
- [ ] Business logic

### Phase 3: Presentation Layer
- [ ] ViewModels
- [ ] UI Components
- [ ] Screen implementations

### Phase 4: Features
- [ ] Google Maps integration
- [ ] Camera & image upload
- [ ] Real-time features
- [ ] AI recommendations

### Phase 5: Polish
- [ ] UI/UX improvements
- [ ] Performance optimization
- [ ] Testing
- [ ] Bug fixes

### Phase 6: Release
- [ ] App signing
- [ ] Play Store preparation
- [ ] Documentation
- [ ] Launch 🚀

---

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run with coverage
./gradlew testDebugUnitTest jacocoTestReport
```

---

## 📝 License

This project is private and proprietary.

---

## 👥 Team

Developed by [Your Team Name]

---

## 📞 Contact

For questions or support, contact: [your-email@example.com]

---

<div align="center">

**Made with ❤️ for Sports Enthusiasts**

</div>
