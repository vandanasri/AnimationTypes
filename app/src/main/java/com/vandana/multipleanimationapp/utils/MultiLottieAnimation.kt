package com.vandana.multipleanimationapp.utils

import android.animation.Animator
import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.airbnb.lottie.LottieAnimationView
import com.vandana.multipleanimationapp.R

class MultiLottieAnimation {

    fun playLottieMultipleAnimation(context: Context,frame: FrameLayout){
        val view = LayoutInflater.from(context).inflate(R.layout.lottie_layout, null)
        //view.layoutParams = FrameLayout.LayoutParams(200.dp, 600.dp)
        frame.removeView(view)
        frame.addView(view)
        val lottieView = view.findViewById<LottieAnimationView>(R.id.animationView)
        lottieView.playAnimation()
        lottieView.repeatCount =0
        lottieView.addAnimatorListener(object: Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                frame.removeView(view)
                lottieView.removeAnimatorListener(this)
            }

            override fun onAnimationCancel(animation: Animator?) {
                frame.removeAllViews()
                lottieView.removeAllAnimatorListeners()
            }

            override fun onAnimationRepeat(animation: Animator?) {}

        })
    }
}