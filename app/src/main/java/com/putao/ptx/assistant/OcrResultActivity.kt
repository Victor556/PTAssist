package com.putao.ptx.assistant

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.SizeF
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.putao.ptx.assistant.app.AssistApplication
import com.putao.ptx.assistant.haicientity.HaiciBean
import com.putao.ptx.assistant.haicientity.ISimplePreview
import com.putao.ptx.assistant.utils.*
import com.putao.ptx.assistant.utils.Constance.KEY_IMG
import com.putao.ptx.assistant.utils.Constance.OCR_PATH
import com.sinovoice.hcicloud.analyze.Ocr
import com.sinovoice.hcicloud.model.OcrResult
import kotlinx.android.synthetic.main.activity_ocr_result.*
import kotlin.concurrent.thread


class OcrResultActivity : AppCompatActivity() {
//    lateinit var ivPreview: ImageView
//    lateinit var llChar: LinearLayout
//    lateinit var llWord: LinearLayout

    val TIME_SCAN = 1500
    val TAG = "OcrResultActivity"

    private val pathRes: String
        get() {
            return intent.getStringExtra(KEY_IMG) ?: OCR_PATH
        }
    private val pathSelect: String
        get() {
            return OCR_PATH//intent.getStringExtra(KEY_SELECT) ?: pathRes
        }


    var charWordSelect: TextView? = null
    var wordSelect: TextView? = null
    var charList: ArrayList<String> = ArrayList()
    var wordList: ArrayList<String> = ArrayList()


    private var mAgain: Dialog? = null
    private var mExit: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr_result)

        initView()
        ivPreview.postDelayed({ initPreviewView() }, 100)
        startOcr()
    }

    private fun initView() {
        var path = pathRes
        val tm = SystemClock.elapsedRealtime()
        var decodeBitmap = BitmapFactory.decodeFile(path)
        decodeBitmap = BitmapUtils.scaleBitmap(decodeBitmap, 0.5f)
        BitmapUtils.saveBitmap(decodeBitmap, pathSelect)
        background.background = BitmapDrawable(resources, decodeBitmap)
        background.foreground = ColorDrawable(Color.argb(112, 0, 0, 0))

        val diff = SystemClock.elapsedRealtime() - tm
        Log.d(TAG, "  save bitmap waste time :$diff")
        ivPreview.setImageBitmap(decodeBitmap)
        //ivPreview.alpha = 0.5f
        var pt = PointF(0f, 0f)
        ivPreview.setOnTouchListener { _, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                    pt.x = e.x
                    pt.y = e.y
                    ivPreview.updateTouchPoint(pt, 30f)
                    //       if (e.action == MotionEvent.ACTION_UP) {
                    updateTouchPoint(pt, 30f)
                    //     }
                    Log.d(TAG, "setOnTouchListener:  action:${e.action}")
                    if (e.action == MotionEvent.ACTION_UP) {
                        val bEmpty = llChar.childCount == 0 && llWord.childCount == 0
                        if (ivPreview.unitMost.size > 0 && charWordSelect != null && !bEmpty) {
                            val text: String = charWordSelect?.text.toString()
                            //showUpdatePreview(text)
                        } else {
                            closePreview()
                        }
                    }
                }
            }
            return@setOnTouchListener true
        }
        vExit.setOnClickListener {
            onBackPressed()
        }
        tvScan.visibility = View.INVISIBLE
        mScan.visibility = View.INVISIBLE
        vAgain.setOnClickListener { startOcr() }
    }

    private lateinit var mViewSimplePreview: View
    private lateinit var tvGoToDetail: TextView
    private lateinit var tvWordTitle: TextView
    private lateinit var tvExplain: TextView
    private lateinit var tvClosePreview: View


    private fun initPreviewView() {
        mViewSimplePreview = LayoutInflater.from(this).inflate(R.layout.word_preview, null)
        var rl = RelativeLayout.LayoutParams(/*ivPreview.realImgShowWidth*/1600, 600)
        rl.addRule(RelativeLayout.CENTER_HORIZONTAL)
        rlRoot.addView(mViewSimplePreview, rl)
        mViewSimplePreview.translationZ = 2f
        tvGoToDetail = mViewSimplePreview.findViewById(R.id.tvGoToDetail) as TextView
        tvGoToDetail.setOnClickListener {
            //TODO
            val intent = Intent();
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            intent.setClassName("com.putao.ptx.ipspirits", "com.putao.ptx.ipspirits.activities.MainActivity");

            val text = tvWordTitle.text.toString()
            var map = HashMap<String, String>().also {
                it.put("title", text.substring(0..text.indexOfAny(charArrayOf(' ', '　'))).trim())
            }
            intent.putExtra("pai_widget_data", map);
            intent.putExtra("pai_widget_type",
                    if (text.all { isAlphbet(it) }) 5/*英语*/ else 1/*字词*/);
            startActivity(intent)
        }
        tvWordTitle = mViewSimplePreview.findViewById(R.id.tvWord) as TextView
        tvExplain = mViewSimplePreview.findViewById(R.id.tvExplain) as TextView
        tvClosePreview = mViewSimplePreview.findViewById(R.id.vClose)
        tvClosePreview.setOnClickListener { closePreview() }
        mViewSimplePreview.invalidate()
        //closePreview()
        mViewSimplePreview.visibility = View.INVISIBLE
        Log.d(TAG, "mViewSimplePreview width:${mViewSimplePreview.width}" +
                " height:${mViewSimplePreview.height} visibility:${mViewSimplePreview.visibility}")

    }

    private fun closePreview() {
        closePreviewInstant()
//        //mViewSimplePreview.visibility = View.INVISIBLE
//        val animate = mViewSimplePreview.animate()
//        with(animate) {
//            cancel()
//            duration = 300
//            alpha(0f)
//            start()
//        }
    }


    private fun closePreviewInstant() {
        mViewSimplePreview.animate().cancel()
        mViewSimplePreview.alpha = 0f
        mViewSimplePreview.visibility = View.INVISIBLE
    }

    private fun showPreviewInstant() {
        mViewSimplePreview.animate().cancel()
        mViewSimplePreview.alpha = 1f
        mViewSimplePreview.visibility = View.VISIBLE
    }

    private fun showPreview() {
        showPreviewInstant()
//        if (mViewSimplePreview.alpha > 0.5f && mViewSimplePreview.visibility == View.VISIBLE) {
//            return
//        }
//        mViewSimplePreview.alpha = 0.01f
//        mViewSimplePreview.visibility = View.VISIBLE
//        val animate = mViewSimplePreview.animate()
//        with(animate) {
//            cancel()
//            duration = 300
//            alpha(1f)
//            start()
//        }
    }

    private fun startOcr() {
        vAgain.isEnabled = false
        mScan.post { startScan() }
        thread {
            val tm = SystemClock.elapsedRealtime()
            var ret: OcrResult? = null
            val wasteTimeRecogChineseText: Long
            var resultStr = ""
            try {
                ret = Ocr().recogChineseText(pathSelect)
                wasteTimeRecogChineseText = SystemClock.elapsedRealtime() - tm
                resultStr = ret.result()
                Log.d(TAG, "onCreate: waste Time RecogChineseText ${wasteTimeRecogChineseText} error code: ${ret.error}")
            } catch(e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "error $e ")
                runOnUiThread {
                    tryShowAgainDlg()
                }
            }

            //val elapsedRealtime = SystemClock.elapsedRealtime()
            //val segmentation = IndexAnalysis.parse(resultStr)
            //var listLocal = Array<String>(segmentation.terms.size) { segmentation.terms[it].realName }
            //var segmentResultLocal = TextUtils.join(" ", listLocal)
            //Log.d(TAG, "onCreate: segment Local waste Time:${SystemClock.elapsedRealtime() - elapsedRealtime} segmentResultLocal： ${segmentResultLocal} ")

            if (ret?.error != 0) {
                runOnUiThread { tryShowAgainDlg() }
            }

            val tm2 = SystemClock.elapsedRealtime()
            //http://10.1.222.16:3032/repeat-query?query= 我爱中国呵呵复旦大学 ？
            //var segmentStr = WebUtils.doGet("http://10.1.222.16:3032/repeat-query", mapOf(Pair("query", ret.result())), "UTF-8")
            var breakList = breakStringForGet(resultStr)
            var bean = SegmentAnsj("", SegmentationBean(ArrayList<TermsBean>()))
            val gson = Gson()
            breakList.fold(null as? Thread) {
                thread, str ->
                return@fold thread {
                    try {
                        val fromJson = gson.fromJson(doSegmentGet(str), SegmentAnsj::class.java)
                        thread?.join()
                        bean.append(fromJson)
                    } catch(e: Exception) {
                        e.printStackTrace()
                        Log.e(TAG, "$e")
                    }
                }
            }?.join()
            ivPreview.setDebugData(bean)

            //var segmentStr = doSegmentGet(text1)

            try {
                var segmentResult = TextUtils.join(" ", Array<String>(bean.segmentation?.terms?.size ?: 10) { bean?.segmentation?.terms?.get(it)?.realName ?: "" })
                val elapsedRealtime = SystemClock.elapsedRealtime()
                Log.d(TAG, "onCreate:on Create inti thread waste time ${elapsedRealtime - tm}ms ocr length:${resultStr.length}" +
                        " multithread waste time:${elapsedRealtime - tm2}ms segmentation： ${segmentResult.trim()} ")
            } catch(e: Exception) {
                Log.e(TAG, "e: $e")
            }

            if (ret?.error != 0) {//识别失败
                //TODO
            }
            runOnUiThread { stopScan() }
        }
    }

    private fun updateTouchPoint(pt: PointF, fl: Float) {
        try {
            var maybes = ArrayList(ivPreview.unitMaybe)
            var mosts = ArrayList(ivPreview.unitMost)
            maybes.addAll(mosts)
            maybes.sortBy { it.range.first }
            llChar.removeAllViews()
            llWord.removeAllViews()
            charList.clear()
            wordList.clear()
            var select = ivPreview.unitMost.maxBy { it.range.last - it.range.first }
            runOnUiThread {
                val f: (TextView) -> Unit = {
                    if (it != charWordSelect) {
                        charWordSelect?.background = getDrawableNormal()
                        charWordSelect = it
                        it.background = getDrawableSelected()
                    }
                    showUpdatePreview(it.text.toString(), true)
                }
                maybes.forEach {
                    val format = format(it.str)
                    val len = format.length
                    var ss = it
                    format.forEach {
                        val s = it.toString()
                        if (!charList.contains(s)) {
                            charList.add(s)
                            getTextView(s, f)?.also {
                                llChar.addView(it)
                                if (ss == select && s == ss.str) {
                                    it.background = getDrawableSelected()
                                    charWordSelect = it
                                    //showUpdatePreview(it.text.toString())
                                }
                            }
                        }
                    }
                    if (len == 1) {
                    } else if (len > 1) {
                        if (!wordList.contains(format)) {
                            wordList.add(format)
                            getTextView(format, f)?.also {
                                if (it.text.trim().length > 1) {
                                    llWord.addView(it)
                                    if (ss == select && format == ss.str) {
                                        it.background = getDrawableSelected()
                                        charWordSelect = it
                                        //showUpdatePreview(it.text.toString())
                                    }
                                }
                            }
                        }
                    }
                }


                Log.d(TAG, "onCreate  llWord child size:${llWord.childCount} llChar child size:${llChar.childCount}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "run: " + e)
        }
    }

    private fun showUpdatePreview(text: String, showNotice: Boolean = false) {
        var m = mMapReqData[text]
        if (m == null) {
            Log.d(TAG, "showUpdatePreview  mMapReqData[text]=${mMapReqData[text]}")
            if (mReqError.contains(text) && showNotice) {
                Toast.makeText(this@OcrResultActivity, "没有查找到“${text}”相关信息！",
                        Toast.LENGTH_SHORT).show()
            }
            closePreview()
            return
        } else {
            val ci = m?.data?.ci
            val zi = m?.data?.zi
            val ec = m?.data?.ec
            val ce = m?.data?.ce

            val f: (ISimplePreview) -> Unit = {
                tvWordTitle.text = it.titleStr()
                tvExplain.text = it.explainStr()
            }

            if (ce != null) {
                f(ce)
            } else if (ci != null) {
                f(ci)
            } else if (zi != null) {
                f(zi)
            } else if (ec != null) {
                f(ec)
            }

        }
        mViewSimplePreview.visibility = View.VISIBLE
        showPreview()
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getDrawableSelected(): Drawable? {
        return resources.getDrawable(R.drawable.handle_pressed)
    }

    private fun getDrawableNormal(): Drawable? {
        return resources.getDrawable(R.drawable.selector_char_word)
    }

    var colors = intArrayOf(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.BLUE)

    private fun getTextView(cc: String, f: (TextView) -> Unit): TextView? {
        Log.d(TAG, "getTextView: $cc")
        var c = format(cc)
        if (c.isNullOrBlank() || c.length == 1 && isAlphbet(c[0])) {
            return null
        }
        AssistApplication.sAppThreadPool.execute {
            try {
                updateHaiciData(c)
            } catch(e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "${e}")
            }
        }
        var p = LayoutInflater.from(this).inflate(R.layout.haici_item, null).findViewById(R.id.tv) as TextView;
        (p.parent as? ViewGroup)?.removeView(p)
        p.text = c
        p.setOnClickListener {

            try {
//                val text = p.text
//                var intent = Intent(this@OcrResultActivity, DictDetailActivity::class.java)
//                intent.putExtra(KEY_DETAIL, text)
//                intent.putExtra(KEY_DETAIL_REQ, getRequest(text.toString()))
//                if (mMapReqData[text] == null || mMapReqData[text]?.httpStatusCode == 201) {
//                    Toast.makeText(this@OcrResultActivity, "查询失败！", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//                startActivity(intent)
                f(p)
            } catch(e: Exception) {
                Log.e(TAG, "getTextView setOnClickListener:$e")
            }
        }
        //var r = Random()
        //p.setBackgroundColor(colors[r.nextInt(colors.size)])

        return p
    }


    private val mReqError: MutableSet<String> = HashSet()
    private val mMapReq: MutableMap<String, String> = HashMap()
    private val mMapReqData: MutableMap<String, HaiciBean> = HashMap()

    private fun getHaiciRequest(str: String): String? {
        val s = mMapReq[str] ?: WebUtils.doPost(str)
                ?.also {
                    mMapReq.put(str, it)
                    getHaiciBean(it)?.let { mMapReqData.put(str, it) }
                }
        mReqError.add(str)
        return s
    }

    private fun updateHaiciData(str: String): HaiciBean? {
        if (mMapReqData[str] == null) {
            getHaiciRequest(str)
        }
        return mMapReqData[str]
    }

    fun getHaiciBean(translateRst: String): HaiciBean? {
        var haiCiReturn: HaiciBean? = null
        try {
            val gson = Gson()
            Log.d(TAG, "json:$translateRst")
            haiCiReturn = gson.fromJson(translateRst, HaiciBean::class.java)
            val beanSrc = gson.toJson(haiCiReturn)
            val regex = "\\}".toRegex()
            val src = translateRst.replace(regex, "}\n")
            val bean = beanSrc.replace(regex, "}\n")
            //Log.i(TAG, "getHaiciBean: translateRst\n$src\n\n  haiCiBean:\n$bean")
        } catch(e: Exception) {
            Log.e(TAG, "getHaiciBean:$e $translateRst")
        }
        return haiCiReturn
    }

    fun format(str: String): String {
        return str.replace(symbolDataRegex, ""); // abcd35
        //return str.replace("[\\pP]".toRegex(), "")
    }




    internal var mAni: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)

    private fun stopScan() {
        //mScan.setVisibility(View.INVISIBLE);
        //tvScan.setVisibility(View.INVISIBLE);
        vAgain.isEnabled = true
        mScan.animate().alpha(0f).setDuration(500).start()
        tvScan.animate().alpha(0f).setDuration(400).start()
        mAni?.cancel()
    }

    private fun startScan() {
        mScan.setAlpha(0f)
        mScan.translationZ = 2f
        mScan.setVisibility(View.VISIBLE)
        mScan.animate().alpha(0.9f).setDuration(800).start()
        val size = updateImgDisplaySize(ivPreview)
        tvScan.layoutParams.width = size.width.toInt()
        mScan.getLayoutParams().width = size.width.toInt() - 20
        tvScan.alpha = 0f
        tvScan.visibility = View.VISIBLE
        tvScan.translationZ = 2f
        tvScan.animate().alpha(0.9f).setDuration(800).start()
        val height = (mScan.parent as View).height
        val offset = 20f + (ivPreview.height - size.height) / 2
        mAni.setFloatValues(offset, height - offset - mScan.height)
        mAni.repeatMode = ValueAnimator.REVERSE
        mAni.duration = TIME_SCAN.toLong()
        mAni.repeatCount = ValueAnimator.INFINITE
        mAni.removeAllUpdateListeners()
        mAni.addUpdateListener {
            mScan.y = it.animatedValue as Float
        }
        mAni.start()
    }

    fun isOCrScanning() = mAni.isRunning

    private fun updateImgDisplaySize(iv: ImageView): SizeF {
        var realImgShowWidth = iv.width.toFloat()
        var realImgShowHeight = iv.width.toFloat()
        val imgDrawable = iv.drawable
        if (imgDrawable != null) {
            //获得ImageView中Image的真实宽高，
            val dw = imgDrawable.bounds.width()
            val dh = imgDrawable.bounds.height()

            //获得ImageView中Image的变换矩阵
            val m = iv.imageMatrix
            val values = FloatArray(10)
            m.getValues(values)

            //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
            val sx = values[0]
            val sy = values[4]

            //计算Image在屏幕上实际绘制的宽高
            realImgShowWidth = dw * sx
            realImgShowHeight = dh * sy
        }
        return SizeF(realImgShowWidth, realImgShowHeight)
    }

    private fun tryShowAgainDlg() {
        if (mAgain == null) {
            val view = LayoutInflater.from(this@OcrResultActivity).inflate(R.layout.dlg_again, null)
            val btnTry = view.findViewById(R.id.btnTry)
            val btnClose = view.findViewById(R.id.btnClose)
            btnTry.setOnClickListener {
                tryCloseExitDlg()
                startOcr()
            }
            btnClose.setOnClickListener {
                finish()
            }
            mAgain = ExitDialog(this@OcrResultActivity,
                    resources.getDimension(R.dimen.width_exit_dlg).toInt(),
                    resources.getDimension(R.dimen.height_exit_dlg).toInt(),
                    view, R.style.pano_dialog)
        }
        if (!(mAgain?.isShowing() ?: false)) {
            try {
                mAgain?.show()
            } catch(e: Exception) {
                e.printStackTrace()
                Log.e(TAG,"tryShowAgainDlg $e")
            }
        }
    }

    private fun tryCloseExitDlg(): Boolean {
        var ret = false
        if (mAgain?.isShowing() ?: false) {
            mAgain?.cancel()
            mAgain = null
            ret = true
        }
        if (mExit?.isShowing() ?: false) {
            mExit?.cancel()
            mExit = null
            ret = true
        }
        return ret
    }


    private fun tryShowExitDlg() {
        if (mAgain == null) {
            val view = LayoutInflater.from(this@OcrResultActivity).inflate(R.layout.dlg_exit, null)
            val btnExit = view.findViewById(R.id.btnExit)
            val btnCancel = view.findViewById(R.id.btnCancel)
            btnExit.setOnClickListener {
                finish()
            }
            btnCancel.setOnClickListener {
                tryCloseExitDlg()
            }
            mAgain = ExitDialog(this@OcrResultActivity,
                    resources.getDimension(R.dimen.width_exit_dlg).toInt(),
                    resources.getDimension(R.dimen.height_exit_dlg).toInt(),
                    view, R.style.pano_dialog)
        }
        if (!(mAgain?.isShowing() ?: false)) {
            mAgain?.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tryCloseExitDlg()
    }

    override fun onBackPressed() {
        if (isOCrScanning()) {
            super.onBackPressed()
        } else {
            tryShowExitDlg()
        }
    }

}