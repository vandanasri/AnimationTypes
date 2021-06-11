package com.vandana.multipleanimationapp.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import com.vandana.multipleanimationapp.R
import java.util.*

class HeartLikeViewPath : View {

    private lateinit var mPaint: Paint

    private lateinit var mBitmap :Bitmap

    private val list: ArrayList<PathObj> = ArrayList()


    private val mRandom = Random()
    private var mFlag = false
    private var mStartTime: Long = 0

    private lateinit var mContext: Context

    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val what = msg.what
            when (what) {
                0 -> {
                    list.add(PathObj(mBitmap))
                    mFlag = true
                    this.post(mRunnable)
                }
                else -> {
                }
            }
        }
    }

    private val mRunnable = Runnable { invalidate() }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mFlag) {
            release()
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (list == null) {
            return
        }
        if (mFlag) {
            drawView(canvas)
            post(mRunnable)
        }
    }

    private fun init(context: Context) {
        mContext = context
        mPaint = Paint()
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    fun isRunning(): Boolean {
        return mFlag
    }

    private fun drawView(canvas: Canvas) {
        try {
            drawPath(canvas)
        } catch (e: Exception) {
        }
    }

    private fun drawPath(canvas: Canvas) {
        if (list.size <= 0) {
            mFlag = false
        }
        var i = 0
        while (i < list.size) {
            try {
                val obj: PathObj = list[i]
                if (obj.getAlpha() <= 0) {
                    list.removeAt(i)
                    i--
                    i++
                    continue
                }
                val src: Rect = obj.getSrcRect()
                val dst: Rect? = obj.getDstRect()
                if (dst == null) {
                    list.removeAt(i)
                    i--
                    i++
                    continue
                }
                obj.getBitmap()?.let {
                    canvas.drawBitmap(it, src, dst, obj.getPaint())
                }
            } catch (e: java.lang.Exception) {
                list.removeAt(i)
                i--
            }
            i++
        }
    }

    fun showHeartView(bitmap: Bitmap) {
        if (System.currentTimeMillis() - mStartTime > 50) {
            mStartTime = System.currentTimeMillis()
            mBitmap = bitmap
            list.add(PathObj(mBitmap))
            mFlag = true
            post(mRunnable)
        }
    }

     fun stop() {
        removeCallbacks(mRunnable)
        mFlag = false
    }

    fun release() {
        stop()
        mBitmap.let {
            if(it.isRecycled)
                it.recycle()
        }
    }

    fun add(count: Int) {
        for (i in 0 until count) {
            val msg = Message.obtain()
            msg.what = 0
            mHandler.sendMessageDelayed(msg, 80 * i.toLong())
        }
    }



    inner class PathObj(private val bitmap: Bitmap) {
        private var curPos = 0
        private var speed = 0f
        private var time = 0
        private val scaleTime = 20
        private val acceleratedSpeed = 0.05f
        private val speedMax = 6f
        private var length = 0
        private val p = FloatArray(2)
        private var alpha = 255
        private val alphaOffset = 8


        private val bitmapWidth = bitmap.width
        private val bitmapHeight = bitmap.height

        private val bitmapWidthDst = bitmapWidth

        private val bitmapHeightDst = bitmapHeight


        private val src = Rect(0, 0, bitmapWidth, bitmapHeight)
        private val dst = Rect(0, 0, bitmapWidthDst / 2, bitmapHeightDst / 2)

        private var paint = Paint()

        val path = Path()
        private val pathMeasure = PathMeasure()

        private var factor :Int = 2
        private var initX :Int= mContext.resources.getDimension(R.dimen.heart_anim_init_x).toInt()
        private var initY :Int = mContext.resources.getDimension(R.dimen.heart_anim_init_y).toInt()
        private var xRand :Int= mContext.resources.getDimension(R.dimen.heart_anim_bezier_x_rand).toInt()
        private var animLengthRand :Int = mContext.resources.getDimension(R.dimen.heart_anim_length_rand).toInt()
        private var bezierFactor = 6
        private var animLength = mContext.resources.getDimension(R.dimen.heart_anim_length).toInt()
        private var xPointFactor = mContext.resources.getDimension(R.dimen.heart_anim_x_point_factor).toInt()

        var x:Int = mRandom.nextInt(xRand)
        var x2:Int = mRandom.nextInt(xRand)

        var y:Int = height - initY
        var y2 :Int = animLength * factor + mRandom.nextInt(animLengthRand)

        init {
            paint.isAntiAlias = true

            factor = y2 / bezierFactor

            x += xPointFactor
            x2 += xPointFactor

            var y3:Int = y - y2
            y2 = y - y2 / 2

            path.moveTo(initX.toFloat(), y.toFloat())
            path.cubicTo(
                initX.toFloat(),
                y - factor.toFloat(),
                x.toFloat(),
                y2 + factor.toFloat(),
                x.toFloat(),
                y2.toFloat()
            )
            path.moveTo(x.toFloat(), y2.toFloat())
            path.cubicTo(x.toFloat(), y2.toFloat() - factor, x2.toFloat(), y3.toFloat() + factor, x2.toFloat(), y3.toFloat())

            pathMeasure.setPath(path, false)
            length = pathMeasure.length.toInt()
            speed = mRandom.nextInt(1) + 1f
        }

        fun getBitmap(): Bitmap? {
            return bitmap
        }

        fun getPaint(): Paint? {
            return paint
        }

        fun getSrcRect(): Rect {
            return src
        }

        fun getDstRect(): Rect? {
            curPos += speed.toInt()
            if (time < scaleTime) {
                speed = 3f
            } else {
                if (speed <= speedMax) {
                    speed += acceleratedSpeed
                }
            }
            if (curPos > length) {
                curPos = length
                return null
            }
            pathMeasure.getPosTan(curPos.toFloat(), p, null)
            if (time < scaleTime) {
                // 放大动画
                val s: Float = time.toFloat() / scaleTime
                dst.left = (p[0] - bitmapWidthDst / 4 * s).toInt()
                dst.right = (p[0] + bitmapWidthDst / 4 * s).toInt()
                dst.top = (p[1] - bitmapHeightDst / 2 * s).toInt()
                dst.bottom = p[1].toInt()
            } else {
                dst.left = (p[0] - bitmapWidthDst / 4).toInt()
                dst.right = (p[0] + bitmapWidthDst / 4).toInt()
                dst.top = (p[1] - bitmapHeightDst / 2).toInt()
                dst.bottom = p[1].toInt()
            }
            time++
            alpha()
            return dst
        }

        private fun alpha(): Int {
            val offset = length - curPos
            if (offset < length / 1.5) {
                alpha -= alphaOffset
                if (alpha < 0) {
                    alpha = 0
                }
                paint.alpha = alpha
            } else if (offset <= 10) {
                alpha = 0
                paint.alpha = alpha
            }
            return 0
        }

        fun getAlpha(): Int {
            return alpha
        }

        fun getTime(): Int {
            return time
        }

    }

}