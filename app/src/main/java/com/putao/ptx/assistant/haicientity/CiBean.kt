package com.putao.ptx.assistant.haicientity

import com.google.gson.annotations.SerializedName

/**
 *
 *
 * <br></br>Description  :
 * <br></br>
 * <br></br>Author       : Victor<liuhe556></liuhe556>@126.com>
 * <br></br>
 * <br></br>Created date : 2017-04-06
 */
class CiBean : ISimplePreview {
    override fun title(): MutableList<String> {
        return ArrayList<String>().also {
            val item1 = item
            if (item1 != null) {
                it.add(item1)
            }
        }
    }

    override fun pronounce(): MutableList<String> {
        return ArrayList<String>().also {
            val item1 = modern
            if (item1 != null) {
                it.addAll(item1.map { it?.pinyin ?: "" })
            }
        }
    }

    override fun basicExplain(): MutableList<String> {
        return ArrayList<String>().also {
            val item1 = modern
            if (item1 != null) {
                it.addAll(item1.map { it?.sense?.get(0)?.def ?: "" })
            }
        }
    }

    override fun example(): MutableList<String> {
        return ArrayList<String>().also {
            val item1 = modern
            if (item1 != null) {
                it.addAll(item1.map { it?.sense?.get(0)?.exp?.get(0)?.ex ?: "" })
            }
        }
    }

    /**
     * item : 国家
     * modern : [{"pinyin":"guó jiā","sense":[{"def":"（名）阶级统治的工具；是统治阶级对被统治阶级实行专政的暴力组织；主要由军队、警察、法庭、监狱等组成。","exp":[{"ex":"～有大有小。（作主语）","src":"《汉书・贾谊传》"}]},{"def":"（名）指一个国家的整个区域。"}]}]
     * synonym : ["国度"]
     */

    @SerializedName("item")
    var item: String? = null
    @SerializedName("modern")
    var modern: List<ModernBean>? = null
    @SerializedName("synonym")
    var synonym: List<String>? = null
    @SerializedName("cn_flag")
    var cn_flag: Boolean = false
    @SerializedName("synonym_flag")
    var synonym_flag: List<*>? = null

    class ModernBean {
        /**
         * pinyin : guó jiā
         * sense : [{"def":"（名）阶级统治的工具；是统治阶级对被统治阶级实行专政的暴力组织；主要由军队、警察、法庭、监狱等组成。","exp":[{"ex":"～有大有小。（作主语）","src":"《汉书・贾谊传》"}]},{"def":"（名）指一个国家的整个区域。"}]
         */

        @SerializedName("pinyin")
        var pinyin: String? = null
        @SerializedName("sense")
        var sense: List<SenseBean>? = null

        class SenseBean {
            /**
             * def : （名）阶级统治的工具；是统治阶级对被统治阶级实行专政的暴力组织；主要由军队、警察、法庭、监狱等组成。
             * exp : [{"ex":"～有大有小。（作主语）","src":"《汉书・贾谊传》"}]
             */

            @SerializedName("def")
            var def: String? = null
            @SerializedName("exp")
            var exp: List<ExpBean>? = null

            class ExpBean {
                /**
                 * ex : ～有大有小。（作主语）
                 * src : 《汉书・贾谊传》
                 */

                @SerializedName("ex")
                var ex: String? = null
                @SerializedName("src")
                var src: String? = null
            }
        }
    }
}