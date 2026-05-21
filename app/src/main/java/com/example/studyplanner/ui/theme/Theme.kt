package com.example.studyplanner.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = StudyBlue,
    onPrimary = StudySurface,

    secondary = StudySuccess,
    onSecondary = StudySurface,

    tertiary = StudyWarning,
    onTertiary = StudySurface,

    background = StudyBackground,
    onBackground = StudyTextDark,

    surface = StudySurface,
    onSurface = StudyTextDark,

    surfaceVariant = StudySurfaceSoft,
    onSurfaceVariant = StudyTextMuted,

    primaryContainer = StudyBlueLight,
    onPrimaryContainer = StudyBlueDark,

    secondaryContainer = StudySuccessLight,
    onSecondaryContainer = StudySuccess,

    tertiaryContainer = StudyWarningLight,
    onTertiaryContainer = StudyWarning,

    error = StudyError,
    onError = StudySurface,

    errorContainer = StudyErrorLight,
    onErrorContainer = StudyError
)

private val DarkColorScheme = darkColorScheme(
    primary = StudyBlue,
    onPrimary = StudySurface,

    secondary = StudySuccess,
    onSecondary = StudySurface,

    tertiary = StudyWarning,
    onTertiary = StudySurface,

    background = DarkBackground,
    onBackground = DarkText,

    surface = DarkSurface,
    onSurface = DarkText,

    surfaceVariant = DarkSurface,
    onSurfaceVariant = DarkMutedText,

    primaryContainer = StudyBlueDark,
    onPrimaryContainer = StudySurface,

    secondaryContainer = StudySuccess,
    onSecondaryContainer = StudySurface,

    tertiaryContainer = StudyWarning,
    onTertiaryContainer = StudySurface,

    error = StudyError,
    onError = StudySurface,

    errorContainer = StudyError,
    onErrorContainer = StudySurface
)

@Composable
fun StudyPlannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}