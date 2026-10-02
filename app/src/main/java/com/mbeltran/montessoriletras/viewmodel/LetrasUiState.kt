package com.mbeltran.montessoriletras.viewmodel

import com.mbeltran.montessoriletras.model.Letter

/** Una letra del grid principal, junto con si el nino ya la explorO. */
data class LetterCardUiModel(
    val letter: Letter,
    val isExplored: Boolean
)

/** Estado de la pantalla principal: el grid de letras y cuantas lleva exploradas. */
data class LetrasUiState(
    val letters: List<LetterCardUiModel> = emptyList(),
    val exploredCount: Int = 0
)
