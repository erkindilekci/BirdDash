package com.erkindilekci.flappybirddash.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.erkindilekci.flappybirddash.model.GameAction
import com.erkindilekci.flappybirddash.model.PipeResetThreshold
import com.erkindilekci.flappybirddash.model.ViewState
import com.erkindilekci.flappybirddash.viewmodel.GameViewModel

@Composable
fun PipeCouple(
    modifier: Modifier = Modifier,
    state: ViewState = ViewState(),
    pipeIndex: Int = 0
) {
    val viewModel: GameViewModel = viewModel()

    val pipeState = state.pipeStateList[pipeIndex]

    Box(
        modifier
    ) {
        GetUpPipe(
            height = pipeState.upHeight,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = pipeState.offset)
        )

        GetDownPipe(
            height = pipeState.downHeight,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = pipeState.offset)
        )

        if (state.playZoneSize.first > 0) {
            val playZoneWidthInDP = with(LocalDensity.current) {
                state.playZoneSize.first.toDp()
            }

            if (pipeState.offset < -playZoneWidthInDP - PipeResetThreshold) {
                viewModel.dispatch(GameAction.PipeExit, pipeIndex = pipeIndex)
            }
        }
    }
}
