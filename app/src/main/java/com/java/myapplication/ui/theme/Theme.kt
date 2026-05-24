package com.java.myapplication.ui.theme

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

// ─── 雨晴樱粉 Light 配色方案 ───
private val RainyColorScheme = lightColorScheme(
    primary = RainyPinkPrimary,
    onPrimary = RainyWhite,
    primaryContainer = RainySakuraSurface,
    onPrimaryContainer = RainyOnSurface,
    secondary = RainyPinkSecondary,
    onSecondary = RainyWhite,
    secondaryContainer = RainySakuraBg,
    onSecondaryContainer = RainyOnSurface,
    tertiary = RainyPinkTertiary,
    onTertiary = RainyOnSurface,
    background = RainySakuraBg,
    onBackground = RainyOnSurface,
    surface = RainyWhite,
    onSurface = RainyOnSurface,
    surfaceVariant = RainySakuraSurface,
    onSurfaceVariant = RainyOnSurfaceVariant,
    outline = RainyOutline,
    outlineVariant = RainyPinkTertiary
)

// ─── 暗色模式（柔和暗粉，夜晚不刺眼）───
private val RainyDarkColorScheme = darkColorScheme(
    primary = RainyPinkPrimary,
    onPrimary = Color(0xFF3C2025),
    primaryContainer = Color(0xFF5D4045),
    onPrimaryContainer = RainySakuraSurface,
    secondary = RainyPinkTertiary,
    onSecondary = Color(0xFF3C2025),
    secondaryContainer = Color(0xFF5D4045),
    onSecondaryContainer = RainySakuraSurface,
    tertiary = RainyPinkSecondary,
    onTertiary = Color(0xFF3C2025),
    background = Color(0xFF1A1015),
    onBackground = Color(0xFFFFEBEF),
    surface = Color(0xFF2D1C24),
    onSurface = Color(0xFFFFEBEF),
    surfaceVariant = Color(0xFF3C2025),
    onSurfaceVariant = RainySakuraSurface,
    outline = RainyPinkSecondary,
    outlineVariant = Color(0xFF5D4045)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 强制使用雨晴配色，不从系统取动态色（保证粉色一致）
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> RainyDarkColorScheme
        else -> RainyColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}