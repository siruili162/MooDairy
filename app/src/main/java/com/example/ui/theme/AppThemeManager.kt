package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppThemeColors {
    // 1. NATURAL TONES (Default Design Theme)
    val NaturalTonesLight = lightColorScheme(
        primary = Color(0xFF8C9C75), // Sage Green
        onPrimary = Color.White,
        secondary = Color(0xFF5A5A40), // Dark Sage
        onSecondary = Color.White,
        tertiary = Color(0xFFE8E2D9), // Warm Grey-Brown
        background = Color(0xFFFDF8F3), // Warm Natural Cream
        surface = Color(0xFFF7F2EB), // Warm light gray-beige
        onBackground = Color(0xFF201A17), // Charcoal-Brown
        onSurface = Color(0xFF4A453E), // Almond text
        outline = Color(0xFFEAE0D5), // Almond border
        surfaceVariant = Color(0xFFF0EBE5), // Soft container
        onSurfaceVariant = Color(0xFF8C7E6A) // Taupe/Brown label
    )

    // 2. WARM SUN
    val WarmSunLight = lightColorScheme(
        primary = Color(0xFFE07A5F),
        onPrimary = Color.White,
        secondary = Color(0xFFF2CC8F),
        onSecondary = Color(0xFF3D405B),
        tertiary = Color(0xFFF4F1DE),
        background = Color(0xFFFFFDF9),
        surface = Color(0xFFFFF2E2),
        onBackground = Color(0xFF3D405B),
        onSurface = Color(0xFF3D405B)
    )

    // 3. QUIET FOREST
    val QuietForestLight = lightColorScheme(
        primary = Color(0xFF4E6C50),
        onPrimary = Color.White,
        secondary = Color(0xFFC1A478),
        onSecondary = Color(0xFF2C3E2E),
        tertiary = Color(0xFFE3EAE4),
        background = Color(0xFFF4F6F4),
        surface = Color(0xFFEAF0EB),
        onBackground = Color(0xFF2C3E2E),
        onSurface = Color(0xFF2C3E2E)
    )

    // 4. DEEP SEA
    val DeepSeaLight = lightColorScheme(
        primary = Color(0xFF2E5B88),
        onPrimary = Color.White,
        secondary = Color(0xFFA5C5E6),
        onSecondary = Color(0xFF1E2E3E),
        tertiary = Color(0xFFEBF3FB),
        background = Color(0xFFF7FAFD),
        surface = Color(0xFFEDF4FC),
        onBackground = Color(0xFF1E2E3E),
        onSurface = Color(0xFF1E2E3E)
    )

    // 5. SAKURA PINK
    val SakuraPinkLight = lightColorScheme(
        primary = Color(0xFFE87A90),
        onPrimary = Color.White,
        secondary = Color(0xFFF8C3CD),
        onSecondary = Color(0xFF5E2F38),
        tertiary = Color(0xFFFFF0F5),
        background = Color(0xFFFFF9FA),
        surface = Color(0xFFFFF0F2),
        onBackground = Color(0xFF5E2F38),
        onSurface = Color(0xFF5E2F38)
    )
}

@Composable
fun MoodDiaryTheme(
    themeName: String,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeName) {
        "NATURAL" -> AppThemeColors.NaturalTonesLight
        "FOREST" -> AppThemeColors.QuietForestLight
        "OCEAN" -> AppThemeColors.DeepSeaLight
        "SAKURA" -> AppThemeColors.SakuraPinkLight
        "WARM" -> AppThemeColors.WarmSunLight
        else -> AppThemeColors.NaturalTonesLight // NATURAL IS DEFAULT
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
