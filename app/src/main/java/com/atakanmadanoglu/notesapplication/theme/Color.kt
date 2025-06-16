package com.atakanmadanoglu.notesapplication.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Primary Colors - Soft Blue/Purple tones
val PrimaryLight = Color(0xFF6B73FF)
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFE1E4FF)
val OnPrimaryContainerLight = Color(0xFF0B0F5C)

val PrimaryDark = Color(0xFFB8C3FF)
val OnPrimaryDark = Color(0xFF1C2678)
val PrimaryContainerDark = Color(0xFF373E8C)
val OnPrimaryContainerDark = Color(0xFFE1E4FF)

// Secondary Colors - Warm accent
val SecondaryLight = Color(0xFF5F5C7B)
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFE4E0FF)
val OnSecondaryContainerLight = Color(0xFF1B1736)

val SecondaryDark = Color(0xFFC8C4E3)
val OnSecondaryDark = Color(0xFF302D4C)
val SecondaryContainerDark = Color(0xFF474463)
val OnSecondaryContainerDark = Color(0xFFE4E0FF)

// Tertiary Colors - Soft green for accents
val TertiaryLight = Color(0xFF7D5260)
val OnTertiaryLight = Color(0xFFFFFFFF)
val TertiaryContainerLight = Color(0xFFFFD8E4)
val OnTertiaryContainerLight = Color(0xFF31111D)

val TertiaryDark = Color(0xFFE4BAC8)
val OnTertiaryDark = Color(0xFF492532)
val TertiaryContainerDark = Color(0xFF633B48)
val OnTertiaryContainerDark = Color(0xFFFFD8E4)

// Error Colors
val ErrorLight = Color(0xFFBA1A1A)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFFDAD6)
val OnErrorContainerLight = Color(0xFF410002)

val ErrorDark = Color(0xFFFFB4AB)
val OnErrorDark = Color(0xFF690005)
val ErrorContainerDark = Color(0xFF93000A)
val OnErrorContainerDark = Color(0xFFFFDAD6)

// Background Colors
val BackgroundLight = Color(0xFFFFFBFF)
val OnBackgroundLight = Color(0xFF1C1B1F)
val SurfaceLight = Color(0xFFFFFBFF)
val OnSurfaceLight = Color(0xFF1C1B1F)

val BackgroundDark = Color(0xFF121212)
val OnBackgroundDark = Color(0xFFE6E1E5)
val SurfaceDark = Color(0xFF1E1E1E)
val OnSurfaceDark = Color(0xFFE6E1E5)

// Surface variants for notes cards
val SurfaceVariantLight = Color(0xFFF4F0F7)
val OnSurfaceVariantLight = Color(0xFF46464F)
val SurfaceVariantDark = Color(0xFF2A2A2A)
val OnSurfaceVariantDark = Color(0xFFC7C5D0)

// Note specific colors
val NoteCardLight = Color(0xFFFFFEFA)
val NoteCardDark = Color(0xFF252525)
val NoteDividerLight = Color(0xFFE8E8E8)
val NoteDividerDark = Color(0xFF404040)


val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = Color(0xFF777680),
    outlineVariant = Color(0xFFC7C5D0)
)

val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = Color(0xFF918F99),
    outlineVariant = Color(0xFF46464F)
)
