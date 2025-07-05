package com.konkuk.medicarecall.ui.theme

import android.R.attr.fontFamily
import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.konkuk.medicarecall.R

val mediCareCallFontBold = FontFamily(Font(R.font.pretendard_bold))
val mediCareCallFontSemiBold = FontFamily(Font(R.font.pretendard_semibold))
val mediCareCallFontMedium = FontFamily(Font(R.font.pretendard_medium))
val mediCareCallFontRegular = FontFamily(Font(R.font.pretendard_regular))

@Immutable
data class MediCareCallTypography(
    // Title
    val B_30 : TextStyle,
    val B_28 : TextStyle,
    val B_26 : TextStyle,
    val SB_24 : TextStyle,
    val SB_22 : TextStyle,
    val B_20 : TextStyle,
    val SB_20 : TextStyle,
    val M_20 : TextStyle,

    // Body
    val SB_18 : TextStyle,
    val R_18 : TextStyle,
    val B_17 : TextStyle,
    val M_17 : TextStyle,
    val R_17 : TextStyle,
    val SB_16 : TextStyle,
    val M_16 : TextStyle,
    val R_16 : TextStyle,

    // Caption
    val R_15 : TextStyle,
    val SB_14 : TextStyle,
    val R_14 : TextStyle,
)

val defaultMediCareCallTypography = MediCareCallTypography(
    // Title
    B_30 = TextStyle(
        fontFamily = mediCareCallFontBold,
        fontSize = 30.sp,
        lineHeight = (30 * 1.3).sp,
        letterSpacing = (-0.2).sp
    ),
    B_28 = TextStyle(
        fontFamily = mediCareCallFontBold,
        fontSize = 28.sp,
        lineHeight = (28 * 1.3).sp,
        letterSpacing = (-0.2).sp
    ),
    B_26 = TextStyle(
        fontFamily = mediCareCallFontBold,
        fontSize = 26.sp,
        lineHeight = (26 * 1.3).sp,
        letterSpacing = (-0.2).sp
    ),
    SB_24 = TextStyle(
        fontFamily = mediCareCallFontSemiBold,
        fontSize = 24.sp,
        lineHeight = (24 * 1.3).sp,
        letterSpacing = (-0.2).sp
    ),
    SB_22 = TextStyle(
        fontFamily = mediCareCallFontSemiBold,
        fontSize = 22.sp,
        lineHeight = (22 * 1.3).sp,
        letterSpacing = (-0.2).sp
    ),
    B_20 = TextStyle(
        fontFamily = mediCareCallFontBold,
        fontSize = 20.sp,
        lineHeight = (20 * 1.3).sp,
        letterSpacing = (-0.2).sp
    ),
    SB_20 = TextStyle(
        fontFamily = mediCareCallFontSemiBold,
        fontSize = 20.sp,
        lineHeight = (20 * 1.3).sp,
        letterSpacing = (-0.2).sp
    ),
    M_20 = TextStyle(
        fontFamily = mediCareCallFontMedium,
        fontSize = 20.sp,
        lineHeight = (20 * 1.3).sp,
        letterSpacing = (-0.2).sp
    ),

    // Body
    SB_18 = TextStyle(
        fontFamily = mediCareCallFontSemiBold,
        fontSize = 18.sp,
        lineHeight = (18 * 1.6).sp,
        letterSpacing = 2.sp
    ),
    R_18 = TextStyle(
        fontFamily = mediCareCallFontRegular,
        fontSize = 18.sp,
        lineHeight = (18 * 1.6).sp,
    ),
    B_17 = TextStyle(
        fontFamily = mediCareCallFontBold,
        fontSize = 17.sp,
        lineHeight = (17 * 1.6).sp,
        letterSpacing = 2.sp
    ),
    M_17 = TextStyle(
        fontFamily = mediCareCallFontMedium,
        fontSize = 17.sp,
        lineHeight = (17 * 1.6).sp,
        letterSpacing = 2.sp
    ),
    M_17 = TextStyle(
        fontFamily = mediCareCallFontRegular,
        fontSize = 17.sp,
        lineHeight = (17 * 1.6).sp,
    ),
    R_17 = TextStyle(
        fontFamily = mediCareCallFontRegular,
        fontSize = 17.sp,
        lineHeight = (17 * 1.6).sp,
    ),
    SB_16 = TextStyle(
        fontFamily = mediCareCallFontSemiBold,
        fontSize = 16.sp,
        lineHeight = (16 * 1.6).sp,
    ),
    M_16 = TextStyle(
        fontFamily = mediCareCallFontMedium,
        fontSize = 16.sp,
        lineHeight = (16 * 1.6).sp,
    ),
    R_16 = TextStyle(
        fontFamily = mediCareCallFontRegular,
        fontSize = 16.sp,
        lineHeight = (16 * 1.6).sp,
    ),

    // Caption
    R_15 = TextStyle(
        fontFamily = mediCareCallFontRegular,
        fontSize = 15.sp,
        lineHeight = (15 * 1.5).sp,
        letterSpacing = (0.1).sp
    ),
    SB_14 = TextStyle(
        fontFamily = mediCareCallFontSemiBold,
        fontSize = 14.sp,
        lineHeight = (14 * 1.5).sp,
        letterSpacing = (0.1).sp
    ),
    R_14 = TextStyle(
        fontFamily = mediCareCallFontRegular,
        fontSize = 14.sp,
        lineHeight = (14 * 1.5).sp,
        letterSpacing = (0.1).sp
    )
)

val LocalMedicareCallTypographyProvider = staticCompositionLocalOf { defaultMediCareCallTypography }

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)