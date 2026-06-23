package org.example.roundapp.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.example.roundapp.core.designsystem.resources.Res
import org.example.roundapp.core.designsystem.resources.plusjakartasans_bold
import org.example.roundapp.core.designsystem.resources.plusjakartasans_light
import org.example.roundapp.core.designsystem.resources.plusjakartasans_medium
import org.example.roundapp.core.designsystem.resources.plusjakartasans_regular
import org.example.roundapp.core.designsystem.resources.plusjakartasans_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun PlusJakartaSansFontFamily(): FontFamily = FontFamily(
    Font(Res.font.plusjakartasans_light, weight = FontWeight.Light),
    Font(Res.font.plusjakartasans_regular, weight = FontWeight.Normal),
    Font(Res.font.plusjakartasans_medium, weight = FontWeight.Medium),
    Font(Res.font.plusjakartasans_semibold, weight = FontWeight.SemiBold),
    Font(Res.font.plusjakartasans_bold, weight = FontWeight.Bold),
)

@Composable
fun appTypography(): Typography {
    val family = PlusJakartaSansFontFamily()
    val base = Typography()
    return Typography(
        displayLarge = base.displayLarge.copy(fontFamily = family),
        displayMedium = base.displayMedium.copy(fontFamily = family),
        displaySmall = base.displaySmall.copy(fontFamily = family),
        headlineLarge = base.headlineLarge.copy(fontFamily = family),
        headlineMedium = base.headlineMedium.copy(fontFamily = family),
        headlineSmall = base.headlineSmall.copy(fontFamily = family),
        titleLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp,
            lineHeight = 36.sp,
        ),
        titleMedium = base.titleMedium.copy(fontFamily = family, fontWeight = FontWeight.SemiBold),
        titleSmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        bodyLarge = base.bodyLarge.copy(fontFamily = family),
        bodyMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        labelLarge = base.labelLarge.copy(fontFamily = family, fontWeight = FontWeight.SemiBold),
        labelMedium = base.labelMedium.copy(fontFamily = family, fontWeight = FontWeight.SemiBold),
        labelSmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
    )
}
