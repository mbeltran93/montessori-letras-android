package com.mbeltran.montessoriletras.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import com.mbeltran.montessoriletras.model.Letter
import com.mbeltran.montessoriletras.ui.theme.CreamBackground
import com.mbeltran.montessoriletras.ui.theme.InkText
import com.mbeltran.montessoriletras.ui.theme.SkyBlue
import kotlinx.coroutines.delay

/**
 * Pantalla de detalle de una letra. Filosofia Montessori en cada decision:
 *
 * - No hay boton de "enviar" ni de "revisar": el trazo se dibuja libremente.
 * - No existe un estado de error: cualquier trazo, por torpe que sea, se
 *   festeja igual despues de un rato de interaccion.
 * - El sonido se puede repetir las veces que haga falta, sin penalidad.
 */
@Composable
fun LetterScreen(
    letter: Letter,
    onBack: () -> Unit,
    onRepeatSound: () -> Unit,
    onCelebrate: () -> Unit,
    modifier: Modifier = Modifier
) {
    var points by remember(letter.char) { mutableStateOf(listOf<Offset>()) }
    var hasStartedDrawing by remember(letter.char) { mutableStateOf(false) }
    var showCelebration by remember(letter.char) { mutableStateOf(false) }

    LaunchedEffect(hasStartedDrawing, letter.char) {
        if (hasStartedDrawing) {
            delay(2_000)
            showCelebration = true
            onCelebrate()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(16.dp)
    ) {
        Button(
            onClick = onBack,
            modifier = Modifier.testTag("back_button")
        ) {
            Text("← Volver")
        }

        Text(
            text = letter.char.toString(),
            fontSize = 120.sp,
            fontWeight = FontWeight.Black,
            color = InkText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag("letter_big_text"),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Text(
            text = "${letter.char} de ${letter.word} ${letter.emoji}",
            fontSize = 28.sp,
            color = InkText,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag("letter_word_emoji")
        )

        Button(onClick = onRepeatSound, modifier = Modifier.testTag("repeat_sound_button")) {
            Text("🔊 Escuchar de nuevo")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(top = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(2.dp, SkyBlue, RoundedCornerShape(24.dp))
                .testTag("trace_canvas")
        ) {
            // Contorno "fantasma" de la letra: el nino dibuja sobre ella,
            // sin importar si se sale del trazo.
            Text(
                text = letter.char.toString(),
                fontSize = 180.sp,
                fontWeight = FontWeight.Black,
                color = InkText.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(letter.char) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                hasStartedDrawing = true
                                points = points + offset
                            }
                        ) { change, _ ->
                            points = points + change.position
                        }
                    }
            ) {
                if (points.size > 1) {
                    val path = Path().apply {
                        moveTo(points.first().x, points.first().y)
                        points.drop(1).forEach { lineTo(it.x, it.y) }
                    }
                    drawPath(path = path, color = SkyBlue, style = Stroke(width = 18f))
                }
            }
        }

        AnimatedVisibility(visible = showCelebration) {
            Text(
                text = "⭐ ¡Muy bien! ⭐",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = InkText,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .testTag("celebration_text")
            )
        }
    }
}
