package com.konkuk.medicarecall.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale

val BG = Color(0xFFFAFAFA)
val dim = Color(0xFF000000)

// Main
val G50 = Color(0xFFE6FAEF)
val G100 = Color(0xFFDAFFED)
val G200 = Color(0xFFB2FFD8)
val G300 = Color(0xFF00CC63)
val main = Color(0xFF00B859)
val G500 = Color(0xFF00A34F)
val G600 = Color(0xFF00994A)
val G700 = Color(0xFF007A3B)
val G800 = Color(0xFF007A3B)
val G900 = Color(0xFF004723)

// System
val Negative = Color(0xFFFF4949)
val Warning = Color(0xFFFFCB3D)
val Positive = Color(0xFF0AD364)
val Active = Color(0xFF2D8FFF)

// Gray
val White = Color(0xFFFFFFFF)
val gray1 = Color(0xFFECECEC)
val gray2 = Color(0xFFD2D2D2)
val gray3 = Color(0xFFAFAFAF)
val gray4 = Color(0xFF8A8A8A)
val gray5 = Color(0xFF666666)
val gray6 = Color(0xFF454545)
val gray7 = Color(0xFF3B3B3B)
val gray8 = Color(0xFF313131)
val gray9 = Color(0xFF272727)
val gray10 = Color(0xFF1F1F1F)
val Black = Color(0xFF000000)

@Immutable
data class MediCareCallColors(
    val bg: Color,
    val dim: Color,
    val main: Color,
    val g50: Color,
    val g100: Color,
    val g200: Color,
    val g300: Color,
    val g500: Color,
    val g600: Color,
    val g700: Color,
    val g800: Color,
    val g900: Color,
    val negative: Color,
    val warning: Color,
    val positive: Color,
    val active: Color,
    val white: Color,
    val gray1: Color,
    val gray2: Color,
    val gray3: Color,
    val gray4: Color,
    val gray5: Color,
    val gray6: Color,
    val gray7: Color,
    val gray8: Color,
    val gray9: Color,
    val gray10: Color,
    val black: Color
)

val defaultMediCareCallColors = MediCareCallColors(
    bg = BG,
    dim = dim,
    main = main,
    g50 = G50,
    g100 = G100,
    g200 = G200,
    g300 = G300,
    g500 = G500,
    g600 = G600,
    g700 = G700,
    g800 = G800,
    g900 = G900,
    negative = Negative,
    warning = Warning,
    positive = Positive,
    active = Active,
    white = White,
    gray1 = gray1,
    gray2 = gray2,
    gray3 = gray3,
    gray4 = gray4,
    gray5 = gray5,
    gray6 = gray6,
    gray7 = gray7,
    gray8 = gray8,
    gray9 = gray9,
    gray10 = gray10,
    black = Black
)

val LocalMediCareCallColorsProvider = staticCompositionLocalOf { defaultMediCareCallColors }

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)