package com.erkindilekci.flappybirddash.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erkindilekci.flappybirddash.R
import com.erkindilekci.flappybirddash.model.GameStatus
import com.erkindilekci.flappybirddash.model.ViewState
import com.erkindilekci.flappybirddash.ui.theme.GroundDividerPurple
import com.erkindilekci.flappybirddash.ui.theme.ScoreFontFamily

@Composable
fun ScoreBoard(
    modifier: Modifier = Modifier,
    state: ViewState = ViewState(),
    clickable: Clickable = Clickable()
) {
    when (state.gameStatus) {
        GameStatus.Running -> RealTimeBoard(modifier, state.score)
        GameStatus.Over -> GameOverBoard(modifier, state.score, state.bestScore, clickable)
        else -> {}
    }
}

@Composable
fun RealTimeBoard(modifier: Modifier, score: Int = 13) {
    Box(
        modifier.fillMaxSize()
    ) {
        Text(
            text = score.toString(),
            modifier = Modifier
                .align(Center)
                .offset(y = RealTimeScoreHeightOffset),
            color = GroundDividerPurple,
            fontSize = SimpleScoreTextSize,
            fontFamily = ScoreFontFamily
        )
    }
}

@Composable
fun GameOverBoard(
    modifier: Modifier,
    score: Int = 13,
    maxScore: Int = 100,
    clickable: Clickable = Clickable()
) {
    Box(
        modifier
    ) {
        Column(
            modifier
                .wrapContentSize()
                .offset(y = OverScoreHeightOffset)
                .align(Center)
        ) {
            GameOverScoreBoard(
                Modifier.align(CenterHorizontally),
                score,
                maxScore
            )

            Spacer(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(40.dp)
            )

            GameOverButton(
                modifier = Modifier
                    .wrapContentSize()
                    .align(CenterHorizontally),
                clickable
            )
        }
    }
}

@Composable
fun GameOverScoreBoard(modifier: Modifier, score: Int = 13, maxScore: Int = 100) {
    Box(
        modifier = modifier.wrapContentSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.score_board_bg),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = Modifier
                .width(ScoreBoardWidth)
                .height(ScoreBoardHeight)
                .align(Center)
        )

        Column(
            modifier = Modifier
                .align(Center)
                .wrapContentSize()
        ) {

            LabelScoreField(modifier, R.drawable.score_bg, score)

            Spacer(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(3.dp)
            )

            LabelScoreField(modifier, R.drawable.best_score_bg, maxScore)
        }
    }
}

@Composable
fun GameOverButton(modifier: Modifier, clickable: Clickable = Clickable()) {
    Row(
        modifier.wrapContentSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.restart_button),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .size(ControlButtonWidth, ControlButtonHeight)
                .align(CenterVertically)
                .clickable(true) { clickable.onRestart() }
        )

        Spacer(
            modifier = Modifier
                .width(8.dp)
                .wrapContentHeight()
        )

        Image(
            painter = painterResource(id = R.drawable.exit_button),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .size(ControlButtonWidth, ControlButtonHeight)
                .align(CenterVertically)
                .clickable(true) {
                    clickable.onExit()
                }
        )
    }
}

@Composable
fun LabelScoreField(modifier: Modifier, infoDrawable: Int = R.drawable.score_bg, score: Int = 13) {
    Column(
        modifier = modifier.wrapContentSize()
    ) {
        Image(
            painter = painterResource(id = infoDrawable),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .size(ScoreInfoButtonWidth, ScoreInfoButtonHeight)
                .align(CenterHorizontally)
        )

        Spacer(
            modifier = Modifier
                .wrapContentWidth()
                .height(3.dp)
        )

        Text(
            text = score.toString(),
            modifier = Modifier.align(CenterHorizontally),
            color = GroundDividerPurple,
            fontSize = OverScoreTextSize,
            fontFamily = ScoreFontFamily
        )
    }
}

val RealTimeScoreHeightOffset = (-150).dp
val OverScoreHeightOffset = 0.dp

val SimpleScoreTextSize = 60.sp
val OverScoreTextSize = 60.sp

val ScoreBoardWidth = 180.dp
val ScoreBoardHeight = 240.dp

val ScoreInfoButtonWidth = 80.dp
val ScoreInfoButtonHeight = 25.dp

val ControlButtonWidth = 120.dp
val ControlButtonHeight = 40.dp
