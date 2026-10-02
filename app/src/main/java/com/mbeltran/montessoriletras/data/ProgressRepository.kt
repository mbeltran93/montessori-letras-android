package com.mbeltran.montessoriletras.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Guarda que letras explorO el nino. No hay puntajes ni "aciertos": es
 * simplemente un registro de que ya visito esa letra, para pintarla con un
 * borde dorado en la pantalla principal.
 *
 * Es una interfaz para poder probar el ViewModel con un fake en los tests
 * unitarios, sin depender de un Context de Android ni de DataStore real.
 */
interface ProgressRepository {
    val exploredLetters: Flow<Set<Char>>
    suspend fun markExplored(letter: Char)
}

private val Context.progressDataStore by preferencesDataStore(name = "progreso_letras")

class DataStoreProgressRepository(private val context: Context) : ProgressRepository {

    companion object {
        private val EXPLORED_KEY = stringSetPreferencesKey("letras_exploradas")
    }

    override val exploredLetters: Flow<Set<Char>> =
        context.progressDataStore.data.map { prefs: Preferences ->
            (prefs[EXPLORED_KEY] ?: emptySet()).mapNotNull { it.firstOrNull() }.toSet()
        }

    override suspend fun markExplored(letter: Char) {
        context.progressDataStore.edit { prefs ->
            val current = prefs[EXPLORED_KEY] ?: emptySet()
            prefs[EXPLORED_KEY] = current + letter.toString()
        }
    }
}
