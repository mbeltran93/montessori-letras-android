package com.mbeltran.montessoriletras.model

/**
 * Una letra del abecedario junto con la palabra y el emoji que la representan
 * en la pantalla de detalle (ej. "A de Abeja" + 🐝).
 *
 * No hay "letras correctas o incorrectas": cada una es simplemente un punto
 * de exploracion para el nino.
 */
data class Letter(
    val char: Char,
    val word: String,
    val emoji: String
) {
    /** Texto que se lee en voz alta al entrar a la pantalla de la letra. */
    val spokenPrompt: String
        get() = "$char. $char de $word."
}
