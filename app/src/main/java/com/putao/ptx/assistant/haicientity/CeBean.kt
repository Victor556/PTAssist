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
class CeBean : ISimplePreview {
    override fun title(): MutableList<String> {
        return ArrayList<String>().also {
            it.add(item ?: "")
        }
    }

    override fun pronounce(): MutableList<String> {
        return ArrayList<String>().also {
            it.addAll(pinyin ?: emptyList())
        }
    }

    override fun basicExplain(): MutableList<String> {
        return ArrayList<String>().also {
            it.addAll(def ?: emptyList())
        }
    }

    override fun example(): MutableList<String> {
        return ArrayList<String>().also {
            it.addAll(sentence?.map { "${it.text}\n${it.trans}" } ?: emptyList())
        }
    }

    /**
     * item : 国
     * def : ["nation","state","country","province","soil"]
     * pinyin : ["guó"]
     * sentence : [{"text":"盟军数以百计的飞机轰炸了那个国家。","trans":"The allied warplanes bombed the country by the hundred."},{"text":"澳大利亚是一个讲英语的国家。","trans":"Australia is an English speaking nation."},{"text":"我深爱我的祖国。","trans":"I love my country with every fiber of my being."},{"text":"十月一日是中国的国庆节。","trans":"October first is the National Day of China."},{"text":"美国是一个西方国家。","trans":"America is a western country."},{"text":"中国和许多国家进行多方面贸易。","trans":"China does a lot of trade with many countries."},{"text":"失业的幽灵在这个国家作祟。","trans":"The specter of unemployment hunted the country."}]
     * phrases : [{"cn":"国家"},{"cn":"江山"},{"cn":"国度"},{"cn":"邦"}]
     * refer_def : [{"py":"zhǎng","v":[{"pos":"形容词","v":[{"k":"（年纪较大） older; elder; senior:","eg":[{"en":"older than her;","cn":"比她年长"},{"en":"My brother is senior to me by two years.","cn":"家兄比我年长两岁。"}]},{"k":"（排行最大） eldest; oldest:","eg":[{"en":"eldest daughter;","cn":"长女"},{"en":"eldest brother","cn":"长兄"}]}]},{"pos":"名词","v":[{"k":"（领导人） chief; head; leader:","eg":[{"en":"head of a delegation;","cn":"代表团团长"},{"en":"chairman of the board;","cn":"董事长"}]}]},{"pos":"动词","v":[{"k":"（生） come into being; begin to grow; form:","eg":[{"en":"get cancer;","cn":"长癌"},{"en":"have a boil;","cn":"长疮"}]},{"k":"（生长; 成长） grow; develop:","eg":[{"en":"Youth is the time of physical growth.","cn":"青年时期是长身体的时期。"},{"en":"She has grown so that she is even a little taller than her mother.","cn":"她长得比母亲都高一点了。"}]},{"k":"（增进; 增加） acquire; enhance; increase:","eg":[{"en":"increase one's knowledge; gain experience;","cn":"长见识"},{"en":"Such a tendency is not to be encouraged.","cn":"此风不可长。"}]}]}]},{"py":"cháng","v":[{"pos":"形容词","v":[{"k":"（两端之间的距离大，兼指时间和空间） long:","eg":[{"en":"In summer the days are long and the nights short.","cn":"夏季昼长夜短。"},{"en":"This is a long river.","cn":"这条河很长。"}]},{"k":"（引申为永远） forever; lasting:","eg":[{"en":"depart from the world forever; pass away","cn":"与世长辞"}]}]},{"pos":"名词","v":[{"k":"（长度） length:","eg":[{"en":"The length of the bridge is 200 metres.","cn":"那座桥有200米长。"},{"en":"The boat won by three lengths.","cn":"这只艇以3艇长之差得胜。"}]},{"k":"（长处） strong point; forte:","eg":[{"en":"overcome one's shortcomings by learning from others' strong points","cn":"取人之长，补己之短"}]},{"k":"（姓氏） a surname:","eg":[{"en":"Chang Wuzi","cn":"长武子"}]}]},{"pos":"动词","v":[{"k":"（对某事做得特别好; 擅长） be good at; be strong in:","eg":[{"en":"She is good at painting.; Painting is her forte.","cn":"她长于绘画。"}]}]},{"pos":"副词","v":[{"k":"（常） often:","eg":[{"en":"go about sth. little by little without a letup","cn":"细水长流"}]}]}]}]
     */

    @SerializedName("item")
    var item: String? = null
    @SerializedName("def")
    var def: List<String>? = null
    @SerializedName("pinyin")
    var pinyin: List<String>? = null
    @SerializedName("sentence")
    var sentence: List<SentenceBean>? = null
    @SerializedName("phrases")
    var phrases: List<PhrasesBean>? = null
    @SerializedName("refer_def")
    var refer_def: List<ReferDefBean>? = null
    @SerializedName("antonym")
    var antonym: List<AntonymBean>? = null
    @SerializedName("synonym")
    var synonym: List<SynonymBean>? = null

    /**
     * cn_flag : false
     * def_flag : []
     */
    @SerializedName("cn_flag")
    private var cnFlag: Boolean = false
    @SerializedName("def_flag")
    private var defFlag: List<*>? = null

    //TODO 近义词，反义词
    class SentenceBean {
        /**
         * text : 盟军数以百计的飞机轰炸了那个国家。
         * trans : The allied warplanes bombed the country by the hundred.
         */

        @SerializedName("text")
        var text: String? = null
        @SerializedName("trans")
        var trans: String? = null
    }

    class PhrasesBean {
        /**
         * cn : 国家
         * cn_flag : false
         */

        @SerializedName("cn")
        var cn: String? = null
        @SerializedName("en")
        var en: String? = null
        @SerializedName("cn_flag")
        var cnFlag: Boolean = false

    }

    class ReferDefBean {
        /**
         * py : zhǎng
         * v : [{"pos":"形容词","v":[{"k":"（年纪较大） older; elder; senior:","eg":[{"en":"older than her;","cn":"比她年长"},{"en":"My brother is senior to me by two years.","cn":"家兄比我年长两岁。"}]},{"k":"（排行最大） eldest; oldest:","eg":[{"en":"eldest daughter;","cn":"长女"},{"en":"eldest brother","cn":"长兄"}]}]},{"pos":"名词","v":[{"k":"（领导人） chief; head; leader:","eg":[{"en":"head of a delegation;","cn":"代表团团长"},{"en":"chairman of the board;","cn":"董事长"}]}]},{"pos":"动词","v":[{"k":"（生） come into being; begin to grow; form:","eg":[{"en":"get cancer;","cn":"长癌"},{"en":"have a boil;","cn":"长疮"}]},{"k":"（生长; 成长） grow; develop:","eg":[{"en":"Youth is the time of physical growth.","cn":"青年时期是长身体的时期。"},{"en":"She has grown so that she is even a little taller than her mother.","cn":"她长得比母亲都高一点了。"}]},{"k":"（增进; 增加） acquire; enhance; increase:","eg":[{"en":"increase one's knowledge; gain experience;","cn":"长见识"},{"en":"Such a tendency is not to be encouraged.","cn":"此风不可长。"}]}]}]
         */

        @SerializedName("py")
        var py: String? = null
        @SerializedName("v")
        var v: List<VBean>? = null
        //TODO null

    }

    class VBeanX {
        /**
         * pos : 形容词
         * v : [{"k":"（年纪较大） older; elder; senior:","eg":[{"en":"older than her;","cn":"比她年长"},{"en":"My brother is senior to me by two years.","cn":"家兄比我年长两岁。"}]},{"k":"（排行最大） eldest; oldest:","eg":[{"en":"eldest daughter;","cn":"长女"},{"en":"eldest brother","cn":"长兄"}]}]
         */

        @SerializedName("pos")
        var pos: String? = null
        @SerializedName("v")
        var v: List<VBean>? = null
    }

    class VBean {
        /**
         * k : （年纪较大） older; elder; senior:
         * eg : [{"en":"older than her;","cn":"比她年长"},{"en":"My brother is senior to me by two years.","cn":"家兄比我年长两岁。"}]
         */

        @SerializedName("k")
        var k: String? = null
        @SerializedName("eg")
        var eg: List<EgBean>? = null
        @SerializedName("phrases")
        var phrases: List<EgBean>? = null

        class EgBean {
            /**
             * en : older than her;
             * cn : 比她年长
             */

            @SerializedName("en")
            var en: String? = null
            @SerializedName("cn")
            var cn: String? = null
        }

    }

    class EgBean {
        /**
         * en : older than her;
         * cn : 比她年长
         */

        @SerializedName("en")
        var en: String? = null
        @SerializedName("cn")
        var cn: String? = null
    }

    class AntonymBean {
        /**
         * cn : 短
         * cn_flag : false
         */

        @SerializedName("cn")
        var cn: String? = null
        @SerializedName("cn_flag")
        var isCnFlag: Boolean = false
    }

    class SynonymBean {
        /**
         * cn : 长短
         * cn_flag : false
         */
        @SerializedName("cn")
        var cn: String? = null
        @SerializedName("cn_flag")
        var isCn_flag: Boolean = false
    }
}
