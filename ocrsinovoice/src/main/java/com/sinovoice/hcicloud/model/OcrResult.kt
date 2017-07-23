package com.sinovoice.hcicloud.model

import android.graphics.RectF

/**
 * <p>
 * <br/>Description  :
 * <br/>
 * <br/>Author       : Victor<liuhe556@126.com>
 * <br/>
 * <br/>Created date : 2017-03-30</p>
 */
data class OcrResult @JvmOverloads constructor(
        var error: Int = 0,
        var text: String = "请求成功",
        var data: OcrData? = OcrData()
) {

    fun result() = data?.forms?.form ?: ""
    fun getPos(): Map<String, RectF> {
        var ret = HashMap<String, RectF>()
        //TODO//获取位置
        return ret;
    }
}
