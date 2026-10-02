# Letras y Sonidos 🔤

![CI](https://github.com/mbeltran93/montessori-letras-android/actions/workflows/ci.yml/badge.svg)

Juego educativo para Android, estilo **Montessori**, para ninos de 2 a 4 anos:
tocan una letra grande y colorida, escuchan su sonido, ven una palabra e
imagen asociada, y la trazan libremente con el dedo. Sin cuentas, sin
anuncios, sin internet, sin la nocion de "error".

Es un proyecto de portafolio: a diferencia de los demas repos (APIs y
backends), este muestra desarrollo **Android nativo con Kotlin + Jetpack
Compose** de punta a punta — UI, estado, persistencia local y testing.

## La app, en palabras

**Pantalla principal.** Un grid de tarjetas grandes y brillantes, una por
cada letra del abecedario en espanol (A a la Z, mas la Ñ: 27 en total). Cada
tarjeta usa un color distinto de una paleta calida (amarillo sol, celeste,
verde, coral, violeta, naranja...). Arriba se ve un contador simple:
"Exploradas: 4 / 27". Las letras que el nino ya visito tienen un borde
dorado y una estrellita en la esquina — nunca un numero de errores, nunca
una tacha.

**Pantalla de la letra.** Al tocar una tarjeta, la letra se muestra enorme
en el centro. Apenas entra, la app lee en voz alta ("A. A de Abeja.") usando
el sintetizador de voz nativo de Android, en espanol. Debajo se ve la
palabra con su emoji ("A de Abeja 🐝"). Hay un boton para volver a escuchar
el sonido las veces que haga falta. Mas abajo, un area de trazado muestra la
letra en un gris muy tenue como guia ("fantasma"), y el nino puede dibujar
encima con el dedo: cualquier trazo, prolijo o no, dispara — despues de un
par de segundos de estar dibujando — una animacion de estrellitas y un
"¡Muy bien!" por voz. No hay boton de "corregir" ni mensaje de "intenta de
nuevo": la interaccion en si misma ya es el logro.

**Progreso.** Simplemente cuenta cuantas letras distintas visito el nino
(no cuantas "aprobo", porque no hay tal cosa). Se guarda en el dispositivo
con Jetpack DataStore y se mantiene entre sesiones; no hay login ni cuentas.

## Filosofia Montessori detras de las decisiones de diseno

- **No existe el error.** No hay validacion de trazos correctos/incorrectos,
  ni mensajes de "mal hecho". El metodo Montessori se apoya en la
  exploracion autodirigida: el adulto (o la app) prepara el ambiente, y el
  nino aprende al manipularlo a su propio ritmo, sin miedo a fallar.
- **Refuerzo siempre positivo, nunca condicional.** Las estrellitas y el
  "¡Muy bien!" aparecen por el solo hecho de participar, no por acertar algo.
- **Targets de toque grandes (64dp o mas).** La motricidad fina de un nino
  de 2-4 anos todavia esta en desarrollo; las tarjetas del grid y los
  botones son deliberadamente grandes para que un dedo chiquito e impreciso
  los pueda activar sin frustracion.
- **Materiales concretos y sensoriales.** El metodo Montessori usa letras de
  lija para que el nino las "sienta" antes de escribirlas. Esta app imita
  esa idea con el trazado libre sobre un contorno guia, en vez de un
  teclado abstracto.
- **Sin recompensas externas que compitan entre si.** No hay puntajes,
  rankings ni comparacion entre ninos: solo el propio avance exploratorio.
- **Sin internet, sin anuncios, sin tracking.** Un ambiente Montessori es un
  ambiente controlado y seguro; la app es 100% offline y no recolecta nada.

## Arquitectura

- **Kotlin + Jetpack Compose (Material 3).**
- **MVVM**: un unico `LetrasViewModel` expone:
  - `uiState: StateFlow<LetrasUiState>` — el grid de letras con su estado de
    exploracion y el contador, derivado de `ProgressRepository.exploredLetters`.
  - `selectedLetter: StateFlow<Letter?>` — que letra esta activa.
  - `selectLetter()`, `clearSelection()`, `markExplored()` como unicas
    acciones (no hay logica de "correcto").
- **Datos**: `LetrasData` es la fuente unica de verdad del abecedario
  (letra → palabra → emoji). `ProgressRepository` es una interfaz; la
  implementacion real (`DataStoreProgressRepository`) persiste el set de
  letras exploradas con **Jetpack DataStore (Preferences)**. En los tests
  unitarios se usa un `FakeProgressRepository` en memoria, sin tocar
  Android ni el disco.
- **Audio**: `SpeechHelper` envuelve `android.speech.tts.TextToSpeech`
  (`Locale("es", "MX")`). No se grabo audio: TTS nativo alcanza y funciona
  offline una vez instalado el paquete de voz en espanol del sistema.
- **Navegacion**: Jetpack Navigation Compose, dos rutas (`home`,
  `letter/{char}`), con el `LetrasViewModel` compartido entre pantallas.
- **Iconografia**: emojis nativos de Android en vez de ilustraciones con
  licencia — la solucion mas practica sin un diseñador grafico dedicado.

## Como compilar e instalar

Requiere el Android SDK (API 34, build-tools 34.0.0) y JDK 17+.

```bash
# Compilar el APK de debug
./gradlew assembleDebug

# El APK queda en:
# app/build/outputs/apk/debug/app-debug.apk

# Instalar en un dispositivo/emulador conectado
./gradlew installDebug
```

## Como correr los tests

```bash
# Tests unitarios (ViewModel, logica de datos) — corren en la JVM, sin emulador
./gradlew testDebugUnitTest

# Tests instrumentados de UI (Compose UI Testing) — requieren un
# emulador o dispositivo conectado
./gradlew connectedDebugAndroidTest
```

- **Unit tests** (`app/src/test/.../LetrasViewModelTest.kt`,
  `LetrasDataTest.kt`): cubren la seleccion de letra, la logica de marcar
  una letra como explorada (y que ese registro es acumulativo, nunca se
  "deshace"), y que cada letra del abecedario mapea a la palabra/emoji
  correcto.
- **UI tests** (`app/src/androidTest/.../LetrasFlowInstrumentedTest.kt`):
  simulan a un nino real con `androidx.compose.ui.test` — abren la pantalla
  principal, tocan la tarjeta de la "A", verifican que se navega a su
  pantalla de detalle y se ve "A de Abeja 🐝", vuelven atras, y verifican
  que la tarjeta de la "A" ya aparece marcada como explorada.

## CI

`.github/workflows/ci.yml` corre en cada push/PR a `main`:

1. `assembleDebug` (compila el APK de debug).
2. `testDebugUnitTest` (los tests unitarios de arriba).
3. `connectedDebugAndroidTest` contra un **emulador Android real**, levantado
   en el runner de GitHub Actions con
   [`reactivecircus/android-emulator-runner`](https://github.com/reactivecircus/android-emulator-runner)
   (API 30, imagen `google_apis`, x86_64) — es decir, los tests de UI de
   verdad se ejecutan en CI, con un emulador de punta a punta.

## Limitaciones conocidas

- Esta maquina de desarrollo es una VM anidada (virtualizacion anidada no
  disponible), por lo que no fue posible levantar un AVD local para correr
  los tests instrumentados de Compose UI en esta sesion. Si corrieron de
  verdad localmente: la **compilacion** (`assembleDebug`) y los **tests
  unitarios** (`testDebugUnitTest`, 19 tests, todos verdes). Los tests de UI
  (`LetrasFlowInstrumentedTest.kt`) quedaron escritos y se ejecutan en el
  pipeline de GitHub Actions contra un emulador real — confirmar el
  resultado en la pestana "Actions" del repo.
- El sonido depende de que el dispositivo tenga instalado el paquete de voz
  en espanol de Android TTS. En dispositivos sin ese paquete, Android suele
  ofrecer instalarlo automaticamente la primera vez que una app pide TTS en
  un idioma no disponible.
- No hay ilustraciones originales: se usan emojis del sistema como
  representacion de cada palabra, por ser la opcion mas practica sin un
  diseñador dedicado en un proyecto de portafolio.
