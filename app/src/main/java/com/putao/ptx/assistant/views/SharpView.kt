package com.putao.ptx.assistant.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.putao.ptx.assistant.R
import com.putao.ptx.assistant.R.*
import kotlinx.android.synthetic.main.activity_main_kt.*


/**
 * <p>
 * <br/>Description  :
 * <br/>
 * <br/>Author       : Victor<liuhe556@126.com>
 * <br/>
 * <br/>Created date : 2017-05-05</p>
 */
class SharpView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    val TAG = "SharpView"
    var bUp = false
    var arrowWidth = 0f
    var arrowHeight = 0f
    var offset = 0f
    var paint = Paint()

    init {

        paint.color = resources.getColor(R.color.arrow)
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (arrowWidth <= 0f || arrowHeight <= 0f) {
            return
        }

        val path = Path()
        if (bUp) {
            path.moveTo(offset - arrowWidth / 2, height.toFloat())// 此点为多边形的起点
            path.lineTo(offset, height.toFloat() - arrowHeight)
            path.lineTo(offset + arrowWidth / 2, height.toFloat())
        } else {
            path.moveTo(offset - arrowWidth / 2, -1f)// 此点为多边形的起点
            path.lineTo(offset, arrowHeight)
            path.lineTo(offset + arrowWidth / 2, -1f)
        }
        path.close() // 使这些点构成封闭的多边形
        canvas?.drawPath(path, paint)
        Log.d(TAG, "onDraw arrowWidth:$arrowWidth arrowHeight:$arrowHeight bUp:$bUp offset:$offset ")
    }
}