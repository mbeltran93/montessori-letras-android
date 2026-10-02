package com.mbeltran.montessoriletras.viewmodel

import com.mbeltran.montessoriletras.data.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** Repositorio en memoria para probar el ViewModel sin DataStore ni Context. */
class FakeProgressRepository(initiallyExplored: Set<Char> = emptySet()) : ProgressRepository {

    private val state = MutableStateFlow(initiallyExplored)
    override val exploredLetters: StateFlow<Set<Char>> = state

    var markExploredCallCount: Int = 0
        private set

    override suspend fun markExplored(letter: Char) {
        markExploredCallCount++
        state.value = state.value + letter
    }
}
