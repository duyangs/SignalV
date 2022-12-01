package com.duangs.signalv

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * @author DuYang
 * @date 2022/12/01
 * @des Signal View
 */
class SignalView : View {

    /**
     * signal pole quantity, default value: 5
     */
    private var signalMaximum: Int = 5

    /**
     * signal level, default value: 0
     */
    private var signalLevel: Int = 0

    /**
     * signal pole primary color, default value: black
     */
    private var primaryColor: Int = Color.BLACK

    /**
     * signal pole level color, default color: white
     */
    private var levelColor: Int = Color.WHITE

    /**
     * signal pole spacing, default value: 5
     */
    private var spacing: Int = 5

    /**
     * signal pole unit width, default value: 5
     */
    private var unitWidth: Int = 5

    /**
     * signal connect state, true: connected; false: unconnected, default value: true
     */
    private var connected: Boolean = true

    /**
     * signal pole shadow color, default color: gray
     */
    private var shadowColor: Int = Color.GRAY

    /**
     * signal pole shadow switch, true: open,false: close, default state: false(close)
     */
    private var shadowOpen: Boolean = false

    /**
     * signal pole paint
     */
    private var mPaint: Paint? = null

    /**
     * paint rect height, init value 0
     */
    private var mRectHeight: Int = 0

    /**
     * paint rect width, init value 0
     */
    private var mRectWidth: Int = 0


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        setAttributeSet(context, attr)
        init()
    }

    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    ) {
        setAttributeSet(context, attr)
        init()
    }

    private fun setAttributeSet(context: Context, attr: AttributeSet) {
        context.obtainStyledAttributes(attr, R.styleable.SignalView).also {
            initValue(it)
        }.recycle()
    }

    private fun initValue(typedArray: TypedArray) {
        typedArray.apply {
            signalMaximum = getInt(R.styleable.SignalView_signal_maximum, signalMaximum)
            signalLevel = getInt(R.styleable.SignalView_signal_level, signalLevel)
            primaryColor = getColor(R.styleable.SignalView_primary_color, primaryColor)
            levelColor = getColor(R.styleable.SignalView_level_color, levelColor)
            spacing = getInt(R.styleable.SignalView_spacing, spacing)
            unitWidth = getInt(R.styleable.SignalView_unit_width, unitWidth)
            connected = getBoolean(R.styleable.SignalView_connected, connected)
            shadowColor = getColor(R.styleable.SignalView_shadow_color, shadowColor)
            shadowOpen = getBoolean(R.styleable.SignalView_shadow_open, shadowOpen)
        }
    }

    private fun init() {
        mPaint = initPaint()
    }

    private fun initPaint(): Paint {
        return Paint().also {
            it.isAntiAlias = true
            shadowSwitch(it)
        }
    }

    private fun shadowSwitch(paint: Paint) {
        if (shadowOpen) {
            val shadowWith = (unitWidth * 0.2).toFloat()
            paint.setShadowLayer(5F, shadowWith, shadowWith, shadowColor)
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initSize()
    }

    private fun initSize() {
        mRectHeight = height
        mRectWidth = width / signalMaximum
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))

    }

    private fun getMeasureSpecInfo(measureSpec: Int): Pair<Int, Int> {
        // 测量模式，从xml可知
        val specMode = MeasureSpec.getMode(measureSpec)
        // 测量大小,从xml中获取
        val specSize = MeasureSpec.getSize(measureSpec)
        return Pair(specMode, specSize)
    }

    private fun measureSpecSize(specMode: Int, specSize: Int, defaultSpecSize: Int): Int {
        return if (specMode == MeasureSpec.EXACTLY) specSize else {
            // wrap_content模式，选择最小值
            if (specMode == MeasureSpec.AT_MOST) {
                defaultSpecSize.coerceAtMost(specSize)
            } else defaultSpecSize
        }
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        val (specMode, specSize) = getMeasureSpecInfo(heightMeasureSpec)
        val defaultSpecSize = 50
        val height = measureSpecSize(specMode, specSize, defaultSpecSize)
        mRectHeight = height
        return height
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val (specMode, specSize) = getMeasureSpecInfo(widthMeasureSpec)
        val defaultSpecSize = 80
        return measureSpecSize(specMode, specSize, defaultSpecSize)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d("SignalView", "onDraw#$mRectWidth")
        mPaint?.also {
            it.strokeWidth = unitWidth.toFloat()
            it.strokeCap = Paint.Cap.ROUND
            it.style = Paint.Style.FILL
            canvasSignalPole(it, canvas)
            canvasSignalConnectState(it, canvas)
        }
    }

    private fun canvasSignalPole(paint: Paint, canvas: Canvas?) {
        val rectWidthFactor = 0.5f
        val rectStartYFactor = 0.1f
        val rectStopYFactor = 0.8f
        val rectStopY = height * rectStopYFactor

        fun getRectX(index: Int) = mRectWidth * (index + rectWidthFactor) + spacing

        fun getRectStartY(index: Int) = mRectHeight * (signalMaximum - index) * rectStartYFactor

        // 绘制信号条，根据强弱显示不同颜色
        (0..signalMaximum).forEach { index ->
            // 前signalValue信号柱绘制实心颜色
            paint.color = if (index < signalLevel) levelColor else primaryColor
            val rectX = getRectX(index)
            // 绘制信号线
            canvas?.drawLine(rectX, getRectStartY(index), rectX, rectStopY, paint)
        }
    }

    private fun canvasSignalConnectState(paint: Paint, canvas: Canvas?) {
        if (connected) return
        paint.color = primaryColor
        val sum = 1f
        val xDifference = 0.2f
        val yDifference = 0.1f

        canvas?.drawLine(
            width * xDifference,
            height * yDifference,
            width * (sum - xDifference),
            height * (sum - yDifference),
            paint
        )
    }

    fun setSignalLevel(level: Int) {
        Log.d("SignalView", "setSignalLevel#$level")
        if (level > signalMaximum) {
            throw Exception("setSignalLevel method value error,can not exceed settings signalMaximum!")
        }
        if (signalLevel != level) {
            signalLevel = level
            initSize()
            this.invalidate()
        }
    }

    fun setConnected(connected: Boolean) {
        if (this.connected != connected) {
            this.connected = connected
            initSize()
            this.invalidate()
        }
    }

}
