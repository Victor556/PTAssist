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
class ChengyuBean : ISimplePreview {
    override fun title(): MutableList<String> {
        return ArrayList<String>().also {
            it.add(item ?: "")
        }
    }

    override fun pronounce(): MutableList<String> {
        return ArrayList<String>().also {
            it.add(pinyin ?: "")
        }
    }

    override fun basicExplain(): MutableList<String> {
        return ArrayList<String>().also {
            it.addAll(sense?.map { it?.def ?: "" } ?: emptyList())
        }
    }

    override fun example(): MutableList<String> {
        return ArrayList<String>().also {
            it.addAll(example?.map { it?.ex ?: "" } ?: emptyList())
        }
    }

    /**
     * item : 刻舟求剑
     * pinyin : kè zhōu qiú jiàn
     * sense : [{"def":"比喻不懂事物已发展变化而仍静止地看问题。"}]
     * csrc : ["《吕氏春秋·察今》》：\u201c楚人有涉江者，其剑自舟中坠于水，遽契其舟曰：\u2018是吾剑之所从坠。\u2019舟止，从其所契者入水求之。舟已行矣，而剑不行，求剑若此，不亦惑乎？\u201d"]
     * example : [{"ex":"似你这样寻根究底，便是～，胶柱鼓瑟了！","src":"清·曹雪芹《红楼梦》第一百二十回"}]
     * synonym : ["守株待兔","墨守成规"]
     * antonym : ["看风使舵","见机行事"]
     * story : 战国时，楚国有个人坐船渡江。船到江心，他一不小心，把随身携带的一把宝剑掉落江中。他赶紧去抓，已经来不及了。 船上的人对此感到非常惋惜，但那楚人似乎胸有成竹，马上掏出一把小刀，在船舷上刻上一个记号，并向大家说：“这是我宝剑落水的地方，所以我要刻上一个记号。” 大家都不理解他为什么这样做，也不再去问他。 船靠岸后那楚人立即在船上刻记号的地方下水，去捞取掉落的宝剑。捞了半天，不见宝剑的影子。他觉得很奇怪，自言自语说：“我的宝剑不就是在这里掉下去吗？我还在这里刻了记号呢，怎么会找不到的呢？” 至此，船上的人纷纷大笑起来，说：“船一直在行进，而你的宝剑却沉入了水底不动，你怎么找得到你的剑呢？” 其实，剑掉落在江中后，船继续行驶，而宝剑却不会再移动。像他这样去找剑，真是太愚蠢可笑了。
     */

    @SerializedName("item")
    var item: String? = null
    @SerializedName("pinyin")
    var pinyin: String? = null
    @SerializedName("story")
    var story: String? = null
    @SerializedName("sense")
    var sense: List<SenseBean>? = null
    @SerializedName("csrc")
    var csrc: List<String>? = null
    @SerializedName("example")
    var example: List<ExampleBean>? = null
    @SerializedName("synonym")
    var synonym: List<String>? = null
    @SerializedName("antonym")
    var antonym: List<String>? = null

    @SerializedName("cn_flag")
    var cn_flag: Boolean = false
    @SerializedName("synonym_flag")
    var synonym_flag: List<*>? = null
    @SerializedName("antonym_flag")
    var antonym_flag: List<*>? = null


    class SenseBean {
        /**
         * def : 比喻不懂事物已发展变化而仍静止地看问题。
         */

        @SerializedName("def")
        var def: String? = null
    }

    class ExampleBean {
        /**
         * ex : 似你这样寻根究底，便是～，胶柱鼓瑟了！
         * src : 清·曹雪芹《红楼梦》第一百二十回
         */

        @SerializedName("ex")
        var ex: String? = null
        @SerializedName("src")
        var src: String? = null
    }
}