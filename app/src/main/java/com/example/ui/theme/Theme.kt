package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = IndigoPrimary,
    onPrimary = Color.White,
    primaryContainer = IndigoContainerDark,
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = CyanAccent,
    onSecondary = Color(0xFF083344),
    secondaryContainer = CyanContainerDark,
    onSecondaryContainer = Color(0xFFCFFAFE),
    tertiary = PinkAccent,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = Color(0xFF263238),
    error = Color(0xFFEF4444),
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = IndigoDark,
    onPrimary = Color.White,
    primaryContainer = IndigoContainerLight,
    onPrimaryContainer = IndigoDark,
    secondary = CyanAccentDark,
    onSecondary = Color.White,
    secondaryContainer = CyanContainerLight,
    onSecondaryContainer = Color(0xFF0369A1),
    tertiary = PinkAccent,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    outlineVariant = Color(0xFFE2E8F0),
    error = Color(0xFFDC2626),
    onError = Color.White
)

object SellAiGradients {
    val brandGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6), Color(0xFF06B6D4))
    )
    val cardGlowDark = Brush.verticalGradient(
        colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A))
    )
    val cardGlowLight = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFFF8FAFC))
    )
    val subtleHeroGradient = Brush.radialGradient(
        colors = listOf(Color(0x336366F1), Color(0x000B0F19)),
        radius = 800f
    )
}

@Composable
fun SellAiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // We use custom cohesive SaaS brand colors
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
