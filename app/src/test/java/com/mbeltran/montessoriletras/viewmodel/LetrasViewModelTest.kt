package com.mbeltran.montessoriletras.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mbeltran.montessoriletras.model.Letter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LetrasViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val letraA = Letter('A', "Abeja", "🐝")
    private val letraB = Letter('B', "Barco", "⛵")
    private val testAlphabet = listOf(letraA, letraB)

    private fun viewModel(repository: FakeProgressRepository = FakeProgressRepository()) =
        LetrasViewModel(repository = repository, alphabet = testAlphabet)

    // --- Logica de seleccion de letra -------------------------------------

    @Test
    fun `no hay letra seleccionada al iniciar`() = runTest {
        val vm = viewModel()
        vm.selectedLetter.test {
            assertThat(awaitItem()).isNull()
        }
    }

    @Test
    fun `selectLetter marca esa letra como la activa`() = runTest {
        val vm = viewModel()

        vm.selectLetter(letraA)

        vm.selectedLetter.test {
            assertThat(awaitItem()).isEqualTo(letraA)
        }
    }

    @Test
    fun `selectLetter se puede cambiar de una letra a otra`() = runTest {
        val vm = viewModel()

        vm.selectLetter(letraA)
        vm.selectLetter(letraB)

        vm.selectedLetter.test {
            assertThat(awaitItem()).isEqualTo(letraB)
        }
    }

    @Test
    fun `clearSelection deja sin letra activa`() = runTest {
        val vm = viewModel()
        vm.selectLetter(letraA)

        vm.clearSelection()

        vm.selectedLetter.test {
            assertThat(awaitItem()).isNull()
        }
    }

    // --- Logica de marcar como explorada -----------------------------------

    @Test
    fun `al inicio ninguna letra esta explorada`() = runTest {
        val vm = viewModel()

        vm.uiState.test {
            val state = awaitItem()
            assertThat(state.exploredCount).isEqualTo(0)
            assertThat(state.letters.all { !it.isExplored }).isTrue()
        }
    }

    @Test
    fun `markExplored marca solo esa letra y sube el contador`() = runTest {
        val repository = FakeProgressRepository()
        val vm = viewModel(repository)

        vm.markExplored(letraA)

        vm.uiState.test {
            val state = awaitItem()
            assertThat(state.exploredCount).isEqualTo(1)
            val cardA = state.letters.first { it.letter.char == 'A' }
            val cardB = state.letters.first { it.letter.char == 'B' }
            assertThat(cardA.isExplored).isTrue()
            assertThat(cardB.isExplored).isFalse()
        }
    }

    @Test
    fun `explorar una letra no se puede deshacer, es un registro acumulativo`() = runTest {
        val repository = FakeProgressRepository()
        val vm = viewModel(repository)

        vm.markExplored(letraA)
        vm.markExplored(letraA)
        vm.markExplored(letraB)

        vm.uiState.test {
            val state = awaitItem()
            assertThat(state.exploredCount).isEqualTo(2)
        }
        assertThat(repository.markExploredCallCount).isEqualTo(3)
    }

    @Test
    fun `el progreso ya guardado se refleja al crear el ViewModel`() = runTest {
        val repository = FakeProgressRepository(initiallyExplored = setOf('A'))
        val vm = viewModel(repository)

        vm.uiState.test {
            val state = awaitItem()
            assertThat(state.exploredCount).isEqualTo(1)
            assertThat(state.letters.first { it.letter.char == 'A' }.isExplored).isTrue()
        }
    }

    // --- Logica de palabra / emoji por letra --------------------------------

    @Test
    fun `wordFor devuelve la palabra correcta para una letra conocida`() {
        assertThat(LetrasViewModel.wordFor('A')).isEqualTo("Abeja")
    }

    @Test
    fun `emojiFor devuelve el emoji correcto para una letra conocida`() {
        assertThat(LetrasViewModel.emojiFor('A')).isEqualTo("🐝")
    }

    @Test
    fun `wordFor y emojiFor no distinguen mayusculas o minusculas`() {
        assertThat(LetrasViewModel.wordFor('a')).isEqualTo("Abeja")
        assertThat(LetrasViewModel.emojiFor('a')).isEqualTo("🐝")
    }

    @Test
    fun `wordFor y emojiFor devuelven null para un caracter que no es letra del abecedario`() {
        assertThat(LetrasViewModel.wordFor('5')).isNull()
        assertThat(LetrasViewModel.emojiFor('5')).isNull()
    }
}
