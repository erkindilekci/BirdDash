package com.erkindilekci.flappybirddash.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.erkindilekci.flappybirddash.R
import com.erkindilekci.flappybirddash.model.GameAction
import com.erkindilekci.flappybirddash.model.TempRoadWidthOffset
import com.erkindilekci.flappybirddash.model.ViewState
import com.erkindilekci.flappybirddash.ui.theme.GroundDividerPurple
import com.erkindilekci.flappybirddash.viewmodel.GameViewModel

@Composable
fun NearForeground(
    modifier: Modifier = Modifier,
    state: ViewState = ViewState()
) {
    val viewModel: GameViewModel = viewModel()

    Column(
        modifier
    ) {
        Divider(
            color = GroundDividerPurple,
            thickness = 5.dp
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            state.roadStateList.forEach { roadState ->
                Image(
                    painter = painterResource(id = R.drawable.foreground_road),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.23f)
                        .offset(x = roadState.offset)
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.foreground_earth),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.77f)
        )

        if (state.playZoneSize.first > 0) {
            state.roadStateList.forEachIndexed { index, roadState ->
                if (roadState.offset <= -TempRoadWidthOffset) {
                    viewModel.dispatch(GameAction.RoadExit, roadIndex = index)
                }
            }
        }
    }
}
