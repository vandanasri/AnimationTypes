package com.vandana.multipleanimationapp.utils

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.view.Display
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.vandana.multipleanimationapp.R


class LikeFlurryAnimation(context: Activity, relativeLay:RelativeLayout) {

    private val activityContext = context
    private val relativeLayout = relativeLay


    @SuppressLint("NewApi")
    fun likeFlurryAnim() {
        val like: Drawable =
            activityContext.getResources().getDrawable(R.drawable.heart_filled)
        val v: View = ImageView(activityContext)
        val imageView= ImageView(v.context)
        imageView.setImageDrawable(like)
        val display: Display? = activityContext.display
        val size = Point()
        display?.getSize(size)
        val paramsImage = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        paramsImage.addRule(RelativeLayout.CENTER_IN_PARENT)
        imageView.layoutParams = paramsImage
        relativeLayout?.addView(imageView)
        val heartParams = RelativeLayout.LayoutParams(
            80, 200
        )
        heartParams.leftMargin = 40
        heartParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        imageView.layoutParams = heartParams
        val animationY =
            ObjectAnimator.ofFloat(imageView, "translationY", -size.y.toFloat())
        animationY.duration = 4000
        animationY.start()
        object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {}
        }.start()
    }

    fun removeFlurryView(){
        Toast.makeText(activityContext,"removeFlurryView", Toast.LENGTH_SHORT).show()
        relativeLayout?.removeAllViews()
    }
}