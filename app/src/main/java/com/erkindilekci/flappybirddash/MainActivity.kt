package com.erkindilekci.flappybirddash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.erkindilekci.flappybirddash.model.GameAction
import com.erkindilekci.flappybirddash.model.GameStatus
import com.erkindilekci.flappybirddash.ui.theme.FlappyBirdDashTheme
import com.erkindilekci.flappybirddash.util.SplashScreenController
import com.erkindilekci.flappybirddash.view.Clickable
import com.erkindilekci.flappybirddash.view.GameScreen
import com.erkindilekci.flappybirddash.viewmodel.GameViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        setContent {
            FlappyBirdDashTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setStatusBarColor(Color.Transparent)
                }

                Surface(color = MaterialTheme.colorScheme.background) {
                    val gameViewModel: GameViewModel = viewModel()

                    LaunchedEffect(key1 = Unit) {
                        while (isActive) {
                            delay(AutoTickDuration)
                            if (gameViewModel.viewState.value.gameStatus != GameStatus.Waiting) {
                                gameViewModel.dispatch(GameAction.AutoTick)
                            }
                        }
                    }

                    Flappy(
                        Clickable(
                            onStart = { gameViewModel.dispatch(GameAction.Start) },
                            onTap = { gameViewModel.dispatch(GameAction.TouchLift) },
                            onRestart = { gameViewModel.dispatch(GameAction.Restart) },
                            onExit = { finish() }
                        )
                    )
                }
            }
        }

        SplashScreenController(splashScreen, viewModel).apply {
            customizeSplashScreen()
        }
    }
}

@Composable
fun Flappy(clickable: Clickable = Clickable()) {
    GameScreen(clickable = clickable)
}

const val AutoTickDuration = 50L
