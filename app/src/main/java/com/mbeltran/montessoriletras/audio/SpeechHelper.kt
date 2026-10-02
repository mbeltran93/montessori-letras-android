package com.mbeltran.montessoriletras.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * Envoltorio muy simple sobre [TextToSpeech] para leer en voz alta la letra,
 * la palabra asociada y los mensajes de refuerzo positivo.
 *
 * Se usa TTS nativo de Android en vez de grabar audio: funciona offline una
 * vez que el paquete de voz en espanol esta instalado en el dispositivo, y
 * evita tener que conseguir/licenciar locuciones.
 */
class SpeechHelper(context: Context) {

    private var isReady = false
    private var pendingText: String? = null

    private lateinit var tts: TextToSpeech

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            isReady = status == TextToSpeech.SUCCESS
            if (isReady) {
                tts.language = Locale("es", "MX")
                pendingText?.let { speak(it) }
                pendingText = null
            }
        }
    }

    fun speak(text: String) {
        if (!isReady) {
            pendingText = text
            return
        }
        tts.stop()
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "montessori_letras_utterance")
    }

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}
