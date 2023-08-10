package com.example.deslocations.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.deslocations.R

private val Ravie = FontFamily(
    Font(R.font.ravie)
)

private val Domine = FontFamily(
    Font(R.font.domine_regular, FontWeight.W300),
    Font(R.font.domine_medium, FontWeight.W400),
    Font(R.font.domine_semi_bold, FontWeight.W500),
    Font(R.font.domine_bold, FontWeight.W600)

)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Ravie,
        fontWeight = FontWeight.W600,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = 1.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Ravie,
        fontWeight = FontWeight.W500,
        fontSize = 28.sp,
        lineHeight = 32.sp,
        letterSpacing = 1.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Ravie,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 1.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 1.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.W400,
        fontSize = 22.sp,
        lineHeight = 26.sp,
        letterSpacing = 1.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.W300,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.W500,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.W300,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.W300,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.W300,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.W300,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.W300,
        fontSize = 14.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.W300,
        fontSize = 9.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    ),
)