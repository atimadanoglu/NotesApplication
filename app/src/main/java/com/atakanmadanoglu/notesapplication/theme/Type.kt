package com.atakanmadanoglu.notesapplication.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.atakanmadanoglu.notesapplication.R

val openSansSemiBoldFontFamily = FontFamily(
    Font(resId = R.font.open_sans_semi_bold)
)

val openSansLightFontFamily = FontFamily(
    Font(resId = R.font.open_sans_light)
)

val openSansRegularFontFamily = FontFamily(
    Font(resId = R.font.open_sans_regular)
)

val openSansLightItalicFontFamily = FontFamily(
    Font(resId = R.font.open_sans_light_italic)
)

val Typography.openSansSemiBold: TextStyle get() = TextStyle(
    fontFamily = openSansSemiBoldFontFamily
)
val Typography.openSansLight: TextStyle get() = TextStyle(
    fontFamily = openSansLightFontFamily
)

val Typography.openSansRegular: TextStyle get() = TextStyle(
    fontFamily = openSansRegularFontFamily
)

val Typography.openSansLightItalic: TextStyle get() = TextStyle(
    fontFamily = openSansLightItalicFontFamily
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)