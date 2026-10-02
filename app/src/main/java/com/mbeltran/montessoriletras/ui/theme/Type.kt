package com.mbeltran.montessoriletras.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Tipografia grande y redonda por defecto (system default, bien gruesa):
// no hace falta descargar una fuente custom para que se vea amigable.
val MontessoriTypography = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.Black, fontSize = 96.sp),
    headlineLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp)
)
