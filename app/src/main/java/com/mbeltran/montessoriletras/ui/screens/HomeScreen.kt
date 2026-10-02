package com.mbeltran.montessoriletras.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mbeltran.montessoriletras.ui.theme.CardPalette
import com.mbeltran.montessoriletras.ui.theme.CreamBackground
import com.mbeltran.montessoriletras.ui.theme.GoldExplored
import com.mbeltran.montessoriletras.ui.theme.InkText
import com.mbeltran.montessoriletras.viewmodel.LetterCardUiModel
import com.mbeltran.montessoriletras.viewmodel.LetrasUiState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row

/**
 * Pantalla principal: grid de todas las letras. Cada tarjeta es grande
 * (minimo 64dp, en la practica mucho mas) para que un dedo chiquito la
 * pueda tocar sin puntería fina, y las letras ya exploradas se distinguen
 * con un borde dorado, nunca con una tacha ni un numero de "fallos".
 */
@Composable
fun HomeScreen(
    uiState: LetrasUiState,
    onLetterClick: (LetterCardUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Letras y Sonidos",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = InkText,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
        Text(
            text = "Exploradas: ${uiState.exploredCount} / ${uiState.letters.size}",
            fontSize = 18.sp,
            color = InkText,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag("explored_counter")
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 88.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().testTag("letters_grid")
        ) {
            items(uiState.letters, key = { it.letter.char }) { card ->
                val color = CardPalette[card.letter.char.code % CardPalette.size]
                LetterCard(card = card, color = color, onClick = { onLetterClick(card) })
            }
        }
    }
}

@Composable
private fun LetterCard(
    card: LetterCardUiModel,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(color)
            .then(
                if (card.isExplored) {
                    Modifier.border(4.dp, GoldExplored, RoundedCornerShape(20.dp))
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick)
            .semantics {
                contentDescription = if (card.isExplored) {
                    "Letra ${card.letter.char}, ya explorada"
                } else {
                    "Letra ${card.letter.char}"
                }
            }
            .testTag("letter_card_${card.letter.char}"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = card.letter.char.toString(),
            fontSize = 40.sp,
            fontWeight = FontWeight.Black,
            color = InkText
        )
        if (card.isExplored) {
            Box(modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)) {
                Text(text = "⭐", fontSize = 16.sp)
            }
        }
    }
}
