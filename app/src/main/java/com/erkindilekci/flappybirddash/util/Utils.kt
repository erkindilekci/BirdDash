package com.erkindilekci.flappybirddash.util

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object DensityUtil {
    fun dxToDp(resources: Resources, px: Int): Int =
        (px / resources.displayMetrics.density + 0.5f).toInt()
}

object ValueUtil {
    fun getRandomDp(fromDp: Dp, toDp: Dp): Dp =
        (fromDp.value.toInt()..toDp.value.toInt()).random().dp
}
