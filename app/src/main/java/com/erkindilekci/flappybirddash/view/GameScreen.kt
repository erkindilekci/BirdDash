package com.erkindilekci.flappybirddash.view

import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.erkindilekci.flappybirddash.*
import com.erkindilekci.flappybirddash.model.*
import com.erkindilekci.flappybirddash.ui.theme.ForegroundEarthYellow
import com.erkindilekci.flappybirddash.viewmodel.GameViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameScreen(
    clickable: Clickable = Clickable()
) {
    val viewModel: GameViewModel = viewModel()
    val viewState by viewModel.viewState.collectAsState()
    var screenSize: Pair<Int, Int>

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForegroundEarthYellow)
            .run {
                pointerInteropFilter {
                    when (it.action) {
                        ACTION_DOWN -> {
                            if (viewState.gameStatus == GameStatus.Waiting) clickable.onStart()
                            else if (viewState.gameStatus == GameStatus.Running) clickable.onTap()
                            else return@pointerInteropFilter false
                        }


                        MotionEvent.ACTION_MOVE -> {
                            return@pointerInteropFilter false
                        }

                        MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                            return@pointerInteropFilter false
                        }
                    }

                    false
                }
            }
    ) {

        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .onGloballyPositioned {
                screenSize = Pair(it.size.width, it.size.height)

                if (viewState.playZoneSize.first <= 0 || viewState.playZoneSize.second <= 0) {
                    viewModel.dispatch(GameAction.ScreenSizeDetect, screenSize)
                }
            }
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
        ) {

            FarBackground(Modifier.fillMaxSize())

            PipeCouple(
                modifier = Modifier.fillMaxSize(),
                state = viewState,
                pipeIndex = 0
            )

            PipeCouple(
                modifier = Modifier.fillMaxSize(),
                state = viewState,
                pipeIndex = 1
            )

            ScoreBoard(
                modifier = Modifier.fillMaxSize(),
                state = viewState,
                clickable = clickable
            )

            val playZoneWidthInDP = with(LocalDensity.current) {
                viewState.playZoneSize.first.toDp()
            }

            val playZoneHeightInDP = with(LocalDensity.current) {
                viewState.playZoneSize.second.toDp()
            }



            if (viewState.gameStatus == GameStatus.Running) {
                viewState.pipeStateList.forEachIndexed { pipeIndex, pipeState ->
                    checkPipeStatus(
                        viewState.birdState.birdHeight,
                        pipeState,
                        playZoneWidthInDP,
                        playZoneHeightInDP
                    ).also {
                        when (it) {
                            PipeStatus.BirdHit -> {
                                viewModel.dispatch(GameAction.HitPipe)
                            }

                            PipeStatus.BirdCrossed -> {
                                viewModel.dispatch(GameAction.CrossedPipe, pipeIndex = pipeIndex)
                            }

                            else -> {}
                        }
                    }
                }
            }

            Bird(
                modifier = Modifier.fillMaxSize(),
                state = viewState
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
        ) {
            NearForeground(
                modifier = Modifier.fillMaxSize(),
                state = viewState
            )
        }
    }
}

@Composable
fun checkPipeStatus(
    birdHeightOffset: Dp,
    pipeState: PipeState,
    zoneWidth: Dp,
    zoneHeight: Dp
): PipeStatus {
    if (pipeState.offset - PipeCoverWidth > -zoneWidth / 2 + BirdSizeWidth / 2) {
        return PipeStatus.BirdComing
    } else if (pipeState.offset - PipeCoverWidth < -zoneWidth / 2 - BirdSizeWidth / 2) {
        return PipeStatus.BirdCrossed
    } else {
        val birdTop = (zoneHeight - BirdSizeHeight) / 2 + birdHeightOffset
        val birdBottom = (zoneHeight + BirdSizeHeight) / 2 + birdHeightOffset

        if (birdTop < pipeState.upHeight || birdBottom > zoneHeight - pipeState.downHeight) {
            return PipeStatus.BirdHit
        }

        return PipeStatus.BirdCrossing
    }
}

data class Clickable(
    val onStart: () -> Unit = {},
    val onTap: () -> Unit = {},
    val onRestart: () -> Unit = {},
    val onExit: () -> Unit = {}
)
