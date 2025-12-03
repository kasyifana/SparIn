package com.example.sparin.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * SparIN Light Color Scheme
 * Based on colorpalette.md - Light Mode section
 */
private val LightColorScheme = lightColorScheme(
    // Primary colors - Crunch (SparIN Gold)
    primary = Crunch,                              // #F3BA60
    onPrimary = Color.White,
    primaryContainer = LightColors.CtaPrimaryHover,// #F5C678
    onPrimaryContainer = LightColors.TextPrimary,  // #202022
    
    // Secondary colors - Chinese Silver (Playful accent)
    secondary = ChineseSilver,                     // #E0DBF3
    onSecondary = LightColors.TextPrimary,         // #202022
    secondaryContainer = LightColors.SurfaceVariant, // #F7F4FF
    onSecondaryContainer = LightColors.TextPrimary,
    
    // Tertiary colors - Vibrant Purple (Premium)
    tertiary = VibrantPurple,                      // #8B5CF6
    onTertiary = Color.White,
    tertiaryContainer = LightColors.SurfaceVariant,
    onTertiaryContainer = LightColors.TextPrimary,
    
    // Background colors
    background = LightColors.BgPrimary,            // #FFFFFF
    onBackground = LightColors.TextPrimary,        // #202022
    
    // Surface colors
    surface = LightColors.SurfaceDefault,          // #FFFFFF
    onSurface = LightColors.TextPrimary,           // #202022
    surfaceVariant = LightColors.SurfaceVariant,   // #F7F4FF
    onSurfaceVariant = LightColors.TextSecondary,  // #736A6A
    
    // Error colors
    error = LightColors.StatusDanger,              // #FF6B6B
    onError = Color.White,
    errorContainer = LightColors.StatusDangerBg,   // #FFEBEB
    onErrorContainer = LightColors.StatusDanger,
    
    // Border/Outline colors
    outline = LightColors.BorderMedium,            // #C7C7C7
    outlineVariant = LightColors.BorderLight,      // #E4E4E4
    
    // Overlay/Scrim
    scrim = Color.Black.copy(alpha = 0.05f),
    
    // Additional surface tones
    surfaceTint = Crunch,
    inverseSurface = Lead,                         // #202022
    inverseOnSurface = Color.White,
    inversePrimary = LightColors.CtaPrimaryHover
)

/**
 * SparIN Dark Color Scheme
 * Based on colorpalette.md - Dark Mode section
 */
private val DarkColorScheme = darkColorScheme(
    // Primary colors - Crunch (SparIN Gold) - stays vibrant in dark mode
    primary = Crunch,                              // #F3BA60
    onPrimary = Color.White,
    primaryContainer = DarkColors.CtaPrimaryPressed,// #D89F4B
    onPrimaryContainer = Color.White,
    
    // Secondary colors - Dreamland (Soft neutral)
    secondary = Dreamland,                         // #B6B1C0
    onSecondary = DarkColors.TextInverse,          // #202022
    secondaryContainer = DarkColors.SurfaceVariant,// #393943
    onSecondaryContainer = DarkColors.TextPrimary, // #FFFFFF
    
    // Tertiary colors - Vibrant Purple (Premium in dark mode)
    tertiary = DarkColors.CtaPurple,               // #8B5CF6
    onTertiary = Color.White,
    tertiaryContainer = DarkColors.SurfacePurple,  // #5B3A8F
    onTertiaryContainer = Color.White,
    
    // Background colors
    background = DarkColors.BgPrimary,             // #202022
    onBackground = DarkColors.TextPrimary,         // #FFFFFF
    
    // Surface colors
    surface = DarkColors.SurfaceDefault,           // #2A2A2D
    onSurface = DarkColors.TextPrimary,            // #FFFFFF
    surfaceVariant = DarkColors.SurfaceVariant,    // #393943
    onSurfaceVariant = DarkColors.TextSecondary,   // #B6B1C0
    
    // Error colors
    error = DarkColors.StatusDanger,               // #FF6B6B
    onError = Color.White,
    errorContainer = DarkColors.StatusDangerBg,    // #3D1A1A
    onErrorContainer = DarkColors.StatusDanger,
    
    // Border/Outline colors
    outline = DarkColors.BorderMedium,             // #525257
    outlineVariant = DarkColors.BorderLight,       // #3A3A3D
    
    // Overlay/Scrim
    scrim = Color.Black.copy(alpha = 0.6f),
    
    // Additional surface tones
    surfaceTint = Crunch,
    inverseSurface = CascadingWhite,               // #F6F6F6
    inverseOnSurface = Lead,                       // #202022
    inversePrimary = DarkColors.CtaPrimaryPressed
)

/**
 * SparIN Theme Composable
 * 
 * Provides theming for the entire SparIN app with support for:
 * - Light Mode and Dark Mode
 * - Dynamic color support (Android 12+)
 * - Custom color palette from colorpalette.md
 * 
 * @param darkTheme Whether to use dark theme (defaults to system preference)
 * @param dynamicColor Whether to use dynamic colors on Android 12+ (defaults to false to use our custom palette)
 * @param content The content to apply the theme to
 */
@Composable
fun SparInTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color disabled by default to use our custom SparIN palette
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}