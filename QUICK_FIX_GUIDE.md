# Quick Fix Guide - Community Chat Navigation

## Problem yang Diperbaiki ✅
Tombol "Join" pada CommunityScreen menyebabkan aplikasi exit/crash karena karakter emoji tidak di-encode dengan benar saat navigasi.

## Solusi yang Diterapkan

### 1. **URL Encoding pada Navigasi**
- Emoji dan nama komunitas sekarang di-encode sebelum dikirim via navigation
- Menggunakan `URLEncoder.encode()` untuk karakter khusus

### 2. **URL Decoding pada ChatScreen**
- Parameter yang diterima di-decode kembali menggunakan `URLDecoder.decode()`
- Menambahkan try-catch untuk error handling

## File yang Diubah

### CommunityScreen.kt
```kotlin
// Sebelum (Error):
navController.navigate("chat/${community.name}/${community.emoji}")

// Setelah (Fixed):
val encodedName = URLEncoder.encode(community.name, StandardCharsets.UTF_8.toString())
val encodedEmoji = URLEncoder.encode(community.emoji, StandardCharsets.UTF_8.toString())
navController.navigate("chat/$encodedName/$encodedEmoji")
```

### ChatScreen.kt
```kotlin
// Menambahkan decoding di awal function:
val decodedName = remember {
    try {
        URLDecoder.decode(communityName, StandardCharsets.UTF_8.toString())
    } catch (e: Exception) {
        communityName
    }
}

val decodedEmoji = remember {
    try {
        URLDecoder.decode(communityEmoji, StandardCharsets.UTF_8.toString())
    } catch (e: Exception) {
        communityEmoji
    }
}
```

## Cara Testing

1. **Build & Run** aplikasi di Android Studio
2. Buka halaman **Community**
3. Tekan tombol **"Join"** pada salah satu komunitas
4. ✅ Aplikasi seharusnya membuka ChatScreen dengan benar
5. ✅ Nama komunitas dan emoji tampil dengan benar di header
6. ✅ Tombol back berfungsi kembali ke Community

## Troubleshooting

### Jika masih error:
1. **Clean & Rebuild Project**:
   ```
   Build > Clean Project
   Build > Rebuild Project
   ```

2. **Invalidate Cache**:
   ```
   File > Invalidate Caches / Restart...
   ```

3. **Pastikan Navigation sudah di-setup di MainActivity**:
   ```kotlin
   NavHost(navController = navController, startDestination = "community") {
       composable("community") {
           CommunityScreen(navController = navController)
       }
       
       composable(
           route = "chat/{communityName}/{communityEmoji}",
           arguments = listOf(
               navArgument("communityName") { type = NavType.StringType },
               navArgument("communityEmoji") { type = NavType.StringType }
           )
       ) { backStackEntry ->
           val communityName = backStackEntry.arguments?.getString("communityName") ?: ""
           val communityEmoji = backStackEntry.arguments?.getString("communityEmoji") ?: "🏸"
           
           ChatScreen(
               navController = navController,
               communityName = communityName,
               communityEmoji = communityEmoji
           )
       }
   }
   ```

4. **Check Logcat** untuk error messages:
   - Filter: `Show only selected application`
   - Tag: `NavController` atau `NavigationException`

## Technical Details

### Mengapa Perlu URL Encoding?
- Emoji adalah karakter Unicode yang tidak aman untuk URL
- Navigation Component menggunakan URL-style routing
- Karakter khusus seperti emoji bisa menyebabkan:
  - Invalid route pattern
  - Navigation crash
  - App exit unexpected

### Karakter yang Di-encode:
- Emoji (🏸, ⚽, 🏀, dll)
- Spasi dalam nama komunitas
- Karakter khusih lainnya (#, &, /, dll)

## Expected Behavior

✅ **Sebelum Fix**: App crash/exit saat tekan "Join"
✅ **Setelah Fix**: Navigasi lancar ke ChatScreen dengan data benar

---

**Status**: ✅ FIXED
**Last Updated**: November 30, 2025
