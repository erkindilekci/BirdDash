package com.erkindilekci.flappybirddash.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Application
import android.graphics.Path
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import com.erkindilekci.flappybirddash.R
import com.erkindilekci.flappybirddash.viewmodel.GameViewModel

class SplashScreenController(
    private val splashScreen: SplashScreen,
    private val viewModel: GameViewModel
) {
    private val defaultExitDuration: Long by lazy {
        viewModel.getApplication<Application>()
            .resources.getInteger(R.integer.splash_exit_total_duration).toLong()
    }

    fun customizeSplashScreen() {
        customizeSplashScreenExit()
    }

    private fun customizeSplashScreenExit() {
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            Log.d(
                "Splash", "SplashScreen#onSplashScreenExit view:$splashScreenViewProvider"
                        + " view:${splashScreenViewProvider.view}"
                        + " icon:${splashScreenViewProvider.iconView}"
            )

            val onExit = {
                splashScreenViewProvider.remove()
            }

            showSplashExitAnimator(splashScreenViewProvider.view, onExit)
            showSplashIconExitAnimator(splashScreenViewProvider.iconView, onExit)
        }
    }

    private fun showSplashExitAnimator(splashScreenView: View, onExit: () -> Unit = {}) {
        Log.d(
            "Splash", "showSplashExitAnimator() splashScreenView:$splashScreenView" +
                    " context:${splashScreenView.context}" +
                    " parent:${splashScreenView.parent}"
        )

        val alphaOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.ALPHA,
            1f,
            0f
        )

        val scaleOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.SCALE_X,
            View.SCALE_Y,
            Path().apply {
                moveTo(1.0f, 1.0f)
                lineTo(0f, 0f)
            }
        )

        AnimatorSet().run {
            duration = defaultExitDuration
            interpolator = AnticipateInterpolator()
            Log.d("Splash", "showSplashExitAnimator() duration:$duration")

            playTogether(scaleOut, alphaOut)

            start()
        }
    }

    private fun showSplashIconExitAnimator(iconView: View, onExit: () -> Unit = {}) {
        Log.d(
            "Splash", "showSplashIconExitAnimator()" +
                    " iconView[:${iconView.width}, ${iconView.height}]" +
                    " translation[:${iconView.translationX}, ${iconView.translationY}]"
        )

        val alphaOut = ObjectAnimator.ofFloat(
            iconView,
            View.ALPHA,
            1f,
            0f
        )

        val scaleOut = ObjectAnimator.ofFloat(
            iconView,
            View.SCALE_X,
            View.SCALE_Y,
            Path().apply {
                moveTo(1.0f, 1.0f)
                lineTo(0.3f, 0.3f)
            }
        )

        val slideUp = ObjectAnimator.ofFloat(
            iconView,
            View.TRANSLATION_Y,
            0f,
            -(iconView.height).toFloat() * 2.25f
        ).apply {
            addUpdateListener {
                Log.d(
                    "Splash",
                    "showSplashIconExitAnimator() translationY:${iconView.translationY}"
                )
            }
        }

        AnimatorSet().run {
            interpolator = AnticipateInterpolator()
            duration = defaultExitDuration
            Log.d("Splash", "showSplashIconExitAnimator() duration:$duration")

            playTogether(alphaOut, scaleOut, slideUp)

            doOnEnd {
                Log.d("Splash", "showSplashIconExitAnimator() onEnd remove")
                onExit()
            }

            start()
        }
    }
}
