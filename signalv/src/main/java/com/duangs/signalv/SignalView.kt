package com.duangs.signalv

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.lang.Exception

class SignalView : View {

    private var signalMaximum: Int = 5 //信号柱数量
    private var signalLevel: Int = 0 //信号等级
    private var primaryColor: Int = Color.BLACK//无信号颜色
    private var levelColor: Int = Color.WHITE//有信号颜色
    private var spacing: Int = 5 //间隙
    private var unitWidth: Int = 5 //信号柱宽度
    private var cornerRadius: Float = 5F //信号柱圆角半径
    private var connected: Boolean = true //链接状态
    private var shadowColor: Int = Color.GRAY //阴影颜色
    private var shadowOpen: Boolean = false //是否开启阴影效果

    private var mPaint: Paint? = null

    private var mRectHeight: Int = 0
    private var mRectWidth: Int = 0


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        setAttributeSet(attr)
        init()
    }

    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    ) {
        setAttributeSet(attr)
        init()
    }

    private fun setAttributeSet(attr: AttributeSet) {
        context?.let {
            val typedArray = it.obtainStyledAttributes(attr, R.styleable.SignalView)
            signalMaximum = typedArray.getInt(R.styleable.SignalView_signal_maximum, signalMaximum)
            signalLevel = typedArray.getInt(R.styleable.SignalView_signal_level, signalLevel)
            primaryColor = typedArray.getColor(R.styleable.SignalView_primary_color, primaryColor)
            levelColor = typedArray.getColor(R.styleable.SignalView_level_color, levelColor)
            spacing = typedArray.getInt(R.styleable.SignalView_spacing, spacing)
            unitWidth = typedArray.getInt(R.styleable.SignalView_unit_width, unitWidth)
            cornerRadius = typedArray.getFloat(R.styleable.SignalView_corner_radius, cornerRadius)
            connected = typedArray.getBoolean(R.styleable.SignalView_connected, connected)
            shadowColor = typedArray.getColor(R.styleable.SignalView_shadow_color,shadowColor)
            shadowOpen = typedArray.getBoolean(R.styleable.SignalView_shadow_open,shadowOpen)
            typedArray.recycle()
        }

    }

    private fun init() {

        mPaint = Paint().apply {
            isAntiAlias = true
            if (shadowOpen) {
                val shadowWith = (unitWidth * 0.2).toFloat()
                setShadowLayer(5F, shadowWith, shadowWith, shadowColor)
                setLayerType(LAYER_TYPE_SOFTWARE, null)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRectHeight = height
        mRectWidth = width / signalMaximum
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        var height: Int
        // 测量模式，从xml可知
        val specMode = MeasureSpec.getMode(heightMeasureSpec)
        // 测量大小,从xml中获取
        val specSize = MeasureSpec.getSize(heightMeasureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            height = specSize
        } else {
            height = 50
            // wrap_content模式，选择最小值
            if (specMode == MeasureSpec.AT_MOST) {
                height = height.coerceAtMost(specSize)
            }
        }
        mRectHeight = height
        return height
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        var width: Int
        // 测量模式，从xml可知
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        // 测量大小,从xml中获取
        val specSize = MeasureSpec.getSize(widthMeasureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            width = specSize
        } else {
            width = 80
            // wrap_content模式，选择最小值
            if (specMode == MeasureSpec.AT_MOST) {
                width = width.coerceAtMost(specSize)
            }
        }
        mRectWidth = width
        return width
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d("SignalView","onDraw#$mRectWidth")
        mPaint?.apply {
            strokeWidth = unitWidth.toFloat()
            strokeCap = Paint.Cap.ROUND
            // 绘制信号条，根据强弱显示不同颜色
            for (i in 0 until signalMaximum) {
                // 前signalValue信号柱绘制实心颜色
                if (i < signalLevel) {
                    color = levelColor
                    style = Paint.Style.FILL
                } else {
                    color = primaryColor
                    style = Paint.Style.FILL
                }
                Log.d("SignalView","onDraw#$mRectWidth")
                val x = (mRectWidth * (i + 0.5) + spacing).toFloat()
                // 绘制信号线
                canvas?.drawLine(
                    x,
                    (mRectHeight.toDouble() * (signalMaximum - i).toDouble() * 0.1).toFloat(),
                    x,
                    (height * 0.8).toFloat(), this
                )
            }

            if (!connected) {
                color = primaryColor
                style = Paint.Style.FILL
                canvas?.drawLine(
                    (width * 0.2).toFloat(),
                    (height * 0.1).toFloat(),
                    (width * 0.8).toFloat(),
                    (height * 0.9).toFloat(),
                    this
                )
            }

        }
    }

    fun setSignalLevel(level:Int){
        Log.d("SignalView","setSignalLevel#$level")
        if (level > signalMaximum){
            throw Exception("setSignalLevel method value error,can not exceed settings signalMaximum!")
        }
        if (signalLevel != level){
            signalLevel = level
            mRectHeight = height
            mRectWidth = width / signalMaximum
            this.invalidate()
        }
    }

    fun setConnected(connected:Boolean){
        if (this.connected != connected){
            this.connected = connected
            this.invalidate()
        }
    }

}
