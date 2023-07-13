package com.erkindilekci.flappybirddash.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.erkindilekci.flappybirddash.R
import com.erkindilekci.flappybirddash.model.BirdHitGroundThreshold
import com.erkindilekci.flappybirddash.model.GameAction
import com.erkindilekci.flappybirddash.model.ViewState
import com.erkindilekci.flappybirddash.viewmodel.GameViewModel

@Composable
fun Bird(
    modifier: Modifier = Modifier,
    state: ViewState = ViewState()
) {
    val viewModel: GameViewModel = viewModel()

    val rotateDegree =
        if (state.isLifting) LiftingDegree
        else if (state.isFalling) FallingDegree
        else if (state.isQuickFalling) DyingDegree
        else if (state.isOver) DeadDegree
        else PendingDegree

    Box(
        modifier
    ) {
        var correctBirdHeight = state.birdState.birdHeight

        if (state.playZoneSize.second > 0) {
            val playZoneHeightInDP = with(LocalDensity.current) {
                state.playZoneSize.second.toDp()
            }

            val fallingThreshold = BirdHitGroundThreshold

            if (correctBirdHeight + fallingThreshold >= playZoneHeightInDP / 2) {
                viewModel.dispatch(GameAction.HitGround)
                correctBirdHeight = playZoneHeightInDP / 2 - fallingThreshold
            }
        }

        Image(
            painter = painterResource(id = R.drawable.bird_match),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = Modifier
                .size(state.birdState.birdW, state.birdState.birdH)
                .align(Alignment.Center)
                .offset(y = correctBirdHeight)
                .rotate(rotateDegree)
        )
    }
}

const val PendingDegree = 0f
const val LiftingDegree = -10f
const val FallingDegree = -LiftingDegree
const val DyingDegree = FallingDegree + 10f
const val DeadDegree = DyingDegree - 10f
