package com.putao.ptx.assistant.utils

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.SystemClock
import android.provider.MediaStore
import android.text.TextPaint
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import java.util.*

/**
 * <p>
 * <br/>Description  :
 * <br/>
 * <br/>Author       : Victor<liuhe556@126.com>
 * <br/>
 * <br/>Created date : 2017-04-19</p>
 */
fun breakStringForGet(str: String, lenlow: Int = 100, lenHigh: Int = 200, label: String = ",.!?，。！？"): List<String> {
    var ret = ArrayList<String>()
    var buf = StringBuffer()
    var max = 0
    for (i in 0..str.length - 1) {
        var tmp = str[i]
        buf.append(tmp)
        val length = buf.length
        max = maxOf(max, length)
        if (length >= lenHigh || length >= lenlow && label.contains(tmp)) {
            ret.add(buf.toString())
            buf.delete(0, length)
        }
    }
    if (buf.length > 0) {
        ret.add(buf.toString())
    }
    print("breakStringForGet  length:${ret.size}  max text length:$max\n")
    return ret
}


fun doSegmentGet(text: String, url: String = "http://10.1.23.57:3033/repeat-query"): String {
    var tm = SystemClock.elapsedRealtime()
    var param = text
    var result = ""
    var line: String? = null
    try {
        param = URLEncoder.encode(param, "utf-8")
        val urlName = url + "?query=" + param
        println("text length:${text.length}  url length:${urlName.length}")
        val U = URL(urlName)
        val connection = U.openConnection()
        connection.connect()
        val inStream = BufferedReader(InputStreamReader(connection.getInputStream(), "utf-8"))
        line = inStream.readLine()
        while (line != null) {
            result += line
            line = inStream.readLine()
        }
        inStream.close()
        println(line)
    } catch (e: Exception) {
        println(line + "$e")
        e.printStackTrace()
        Log.e(TAG, "  doSegmentGet $e")
    }
    Log.d(TAG, " waste time:${SystemClock.elapsedRealtime() - tm}")
    return result
}

fun Bitmap?.isBitmapPure(max: Int = 10): Boolean {
    this ?: return false
    val tm = System.currentTimeMillis()
    val w = width
    val h = height
    val p = getPixel(w / 2, h / 2)
    for (i in 0..w step 3) {
        for (j in 0..h step 3) {
            if (i < w && j < h && Math.abs(getPixel(i, j) - p) > max) {
                print("isBitmapPure:false waste time:${System.currentTimeMillis() - tm}\n")
                return false
            }
        }
    }
    return true
}


fun Context.isConnectedToNet(): Boolean {
    val tm = SystemClock.elapsedRealtime()
    val cnn = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    //wifi网络
    val wifiNetInfo = cnn.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    val mobileNetInfo = cnn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
    val ret = wifiNetInfo != null && wifiNetInfo.isConnected
            || mobileNetInfo != null && mobileNetInfo.isConnected
    Log.d(TAG, "isConnectedToNet: $ret  waste time:" + (SystemClock.elapsedRealtime() - tm))
    return ret
}

/**
 * 将px值转换为dip或dp值，保证尺寸大小不变

 * @param pxValue
 * *
 * @param scale
 * *            （DisplayMetrics类中属性density）
 * *
 * @return
 */
fun Context.px2dp(pxValue: Float) = (pxValue / density + 0.5f).toInt()

/**
 * 将dip或dp值转换为px值，保证尺寸大小不变

 * @param dipValue
 * *
 * @param scale
 * *            （DisplayMetrics类中属性density）
 * *
 * @return
 */
fun Context.dip2px(dipValue: Float) = (dipValue * density + 0.5f).toInt()

/**
 * 将px值转换为sp值，保证文字大小不变

 * @param pxValue
 * *
 * @param fontScale
 * *            （DisplayMetrics类中属性scaledDensity）
 * *
 * @return
 */
fun Context.px2sp(pxValue: Float) = (pxValue / scaledDensity + 0.5f).toInt()

/**
 * 将sp值转换为px值，保证文字大小不变

 * @param spValue
 * *
 * @param fontScale
 * *            （DisplayMetrics类中属性scaledDensity）
 * *
 * @return
 */
fun Context.sp2px(spValue: Float) = (spValue * scaledDensity + 0.5f)//.toInt()

val Context.scaledDensity: Float get() = resources.displayMetrics.scaledDensity
val Context.density: Float get() = resources.displayMetrics.density

/**
 * 删除照片
 */
fun Context.deleteImage(imgPath: String?): Boolean {
    if (imgPath.isNullOrEmpty()) {
        return false
    }
    val resolver = getContentResolver()
    val cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf<String>(MediaStore.Images.Media._ID), MediaStore.Images.Media.DATA + "=?",
            arrayOf(imgPath), null)
    var result = false
    val file = File(imgPath)
    if (cursor.moveToFirst()) {
        val id = cursor.getLong(0)
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val uri = ContentUris.withAppendedId(contentUri, id)
        val count = getContentResolver().delete(uri, null, null)
        result = count == 1
    } else {
        result = file.delete()
    }
    if (file.exists()) {
        file.delete()
    }
    Log.i(TAG, "deleteImage $imgPath")
    return !file.exists()
}

//删除文件夹和文件夹里面的文件
fun deleteDirFile(path: String, exclude: String = "")/*: Boolean */ {
    val dir = File(path)
    if (dir == null || !dir.exists() || !dir.isDirectory)
        return

    for (file in dir.listFiles()) {
        if (file.isFile && !file.absolutePath.equals(exclude))
            file.delete() // 删除其他文件
        else if (file.isDirectory)
            deleteDirFile(file.path, exclude) // 递规的方式删除文件夹
    }
    //return dir.delete()// 删除目录本身
}

val TAG: String = "Utils"

val symbolRegex = "[\\p{Punct}\\p{Space}]+".toRegex()
val symbolDataRegex = "[\\p{Punct}\\p{Space}\\d]+".toRegex()
fun isAlphbet(c: Char) = c in 'a'..'z' || c in 'A'..'Z'

fun getProperFlowlayoutWidth(pain: TextPaint, list: List<String>, maxWidth: Int, marginPadding: Int): Int {
    val cnt = getFlowlayoutContent(pain, list, maxWidth, marginPadding).size
    var i = 1
    val step = 20
    while (getFlowlayoutContent(pain, list, maxWidth - step * i, marginPadding).size <= cnt) {
        i++
    }
    return maxWidth - step * (i - 1)
}

fun getFlowlayoutContent(pain: TextPaint, list: List<String>, maxWidth: Int, marginPadding: Int): ArrayList<List<String>> {
    var tm = SystemClock.elapsedRealtime()
    val width = maxWidth

    /**
     * 存储所有的View，按行记录
     */
    val mAllViews = ArrayList<List<String>>()
    /**
     * 记录每一行的最大宽度
     */
    val mLineWidth = ArrayList<Float>()
    var lineWidth = 0f
    // 存储每一行所有的childView
    var lineViews: MutableList<String> = ArrayList<String>()
    val cCount = list.size
    // 遍历所有的孩子
    for (i in 0..cCount - 1) {
        val child = list[i]
        val childWidth = marginPadding + pain.measureText(child)

        // 如果已经需要换行
        if (childWidth + lineWidth > width) {
            //记录下这一行所有View的宽度之和
            mLineWidth.add(lineWidth)
            // 将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
            mAllViews.add(lineViews)
            lineWidth = 0f// 重置行宽
            lineViews = ArrayList<String>()
        }
        /**
         * 如果不需要换行，则累加
         */
        lineWidth += childWidth
        lineViews.add(child)
    }
    // 记录最后一行
    mLineWidth.add(lineWidth)
    mAllViews.add(lineViews)
    mAllViews.filterNotNull()
    val diff = SystemClock.elapsedRealtime() - tm
    if (diff > 10) {
        Log.i(TAG, "getFlowlayoutContent waste time:${diff}ms")
    }
    return mAllViews
}


val testStr = """所谓龙生九子，都不像龙的说法，是这样的——
    大儿是叫囚牛：它平身喜爱音乐，故常立在琴头上。如汉族的胡琴，白族的三弦琴等。
    而蒙古的马头琴也可能是囚牛的变种。
    二儿子是睚毗：它平身爱杀所以多被安在兵器上，用以威摄敌军。同时又用在仪仗上，
    以显得更加威严。
    三儿是嘲风：是只兽形龙，样子有点像狗，它善于了望，故多安在殿角上》裾说可以威
    摄妖魔、消灭灾祸。
    第四儿是蒲牢：喜欢吼叫，人们就把它安在钟上，大多是蒲牢的形象。据说它是住在海
    滨的，但却十分怕鲸鱼，一但鲸鱼发起攻击，它就会吓得乱叫。故人们把木杵造成鲸的
    形状，以令铜钟格外响亮。
    第五儿是狻猊：形似狮子。是外来品，随佛教传入中国的，所以性格有点像佛。它好安
    静、又爱烟火。所以往往把它安在佛位上或香炉上，让它为佛门护法。
    第六儿是霸下：又名焱屌，样子似龟。相传上古时它常背起三山五岳来兴风作浪。后被
    夏禹收服，为夏禹立下不少汗马功劳.治水成攻后，夏禹就把它的功绩，让它自己背起。
    故中国的石碑多由它背起的。
    第七儿是狴犴：又名宪章，样子像虎。相传它主持正义，而且能明是非，因此它被安在
    狱门上下、门大堂两则、以及官员出巡时肃静回避的牌上端，以维护公堂的肃然之气。
    第八儿是负屄.•因它喜爱文学，故多安在石碑的两则。
    么子是螭吻：又名鸱尾，鱼形的龙。相传是大约在南北朝时，由印度‘摩竭鱼’随佛教
    传入的。它是佛经中，雨神座下之物，能够灭火。故此，螭吻由此变化出来，所以它多
    安在屋脊两头，作消灾灭火的功效。中国民间，一直流传着龙生九子不成龙的说法，也
    就是说龙的九种子嗣都不是龙，而是九种不同的动物。这个传说有很多个版本，各不统
    一，不过基本上都是九种动物排名的差异，对于九种动物本身基本都是一样的。
    李东阳《怀麓堂集》中记载：“龙生九子不成龙，各有所好。囚牛，平生好音乐，
    今胡琴头上刻兽是其遗像；睚眦（音：牙字），平生好杀，今刀柄上龙吞口是其遗像；
    嘲凤，平生好险，今殿角走兽是其遗像；蒲牢，平生好鸣，今钟上兽钮是其遗像；狻猊
    (音：酸尼），平生好坐，今佛座狮子是其遗像；霸下，乎生好负重，今碑座兽是其遗
    像；狴犴（音：毕案），平生好讼，今狱门上狮子头是其遗像；焱M (音：毕戏），平生
    好文，今碑两旁文龙是其遗像；鸱吻，平生好吞，今殿脊兽头是其遗像。
    第七儿是狴犴：又名宪章，样子像虎。相传它主持正义，而且能明是非，因此它被安在
    狱门上下、门大堂两则、以及官员出巡时肃静回避的牌上端，以维护公堂的肃然之气。
    第八儿是负屄.•因它喜爱文学，故多安在石碑的两则。
    么子是螭吻：又名鸱尾，鱼形的龙。相传是大约在南北朝时，由印度‘摩竭鱼’随佛教
    传入的。它是佛经中，雨神座下之物，能够灭火。故此，螭吻由此变化出来，所以它多
    安在屋脊两头，作消灾灭火的功效。中国民间，一直流传着龙生九子不成龙的说法，也
    就是说龙的九种子嗣都不是龙，而是九种不同的动物。这个传说有很多个版本，各不统
    一，不过基本上都是九种动物排名的差异，对于九种动物本身基本都是一样的。
    李东阳《怀麓堂集》中记载：“龙生九子不成龙，各有所好。囚牛，平生好音乐，
    今胡琴头上刻兽是其遗像；睚眦（音：牙字），平生好杀，今刀柄上龙吞口是其遗像；
    嘲凤，平生好险，今殿角走兽是其遗像；蒲牢，平生好鸣，今钟上兽钮是其遗像；狻猊
    (音：酸尼），平生好坐，今佛座狮子是其遗像；霸下，乎生好负重，今碑座兽是其遗
    像；狴犴（音：毕案），平生好讼，今狱门上狮子头是其遗像；焱M (音：毕戏），平生
    好文，今碑两旁文龙是其遗像；鸱吻，平生好吞，今殿脊兽头是其遗像。"""

fun getTestStr(cnt: Int): String {
    val tm = System.currentTimeMillis()
    val i = testStr.length - cnt
    val start = 0//if (i > 0) Random().nextInt(i - 1) else 0
    val range = start..minOf(start + cnt, testStr.length)
    val substring = testStr.substring(range)
    print(TAG + "getTestStr cnt:$cnt waste time:${System.currentTimeMillis() - tm} ")
    return substring
}
