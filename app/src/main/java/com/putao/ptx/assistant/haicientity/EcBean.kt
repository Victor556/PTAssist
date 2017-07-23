package com.putao.ptx.assistant.haicientity

import com.google.gson.annotations.SerializedName

/**
 *
 *
 * <br></br>Description  :
 * <br></br>
 * <br></br>Author       : Victor<liuhe556></liuhe556>@126.com>
 * <br></br>
 * <br></br>Created date : 2017-04-05
 */
class EcBean : ISimplePreview {
    override fun title(): MutableList<String> {
        return ArrayList<String>().also {
            it.add(item ?: "")
        }
    }

    override fun pronounce(): MutableList<String> {
        return ArrayList<String>().also {
            it.add(pronunciation?.amE?.symbol ?: "")
            it.add(pronunciation?.brE?.symbol ?: "")
        }
    }

    override fun basicExplain(): MutableList<String> {
        return ArrayList<String>().also {
            it.addAll(translation?.map { it.v ?: "" } ?: emptyList())
        }
    }

    override fun example(): MutableList<String> {
        return ArrayList<String>().also {
            it.addAll(sentence?.flatMap { it.v?.map { it.text ?: "" } ?: emptyList() } ?: emptyList())
        }
    }

    /**
     * item : good
     * translation : [{"pos":"adj.","v":"好的；上等的；优秀的"},{"pos":"n.","v":"好处；善行"},{"v":"(复)goods：商品；货物."}]
     * transform : [{"w":"better","t":"比较级"},{"w":"best","t":"最高级"}]
     * pronunciation : {"BrE":{"symbol":"ɡʊd","url":"http://audio.dict.cn/fbTd30uN1f07b482e832bc91fbd568ffff987d19.mp3?t=good"},"AmE":{"symbol":"ɡʊd","url":"http://audio.dict.cn/fuTd30uNd3579d8c2f263d2b18b2716c1d4d9bff.mp3?t=good"}}
     * chart_senses : {"1":{"percent":96,"sense":"机器人"},"2":{"percent":4,"sense":"有人类特征的"}}
     * sentence : [{"pos":"adj.","v":[{"text":"A good video camera will cost you a lot of money.","trans":"一台好的摄像机要花掉你很多钱。"},{"text":"My one good suit is at the cleaner's.","trans":"我那套讲究的衣服还在洗衣店里呢。"},{"text":"If you train hard, you'll make a good footballer.","trans":"你要刻苦训练就能成为优秀的足球运动员。"}]},{"pos":"n.","v":[{"text":"Complaining won't do you any good.","trans":"抱怨对你没有任何好处。"},{"text":"Good is rewarded with evil.","trans":"善有恶报。"}]}]
     * ophrase : [{"pos":"adj.","v":[{"phrase":"a good few","details":{"explanation":"相当多(的),几个 a considerable number (of); several"}},{"phrase":"as good as","details":{"explanation":"几乎,实际上 almost, practically"}},{"phrase":"good and \\u002e\\u002e\\u002e","details":{"explanation":"〈美口〉完全,彻底 completely","more_details":[{"phrase":"good and+adj\\u002e","notes":["I won't go until I'm good and ready.","我完全准备好了才去。","She made me good and mad.","她使我如痴如醉。","She was good and angry.","她非常生气。","I was good and tired.","我很累。","He knew good and well that she had stolen his watch.","他清楚地知道她曾偷了他的表。","The melon was good and sweet.","这瓜很甜。"]}]}},{"phrase":"good for sb","details":{"explanation":"某人干得好(用以祝贺某人) doing well (used when congratulating sb)"}}]},{"pos":"n.","v":[{"phrase":"do good","details":{"explanation":"有益于某人 benefit sb","more_details":[{"phrase":"do good","notes":["This cough medicine tastes nice but it doesn't do much good.","这咳嗽药不难吃,但作用不大。"]},{"phrase":"do sb good","notes":["Eat more fruit, it will do you good.","多吃水果,对你有好处。","Much good may it do you.","但愿对你大有好处。"]},{"phrase":"do good to sb","notes":["You should do good to others.","你应该对别人行善。"]}]}},{"phrase":"for good (and all)","details":{"explanation":"永久,永远,决定性地 permanently; finally"}},{"phrase":"to the good","details":{"explanation":"(用以记述某人的财务状况)盈余 (used to describe sb's financial state) in credit"}},{"phrase":"up to no good","details":{"explanation":"做坏事,淘气,恶作剧 doing sth wrong, mischievous, etc."}}]}]
     * vocabulary : [{"pos":"adj.","v":[{"explanation":"～+名词","list":[{"en":"good beating","cn":"痛打"},{"en":"good distance","cn":"相当远的距离"},{"en":"good journey","cn":"一路平安"},{"en":"good old days","cn":"美好的往日"},{"en":"good six feet","cn":"足足六英尺"},{"en":"good works","cn":"慈善事业"}]},{"explanation":"～+介词","list":[{"en":"good as","cn":"和\u2026几乎一样,实际上等于,差不多,非常"},{"en":"good at","cn":"擅长\u2026的"},{"en":"good at catching rats","cn":"善于捕鼠"},{"en":"good at French","cn":"精通法文"},{"en":"good for","cn":"对\u2026有用,有效,宜于,适用,胜任"},{"en":"good for nothing","cn":"一无所长"},{"en":"good in","cn":"擅长\u2026的"},{"en":"good to","cn":"对\u2026厚道,对\u2026很好,有效到\u2026程度"},{"en":"good to one's children","cn":"对孩子和蔼"},{"en":"good to the poor","cn":"对穷人慈善"},{"en":"good to the taste","cn":"味道好"},{"en":"good with","cn":"在\u2026方面有本事的"}]}]},{"pos":"n.","v":[{"explanation":"动词+～","list":[{"en":"do good","cn":"做好事"}]},{"explanation":"形容词+～","list":[{"en":"common good","cn":"共同的利益"}]},{"explanation":"介词+～","list":[{"en":"for good","cn":"从善"},{"en":"a lot of good","cn":"许多善事"}]}]}]
     * word_diff : [{"pos":"adj.","v":[{"items":"be good for sb, be good to sb","notes":["1.be good for sb 表示\u201c有益于某人\u201d, be good to sb 表示\u201c待某人好\u201d。","2.be good for还可接sth/to- v ,而be good to不可。"]},{"items":"good at, good to, good with","notes":["这三个短语都常用于系动词后作表语,其区别是：","1.good at的意思是\u201c善于,擅长\u201d; good with的意思是\u201c善用(某物)\u201d\u201c善待(某人)\u201d; good to的意思是\u201c对\u2026慈爱友善\u201d\u201c对\u2026有益\u201d。","2.good at后主要接表示科目、活动的名词或动名词, good with后主要接表示工具、人体器官、人的名词; good to后主要接表示人或人格化的名词。","3.good at的反义词是bad at。good at中的at可换成in; good to作\u201c对\u2026有益\u201d解时介词to可换成for。"]},{"items":"good, fine, nice, well","notes":["这组词都有\u201c好\u201d的意思。其区别在于:good意为\u201c好\u201d,常用词,含义很广,一般用作定语和表语; fine主要指质量、特点、能力方面的\u201c好\u201d,语气比good强,也可指健康状况,相当于well; nice指某人或某物能取悦他人的感官,使人感到喜悦,感到舒适; well主要指人的健康状况好,只用作表语,有时也可指情况状态正常,良好。例如:","He is a man of good family.他是一个家世很好的人。","He is living in a fine house.他住在一幢华丽的房子里。","We had a very nice trip to the Jiangnan Park yesterday.昨天我们去江南公园畅游了一番。"]},{"items":"good, goodly, well","notes":["这组形容词的共同意思是\u201c好的\u201d。其区别是：","1.good可指人或物的\u201c好\u201d,使用广泛; well专指人身体\u201c健康的\u201d,也指\u201c良好的\u201d\u201c幸运的\u201d; goodly指某物是\u201c质量好的\u201d\u201c极好的\u201d,也可指某人或某物是\u201c好看的\u201d\u201c漂亮的\u201d。例如:","I am very well, thank you.谢谢你,我身体很好。","So far as I know, things are well with them now.据我所知,现在他们事事顺利。","A beautiful car is a goodly gift.一辆漂亮的小车是上等礼品。","The table spread with food made a goodly sign.摆满了食物的桌子显得非常美观。","John is a goodly youth.约翰是一个漂亮的青年。","2.good可作表语,也可作定语; well只能作表语。"]},{"items":"下面两个句子意思不同:","notes":["She looks good.","她看起来很好看。","She looks well.","她看起来很健康。"]}]},{"pos":"n.","v":[{"items":"good for, for good","notes":["这两个短语构成相同,词序不同。其区别是：","1.good for中的good是形容词,也可作为名词; for good中的good是名词。","2.good for的意思是\u201c对\u2026有用〔益〕\u201d; 而for good的意思是\u201c永远\u201d\u201c永久\u201d\u201c为\u2026利益\u201d。","3.good for作形容词性短语时,在句中作表语或后置定语; 而for good是副词性短语,在句中作状语。","4.good for作\u201c价值为\u2026\u201d解时后接表示币值的名词,仅用于口语中,而for good无此意。"]},{"items":"good, goods","notes":["可以把good和goods看作是单复形式意义不同的词,也可干脆把goods看作是仅以名词形式出现的另一个词,意思是\u201c货物\u201d,没有单数形式,谓语动词用单数。"]}]}]
     * synonym : [{"pos":"adj.","v":[{"en":"able"},{"en":"acceptable"},{"en":"accomplished"},{"en":"admirable"},{"en":"agreeable"},{"en":"beneficial"},{"en":"cheerful"},{"en":"clear"},{"en":"considerate"},{"en":"convenient"},{"en":"correct"},{"en":"dependable"},{"en":"dutiful"},{"en":"efficient"},{"en":"enjoyable"},{"en":"fair"},{"en":"friendly"},{"en":"full"},{"en":"gracious"},{"en":"great"},{"en":"healthy"},{"en":"large"},{"en":"long"},{"en":"loyal"},{"en":"nice"},{"en":"noble"},{"en":"precious"},{"en":"super"}]},{"pos":"n.","v":[{"en":"advantage"},{"en":"benefit"},{"en":"excellence"},{"en":"goodness"},{"en":"interest"},{"en":"merit"},{"en":"morality"},{"en":"profit"},{"en":"right"},{"en":"use"},{"en":"usefulness"},{"en":"virtue"},{"en":"weal"},{"en":"worth"}]}]
     * antonym : [{"pos":"adj.","v":[{"en":"bad"},{"en":"evil"},{"en":"poor"},{"en":"unsuitable"},{"en":"unfriendly"},{"en":"unkind"},{"en":"ill"},{"en":"sick"},{"en":"unhealthy"}]}]
     * xx : {"item":"good","body":{"1":{"pos":"形容词","sense":"好的","phrase":{"1":{"en":"have a good time","cn":"过得愉快"},"2":{"en":"be good at ...","cn":"擅长于\u2026\u2026"}},"sen":{"pic":"http://dc.dfile.cn/v/89942511/5aaeabcd89db9543d5c6e8c0859b9090/xxjb/2/xxjbgood_1.jpg","en":{"1":"I am good at Maths."},"cn":{"1":"我数学很好。"}},"kt":{"1":{"text":"good和bad构成一组反义词。good可指人或物的\u201c好\u201d，使用广泛；bad指任何不好的或不合需要的品质。","eg":{"1":{"en":{"1":"good weather"},"cn":{"1":"好天气"},"pic":"http://dc.dfile.cn/v/89942511/63f811fae395a025a684f58448f07f6e/xxjb/2/xxjbgood_2.jpg"},"2":{"en":{"1":"bad news"},"cn":{"1":"坏消息"},"pic":"http://dc.dfile.cn/v/89942511/3412ac02f32b8f57520ebf42e73e4e84/xxjb/2/xxjbgood_3.jpg"}}}}}}}
     * chuzhong : {"item":"good","rate":{"cloze":"0.235(191/813)|2007(14)|2008(37)|2009(2)|2010(26)|2011(62)|2012(15)|2013(19)|2014(16)","reading":"0.558(454/813)|2007(56)|2008(118)|2009(53)|2010(32)|2011(47)|2012(31)|2013(60)|2014(57)","translation":"0.039(32/813)|2008(10)|2013(12)|2014(10)","danxuan":"0.167(136/813)|2007(10)|2008(26)|2009(3)|2010(29)|2011(3)|2012(24)|2013(14)|2014(27)","title":"在2007-2014年中考英语中，该词的考频统计如下："},"phrase":{"1":{"en":"be good at","cn":"擅长，善于"},"2":{"en":"be good for","cn":"对\u2026\u2026有好处"},"3":{"en":"be good to","cn":"对\u2026\u2026态度好"}},"transform":{"1":{"pos":"com.","text":"better"},"2":{"pos":"sup.","text":"best"}},"derivative":{"1":{"pos":"n.","text":"goodness"}},"basic":{"1":{"pos":"adj.","sense":"好；良好的","typical":{"1":{"en":"This is a good place for a picnic.","cn":"这是一个野餐的好地方。"}},"zhenti":{"1":{"en":"Recycling is good, so don't throw away bottles or newspapers.","src":"2014年中考英语江西卷 单项选择"},"2":{"en":"It's good for your health to get up early and exercise.","src":"2014年中考英语陕西省卷 翻译"}}}},"ss":["adj.好；良好的"]}
     */

    @SerializedName("item")
    var item: String? = null
    @SerializedName("pronunciation")
    var pronunciation: PronunciationBean? = null
    @SerializedName("translation")
    var translation: List<TranslationBean>? = null
    @SerializedName("sentence")
    var sentence: List<SentenceBean>? = null

    /*没有用到，注销*/
//    @SerializedName("chart_senses")
//    var chartSenses: Map<String, ChartSensesBean>? = null
//    @SerializedName("xx")
//    var xx: XxBean? = null
//    @SerializedName("chuzhong")
//    var chuzhong: ChuzhongBean? = null
//    @SerializedName("transform")
//    var transform: List<TransformBeanX>? = null
//    @SerializedName("ophrase")
//    var ophrase: List<OphraseBean>? = null
//    @SerializedName("vocabulary")
//    var vocabulary: List<VocabularyBean>? = null
//    @SerializedName("word_diff")
//    var wordDiff: List<WordDiffBean>? = null
//    @SerializedName("synonym")
//    var synonym: List<SynonymBean>? = null
//    @SerializedName("antonym")
//    var antonym: List<AntonymBean>? = null

    class PronunciationBean {
        /**
         * BrE : {"symbol":"ɡʊd","url":"http://audio.dict.cn/fbTd30uN1f07b482e832bc91fbd568ffff987d19.mp3?t=good"}
         * AmE : {"symbol":"ɡʊd","url":"http://audio.dict.cn/fuTd30uNd3579d8c2f263d2b18b2716c1d4d9bff.mp3?t=good"}
         */

        @SerializedName("BrE")
        var brE: BrEBean? = null
        @SerializedName("AmE")
        var amE: AmEBean? = null

        class BrEBean {
            /**
             * symbol : ɡʊd
             * url : http://audio.dict.cn/fbTd30uN1f07b482e832bc91fbd568ffff987d19.mp3?t=good
             */

            @SerializedName("symbol")
            var symbol: String? = null
            @SerializedName("url")
            var url: String? = null
        }

        class AmEBean {
            /**
             * symbol : ɡʊd
             * url : http://audio.dict.cn/fuTd30uNd3579d8c2f263d2b18b2716c1d4d9bff.mp3?t=good
             */

            @SerializedName("symbol")
            var symbol: String? = null
            @SerializedName("url")
            var url: String? = null
        }
    }

    class ChartSensesBean {
        /**
         * 1 : {"percent":96,"sense":"机器人"}
         * 2 : {"percent":4,"sense":"有人类特征的"}
         */
        @SerializedName("percent")
        var percent: Int = 0
        @SerializedName("sense")
        var sense: String? = null
    }

    class XxBean {
        /**
         * item : good
         * body : {"1":{"pos":"形容词","sense":"好的","phrase":{"1":{"en":"have a good time","cn":"过得愉快"},"2":{"en":"be good at ...","cn":"擅长于\u2026\u2026"}},"sen":{"pic":"http://dc.dfile.cn/v/89942511/5aaeabcd89db9543d5c6e8c0859b9090/xxjb/2/xxjbgood_1.jpg","en":{"1":"I am good at Maths."},"cn":{"1":"我数学很好。"}},"kt":{"1":{"text":"good和bad构成一组反义词。good可指人或物的\u201c好\u201d，使用广泛；bad指任何不好的或不合需要的品质。","eg":{"1":{"en":{"1":"good weather"},"cn":{"1":"好天气"},"pic":"http://dc.dfile.cn/v/89942511/63f811fae395a025a684f58448f07f6e/xxjb/2/xxjbgood_2.jpg"},"2":{"en":{"1":"bad news"},"cn":{"1":"坏消息"},"pic":"http://dc.dfile.cn/v/89942511/3412ac02f32b8f57520ebf42e73e4e84/xxjb/2/xxjbgood_3.jpg"}}}}}}
         */

        @SerializedName("item")
        var item: String? = null
        @SerializedName("body")
        var body: Map<String, BodyBean>? = null

        class BodyBean {
            /**
             * 1 : {"pos":"形容词","sense":"好的","phrase":{"1":{"en":"have a good time","cn":"过得愉快"},"2":{"en":"be good at ...","cn":"擅长于\u2026\u2026"}},"sen":{"pic":"http://dc.dfile.cn/v/89942511/5aaeabcd89db9543d5c6e8c0859b9090/xxjb/2/xxjbgood_1.jpg","en":{"1":"I am good at Maths."},"cn":{"1":"我数学很好。"}},"kt":{"1":{"text":"good和bad构成一组反义词。good可指人或物的\u201c好\u201d，使用广泛；bad指任何不好的或不合需要的品质。","eg":{"1":{"en":{"1":"good weather"},"cn":{"1":"好天气"},"pic":"http://dc.dfile.cn/v/89942511/63f811fae395a025a684f58448f07f6e/xxjb/2/xxjbgood_2.jpg"},"2":{"en":{"1":"bad news"},"cn":{"1":"坏消息"},"pic":"http://dc.dfile.cn/v/89942511/3412ac02f32b8f57520ebf42e73e4e84/xxjb/2/xxjbgood_3.jpg"}}}}}
             */

            /**
             * pos : 形容词
             * sense : 好的
             * phrase : {"1":{"en":"have a good time","cn":"过得愉快"},"2":{"en":"be good at ...","cn":"擅长于\u2026\u2026"}}
             * sen : {"pic":"http://dc.dfile.cn/v/89942511/5aaeabcd89db9543d5c6e8c0859b9090/xxjb/2/xxjbgood_1.jpg","en":{"1":"I am good at Maths."},"cn":{"1":"我数学很好。"}}
             * kt : {"1":{"text":"good和bad构成一组反义词。good可指人或物的\u201c好\u201d，使用广泛；bad指任何不好的或不合需要的品质。","eg":{"1":{"en":{"1":"good weather"},"cn":{"1":"好天气"},"pic":"http://dc.dfile.cn/v/89942511/63f811fae395a025a684f58448f07f6e/xxjb/2/xxjbgood_2.jpg"},"2":{"en":{"1":"bad news"},"cn":{"1":"坏消息"},"pic":"http://dc.dfile.cn/v/89942511/3412ac02f32b8f57520ebf42e73e4e84/xxjb/2/xxjbgood_3.jpg"}}}}
             */

            @SerializedName("pos")
            var pos: String? = null
            @SerializedName("sense")
            var sense: String? = null
            @SerializedName("phrase")
            var phrase: PhraseBean? = null
            @SerializedName("sen")
            var sen: SenBean? = null
            @SerializedName("kt")
            var kt: Map<String, KtBean>? = null

            class PhraseBean {
                /**
                 * 1 : {"en":"have a good time","cn":"过得愉快"}
                 * 2 : {"en":"be good at ...","cn":"擅长于\u2026\u2026"}
                 */
                /**
                 * en : have a good time
                 * cn : 过得愉快
                 */

                @SerializedName("en")
                var en: String? = null
                @SerializedName("cn")
                var cn: String? = null
            }

            class SenBean {
                /**
                 * pic : http://dc.dfile.cn/v/89942511/5aaeabcd89db9543d5c6e8c0859b9090/xxjb/2/xxjbgood_1.jpg
                 * en : {"1":"I am good at Maths."}
                 * cn : {"1":"我数学很好。"}
                 */

                @SerializedName("pic")
                var pic: String? = null
                @SerializedName("en")
                var en: Map<String, String>? = null
                @SerializedName("cn")
                var cn: Map<String, String>? = null
            }

            class KtBean {
                /**
                 * 1 : {"text":"good和bad构成一组反义词。good可指人或物的\u201c好\u201d，使用广泛；bad指任何不好的或不合需要的品质。","eg":{"1":{"en":{"1":"good weather"},"cn":{"1":"好天气"},"pic":"http://dc.dfile.cn/v/89942511/63f811fae395a025a684f58448f07f6e/xxjb/2/xxjbgood_2.jpg"},"2":{"en":{"1":"bad news"},"cn":{"1":"坏消息"},"pic":"http://dc.dfile.cn/v/89942511/3412ac02f32b8f57520ebf42e73e4e84/xxjb/2/xxjbgood_3.jpg"}}}
                 */

                /**
                 * text : good和bad构成一组反义词。good可指人或物的“好”，使用广泛；bad指任何不好的或不合需要的品质。
                 * eg : {"1":{"en":{"1":"good weather"},"cn":{"1":"好天气"},"pic":"http://dc.dfile.cn/v/89942511/63f811fae395a025a684f58448f07f6e/xxjb/2/xxjbgood_2.jpg"},"2":{"en":{"1":"bad news"},"cn":{"1":"坏消息"},"pic":"http://dc.dfile.cn/v/89942511/3412ac02f32b8f57520ebf42e73e4e84/xxjb/2/xxjbgood_3.jpg"}}
                 */

                @SerializedName("text")
                var text: String? = null
                @SerializedName("eg")
                var eg: Map<String, EgBean>? = null

                class EgBean {
                    /**
                     * 1 : {"en":{"1":"good weather"},"cn":{"1":"好天气"},"pic":"http://dc.dfile.cn/v/89942511/63f811fae395a025a684f58448f07f6e/xxjb/2/xxjbgood_2.jpg"}
                     * 2 : {"en":{"1":"bad news"},"cn":{"1":"坏消息"},"pic":"http://dc.dfile.cn/v/89942511/3412ac02f32b8f57520ebf42e73e4e84/xxjb/2/xxjbgood_3.jpg"}
                     */
                    /**
                     * en : {"1":"good weather"}
                     * cn : {"1":"好天气"}
                     * pic : http://dc.dfile.cn/v/89942511/63f811fae395a025a684f58448f07f6e/xxjb/2/xxjbgood_2.jpg
                     */

                    @SerializedName("en")
                    var en: Map<String, String>? = null
                    @SerializedName("cn")
                    var cn: Map<String, String>? = null
                    @SerializedName("pic")
                    var pic: String? = null
                }
            }
        }
    }

    class ChuzhongBean {
        /**
         * item : good
         * rate : {"cloze":"0.235(191/813)|2007(14)|2008(37)|2009(2)|2010(26)|2011(62)|2012(15)|2013(19)|2014(16)","reading":"0.558(454/813)|2007(56)|2008(118)|2009(53)|2010(32)|2011(47)|2012(31)|2013(60)|2014(57)","translation":"0.039(32/813)|2008(10)|2013(12)|2014(10)","danxuan":"0.167(136/813)|2007(10)|2008(26)|2009(3)|2010(29)|2011(3)|2012(24)|2013(14)|2014(27)","title":"在2007-2014年中考英语中，该词的考频统计如下："}
         * phrase : {"1":{"en":"be good at","cn":"擅长，善于"},"2":{"en":"be good for","cn":"对\u2026\u2026有好处"},"3":{"en":"be good to","cn":"对\u2026\u2026态度好"}}
         * transform : {"1":{"pos":"com.","text":"better"},"2":{"pos":"sup.","text":"best"}}
         * derivative : {"1":{"pos":"n.","text":"goodness"}}
         * basic : {"1":{"pos":"adj.","sense":"好；良好的","typical":{"1":{"en":"This is a good place for a picnic.","cn":"这是一个野餐的好地方。"}},"zhenti":{"1":{"en":"Recycling is good, so don't throw away bottles or newspapers.","src":"2014年中考英语江西卷 单项选择"},"2":{"en":"It's good for your health to get up early and exercise.","src":"2014年中考英语陕西省卷 翻译"}}}}
         * ss : ["adj.好；良好的"]
         */

        @SerializedName("item")
        var item: String? = null
        @SerializedName("rate")
        var rate: RateBean? = null
        @SerializedName("phrase")
        var phrase: Map<String, PhraseBeanX>? = null
        @SerializedName("transform")
        var transform: Map<String, TransformBean>? = null
        @SerializedName("derivative")
        var derivative: Map<String, DerivativeBean>? = null
        @SerializedName("basic")
        var basic: Map<String, BasicBean>? = null
        @SerializedName("ss")
        var ss: List<String>? = null

        class RateBean {
            /**
             * cloze : 0.235(191/813)|2007(14)|2008(37)|2009(2)|2010(26)|2011(62)|2012(15)|2013(19)|2014(16)
             * reading : 0.558(454/813)|2007(56)|2008(118)|2009(53)|2010(32)|2011(47)|2012(31)|2013(60)|2014(57)
             * translation : 0.039(32/813)|2008(10)|2013(12)|2014(10)
             * danxuan : 0.167(136/813)|2007(10)|2008(26)|2009(3)|2010(29)|2011(3)|2012(24)|2013(14)|2014(27)
             * title : 在2007-2014年中考英语中，该词的考频统计如下：
             */

            @SerializedName("cloze")
            var cloze: String? = null
            @SerializedName("reading")
            var reading: String? = null
            @SerializedName("translation")
            var translation: String? = null
            @SerializedName("danxuan")
            var danxuan: String? = null
            @SerializedName("title")
            var title: String? = null
        }

        class PhraseBeanX {
            /**
             * 1 : {"en":"be good at","cn":"擅长，善于"}
             * 2 : {"en":"be good for","cn":"对\u2026\u2026有好处"}
             * 3 : {"en":"be good to","cn":"对\u2026\u2026态度好"}
             */

            /**
             * en : be good at
             * cn : 擅长，善于
             */

            @SerializedName("en")
            var en: String? = null
            @SerializedName("cn")
            var cn: String? = null
        }

        class TransformBean {
            /**
             * 1 : {"pos":"com.","text":"better"}
             * 2 : {"pos":"sup.","text":"best"}
             */

            /**
             * pos : com.
             * text : better
             */

            @SerializedName("pos")
            var pos: String? = null
            @SerializedName("text")
            var text: String? = null
        }

        class DerivativeBean {
            /**
             * 1 : {"pos":"n.","text":"goodness"}
             */
            /**
             * pos : n.
             * text : goodness
             */

            @SerializedName("pos")
            var pos: String? = null
            @SerializedName("text")
            var text: String? = null
        }

        class BasicBean {
            /**
             * 1 : {"pos":"adj.","sense":"好；良好的","typical":{"1":{"en":"This is a good place for a picnic.","cn":"这是一个野餐的好地方。"}},"zhenti":{"1":{"en":"Recycling is good, so don't throw away bottles or newspapers.","src":"2014年中考英语江西卷 单项选择"},"2":{"en":"It's good for your health to get up early and exercise.","src":"2014年中考英语陕西省卷 翻译"}}}
             */

            /**
             * pos : adj.
             * sense : 好；良好的
             * typical : {"1":{"en":"This is a good place for a picnic.","cn":"这是一个野餐的好地方。"}}
             * zhenti : {"1":{"en":"Recycling is good, so don't throw away bottles or newspapers.","src":"2014年中考英语江西卷 单项选择"},"2":{"en":"It's good for your health to get up early and exercise.","src":"2014年中考英语陕西省卷 翻译"}}
             */

            @SerializedName("pos")
            var pos: String? = null
            @SerializedName("sense")
            var sense: String? = null
            @SerializedName("typical")
            var typical: Map<String, TypicalBean>? = null
            @SerializedName("zhenti")
            var zhenti: Map<String, ZhentiBean>? = null

            class TypicalBean {
                /**
                 * 1 : {"en":"This is a good place for a picnic.","cn":"这是一个野餐的好地方。"}
                 */

                /**
                 * en : This is a good place for a picnic.
                 * cn : 这是一个野餐的好地方。
                 */

                @SerializedName("en")
                var en: String? = null
                @SerializedName("cn")
                var cn: String? = null
            }

            class ZhentiBean {
                /**
                 * 1 : {"en":"Recycling is good, so don't throw away bottles or newspapers.","src":"2014年中考英语江西卷 单项选择"}
                 * 2 : {"en":"It's good for your health to get up early and exercise.","src":"2014年中考英语陕西省卷 翻译"}
                 */

                /**
                 * en : Recycling is good, so don't throw away bottles or newspapers.
                 * src : 2014年中考英语江西卷 单项选择
                 */

                @SerializedName("en")
                var en: String? = null
                @SerializedName("src")
                var src: String? = null

            }
        }
    }

    class TranslationBean {
        /**
         * pos : adj.
         * v : 好的；上等的；优秀的
         */

        @SerializedName("pos")
        var pos: String? = null
        @SerializedName("v")
        var v: String? = null
    }

    class TransformBeanX {
        /**
         * w : better
         * t : 比较级
         */

        @SerializedName("w")
        var w: String? = null
        @SerializedName("t")
        var t: String? = null
    }

    class SentenceBean {
        /**
         * pos : adj.
         * v : [{"text":"A good video camera will cost you a lot of money.","trans":"一台好的摄像机要花掉你很多钱。"},{"text":"My one good suit is at the cleaner's.","trans":"我那套讲究的衣服还在洗衣店里呢。"},{"text":"If you train hard, you'll make a good footballer.","trans":"你要刻苦训练就能成为优秀的足球运动员。"}]
         */

        @SerializedName("pos")
        var pos: String? = null
        @SerializedName("v")
        var v: List<VBean>? = null

        class VBean {
            /**
             * text : A good video camera will cost you a lot of money.
             * trans : 一台好的摄像机要花掉你很多钱。
             */

            @SerializedName("text")
            var text: String? = null
            @SerializedName("trans")
            var trans: String? = null
        }
    }

    class OphraseBean {
        /**
         * pos : adj.
         * v : [{"phrase":"a good few","details":{"explanation":"相当多(的),几个 a considerable number (of); several"}},{"phrase":"as good as","details":{"explanation":"几乎,实际上 almost, practically"}},{"phrase":"good and \\u002e\\u002e\\u002e","details":{"explanation":"〈美口〉完全,彻底 completely","more_details":[{"phrase":"good and+adj\\u002e","notes":["I won't go until I'm good and ready.","我完全准备好了才去。","She made me good and mad.","她使我如痴如醉。","She was good and angry.","她非常生气。","I was good and tired.","我很累。","He knew good and well that she had stolen his watch.","他清楚地知道她曾偷了他的表。","The melon was good and sweet.","这瓜很甜。"]}]}},{"phrase":"good for sb","details":{"explanation":"某人干得好(用以祝贺某人) doing well (used when congratulating sb)"}}]
         */

        @SerializedName("pos")
        var pos: String? = null
        @SerializedName("v")
        var v: List<VBeanX>? = null
        @SerializedName("en_flag")
        var enFlag: Boolean = false

        class VBeanX {
            /**
             * phrase : a good few
             * details : {"explanation":"相当多(的),几个 a considerable number (of); several"}
             */

            @SerializedName("phrase")
            var phrase: String? = null
            @SerializedName("details")
            var details: DetailsBean? = null

            class DetailsBean {
                /**
                 * explanation : 相当多(的),几个 a considerable number (of); several
                 */

                @SerializedName("explanation")
                var explanation: String? = null
                @SerializedName("more_details")
                private var moreDetails: List<MoreDetailsBean>? = null

                class MoreDetailsBean {
                    /**
                     * phrase : good and+adj\u002e
                     * notes : ["I won't go until I'm good and ready.","我完全准备好了才去。","She made me good and mad.","她使我如痴如醉。","She was good and angry.","她非常生气。","I was good and tired.","我很累。","He knew good and well that she had stolen his watch.","他清楚地知道她曾偷了他的表。","The melon was good and sweet.","这瓜很甜。"]
                     */

                    @SerializedName("phrase")
                    var phrase: String? = null
                    @SerializedName("notes")
                    var notes: List<String>? = null
                }
            }
        }
    }

    class VocabularyBean {
        /**
         * pos : adj.
         * v : [{"explanation":"～+名词","list":[{"en":"good beating","cn":"痛打"},{"en":"good distance","cn":"相当远的距离"},{"en":"good journey","cn":"一路平安"},{"en":"good old days","cn":"美好的往日"},{"en":"good six feet","cn":"足足六英尺"},{"en":"good works","cn":"慈善事业"}]},{"explanation":"～+介词","list":[{"en":"good as","cn":"和\u2026几乎一样,实际上等于,差不多,非常"},{"en":"good at","cn":"擅长\u2026的"},{"en":"good at catching rats","cn":"善于捕鼠"},{"en":"good at French","cn":"精通法文"},{"en":"good for","cn":"对\u2026有用,有效,宜于,适用,胜任"},{"en":"good for nothing","cn":"一无所长"},{"en":"good in","cn":"擅长\u2026的"},{"en":"good to","cn":"对\u2026厚道,对\u2026很好,有效到\u2026程度"},{"en":"good to one's children","cn":"对孩子和蔼"},{"en":"good to the poor","cn":"对穷人慈善"},{"en":"good to the taste","cn":"味道好"},{"en":"good with","cn":"在\u2026方面有本事的"}]}]
         */

        @SerializedName("pos")
        var pos: String? = null
        @SerializedName("v")
        var v: List<VBeanXX>? = null

        class VBeanXX {
            /**
             * explanation : ～+名词
             * list : [{"en":"good beating","cn":"痛打"},{"en":"good distance","cn":"相当远的距离"},{"en":"good journey","cn":"一路平安"},{"en":"good old days","cn":"美好的往日"},{"en":"good six feet","cn":"足足六英尺"},{"en":"good works","cn":"慈善事业"}]
             */

            @SerializedName("explanation")
            var explanation: String? = null
            @SerializedName("list")
            var list: List<ListBean>? = null

            class ListBean {
                /**
                 * en : good beating
                 * cn : 痛打
                 */

                @SerializedName("en")
                var en: String? = null
                @SerializedName("cn")
                var cn: String? = null
            }
        }
    }

    class WordDiffBean {
        /**
         * pos : adj.
         * v : [{"items":"be good for sb, be good to sb","notes":["1.be good for sb 表示\u201c有益于某人\u201d, be good to sb 表示\u201c待某人好\u201d。","2.be good for还可接sth/to- v ,而be good to不可。"]},{"items":"good at, good to, good with","notes":["这三个短语都常用于系动词后作表语,其区别是：","1.good at的意思是\u201c善于,擅长\u201d; good with的意思是\u201c善用(某物)\u201d\u201c善待(某人)\u201d; good to的意思是\u201c对\u2026慈爱友善\u201d\u201c对\u2026有益\u201d。","2.good at后主要接表示科目、活动的名词或动名词, good with后主要接表示工具、人体器官、人的名词; good to后主要接表示人或人格化的名词。","3.good at的反义词是bad at。good at中的at可换成in; good to作\u201c对\u2026有益\u201d解时介词to可换成for。"]},{"items":"good, fine, nice, well","notes":["这组词都有\u201c好\u201d的意思。其区别在于:good意为\u201c好\u201d,常用词,含义很广,一般用作定语和表语; fine主要指质量、特点、能力方面的\u201c好\u201d,语气比good强,也可指健康状况,相当于well; nice指某人或某物能取悦他人的感官,使人感到喜悦,感到舒适; well主要指人的健康状况好,只用作表语,有时也可指情况状态正常,良好。例如:","He is a man of good family.他是一个家世很好的人。","He is living in a fine house.他住在一幢华丽的房子里。","We had a very nice trip to the Jiangnan Park yesterday.昨天我们去江南公园畅游了一番。"]},{"items":"good, goodly, well","notes":["这组形容词的共同意思是\u201c好的\u201d。其区别是：","1.good可指人或物的\u201c好\u201d,使用广泛; well专指人身体\u201c健康的\u201d,也指\u201c良好的\u201d\u201c幸运的\u201d; goodly指某物是\u201c质量好的\u201d\u201c极好的\u201d,也可指某人或某物是\u201c好看的\u201d\u201c漂亮的\u201d。例如:","I am very well, thank you.谢谢你,我身体很好。","So far as I know, things are well with them now.据我所知,现在他们事事顺利。","A beautiful car is a goodly gift.一辆漂亮的小车是上等礼品。","The table spread with food made a goodly sign.摆满了食物的桌子显得非常美观。","John is a goodly youth.约翰是一个漂亮的青年。","2.good可作表语,也可作定语; well只能作表语。"]},{"items":"下面两个句子意思不同:","notes":["She looks good.","她看起来很好看。","She looks well.","她看起来很健康。"]}]
         */

        @SerializedName("pos")
        var pos: String? = null
        @SerializedName("v")
        var v: List<VBeanXXX>? = null

        class VBeanXXX {
            /**
             * items : be good for sb, be good to sb
             * notes : ["1.be good for sb 表示\u201c有益于某人\u201d, be good to sb 表示\u201c待某人好\u201d。","2.be good for还可接sth/to- v ,而be good to不可。"]
             */

            @SerializedName("items")
            var items: String? = null
            @SerializedName("notes")
            var notes: List<String>? = null
        }
    }

    class SynonymBean {
        /**
         * pos : adj.
         * v : [{"en":"able"},{"en":"acceptable"},{"en":"accomplished"},{"en":"admirable"},{"en":"agreeable"},{"en":"beneficial"},{"en":"cheerful"},{"en":"clear"},{"en":"considerate"},{"en":"convenient"},{"en":"correct"},{"en":"dependable"},{"en":"dutiful"},{"en":"efficient"},{"en":"enjoyable"},{"en":"fair"},{"en":"friendly"},{"en":"full"},{"en":"gracious"},{"en":"great"},{"en":"healthy"},{"en":"large"},{"en":"long"},{"en":"loyal"},{"en":"nice"},{"en":"noble"},{"en":"precious"},{"en":"super"}]
         */

        @SerializedName("pos")
        var pos: String? = null
        @SerializedName("v")
        var v: List<VBeanXXXX>? = null

        class VBeanXXXX {
            /**
             * en : able
             */

            @SerializedName("en")
            var en: String? = null
        }
    }

    class AntonymBean {
        /**
         * pos : adj.
         * v : [{"en":"bad"},{"en":"evil"},{"en":"poor"},{"en":"unsuitable"},{"en":"unfriendly"},{"en":"unkind"},{"en":"ill"},{"en":"sick"},{"en":"unhealthy"}]
         */

        @SerializedName("pos")
        var pos: String? = null
        @SerializedName("v")
        var v: List<VBeanXXXXX>? = null

        class VBeanXXXXX {
            /**
             * en : bad
             */

            @SerializedName("en")
            var en: String? = null
        }
    }
}