package com.putao.ptx.assistant.utils

import com.google.gson.annotations.SerializedName

/**
 *
 *
 * <br></br>Description  :
 * <br></br>
 * <br></br>Author       : Victor<liuhe556></liuhe556>@126.com>
 * <br></br>
 * <br></br>Created date : 2017-04-18
 */
data class SegmentAnsj @JvmOverloads constructor(
        @SerializedName("para")
        var para: String? = null,
        @SerializedName("segmentation")
        var segmentation: SegmentationBean? = null) {
    fun append(a: SegmentAnsj?): SegmentAnsj {
        a?:return this
        val len = this.para?.length ?: 0
        this.para += a.para
        val terms = this.segmentation?.terms
        a.segmentation?.terms?.forEach {
            terms?.add(it.copy().also { it.offe += len })
        }
        return this@SegmentAnsj
    }
}


data class SegmentationBean @JvmOverloads constructor(
        @SerializedName("terms")
        var terms: ArrayList<TermsBean>? = null
)

data class TermsBean @JvmOverloads constructor(
        /**
         * name : 我
         * natureStr : r
         * newWord : false
         * offe : 0
         * realName : 我
         */
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("natureStr")
        var natureStr: String? = null,
        @SerializedName("newWord")
        var isNewWord: Boolean = false,
        @SerializedName("offe")
        var offe: Int = 0,
        @SerializedName("realName")
        var realName: String? = null
) {
    override fun toString() = realName ?: ""

}