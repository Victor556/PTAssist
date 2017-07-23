package com.putao.ptx.assistant.utils

import android.graphics.PointF
import android.graphics.RectF
import com.google.gson.annotations.SerializedName

/**
 *
 *
 * <br></br>Description  :
 * <br></br>
 * <br></br>Author       : Victor<liuhe556></liuhe556>@126.com>
 * <br></br>
 * <br></br>Created date : 2017-04-14
 */
data class SegmentResult @JvmOverloads constructor(
        @SerializedName("query")
        var query: String? = null,
        @SerializedName("mergeSegment")
        var mergeSegment: List<String>? = null) {
    var result: MutableList<IntRange> = ArrayList()

    init {
        updateResult(result, mergeSegment)
    }

    fun updateResult(result: MutableList<IntRange>, mergeSegment: List<String>?) {
        result.clear()
        mergeSegment?.forEach {
            val split = it.split("#".toRegex())
            result.add(IntRange(split[0].toInt() - 1, split[split.size - 1].toInt() - 1))
        }
    }
}

/**
 * query :  我爱中国呵呵复旦大学 ？
 * mergeSegment : ["1","2","3","4","5","4#5","6","7","6#7","8","9","10","11","8#9#10#11","12","13","8#9","10#11"]
 */


class OcrAreaResult @JvmOverloads constructor(var src: String = "", var result: String = "") {
    var units: ArrayList<SegmentUnit> = ArrayList()

    @JvmOverloads
    fun getUnitsByIndex(idx: Int, units: Collection<SegmentUnit> = this.units)
            = ArrayList<SegmentUnit>().also {
        it.addAll(units.filter { idx in it.range })
    }

    @JvmOverloads
    fun getUnitsByPoint(pt: PointF, units: Collection<SegmentUnit> = this.units)
            = ArrayList<SegmentUnit>().also {
        it.addAll(units.filter { it.rectf.contains(pt.x, pt.y) })
    }


    @JvmOverloads
    fun getUnitsByRange(pt: PointF, range: Float = 0f, units: Collection<SegmentUnit> = this.units)
            = ArrayList<SegmentUnit>().also {
        var tmp = RectF()
        it.addAll(units.filter { f ->
            tmp.also {
                it.set(f.rectf)
                it.inset(-range, -range)
            }.contains(pt.x, pt.y)
        })
    }

    /**
     * 找距离最近的
     */
    fun selectNearestUnit(pt: PointF, units: Collection<SegmentUnit>): SegmentUnit? {
        val element = units.minBy {
            var minHori = if (pt.x >= it.rectf.right || pt.x <= it.rectf.left) {
                minOf(Math.abs(pt.x - it.rectf.right), Math.abs(pt.x - it.rectf.left))
            } else {
                100000f
            }
            var minVert = if (pt.y > it.rectf.bottom || pt.y < it.rectf.top) {
                minOf(Math.abs(pt.y - it.rectf.top), Math.abs(pt.y - it.rectf.bottom))
            } else {
                100000f
            }
            minOf(minHori, minVert)
        }
        print(TAG + " selectNearestUnit:$element")
        return element
    }

    val TAG = "OcrAreaResult"
}

data class SegmentUnit @JvmOverloads constructor(var str: String = "", var rectf: RectF = RectF(), var range: IntRange = (0..1))