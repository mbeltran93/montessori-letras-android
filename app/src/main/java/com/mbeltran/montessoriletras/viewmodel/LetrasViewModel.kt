package com.mbeltran.montessoriletras.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbeltran.montessoriletras.data.LetrasData
import com.mbeltran.montessoriletras.data.ProgressRepository
import com.mbeltran.montessoriletras.model.Letter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel de toda la app: expone el abecedario con su progreso de
 * exploracion, y cual es la letra actualmente seleccionada (para la
 * pantalla de detalle).
 *
 * Filosofia Montessori aplicada aca: no existe un estado de "error". Solo
 * existen letras "exploradas" o "por explorar", y esa marca no se puede
 * perder ni "reprobar": una vez que el nino la visita, queda.
 */
class LetrasViewModel(
    private val repository: ProgressRepository,
    private val alphabet: List<Letter> = LetrasData.alphabet
) : ViewModel() {

    private val _selectedLetter = MutableStateFlow<Letter?>(null)
    val selectedLetter: StateFlow<Letter?> = _selectedLetter.asStateFlow()

    val uiState: StateFlow<LetrasUiState> = repository.exploredLetters
        .map { explored -> buildUiState(explored) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = buildUiState(emptySet())
        )

    private fun buildUiState(explored: Set<Char>): LetrasUiState {
        val cards = alphabet.map { letter ->
            LetterCardUiModel(
                letter = letter,
                isExplored = explored.any { it.equals(letter.char, ignoreCase = true) }
            )
        }
        return LetrasUiState(letters = cards, exploredCount = cards.count { it.isExplored })
    }

    /** El nino tocO una letra del grid: se vuelve la letra activa. */
    fun selectLetter(letter: Letter) {
        _selectedLetter.value = letter
    }

    /** Vuelve a la pantalla principal: ya no hay letra activa. */
    fun clearSelection() {
        _selectedLetter.value = null
    }

    /**
     * Marca una letra como explorada. Se llama al entrar a su pantalla de
     * detalle (no hace falta "completar" nada): el solo hecho de visitarla
     * ya cuenta como exploracion, siguiendo el espiritu Montessori de que
     * no hay nada que aprobar.
     */
    fun markExplored(letter: Letter) {
        viewModelScope.launch {
            repository.markExplored(letter.char)
        }
    }

    companion object {
        /** Palabra asociada a una letra, ej. "Abeja" para la 'A'. */
        fun wordFor(char: Char): String? = LetrasData.byChar(char)?.word

        /** Emoji asociado a una letra, ej. "🐝" para la 'A'. */
        fun emojiFor(char: Char): String? = LetrasData.byChar(char)?.emoji
    }
}
