package com.eliascoelho911.homeapp.ds.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.eliascoelho911.homeapp.ds.R

private val RubikFontFamily = FontFamily(
    Font(R.font.rubik_regular),
    Font(R.font.rubik_medium, weight = FontWeight.Medium),
    Font(R.font.rubik_semibold, weight = FontWeight.SemiBold),
    Font(R.font.rubik_bold, weight = FontWeight.Bold),
)

internal val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 64.sp,
        fontSize = 57.sp,
        fontWeight = FontWeight.Normal
    ),
    displayMedium = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 52.sp,
        fontSize = 45.sp,
        fontWeight = FontWeight.Normal
    ),
    displaySmall = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 44.sp,
        fontSize = 36.sp,
        fontWeight = FontWeight.Normal
    ),
    headlineLarge = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 40.sp,
        fontSize = 32.sp,
        fontWeight = FontWeight.Normal
    ),
    headlineMedium = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 36.sp,
        fontSize = 28.sp,
        fontWeight = FontWeight.Normal
    ),
    headlineSmall = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 32.sp,
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal
    ),
    titleLarge = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 28.sp,
        fontSize = 22.sp,
        fontWeight = FontWeight.Normal
    ),
    titleMedium = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 24.sp,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 20.sp,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp
    ),
    labelLarge = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 20.sp,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 16.sp,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 6.sp,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 24.sp,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 20.sp,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = RubikFontFamily,
        lineHeight = 16.sp,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.4.sp
    ),
)