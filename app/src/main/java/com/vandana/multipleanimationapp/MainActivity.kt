package com.vandana.multipleanimationapp

import android.animation.Animator
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.airbnb.lottie.LottieAnimationView
import com.vandana.multipleanimationapp.utils.HeartLikeViewPath
import com.vandana.multipleanimationapp.utils.LikeFlurryAnimation
import com.vandana.multipleanimationapp.utils.MultiLottieAnimation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val frame = findViewById<FrameLayout>(R.id.frame)
        val multiLottieButton = findViewById<Button>(R.id.multiLottieBtn)
        val flurryButton = findViewById<Button>(R.id.flurryBtn)
        val likePathViewButton = findViewById<Button>(R.id.likePathButton)
        val likePathView = findViewById<HeartLikeViewPath>(R.id.likePathView)
        val mFlurryRelativeLayout = findViewById<RelativeLayout>(R.id.flurryLay)
        val flurryAnim =  LikeFlurryAnimation(this,mFlurryRelativeLayout)
        val multipleLottieAnim = MultiLottieAnimation()

        multiLottieButton.setOnClickListener {
            multipleLottieAnim.playLottieMultipleAnimation(this,frame)
        }
        flurryButton.setOnClickListener {
            flurryAnim.likeFlurryAnim()
        }

        likePathViewButton.setOnClickListener {
            likePathView.showHeartView(BitmapFactory.decodeResource(resources, R.drawable.heart))
        }


    }


}