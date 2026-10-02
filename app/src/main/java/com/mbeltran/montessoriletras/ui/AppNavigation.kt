package com.mbeltran.montessoriletras.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mbeltran.montessoriletras.audio.SpeechHelper
import com.mbeltran.montessoriletras.data.LetrasData
import com.mbeltran.montessoriletras.ui.screens.HomeScreen
import com.mbeltran.montessoriletras.ui.screens.LetterScreen
import com.mbeltran.montessoriletras.viewmodel.LetrasViewModel

object Routes {
    const val HOME = "home"
    const val LETTER = "letter/{char}"
    fun letter(char: Char) = "letter/$char"
}

@Composable
fun AppNavigation(
    viewModel: LetrasViewModel,
    speechHelper: SpeechHelper,
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                uiState = uiState,
                onLetterClick = { card ->
                    viewModel.selectLetter(card.letter)
                    navController.navigate(Routes.letter(card.letter.char))
                }
            )
        }
        composable(Routes.LETTER) { backStackEntry ->
            val charArg = backStackEntry.arguments?.getString("char")?.firstOrNull()
            val letter = charArg?.let { LetrasData.byChar(it) }
            if (letter != null) {
                LaunchedEffect(letter.char) {
                    viewModel.markExplored(letter)
                    speechHelper.speak(letter.spokenPrompt)
                }
                LetterScreen(
                    letter = letter,
                    onBack = {
                        viewModel.clearSelection()
                        navController.popBackStack()
                    },
                    onRepeatSound = { speechHelper.speak(letter.spokenPrompt) },
                    onCelebrate = { speechHelper.speak("¡Muy bien!") }
                )
            }
        }
    }
}
