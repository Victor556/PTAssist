package com.putao.ptx.assistant.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.putao.ptx.assistant.utils.OcrAreaResult
import com.putao.ptx.assistant.utils.SegmentAnsj
import com.putao.ptx.assistant.utils.SegmentUnit
import com.putao.ptx.assistant.utils.symbolDataRegex

/**
 * <p>
 * <br/>Description  :
 * <br/>
 * <br/>Author       : Victor<liuhe556@126.com>
 * <br/>
 * <br/>Created date : 2017-04-13</p>
 */
class CrdImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : OcrImageView(context, attrs, defStyleAttr) {
    var sentenceSegment: OcrAreaResult = OcrAreaResult()
    var unitAll: ArrayList<SegmentUnit> = ArrayList()
    var unitMost: ArrayList<SegmentUnit> = ArrayList()
    var unitMaybe: ArrayList<SegmentUnit> = ArrayList()

    /**普通区域*/
    var mPainNormal: Paint = Paint()
    /**一定范围内的可能点击区域*/
    var mPainMaybe: Paint = Paint()
    /**精确的点击区域*/
    var mPainMost: Paint = Paint()


    /**文字*/
    var mPainText: Paint = Paint()


    var mPainTouch: Paint = Paint()

    var lastPoint: PointF = PointF(0f, 0f)
    var touchRange: Float = 0.0f

    private val TEXT_SIZE = 50f

    init {
        initPain(mPainNormal)
        initPain(mPainMaybe)
        initPain(mPainMost)
        initPain(mPainText)

        with(mPainTouch) {
            color = Color.parseColor("#a0000000")
            style = Paint.Style.FILL
        }


        mPainMaybe.color = Color.RED
        mPainMaybe.strokeWidth = 2f
        mPainMost.color = Color.RED
        mPainMost.strokeWidth = 4f

        with(mPainText) {
            color = Color.RED
            style = Paint.Style.FILL
            textSize = TEXT_SIZE
        }
    }

    private fun initPain(p: Paint) {
        with(p) {
            isAntiAlias = true
            strokeWidth = 1f
            style = Paint.Style.STROKE
            color = Color.BLUE
            textSize = TEXT_SIZE
        }
    }


    fun updateTouchPoint(pt: PointF = lastPoint, range: Float = touchRange) {
        with(unitAll) {
            clear()
            addAll(sentenceSegment.units)
        }

        with(unitMaybe) {
            clear()
            addAll(sentenceSegment.getUnitsByRange(pt, range, unitAll))
        }

        with(unitMost) {
            clear()
            //addAll(sentenceSegment.getUnitsByRange(pt))
            addAll(sentenceSegment.getUnitsByRange(pt, 0f, unitMaybe))
            if (size == 0) {
                sentenceSegment.selectNearestUnit(pt, unitMaybe)?.also {
                    add(it)
                }
            }
        }

        unitAll.removeAll(unitMaybe)
        unitMaybe.removeAll(unitMost)

        lastPoint = pt
        touchRange = range
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mPainText.color = Color.RED
        val regex = symbolDataRegex
        unitAll.onEach {
            var str = it.str.replace(regex, " ")
            if (str.isNotBlank()) {
                canvas.drawRect(it.rectf, mPainNormal)
                //TODO 模拟显示
                canvas.drawText(str,
                        it.rectf.left, it.rectf.bottom - mPainText.textSize * 0.15f, mPainText)
            }
        }

        mPainText.color = Color.BLACK
        unitMaybe.onEach {
            var str = it.str.replace(regex, " ")
            if (str.isNotBlank()) {
                canvas.drawRect(it.rectf, mPainMaybe)
                //TODO 模拟显示
                canvas.drawText(str,
                        it.rectf.left, it.rectf.bottom - mPainText.textSize * 0.15f, mPainText)
            }
        }

        mPainText.color = Color.BLUE
        unitMost.onEach {
            var str = it.str.replace(regex, " ")
            if (str.isNotBlank()) {
                canvas.drawRect(it.rectf, mPainMost)
                //TODO 模拟显示
                canvas.drawText(str,
                        it.rectf.left, it.rectf.bottom - mPainText.textSize * 0.15f, mPainText)
            }
        }

        if (touchRange > 2) {
            canvas.drawCircle(lastPoint.x, lastPoint.y, touchRange, mPainTouch)
        }
    }


    /**
     * 添加模拟数据
     */
    fun setDebugData(para: SegmentAnsj, size: Float = mPainText.textSize): OcrAreaResult {
        updateImgDisplaySize()
        var ret = OcrAreaResult(src = para.para ?: "")
        val cnt = /*width*/realImgShowWidth / size - 1
        val offsetW = (width - realImgShowWidth) / 2
        val offsetH = (height - realImgShowHeight) / 2
        para.segmentation?.terms?.forEach {
            val rn = it.realName
            if (rn != null) {
                val offe = it.offe
                val fl = offsetW + offe % cnt * size
                val f2 = offsetH + (offe / cnt).toInt() * size * 1.2f
                ret.units.add(
                        SegmentUnit(str = rn,
                                rectf = RectF(fl, f2, fl + rn.length * size, f2 + size),
                                range = offe..(offe + rn.length))
                )
            }
        }
        sentenceSegment = ret
        updateTouchPoint()
        return ret
    }
}
