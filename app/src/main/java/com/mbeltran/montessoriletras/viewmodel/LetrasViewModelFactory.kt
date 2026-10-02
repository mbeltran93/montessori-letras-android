package com.mbeltran.montessoriletras.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mbeltran.montessoriletras.data.DataStoreProgressRepository

/**
 * Fabrica manual del ViewModel (sin Hilt: la app es chica y no lo necesita).
 * Crea el repositorio real respaldado por DataStore a partir del
 * [Context] de la aplicacion.
 */
class LetrasViewModelFactory(private val appContext: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == LetrasViewModel::class.java) {
            "LetrasViewModelFactory solo sabe crear LetrasViewModel"
        }
        val repository = DataStoreProgressRepository(appContext)
        return LetrasViewModel(repository) as T
    }
}
