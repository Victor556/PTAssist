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
class ZiBean : ISimplePreview {
    override fun title(): MutableList<String> {
        return ArrayList<String>().also {
            it.add(item ?: "")
        }
    }

    override fun pronounce(): MutableList<String> {
        return ArrayList<String>().also {
            it.addAll(modern?.map { it?.pinyin ?: "" } ?: emptyList())
        }
    }

    override fun basicExplain(): MutableList<String> {
        return ArrayList<String>().also {
            it.addAll(modern?.flatMap {
                it?.sense?.flatMap {
                    it.subsense?.map {
                        it.def ?: ""
                    } ?: emptyList()
                } ?: emptyList<String>()
            } ?: emptyList<String>())
        }
    }

    override fun example(): MutableList<String> {
        return ArrayList<String>().also {
            it.addAll(modern?.flatMap {
                it?.sense?.flatMap {
                    it.subsense?.map {
                        it.exp ?: ""
                    } ?: emptyList()
                } ?: emptyList<String>()
            } ?: emptyList<String>())
        }
    }

    /**
     * item : 长
     * fan : ["長"]
     * bushou : 丿部
     * bihua : 4笔
     * wubi : TAYI
     * jiegou : 单一结构
     * zaozi : 象形
     * bishun : http://dict.cn/apis/output.php?id=hanzi_2_17589_0
     * modern : [{"pinyin":"cháng","binome":[{"pinyin":"āqín","word":"吖嗪","sense":[{"pos":"名","subsense":[{"def":"一类含有一个或几个氮原子的不饱和六节杂环化合物的总称。如吡啶、哒嗪等。"}]}]}],"audio":"http://audio.dict.cn/fzz0oN76cd6a7092a105beeaa5243a68df9676.mp3?t=ch%C3%A1ng","sense":[{"pos":"形","subsense":[{"def":"空间或时间两端之间的距离大","exp":"～途｜～路｜～假"},{"def":"优秀的","exp":"～处｜一展～才"},{"def":"旧时读（zhànɡ），指多余的，剩余的","exp":"一无～物"}]},{"pos":"名","subsense":[{"def":"两点之间的距离","exp":"身～｜波～｜周～"},{"def":"优点；专精的技能","exp":"特～｜取～补短｜一技之～"},{"def":"（Cháng）姓"}]},{"pos":"动","subsense":[{"def":"两端点的距离达到","exp":"绳～２米｜全程～１００米｜这条河～５０千米"},{"def":"对某事做得特别好；精通","exp":"～音律｜～于写作｜～于演讲"}]},{"pos":"副","subsense":[{"def":"长时间地；久远地","exp":"～谈｜～逝｜细水～流"}]}]},{"pinyin":"zhǎng","audio":"http://audio.dict.cn/fzz0p6e18022356092a009fc6763961cef093c.mp3?t=zh%C7%8Eng","sense":[{"pos":"动","subsense":[{"def":"年纪较大","exp":"我～她三岁"},{"def":"辈分较高","exp":"舅舅比外甥～一辈"},{"def":"生；出现","exp":"～锈｜～草｜山上～满了树"},{"def":"发育","exp":"生～｜成～｜～得快"},{"def":"增进；增加","exp":"增～｜～见识｜～力气"}]},{"pos":"名","subsense":[{"def":"主持人；领导人","exp":"校～｜首～｜村～"},{"def":"年纪大或辈分大的人","exp":"尊～｜师～｜兄～"}]},{"pos":"形","subsense":[{"def":"排行最大或第一的","exp":"～子｜～兄｜～孙"},{"def":"年纪较大的；辈分高的","exp":"～亲｜～者｜～辈"}]}]}]
     */

    @SerializedName("item")
    var item: String? = null
    @SerializedName("bushou")
    var bushou: String? = null
    @SerializedName("bihua")
    var bihua: String? = null
    @SerializedName("wubi")
    var wubi: String? = null
    @SerializedName("jiegou")
    var jiegou: String? = null
    @SerializedName("zaozi")
    var zaozi: String? = null
    @SerializedName("bishun")
    var bishun: String? = null
    @SerializedName("fan")
    var fan: List<String>? = null
    @SerializedName("modern")
    var modern: List<ModernBean>? = null
    /**
     * cn_flag : true
     * fan_flag : []
     */
    @SerializedName("cn_flag")
    var cnFlag: Boolean = false
    @SerializedName("fan_flag")
    var fanFlag: List<*>? = null

    class ModernBean {
        /**
         * pinyin : cháng
         * binome : [{"pinyin":"āqín","word":"吖嗪","sense":[{"pos":"名","subsense":[{"def":"一类含有一个或几个氮原子的不饱和六节杂环化合物的总称。如吡啶、哒嗪等。"}]}]}]
         * audio : http://audio.dict.cn/fzz0oN76cd6a7092a105beeaa5243a68df9676.mp3?t=ch%C3%A1ng
         * sense : [{"pos":"形","subsense":[{"def":"空间或时间两端之间的距离大","exp":"～途｜～路｜～假"},{"def":"优秀的","exp":"～处｜一展～才"},{"def":"旧时读（zhànɡ），指多余的，剩余的","exp":"一无～物"}]},{"pos":"名","subsense":[{"def":"两点之间的距离","exp":"身～｜波～｜周～"},{"def":"优点；专精的技能","exp":"特～｜取～补短｜一技之～"},{"def":"（Cháng）姓"}]},{"pos":"动","subsense":[{"def":"两端点的距离达到","exp":"绳～２米｜全程～１００米｜这条河～５０千米"},{"def":"对某事做得特别好；精通","exp":"～音律｜～于写作｜～于演讲"}]},{"pos":"副","subsense":[{"def":"长时间地；久远地","exp":"～谈｜～逝｜细水～流"}]}]
         */

        @SerializedName("pinyin")
        var pinyin: String? = null
        @SerializedName("audio")
        var audio: String? = null
        @SerializedName("binome")
        var binome: List<BinomeBean>? = null
        @SerializedName("sense")
        var sense: List<SenseBeanX>? = null

        class BinomeBean {
            /**
             * pinyin : āqín
             * word : 吖嗪
             * sense : [{"pos":"名","subsense":[{"def":"一类含有一个或几个氮原子的不饱和六节杂环化合物的总称。如吡啶、哒嗪等。"}]}]
             */

            @SerializedName("pinyin")
            var pinyin: String? = null
            @SerializedName("word")
            var word: String? = null
            @SerializedName("sense")
            var sense: List<SenseBean>? = null

            class SenseBean {
                /**
                 * pos : 名
                 * subsense : [{"def":"一类含有一个或几个氮原子的不饱和六节杂环化合物的总称。如吡啶、哒嗪等。"}]
                 */

                @SerializedName("pos")
                var pos: String? = null
                @SerializedName("subsense")
                var subsense: List<SubsenseBean>? = null

                class SubsenseBean {
                    /**
                     * def : 一类含有一个或几个氮原子的不饱和六节杂环化合物的总称。如吡啶、哒嗪等。
                     */

                    @SerializedName("def")
                    var def: String? = null
                }
            }
        }

        class SenseBeanX {
            /**
             * pos : 形
             * subsense : [{"def":"空间或时间两端之间的距离大","exp":"～途｜～路｜～假"},{"def":"优秀的","exp":"～处｜一展～才"},{"def":"旧时读（zhànɡ），指多余的，剩余的","exp":"一无～物"}]
             */

            @SerializedName("pos")
            var pos: String? = null
            @SerializedName("subsense")
            var subsense: List<SubsenseBeanX>? = null

            class SubsenseBeanX {
                /**
                 * def : 空间或时间两端之间的距离大
                 * exp : ～途｜～路｜～假
                 */

                @SerializedName("def")
                var def: String? = null
                @SerializedName("exp")
                var exp: String? = null
            }
        }
    }
}