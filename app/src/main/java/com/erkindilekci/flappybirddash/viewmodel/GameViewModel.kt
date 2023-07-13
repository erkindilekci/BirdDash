package com.erkindilekci.flappybirddash.viewmodel

import android.app.Application
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.erkindilekci.flappybirddash.model.BirdPipeDistanceFraction
import com.erkindilekci.flappybirddash.model.BirdSizeHeight
import com.erkindilekci.flappybirddash.model.BirdSizeWidth
import com.erkindilekci.flappybirddash.model.CenterPipeFraction
import com.erkindilekci.flappybirddash.model.GameAction
import com.erkindilekci.flappybirddash.model.GameStatus
import com.erkindilekci.flappybirddash.model.HighPipe
import com.erkindilekci.flappybirddash.model.LowPipe
import com.erkindilekci.flappybirddash.model.MaxPipeFraction
import com.erkindilekci.flappybirddash.model.MiddlePipe
import com.erkindilekci.flappybirddash.model.MinPipeFraction
import com.erkindilekci.flappybirddash.model.PipeDistance
import com.erkindilekci.flappybirddash.model.PipeDistanceFraction
import com.erkindilekci.flappybirddash.model.PipeState
import com.erkindilekci.flappybirddash.model.RoadState
import com.erkindilekci.flappybirddash.model.TotalPipeHeight
import com.erkindilekci.flappybirddash.model.ViewState
import com.erkindilekci.flappybirddash.util.DensityUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    fun dispatch(
        action: GameAction,
        playZoneSize: Pair<Int, Int> = Pair(0, 0),
        pipeIndex: Int = -1,
        roadIndex: Int = -1
    ) {
        if (playZoneSize.first > 0 && playZoneSize.second > 0) {
            viewState.value.playZoneSize = playZoneSize
        }

        if (pipeIndex > -1) {
            viewState.value.targetPipeIndex = pipeIndex
        }

        if (roadIndex > -1) {
            viewState.value.targetRoadIndex = roadIndex
        }

        response(action, viewState.value)
    }

    private fun response(action: GameAction, state: ViewState) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                emit(when (action) {
                    GameAction.Start -> run {
                        state.copy(
                            gameStatus = GameStatus.Running,
                        )
                    }

                    GameAction.AutoTick -> run {
                        if (state.gameStatus == GameStatus.Waiting) {
                            return@run state
                        }

                        if (state.gameStatus == GameStatus.Dying) {
                            val newBirdState = state.birdState.quickFall()
                            return@run state.copy(
                                birdState = newBirdState
                            )
                        }

                        if (state.gameStatus == GameStatus.Over) {
                            return@run state.copy()
                        }

                        val newPipeStateList: List<PipeState> = listOf(
                            state.pipeStateList[0].move(),
                            state.pipeStateList[1].move()
                        )

                        val newBirdState = state.birdState.fall()

                        val newRoadStateList: List<RoadState> = listOf(
                            state.roadStateList[0].move(),
                            state.roadStateList[1].move()
                        )

                        state.copy(
                            gameStatus = GameStatus.Running,
                            birdState = newBirdState,
                            pipeStateList = newPipeStateList,
                            roadStateList = newRoadStateList
                        )
                    }

                    GameAction.TouchLift -> run {
                        if (state.gameStatus == GameStatus.Over) {
                            return@run state.copy()
                        }

                        if (state.gameStatus == GameStatus.Dying) {
                            return@run state.copy()
                        }

                        val newBirdState = state.birdState.lift()

                        state.copy(
                            gameStatus = GameStatus.Running,
                            birdState = newBirdState
                        )
                    }

                    GameAction.ScreenSizeDetect -> run {
                        val playZoneHeightInDp = DensityUtil.dxToDp(
                            getApplication<Application>().resources,
                            state.playZoneSize.second
                        )

                        TotalPipeHeight = playZoneHeightInDp.dp
                        HighPipe = TotalPipeHeight * MaxPipeFraction
                        MiddlePipe = TotalPipeHeight * CenterPipeFraction
                        LowPipe = TotalPipeHeight * MinPipeFraction
                        PipeDistance = TotalPipeHeight * PipeDistanceFraction

                        BirdSizeHeight = PipeDistance * BirdPipeDistanceFraction
                        BirdSizeWidth = BirdSizeHeight * 1.44f

                        val newPipeStateList: List<PipeState> = listOf(
                            state.pipeStateList[0].correct(),
                            state.pipeStateList[1].correct()
                        )

                        val newBirdState = state.birdState.correct()

                        state.copy(
                            birdState = newBirdState,
                            pipeStateList = newPipeStateList
                        )
                    }

                    GameAction.PipeExit -> run {
                        val newPipeStateList: List<PipeState> =
                            if (state.targetPipeIndex == 0) {
                                listOf(
                                    state.pipeStateList[0].reset(),
                                    state.pipeStateList[1]
                                )
                            } else {
                                listOf(
                                    state.pipeStateList[0],
                                    state.pipeStateList[1].reset()
                                )
                            }

                        state.copy(
                            gameStatus = GameStatus.Running,
                            pipeStateList = newPipeStateList
                        )
                    }

                    GameAction.RoadExit -> run {
                        val newRoadState: List<RoadState> =
                            if (state.targetRoadIndex == 0) {
                                listOf(state.roadStateList[0].reset(), state.roadStateList[1])
                            } else {
                                listOf(state.roadStateList[0], state.roadStateList[1].reset())
                            }

                        state.copy(
                            gameStatus = GameStatus.Running,
                            roadStateList = newRoadState
                        )
                    }

                    GameAction.HitGround -> run {
                        state.copy(
                            gameStatus = GameStatus.Over
                        )
                    }

                    GameAction.HitPipe -> run {
                        if (state.gameStatus == GameStatus.Dying) {
                            return@run state.copy()
                        }

                        val newBirdState = state.birdState.quickFall()

                        state.copy(
                            gameStatus = GameStatus.Dying,
                            birdState = newBirdState
                        )
                    }

                    GameAction.CrossedPipe -> run {
                        val targetPipeState = state.pipeStateList[state.targetPipeIndex]

                        if (targetPipeState.counted
                            || (!targetPipeState.counted && targetPipeState.offset > 0.dp)
                        ) {
                            return@run state.copy()
                        }

                        val countedPipeState = targetPipeState.count()
                        val newPipeStateList = if (state.targetPipeIndex == 0) {
                            listOf(countedPipeState, state.pipeStateList[1])
                        } else {
                            listOf(state.pipeStateList[0], countedPipeState)
                        }

                        state.copy(
                            pipeStateList = newPipeStateList,
                            score = state.score + 1,
                            bestScore = (state.score + 1).coerceAtLeast(state.bestScore)
                        )
                    }

                    GameAction.Restart -> run {
                        state.reset(state.bestScore)
                    }
                })
            }
        }
    }

    private fun emit(state: ViewState) {
        _viewState.value = state
    }
}
