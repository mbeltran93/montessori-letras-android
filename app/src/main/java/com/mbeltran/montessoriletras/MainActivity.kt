package com.mbeltran.montessoriletras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mbeltran.montessoriletras.audio.SpeechHelper
import com.mbeltran.montessoriletras.ui.AppNavigation
import com.mbeltran.montessoriletras.ui.theme.MontessoriLetrasTheme
import com.mbeltran.montessoriletras.viewmodel.LetrasViewModel
import com.mbeltran.montessoriletras.viewmodel.LetrasViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: LetrasViewModel by viewModels {
        LetrasViewModelFactory(applicationContext)
    }

    private lateinit var speechHelper: SpeechHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        speechHelper = SpeechHelper(applicationContext)

        setContent {
            MontessoriLetrasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(viewModel = viewModel, speechHelper = speechHelper)
                }
            }
        }
    }

    override fun onDestroy() {
        speechHelper.shutdown()
        super.onDestroy()
    }
}
