package com.example.sparin.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================
// LEGACY COLORS (Compatibility)
// ============================================
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// ============================================
// BRAND COLORS (Global - Theme Independent)
// ============================================

// Primary Brand Colors
val Crunch = Color(0xFFF3BA60)            // Primary CTA, tombol penting, highlight aksi
val ChineseSilver = Color(0xFFE0DBF3)     // Background playful, accent card, decorative
val Dreamland = Color(0xFFB6B1C0)         // Border, secondary text, badge outline

// Neutral Colors
val Lead = Color(0xFF202022)              // Text & icon di light mode, BG di dark mode
val CascadingWhite = Color(0xFFF6F6F6)    // Background di light mode, surface
val WarmHaze = Color(0xFF736A6A)          // Subtitle, supporting info, disabled state

// Accent Colors (Inspired by Design)
val SportyCyan = Color(0xFF6FEDD6)        // Cards, badges, playful elements
val NeonLime = Color(0xFFC8FF00)          // High-energy CTA, focus states
val VibrantPurple = Color(0xFF8B5CF6)     // Premium features, highlights
val SoftPeach = Color(0xFFFFD4B8)         // Warm accents, secondary cards

// ============================================
// LIGHT MODE COLORS
// ============================================
object LightColors {
    // Backgrounds
    val BgPrimary = Color(0xFFFFFFFF)
    val BgSecondary = Color(0xFFF6F6F6)
    val BgTertiary = Color(0xFFE0DBF3)
    val BgAccentCyan = Color(0xFFE5FAF6)
    val BgAccentPeach = Color(0xFFFFF5EE)
    
    // Surfaces
    val SurfaceDefault = Color(0xFFFFFFFF)
    val SurfaceElevated = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFF7F4FF)
    val SurfaceAccent = Color(0xFFE0DBF3)
    val SurfaceCyan = Color(0xFF6FEDD6)
    val SurfaceNeon = Color(0xFFC8FF00)
    val SurfacePeach = Color(0xFFFFD4B8)
    
    // Text
    val TextPrimary = Color(0xFF202022)
    val TextSecondary = Color(0xFF736A6A)
    val TextTertiary = Color(0xFFA8A4AD)
    val TextInverse = Color(0xFFFFFFFF)
    val TextOnCyan = Color(0xFF0A4A3F)
    val TextOnNeon = Color(0xFF2D3A00)
    val TextLink = Color(0xFF8B5CF6)
    
    // CTA (Call To Action)
    val CtaPrimary = Color(0xFFF3BA60)
    val CtaPrimaryHover = Color(0xFFF5C678)
    val CtaPrimaryPressed = Color(0xFFD89F4B)
    val CtaPrimaryDisabled = Color(0xFFF9E4C1)
    val CtaSecondary = Color(0xFFE0DBF3)
    val CtaSecondaryHover = Color(0xFFE8E4F7)
    val CtaSecondaryPressed = Color(0xFFC6C0DD)
    val CtaNeon = Color(0xFFC8FF00)
    val CtaNeonPressed = Color(0xFFA3D400)
    
    // Borders
    val BorderLight = Color(0xFFE4E4E4)
    val BorderMedium = Color(0xFFC7C7C7)
    val BorderDark = Color(0xFFA8A4AD)
    val BorderAccent = Color(0xFFB6B1C0)
    val BorderCyan = Color(0xFF6FEDD6)
    val BorderFocus = Color(0xFF8B5CF6)
    
    // Status
    val StatusSuccess = Color(0xFF45D27B)
    val StatusSuccessBg = Color(0xFFE8F8EF)
    val StatusSuccessBorder = Color(0xFFA3E6C1)
    val StatusWarning = Color(0xFFF3BA60)
    val StatusWarningBg = Color(0xFFFEF5E7)
    val StatusWarningBorder = Color(0xFFF9DCA8)
    val StatusDanger = Color(0xFFFF6B6B)
    val StatusDangerBg = Color(0xFFFFEBEB)
    val StatusDangerBorder = Color(0xFFFFB5B5)
    val StatusInfo = Color(0xFF7DC8F7)
    val StatusInfoBg = Color(0xFFEAF6FE)
    val StatusInfoBorder = Color(0xFFBEE3FB)
    
    // Icons
    val IconPrimary = Color(0xFF202022)
    val IconSecondary = Color(0xFF736A6A)
    val IconTertiary = Color(0xFFA8A4AD)
    val IconInverse = Color(0xFFFFFFFF)
    val IconAccent = Color(0xFFF3BA60)
    val IconCyan = Color(0xFF6FEDD6)
}

// ============================================
// DARK MODE COLORS
// ============================================
object DarkColors {
    // Backgrounds
    val BgPrimary = Color(0xFF202022)
    val BgSecondary = Color(0xFF2A2A2D)
    val BgTertiary = Color(0xFF3A3A3D)
    val BgPurpleDark = Color(0xFF2D1B4E)
    val BgAccentCyan = Color(0xFF1A4A44)
    val BgAccentNeon = Color(0xFF3A4500)
    
    // Surfaces
    val SurfaceDefault = Color(0xFF2A2A2D)
    val SurfaceElevated = Color(0xFF3A3A3D)
    val SurfaceVariant = Color(0xFF393943)
    val SurfaceAccent = Color(0xFFE0DBF3)
    val SurfacePurple = Color(0xFF5B3A8F)
    val SurfaceCyan = Color(0xFF6FEDD6)
    val SurfaceNeon = Color(0xFFC8FF00)
    val SurfacePeach = Color(0xFFFFD4B8)
    
    // Text
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFB6B1C0)
    val TextTertiary = Color(0xFF928C9C)
    val TextDisabled = Color(0xFF5A5A5F)
    val TextInverse = Color(0xFF202022)
    val TextOnCyan = Color(0xFF0A4A3F)
    val TextOnNeon = Color(0xFF2D3A00)
    val TextLink = Color(0xFFA78BFA)
    
    // CTA (Call To Action)
    val CtaPrimary = Color(0xFFF3BA60)
    val CtaPrimaryHover = Color(0xFFF5C678)
    val CtaPrimaryPressed = Color(0xFFD89F4B)
    val CtaPrimaryDisabled = Color(0xFF6B5A3D)
    val CtaSecondary = Color(0xFFB6B1C0)
    val CtaSecondaryHover = Color(0xFFC5C1CF)
    val CtaSecondaryPressed = Color(0xFF9E99A7)
    val CtaNeon = Color(0xFFC8FF00)
    val CtaNeonPressed = Color(0xFFA3D400)
    val CtaPurple = Color(0xFF8B5CF6)
    
    // Borders
    val BorderLight = Color(0xFF3A3A3D)
    val BorderMedium = Color(0xFF525257)
    val BorderDark = Color(0xFF6A6A6F)
    val BorderAccent = Color(0xFFE0DBF3)
    val BorderCyan = Color(0xFF6FEDD6)
    val BorderNeon = Color(0xFFC8FF00)
    val BorderFocus = Color(0xFFA78BFA)
    
    // Status
    val StatusSuccess = Color(0xFF45D27B)
    val StatusSuccessBg = Color(0xFF1A3D2A)
    val StatusSuccessBorder = Color(0xFF2D6B47)
    val StatusWarning = Color(0xFFF3BA60)
    val StatusWarningBg = Color(0xFF3D2F1A)
    val StatusWarningBorder = Color(0xFF6B5234)
    val StatusDanger = Color(0xFFFF6B6B)
    val StatusDangerBg = Color(0xFF3D1A1A)
    val StatusDangerBorder = Color(0xFF6B2D2D)
    val StatusInfo = Color(0xFF7DC8F7)
    val StatusInfoBg = Color(0xFF1A2F3D)
    val StatusInfoBorder = Color(0xFF2D526B)
    
    // Icons
    val IconPrimary = Color(0xFFFFFFFF)
    val IconSecondary = Color(0xFFB6B1C0)
    val IconTertiary = Color(0xFF928C9C)
    val IconInverse = Color(0xFF202022)
    val IconAccent = Color(0xFFF3BA60)
    val IconCyan = Color(0xFF6FEDD6)
    val IconNeon = Color(0xFFC8FF00)
}

// ============================================
// LEGACY SPORT-TECH COLORS (Keeping for compatibility)
// ============================================
val SmokySilver = Color(0xFFE0E0E5)
val Onyx = Color(0xFF1C1C1E)
val NeutralInk = Color(0xFF202022)
val MistGray = Color(0xFFB6B1C0)

// Extended palette for gradients and accents
val SoftLavender = Color(0xFFEDE9F7)
val PeachGlow = Color(0xFFF8D5A3)
val MintBreeze = Color(0xFFD4E8E5)
val SkyMist = Color(0xFFD6E5F3)
val RoseDust = Color(0xFFF3E0E6)
val SunsetOrange = Color(0xFFE8A857)

// Sport-Tech Premium Colors
val SlateCharcoal = Color(0xFF2D2D32)
val TitaniumGray = Color(0xFF8A8A8E)
val IceWhite = Color(0xFFFAFAFA)
val GoldenAmber = Color(0xFFD4A649)
val CoolSteel = Color(0xFFE8E8EC)
val ShadowMist = Color(0xFFD1D1D6)

// Neumorphism shadows
val NeumorphLight = Color(0xFFFFFFFF)
val NeumorphDark = Color(0xFFD1CCD9)

// Floating Bottom Nav Colors (Gen-Z Premium Aesthetic)
val NavBarDark = Color(0xFF1A1A2E)
val NavBarDarkGlossy = Color(0xFF16213E)
val NavBarInactiveIcon = Color(0xFF6B7280)
val NavBarActiveWhite = Color(0xFFFFFFFF)
val NavBarShadow = Color(0xFF0F0F1A)

// Sport Category Colors - Mature & Premium
val BadmintonAmber = Color(0xFFE8985A)
val FutsalEmerald = Color(0xFF6BA368)
val BasketCoral = Color(0xFFD46B6B)
val TennisTeal = Color(0xFF5AAFB8)
val VolleyOrchid = Color(0xFF9B7BB8)
val GymTaupe = Color(0xFF8B7355)
val RunningGold = Color(0xFFD4B84A)
val CyclingAzure = Color(0xFF5A8ED4)
