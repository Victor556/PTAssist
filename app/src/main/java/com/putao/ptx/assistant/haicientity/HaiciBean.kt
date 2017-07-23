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
class HaiciBean {

    /**
     * http_status_code : 200
     * data : {"ce":{"item":"长","def":["long","length","forever","always","constantly","longitude","lengthily"],"pinyin":["zhǎng","cháng"],"sentence":[{"text":"我对他并不了解，虽然我认识他已经很长时间了。","trans":"I don't know him well though I've known him for a long time."},{"text":"她保持着长距离游泳的世界纪录。","trans":"She holds the world record for long distance swimming."},{"text":"我和她长谈了一次。","trans":"I had a long talk with her."},{"text":"我不喜欢这种长时间的等待。","trans":"I don't like this long wait."},{"text":"这条线是那条线的四倍长。","trans":"This line is four times as long as that one."},{"text":"她身材苗条，有一头长长的黑发。","trans":"She was slender and had long dark hair."},{"text":"到那儿要多长时间？","trans":"How long will it take me to get there?"},{"text":"这座桥差不多有两公里长。","trans":"The bridge is almost 2 kilometers long."}],"antonym":[{"cn":"短","cn_flag":false},{"cn":"消","cn_flag":false},{"cn":"幼","cn_flag":true},{"cn":"少","cn_flag":true}],"synonym":[{"cn":"长短","cn_flag":false},{"cn":"长度","cn_flag":false},{"cn":"冗长","cn_flag":false},{"cn":"尺寸","cn_flag":false},{"cn":"生长","cn_flag":false},{"cn":"生根","cn_flag":false},{"cn":"漫长","cn_flag":false},{"cn":"漫漫","cn_flag":false},{"cn":"修长","cn_flag":false},{"cn":"修","cn_flag":false}],"refer_def":[{"py":"zhǎng","v":[{"k":"（排行最大） eldest; oldest:","eg":[{"en":"eldest daughter;","cn":"长女"},{"en":"eldest brother","cn":"长兄"}]},{"k":"（领导人） chief; head; leader:","eg":[{"en":"head of a delegation;","cn":"代表团团长"},{"en":"chairman of the board;","cn":"董事长"}]},{"k":"（增进; 增加） acquire; enhance; increase:","eg":[{"en":"increase one's knowledge; gain experience;","cn":"长见识"},{"en":"Such a tendency is not to be encouraged.","cn":"此风不可长。"}]}]},{"py":"cháng","v":[{"k":"（引申为永远） forever; lasting:","eg":[{"en":"depart from the world forever; pass away","cn":"与世长辞"}]},{"k":"（姓氏） a surname:","eg":[{"en":"Chang Wuzi","cn":"长武子"}]},{"k":"（对某事做得特别好; 擅长） be good at; be strong in:","eg":[{"en":"She is good at painting.; Painting is her forte.","cn":"她长于绘画。"}]},{"k":"（常） often:","eg":[{"en":"go about sth. little by little without a letup","cn":"细水长流"}]}]}],"cn_flag":true,"def_flag":[]},"zi":{"item":"长","fan":["長"],"bushou":"丿部","bihua":"4笔","wubi":"TAYI","jiegou":"单一结构","zaozi":"象形","bishun":"http://dict.cn/apis/output.php?id=hanzi_2_17589_0","modern":[{"pinyin":"cháng","sense":[{"pos":"形","subsense":[{"def":"空间或时间两端之间的距离大","exp":"～途｜～路｜～假"},{"def":"优秀的","exp":"～处｜一展～才"},{"def":"旧时读（zhànɡ），指多余的，剩余的","exp":"一无～物"}]},{"pos":"名","subsense":[{"def":"两点之间的距离","exp":"身～｜波～｜周～"},{"def":"优点；专精的技能","exp":"特～｜取～补短｜一技之～"},{"def":"（Cháng）姓"}]},{"pos":"动","subsense":[{"def":"两端点的距离达到","exp":"绳～２米｜全程～１００米｜这条河～５０千米"},{"def":"对某事做得特别好；精通","exp":"～音律｜～于写作｜～于演讲"}]},{"pos":"副","subsense":[{"def":"长时间地；久远地","exp":"～谈｜～逝｜细水～流"}]}],"audio":"http://audio.dict.cn/fzz0oN76cd6a7092a105beeaa5243a68df9676.mp3?t=ch%C3%A1ng"},{"pinyin":"zhǎng","sense":[{"pos":"动","subsense":[{"def":"年纪较大","exp":"我～她三岁"},{"def":"辈分较高","exp":"舅舅比外甥～一辈"},{"def":"生；出现","exp":"～锈｜～草｜山上～满了树"},{"def":"发育","exp":"生～｜成～｜～得快"},{"def":"增进；增加","exp":"增～｜～见识｜～力气"}]},{"pos":"名","subsense":[{"def":"主持人；领导人","exp":"校～｜首～｜村～"},{"def":"年纪大或辈分大的人","exp":"尊～｜师～｜兄～"}]},{"pos":"形","subsense":[{"def":"排行最大或第一的","exp":"～子｜～兄｜～孙"},{"def":"年纪较大的；辈分高的","exp":"～亲｜～者｜～辈"}]}],"audio":"http://audio.dict.cn/fzz0p6e18022356092a009fc6763961cef093c.mp3?t=zh%C7%8Eng"}],"cn_flag":true,"fan_flag":[]}}
     * msg :
     */

    @SerializedName("http_status_code")
    var httpStatusCode: Int = 0//200为正常201表示
    @SerializedName("data")
    var data: DataBean? = null
    @SerializedName("msg")
    var msg: String? = null

    class DataBean {

        @SerializedName("ce")
        var ce: CeBean? = null

        @SerializedName("zi")
        var zi: ZiBean? = null

        @SerializedName("chengyu")
        var chengyu: ChengyuBean? = null

        @SerializedName("ec")
        var ec: EcBean? = null

        @SerializedName("ci")
        var ci: CiBean? = null
    }

    fun isValid(): Boolean = data?.ce?.isTitleExplainValid() ?: false
            || data?.zi?.isTitleExplainValid() ?: false
            || data?.chengyu?.isTitleExplainValid() ?: false
            || data?.ec?.isTitleExplainValid() ?: false
            || data?.ci?.isTitleExplainValid() ?: false
}

