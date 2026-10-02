package com.mbeltran.montessoriletras

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Simula a un nino usando la app de punta a punta:
 *  1. Abre la pantalla principal y ve el grid de letras.
 *  2. Toca la letra "A".
 *  3. Llega a la pantalla de la letra y ve "A de Abeja" con su emoji.
 *  4. Vuelve atras.
 *  5. La tarjeta de la "A" ahora aparece marcada como explorada.
 *
 * Corre contra un emulador real (ver .github/workflows/ci.yml); en esta
 * maquina de desarrollo no se pudo levantar un AVD, asi que este test no se
 * ejecuto localmente (ver README, seccion "Limitaciones").
 */
@RunWith(AndroidJUnit4::class)
class LetrasFlowInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun tocarUnaLetra_navegaADetalle_yQuedaMarcadaComoExploradaAlVolver() {
        // Pantalla principal: el grid muestra la tarjeta de la "A", sin explorar.
        composeTestRule
            .onNodeWithTag("letter_card_A")
            .assert(hasContentDescription("Letra A"))

        // El nino toca la letra "A".
        composeTestRule.onNodeWithTag("letter_card_A").performClick()
        composeTestRule.waitForIdle()

        // Pantalla de detalle: se ve la letra enorme y "A de Abeja" con su emoji.
        composeTestRule.onNodeWithTag("letter_big_text").assertTextEquals("A")
        composeTestRule.onNodeWithTag("letter_word_emoji").assertTextContains(
            "Abeja",
            substring = true
        )

        // Vuelve a la pantalla principal.
        composeTestRule.onNodeWithTag("back_button").performClick()
        composeTestRule.waitForIdle()

        // La exploracion quedo guardada: ahora la tarjeta esta marcada.
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodesWithContentDescription("Letra A, ya explorada")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeTestRule
            .onNodeWithTag("letter_card_A")
            .assert(hasContentDescription("Letra A, ya explorada"))
    }

    @Test
    fun elGridMuestraLas27LetrasDelAbecedarioEnEspanol() {
        composeTestRule.onNodeWithTag("letter_card_A").assertExists()
        composeTestRule.onNodeWithTag("letter_card_Ñ").assertExists()
        composeTestRule.onNodeWithTag("letter_card_Z").assertExists()
    }
}
