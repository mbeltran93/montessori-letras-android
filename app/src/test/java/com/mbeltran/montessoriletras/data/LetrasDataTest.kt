package com.mbeltran.montessoriletras.data

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LetrasDataTest {

    @Test
    fun `el abecedario tiene 27 letras, las 26 de la A a la Z mas la enie`() {
        assertThat(LetrasData.alphabet).hasSize(27)
        assertThat(LetrasData.alphabet.map { it.char }).contains('Ñ')
    }

    @Test
    fun `cada letra tiene una palabra y un emoji no vacios`() {
        LetrasData.alphabet.forEach { letter ->
            assertThat(letter.word).isNotEmpty()
            assertThat(letter.emoji).isNotEmpty()
        }
    }

    @Test
    fun `la palabra de cada letra empieza con esa misma letra`() {
        LetrasData.alphabet.forEach { letter ->
            val primeraLetraPalabra = letter.word.first().uppercaseChar()
            assertThat(primeraLetraPalabra).isEqualTo(letter.char.uppercaseChar())
        }
    }

    @Test
    fun `no hay letras repetidas`() {
        val chars = LetrasData.alphabet.map { it.char }
        assertThat(chars.toSet()).hasSize(chars.size)
    }

    @Test
    fun `byChar encuentra la letra A con su palabra Abeja`() {
        val letter = LetrasData.byChar('A')
        assertThat(letter).isNotNull()
        assertThat(letter?.word).isEqualTo("Abeja")
    }

    @Test
    fun `byChar no distingue mayusculas de minusculas`() {
        assertThat(LetrasData.byChar('a')).isEqualTo(LetrasData.byChar('A'))
    }

    @Test
    fun `byChar devuelve null para un caracter que no esta en el abecedario`() {
        assertThat(LetrasData.byChar('5')).isNull()
    }
}
