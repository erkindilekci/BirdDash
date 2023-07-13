package com.erkindilekci.flappybirddash.model

data class ViewState(
    val gameStatus: GameStatus = GameStatus.Waiting,

    val birdState: BirdState = BirdState(),

    val pipeStateList: List<PipeState> = PipeStateList,
    var targetPipeIndex: Int = -1,

    val roadStateList: List<RoadState> = RoadStateList,
    var targetRoadIndex: Int = -1,

    var playZoneSize: Pair<Int, Int> = Pair(0, 0),

    val score: Int = 0,
    val bestScore: Int = 0,
) {
    val isLifting get() = gameStatus == GameStatus.Running && birdState.isLifting
    val isFalling get() = gameStatus == GameStatus.Running && !birdState.isLifting
    val isQuickFalling get() = gameStatus == GameStatus.Dying
    val isOver get() = gameStatus == GameStatus.Over
    fun reset(bestScore: Int): ViewState =
        ViewState(bestScore = bestScore)
}

enum class GameStatus {
    Waiting,
    Running,
    Dying,
    Over
}

sealed class GameAction {
    object Start : GameAction()
    object AutoTick : GameAction()
    object TouchLift : GameAction()

    object ScreenSizeDetect : GameAction()
    object PipeExit : GameAction()
    object RoadExit : GameAction()

    object HitPipe : GameAction()
    object HitGround : GameAction()
    object CrossedPipe : GameAction()

    object Restart : GameAction()
}