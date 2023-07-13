package com.erkindilekci.flappybirddash.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.erkindilekci.flappybirddash.util.ValueUtil
import com.erkindilekci.flappybirddash.view.PipeCoverWidth

data class PipeState(
    var offset: Dp = FirstPipeWidthOffset,
    var upHeight: Dp = ValueUtil.getRandomDp(LowPipe, HighPipe),
    var downHeight: Dp = TotalPipeHeight - upHeight - PipeDistance,
    var counted: Boolean = false
) {
    fun move(): PipeState =
        copy(offset = offset - PipeMoveVelocity)

    fun count(): PipeState =
        copy(counted = true)

    fun correct(): PipeState {
        val newUpHeight = ValueUtil.getRandomDp(LowPipe, HighPipe)
        return copy(
            upHeight = newUpHeight,
            downHeight = TotalPipeHeight - newUpHeight - PipeDistance
        )
    }

    fun reset(): PipeState {
        val newUpHeight = ValueUtil.getRandomDp(LowPipe, HighPipe)
        return copy(
            offset = FirstPipeWidthOffset,
            upHeight = newUpHeight,
            downHeight = TotalPipeHeight - newUpHeight - PipeDistance,
            counted = false
        )
    }
}

val TotalPipeWidth = 412.dp

val PreviewPipeWidthOffset = -PipeCoverWidth
val FirstPipeWidthOffset = PipeCoverWidth * 2
val SecondPipeWidthOffset = (TotalPipeWidth + FirstPipeWidthOffset * 3) / 2

val PipeMoveVelocity = 8.dp
val PipeResetThreshold = 0.dp

val DefaultTotalPipeHeight = 660.dp
var TotalPipeHeight = DefaultTotalPipeHeight

const val MaxPipeFraction = 0.6f
const val MinPipeFraction = 0.2f
const val PipeDistanceFraction = 1.0f - MaxPipeFraction - MinPipeFraction
const val CenterPipeFraction = (MaxPipeFraction + MinPipeFraction) / 2

var HighPipe = TotalPipeHeight * MaxPipeFraction
var MiddlePipe = TotalPipeHeight * CenterPipeFraction
var LowPipe = TotalPipeHeight * MinPipeFraction
var PipeDistance = TotalPipeHeight * PipeDistanceFraction

val PipeStateList = listOf(
    PipeState(offset = FirstPipeWidthOffset),
    PipeState(offset = (SecondPipeWidthOffset))
)

val PreviewPipeState = PipeState(
    offset = PreviewPipeWidthOffset,
    upHeight = MiddlePipe
)

enum class PipeStatus {
    BirdComing,
    BirdHit,
    BirdCrossing,
    BirdCrossed
}
