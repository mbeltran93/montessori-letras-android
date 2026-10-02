package com.mbeltran.montessoriletras.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = SkyBlue,
    secondary = SunYellow,
    tertiary = CoralPink,
    background = CreamBackground,
    surface = CreamBackground,
    onBackground = InkText,
    onSurface = InkText
)

// La app esta pensada para usarse siempre en modo claro y alegre (es para
// ninos muy chicos), pero se define un esquema oscuro razonable por si el
// sistema lo fuerza.
private val DarkColors = darkColorScheme(
    primary = SkyBlue,
    secondary = SunYellow,
    tertiary = CoralPink,
    background = InkText,
    surface = InkText
)

@Composable
fun MontessoriLetrasTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = MontessoriTypography,
        content = content
    )
}
