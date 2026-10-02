package com.mbeltran.montessoriletras.data

import com.mbeltran.montessoriletras.model.Letter

/**
 * Fuente unica de verdad para el abecedario en espanol que usa la app,
 * con una palabra y un emoji representativo por cada letra.
 *
 * Se usan emojis en vez de ilustraciones con licencia: es la solucion mas
 * practica para un prototipo de portafolio sin un diseñador grafico, y
 * Android los renderiza nativamente en cualquier dispositivo.
 */
object LetrasData {

    val alphabet: List<Letter> = listOf(
        Letter('A', "Abeja", "🐝"),       // 🐝
        Letter('B', "Barco", "⛵"),             // ⛵
        Letter('C', "Casa", "🏠"),        // 🏠
        Letter('D', "Dado", "🎲"),        // 🎲
        Letter('E', "Elefante", "🐘"),    // 🐘
        Letter('F', "Flor", "🌸"),        // 🌸
        Letter('G', "Gato", "🐱"),        // 🐱
        Letter('H', "Helado", "🍦"),      // 🍦
        Letter('I', "Iguana", "🦎"),      // 🦎
        Letter('J', "Jirafa", "🦒"),      // 🦒
        Letter('K', "Koala", "🐨"),       // 🐨
        Letter('L', "Luna", "🌙"),        // 🌙
        Letter('M', "Mariposa", "🦋"),    // 🦋
        Letter('N', "Nube", "☁"),              // ☁
        Letter('Ñ', "Ñandú", "🐦"),       // 🐦
        Letter('O', "Oso", "🐻"),         // 🐻
        Letter('P', "Pato", "🦆"),        // 🦆
        Letter('Q', "Queso", "🧀"),       // 🧀
        Letter('R', "Raton", "🐭"),       // 🐭
        Letter('S', "Sol", "☀"),               // ☀
        Letter('T', "Tortuga", "🐢"),     // 🐢
        Letter('U', "Uva", "🍇"),         // 🍇
        Letter('V', "Vaca", "🐮"),        // 🐮
        Letter('W', "Wafle", "🧇"),       // 🧇
        Letter('X', "Xilofono", "🎹"),    // 🎹
        Letter('Y', "Yoyo", "🪀"),        // 🪀
        Letter('Z', "Zorro", "🦊")        // 🦊
    )

    fun byChar(char: Char): Letter? =
        alphabet.firstOrNull { it.char.equals(char, ignoreCase = true) }
}
