package com.example.mypharmacy.view.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Light theme color scheme
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryVariant,
    onPrimaryContainer = OnPrimary,

    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryVariant,
    onSecondaryContainer = OnSecondary,

    background = Background,
    onBackground = OnBackground,

    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,

    error = Error,
    onError = Color.White,

    outline = Divider
)

// Dark theme color scheme (we'll use a simplified version for now)
private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryVariant,
    onPrimaryContainer = OnPrimary,

    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryVariant,
    onSecondaryContainer = OnSecondary,

    background = Color(0xFF121212),
    onBackground = Color.White,

    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color.LightGray,

    error = Error,
    onError = Color.White,

    outline = Color(0xFF3E3E3E)
)

// Custom color palette for status colors
class ExtendedColors(
    val warningColor: Color,
    val warningContainer: Color,
    val successColor: Color,
    val successContainer: Color,
    val infoColor: Color,
    val infoContainer: Color,
    val lowStock: Color,
    val mediumStock: Color,
    val goodStock: Color,
    val cardBorder: Color,
    val errorContainer: Color? = null  // Added errorContainer property with default value null
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        warningColor = Warning,
        warningContainer = WarningBackground,
        successColor = Success,
        successContainer = SuccessBackground,
        infoColor = Info,
        infoContainer = InfoBackground,
        lowStock = LowStock,
        mediumStock = MediumStock,
        goodStock = GoodStock,
        cardBorder = CardBorder,
        errorContainer = null  // Initialize with null
    )
}

@Composable
fun MyPharmacyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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

    // Extended colors for light/dark themes
    val extendedColors = if (darkTheme) {
        ExtendedColors(
            warningColor = Warning,
            warningContainer = Color(0xFF332400),
            successColor = Success,
            successContainer = Color(0xFF002212),
            infoColor = Info,
            infoContainer = Color(0xFF001F2E),
            lowStock = LowStock,
            mediumStock = MediumStock,
            goodStock = GoodStock,
            cardBorder = Color(0xFF3E3E3E),
            errorContainer = null  // Use null to fall back to Material3's errorContainer
        )
    } else {
        ExtendedColors(
            warningColor = Warning,
            warningContainer = WarningBackground,
            successColor = Success,
            successContainer = SuccessBackground,
            infoColor = Info,
            infoContainer = InfoBackground,
            lowStock = LowStock,
            mediumStock = MediumStock,
            goodStock = GoodStock,
            cardBorder = CardBorder,
            errorContainer = null  // Use null to fall back to Material3's errorContainer
        )
    }

    // Update status bar color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PharmacyTypography,
            shapes = PharmacyShapes,
            content = content
        )
    }
}

// Extension property to access the extended colors easily
val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColors.current