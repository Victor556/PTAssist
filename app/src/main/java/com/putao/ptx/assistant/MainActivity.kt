package com.putao.ptx.assistant

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.gson.Gson
import com.putao.ptx.assistant.app.AssistApplication
import com.putao.ptx.assistant.haicientity.HaiciBean
import com.putao.ptx.assistant.utils.*
import com.putao.ptx.assistant.utils.Constance.IS_MANUAL
import com.putao.ptx.assistant.utils.Constance.KEY_IMG
import com.putao.ptx.assistant.views.OcrImageView
import com.putao.ptx.assistant.views.SharpView
import com.sinovoice.hcicloud.analyze.Ocr
import com.sinovoice.hcicloud.model.OcrResult
import kotlinx.android.synthetic.main.activity_main_kt.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.concurrent.thread


class MainActivity : BaseActivity() {

    private val PARTIAL_PATH = Constance.OCR_PATH

    val TAG = "BaseActivity"

    lateinit var mDisplay: OcrImageView

    //var ivPreview: ImageView? = null

    lateinit var mScan: View


    var fullOcr = false

    //var tvScan: View? = null
    val mSimplePreview: ViewSimplePreview by lazy {
        val ret = ViewSimplePreview(rlRoot, llRst.translationZ + 0.0001f, this::getPreviewLayoutPara)
        ret.tranz = frameLayout.translationZ + 0.00001f
        ret
    }

    val mMsgDlg: MsgDialog by lazy {
        MsgDialog(this@MainActivity,
                mCancelListener = DialogInterface.OnCancelListener { ivDisplay.selectedRect = null })
    }

    val ignoreStr by lazy {
        ArrayList<String>().also {
            it.addAll(resources.getStringArray(R.array.ignore))
        }
    }

    private val mScanDlg: ExitDialog by lazy {
        val tv = TextView(this@MainActivity)
        with(tv) {
            setBackgroundResource(R.drawable.shape_pic_dlg)
            text = "正在识别..."
            textSize = /*sp2px*/(18f)
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
        }
        val ret = ExitDialog(this@MainActivity,
                resources.getDimension(R.dimen.scan_dlg_width).toInt(),
                resources.getDimension(R.dimen.scan_dlg_height).toInt(),
                tv, R.style.pano_dialog
        )
        ret
    }

    //var ocrRst: EditText? = null

    //var translate: View? = null

    internal val mAni: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)

    var initFrameLayoutParam: RelativeLayout.LayoutParams? = null

    private var mDeletePic: Boolean = false
    /**
     * 是否手动选择
     */
    private var mSelectManual: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_kt)

        setFullActivity()

        initSelectPath(savedInstanceState)

        initViewKt()
        initView()

        mDisplay.setOnClickListener {
            //       ocrRecg();
        }
        Log.d(TAG, "onCreate ${this@MainActivity} savedInstanceState:$savedInstanceState")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        afterEnterAct()
        Log.d(TAG, "onNewIntent")
    }

    private fun afterEnterAct() {
        initSelectPath(null)
        val params = initFrameLayoutParam
        if (params != null) {
            val p = frameLayout.layoutParams as RelativeLayout.LayoutParams
            p.width = params.width
            p.height = params.height
            p.topMargin = params.topMargin
        }

        updateImageViewBitmap()
        mDisplay.postDelayed({
            updateImageViewBitmap()
            updateFramelayoutParam()
        }, 50)
        //mDisplay.postDelayed({ mDisplay.updateImgDisplaySize() }, 100/*不能太小*/)
        mDisplay.postDelayed({
            updateImageViewBitmap()
            updateFramelayoutParam()
        }, 150)
        //mDisplay.postDelayed({ updateImageViewBitmap() }, 10/*不能太小*/)
        //mDisplay.postDelayed({ mDisplay.updateImgDisplaySize() }, 200/*不能太小*/)

        mMapReq.clear()
        mMapReqData.clear()

        onClickRet()
        tryCloseExitDlg()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        initSelectPath(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState")
    }

    private fun initSelectPath(savedInstanceState: Bundle?) {
        initActParam(savedInstanceState)

        if (mSelectedPath.isNullOrEmpty()) {
            val func: (Long) -> String? = { Thread.sleep(it/*500*/);getLastScreenPic(5000) }
            mSelectedPath = getLastScreenPic(4000) ?: {
                var tmp: String? = null
                val tm = SystemClock.elapsedRealtime()
                while (SystemClock.elapsedRealtime() - tm < 3000 && tmp == null) tmp = func(300)
                tmp
            }.invoke() ?: { Thread.sleep(300);getLastScreenPic(50000000) }.invoke() ?: ""
        }
        mDeletePic = !mSelectedPath.isNullOrBlank()
        if (!mSelectedPath.isNullOrBlank()) {
            clearExtraFiles()
        }

        Log.d(TAG, "initSelectPath mSelectedPath:$mSelectedPath")
    }

    private fun initActParam(bundle: Bundle? = null) {
        if (bundle != null) {
            mSelectedPath = bundle.getString(KEY_IMG, mSelectedPath) ?: mSelectedPath
            mSelectManual = bundle.getBoolean(IS_MANUAL, mSelectManual)
        } else {
            mSelectedPath = intent.getStringExtra(KEY_IMG) ?: mSelectedPath
            mSelectManual = intent.getBooleanExtra(IS_MANUAL, false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_IMG, mSelectedPath)
        outState.putBoolean(IS_MANUAL, mSelectManual)
        mDeletePic = false
        Log.d(TAG, "onSaveInstanceState mDeletePic:$mDeletePic mSelectedPath:$mSelectedPath ")
    }


//    override fun onConfigurationChanged(newConfig: Configuration) {
//        val tm = SystemClock.elapsedRealtime()
//        super.onConfigurationChanged(newConfig)
//        ivDisplay.updateImgDisplaySize()
//
//        Log.d(TAG, "onConfigurationChanged: waste Time:" + (SystemClock.elapsedRealtime() - tm))
//    }

    private fun getLastScreenPic(diff: Long = 4000L): String? {
        val files = File("/sdcard/Pictures/Pai/").listFiles()
        if (files == null || files.isEmpty()) {
            return null
        }
        val tm = System.currentTimeMillis()
        var tmDiff = -1000L
        val file = files.minBy { Math.abs(tm - it.lastModified()) }?.takeIf {
            tmDiff = tm - it.lastModified()
            tmDiff in 1200..diff//必须大于一定值
        }
        val absolutePath = file?.absolutePath
        Log.i(TAG, "getLastScreenPic absolutePath:${absolutePath} timeDiff:$tmDiff")
        return absolutePath
    }

    private fun setFullActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = window
            // 透明状态栏
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            // 透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun initViewKt() {
        mDisplay = ivDisplay
        //ivPreview=ivPreview
        mScan = scan
        //tvScan=tvScan
    }

    val MIN_WIDTH = 150F
    val MIN_HEIGHT = 100F

    private val lvAdapter: AdapterFlowView by lazy {
        AdapterFlowView(this@MainActivity, this::getTextView, {
            tv, select ->
            if (select) {
                tv.background = getDrawableSelected()
                tv.setTextColor(Color.WHITE)
                val pair = tv.tag as? Pair<Int, Int> ?: -1 to -1
                if (pair != lvAdapter.select) {
                    lvAdapter.select = pair
                }
            } else {
                tv.background = getDrawableNormal()
                tv.setTextColor(Color.BLACK)
            }
            tv.alpha = if (tv.text in mReqError) 0.5f else 1.0f
        }, { lv.layoutParams.width - getLvMarginPadding() to getFlowTextHeight() },
                {
                    //更新词条
                    it?.filter { mMapReqData[it] == null && mMapReq[it] == null }?.let {
                        if (it.isNotEmpty()) updateHaiciDataInThread(it, {
                            lv.removeCallbacks(mRunUpdate)
                            lv.postDelayed(mRunUpdate, 250)
                            Log.i(TAG, "update haici data! $it")
                        }
                        )
                    }
                })
    }

    private val mRunUpdate by lazy {
        Runnable {
            //谨防出现循环刷新
            Log.d(TAG, "notifyDataSetChanged")
            lvAdapter.notifyDataSetChanged()
        }
    }

    private fun updateHaiciDataInThread(it: List<String>, afterFinish: (() -> Unit)? = null) {
        AssistApplication.sAppThreadPool.execute {
            it.forEach {
                try {
                    if (!it.isNullOrBlank() && mMapReqData[it] == null) {
                        updateHaiciData(it)
                    }
                } catch(e: Exception) {
                    e.printStackTrace()
                    Log.e(TAG, "${e}")
                }
            }
            if (it.isNotEmpty()) afterFinish?.invoke()
        }
    }

    private fun initView() {
        //showSegmentWords(false)
        lv.adapter = lvAdapter
        lv.itemsCanFocus = true
        arrowUp.bUp = true
        arrowDown.bUp = false
        btnRet.setOnClickListener {
            onClickRet()
        }
        btnFull.setOnClickListener {
            ivDisplay.selectedRect = null
            btnCloseWord.performClick()
            fullOcr = true
            startOcr(mSelectedPath)
        }
        val func: (View, MotionEvent) -> Boolean = { v, event ->
            val view = v as TextView
            if (event.action == KeyEvent.ACTION_DOWN) {
                view.setTextColor(Color.WHITE)
                view.setBackgroundResource(R.drawable.shape_btn_bottom_press)
            } else if (event.action == MotionEvent.ACTION_UP
                    || event.action == MotionEvent.ACTION_OUTSIDE
                    || event.action == MotionEvent.ACTION_CANCEL) {
                view.setTextColor(Color.BLACK)
                view.setBackgroundResource(R.drawable.shape_btn_bottom_normal)
            }
            false
        }
        btnClose.setOnTouchListener(func)
        //btnFull.setOnTouchListener(func)
        btnClose.setOnClickListener { onBackPressed() }
        btnCloseWord.setOnClickListener {
            showSegmentWords(false)
            showReturnBtn(false)
        }

        initFrameLayoutParam = RelativeLayout.LayoutParams(frameLayout.layoutParams as? RelativeLayout.LayoutParams)
        updateImageViewBitmap()
        mDisplay.postDelayed({
            updateFramelayoutParam()
        }, 30)
        mDisplay.setOnTouchListener(object : View.OnTouchListener {
            var tmLastPointUp: Long = 0
            var bitmap: Bitmap? = null
            var select = RectF()
            val ptEnd = PointF()
            val ptSt = PointF()
            internal var pLast = PointF()
            private var bAllow: Boolean = true
            var firstTouch = true

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val action = event.action
                when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (firstTouch) {
                            firstTouch = false
                            //ivDisplay.updateImgDisplaySize()
                        }

                        if (isOcrScanning()) {
                            return false
                        }
                        if (!bAllow) {
                            bAllow = true
                        }

                        if (lvAdapter.count > 0) {
                            showSegmentWords(visible = false, removeChildren = true)
                            ivDisplay.selectedRect = null
                            bAllow = false
                            return false
                        }
                        ptSt.x = event.getX()
                        ptSt.y = event.getY()
                        ptEnd.set(ptSt)
                        updateSelectedRect()
                        bitmap = mDisplay.getSectorBitmap(select)
                        if (bitmap != null) {
                            ivPreview.setImageBitmap(bitmap)
                            mDisplay.setSelectedRect(select)
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if (!bAllow || isOcrScanning()) {
                            return false
                        }

                        val dx = event.x - pLast.x
                        val dy = event.y - pLast.y

                        //boolean changed = updateSelectedRect(tmp, event);
                        if (event.pointerCount == 1) {
                            ptSt.y = event.getY()//add move rect
                            ptEnd.x = event.getX()
                            ptEnd.y = ptSt.y//event.getY()
                        } else {
                            ptSt.offset(dx, dy)
                            ptEnd.offset(dx, dy)
                        }
                        updateSelectedRect()

                        bitmap = mDisplay.getSectorBitmap(select)
                        if (bitmap != null) {
                            ivPreview.setImageBitmap(bitmap)
                            mDisplay.selectedRect = select
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        if (!bAllow || isOcrScanning()) {
                            return false
                        }
                        if (SystemClock.elapsedRealtime() - tmLastPointUp > 800) {
                            //updateSelectedRect(select, event);
                        }
                        bitmap = mDisplay.getSectorBitmap(select)
                        if (bitmap != null) {
//                            select.left = maxOf(select.left, 0f)
//                            select.top = maxOf(select.top, 0f)
//                            select.right = minOf(select.right, mDisplay.height.toFloat())
//                            select.bottom = minOf(select.bottom, mDisplay.width.toFloat())
                            ivPreview.setImageBitmap(bitmap)
                            BitmapUtils.saveBitmap(bitmap, PARTIAL_PATH)
                            mDisplay.selectedRect = select
                        }
                        tmLastPointUp = SystemClock.elapsedRealtime()
                        if (!bitmap.isBitmapPure()) {
                            if (!isOcrScanning()) {
                                fullOcr = false
                                startOcr()
                            }
                        } else {
                            //mMsgDlg.show("选择区域颜色太纯净，请重新选择！", 1500L)
                            mMsgDlg.showToastNoWord()
                        }
                    }
                    MotionEvent.ACTION_POINTER_UP -> tmLastPointUp = SystemClock.elapsedRealtime()
                }
                pLast.x = event.x
                pLast.y = event.y
                return true/*false*/
            }

            private fun updateSelectedRect() {
                select.left = ptSt.x
                select.top = ptSt.y
                select.right = ptEnd.x
                select.bottom = ptEnd.y
                if (select.width() < 0f) {
                    val left = select.left
                    select.left = select.right
                    select.right = left
                }

                if (select.height() < 0f) {
                    val top = select.top
                    select.top = select.bottom
                    select.bottom = top
                }
                select.inset(-MIN_WIDTH / 2, -MIN_HEIGHT / 2)

                select.left = Math.max(select.left, 0f)
                select.top = Math.max(select.top, 0f)
                select.right = Math.min(select.right, ivDisplay.width.toFloat())
                select.bottom = Math.min(select.bottom, ivDisplay.height.toFloat())
            }
        })

        try {
            ivPreview.setImageBitmap(BitmapFactory.decodeFile(PARTIAL_PATH))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "initView: " + e)
        }
        if (!BuildConfig.DEBUG) {
            ivPreview.visibility = View.GONE
        }
    }

    private fun updateImageViewBitmap() {
        var decodeBitmap = BitmapFactory.decodeFile(mSelectedPath)
        decodeBitmap = BitmapUtils.scaleBitmap(decodeBitmap, 1.0f/*0.5f*/)
        //BitmapUtils.saveBitmap(decodeBitmap, pathSelect)
        background.background = BitmapDrawable(resources, decodeBitmap)
        background.foreground = ColorDrawable(Color.argb(160, 0, 0, 0))
        mDisplay.setImageBitmap(decodeBitmap/*BitmapFactory.decodeFile(mSelectedPath)*/)
    }

    private fun updateFramelayoutParam() {
        //mDisplay.updateImgDisplaySize()
        Log.d(TAG, "updateFramelayoutParam: updateImgDisplaySize 1${mDisplay.frameF}")
        val params = frameLayout.layoutParams as RelativeLayout.LayoutParams
        params.width = mDisplay.realImgShowWidth
        params.height = mDisplay.realImgShowHeight
        params.topMargin += (mDisplay.height - mDisplay.realImgShowHeight) / 2
        //params.bottomMargin = (mDisplay.height - mDisplay.realImgShowHeight) / 2
        frameLayout.invalidate()
        ivDisplay.invalidate()
        //mDisplay.postDelayed({ mDisplay.updateImgDisplaySize() }, 30/*不能太小*/)
    }

    /**
     * 在OnResume方法执行之后再调用
     */
    private fun onClickRet() {
        showSegmentWords(false, true)
        mSimplePreview.closePreview()
        showReturnBtn(false)
        stopScan()
        mOcrThread?.isCancel = true
        ivDisplay.selectedRect = null
    }

    private fun getPreviewLayoutPara(para: RelativeLayout.LayoutParams?): RelativeLayout.LayoutParams {
        val rl = para ?: RelativeLayout.LayoutParams(ivDisplay.width, previewHeight)
        rl.width = ivDisplay.width
        rl.height = previewHeight
        rl.addRule(RelativeLayout.CENTER_HORIZONTAL)
        rl.addRule(RelativeLayout.ALIGN_BOTTOM, frameLayout.id)
        rl.addRule(RelativeLayout.ALIGN_LEFT, frameLayout.id)
        rl.leftMargin = ivDisplay.frameF.left.toInt()
        rl.bottomMargin = (ivDisplay.height - ivDisplay.frameF.bottom).toInt()
        return rl
    }

    private val forground = ColorDrawable(Color.argb(0x44, 0, 0, 0))

    private fun showSegmentWords(visible: Boolean = true, removeChildren: Boolean = false) {
        llRst.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        //btnCloseWord.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        ivDisplay.foreground = if (visible) forground else null
        if (removeChildren || !visible) {
            lvAdapter.clearData()
        }
        if (!visible) {
            mSimplePreview.closePreview()
        }
        showReturnBtn(visible)
    }

    private val mAgain: Dialog by lazy {
        val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.dlg_exit, null)
        view.findViewById(R.id.btnExit).setOnClickListener {
            finish()
        }
        view.findViewById(R.id.btnCancel).setOnClickListener {
            tryCloseExitDlg()
        }
        ExitDialog(this@MainActivity,
                resources.getDimension(R.dimen.width_exit_dlg).toInt(),
                resources.getDimension(R.dimen.height_exit_dlg).toInt(),
                view, R.style.pano_dialog)
    }
    private var mOcrThread: OcrThread? = null

    private val TIME_SHOW = 1500L

    private fun startOcr(picPath: String = PARTIAL_PATH/*this@MainActivity.mSelectedPath*/) {
        if (!isConnectedToNet()) {
            mMsgDlg.showToastNoNet()
            return
        }

        if (TextUtils.isEmpty(picPath) || !File(picPath).exists()) {
            mMsgDlg.show("请选择有效的图片！", time = TIME_SHOW)
            return
        }

        mScan.post {
            startScan()
            showReturnBtn(true)
        }

        mOcrThread = object : OcrThread() {
            override fun run() {
                val tm = SystemClock.elapsedRealtime()
                val ret = if (!useTest) Ocr().recogChineseText(picPath/*PARTIAL_PATH*/) else {
                    OcrResult().also {
                        it.data?.forms?.form = if (picPath == PARTIAL_PATH) {
                            val r = ivDisplay.selectedRect
                            val area = r.width() * r.height()
                            if (area < MIN_WIDTH * MIN_HEIGHT * 1.1f) {
                                getTestStr(0)
                            } else {
                                getTestStr((area / (50 * 50f)).toInt())
                            }
                        } else testStr
                    }
                }
                val wasteTime = SystemClock.elapsedRealtime() - tm
                Log.d(TAG, "run: ocr waste Time:" + wasteTime)
                if (ret == null || isCancel) {
                    runOnUiThread {
                        //tryShowAgainDlg()
                        if (!this.isCancel) {
                            mMsgDlg.show("识别出错！", TIME_SHOW)
                            //Toast.makeText(this@MainActivity, "识别出错！", Toast.LENGTH_SHORT).show()
                        }
                        stopScan()
                        showReturnBtn(false)
                    }
                    return
                }
                if (ret.error != 0 || isCancel) {
                    runOnUiThread {
                        //tryShowAgainDlg()
                        if (!this.isCancel) {
                            mMsgDlg.showToastNoWord()
                            //Toast.makeText(this@MainActivity, "没有识别到文字！", Toast.LENGTH_SHORT).show()
                        }
                        stopScan()
                        showReturnBtn(false)
                    }
                    return
                }
                val resultStr = ret.result()
                val breakList = breakStringForGet(resultStr)
                val bean = SegmentAnsj("", SegmentationBean(ArrayList<TermsBean>()))
                val gson = Gson()
                @Suppress("CAST_NEVER_SUCCEEDS")
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
                val terms = bean.segmentation?.terms
                val filter = terms?.filter { it.realName in ignoreStr }
                terms?.removeAll(filter as Collection<TermsBean>)
                removeCharInWord(bean)
                val size = bean.segmentation?.terms?.size
                if (size == 0 || size == null || this.isCancel) {
                    runOnUiThread {
                        if (!this.isCancel) {
                            mMsgDlg.show("网络分词出错！", TIME_SHOW)
                            //Toast.makeText(this@MainActivity, "网络分词出错！", Toast.LENGTH_SHORT).show()
                        }
                        stopScan()
                        showReturnBtn(false)
                    }
                    return
                }

                ivDisplay.postDelayed(
                        {
                            showResultViews(bean)
                            stopScan()
                            showReturnBtn(true)
//                mLogView.visibility = View.VISIBLE
//                mLogView.text = resultStr
                            mDisplay.setOcrResult(ret)
                            Log.d(TAG, "run: initView:" + resultStr)
                        }, 0/*TIME_SCAN - wasteTime % TIME_SCAN*/)
            }
        }
        mOcrThread?.start()
    }

    val useTest = if (!BuildConfig.DEBUG) false else /*true*/ false
    private fun removeCharInWord(bean: SegmentAnsj) {
        val list = bean.segmentation?.terms
        //var natureStr: String? = ""
        val listRemove = HashSet<Int>()
        list?.forEachIndexed {
            index, termsBean ->
            val natureStr = termsBean.realName
            if (natureStr != null && natureStr.length > 1) {
                (0..(natureStr.length - 1)).mapTo(listRemove) { termsBean.offe + it }
            }
        }
        val buf = StringBuffer()
        list?.removeAll {
            val b = listRemove.contains(it.offe) && it.realName?.length == 1
            if (b) {
                buf.append(if (buf.isNotEmpty()) ", " else "" + it.realName)
            }
            b
        }
        Log.d(TAG, "removeCharInWord $listRemove remove:${buf}")
    }

    var charList: ArrayList<String> = ArrayList()

    private fun showResultViews(bean: SegmentAnsj) {

        var format: String
        charList.clear()
        showSegmentWords(false, true)
        val filterNotNull = bean.segmentation?.terms?.map {
            it.realName?.let { format(it) }
        }?.filterNot { it.isNullOrBlank() }

        thread {
            val lists = ArrayList<ArrayList<String>>()
            var tmp = ArrayList<String>()
            filterNotNull?.forEach {
                if (it != null && !it.isNullOrBlank() && mMapReqData[it] == null) {
                    if (tmp.size > 30) {
                        lists.add(tmp)
                        tmp = ArrayList<String>()
                    } else {
                        tmp.add(it)
                    }
                }
            }
            //lists.forEach { updateHaiciDataInThread(it) }
        }


        val cnt = 30
        val size = filterNotNull?.size ?: 0
        val tooMany = size > cnt
        Log.i(TAG, "showResultViews: segment size:${size} ")

        if (filterNotNull != null) {
            val list = ArrayList<String>()
            if (!tooMany) {
                filterNotNull.forEach {
                    if (it != null && it !in list) {
                        list += it
                    }
                }
            } else {
                list.addAll(filterNotNull.filterNotNull())
            }

//            if (size <= cnt) {
//                filterNotNull.forEach {
//                    if (!tooMany && format !in charList) {
//                        charList.add(format)
//                        getTextView(format, f)?.also {
//                            //flowLayout.addView(it)
//                        }
//                    }
//                }
            updateFlowlayout(list)
        }
    }

    val arrowHeight by lazy { resources.getDimension(R.dimen.arrow_height) }
    val previewHeight by lazy { resources.getDimension(R.dimen.height_word_preview).toInt() }
    val llRstPara by lazy { llRst.layoutParams as RelativeLayout.LayoutParams }
    val lvParams by lazy { lv.layoutParams as ViewGroup.MarginLayoutParams }

    private val marginPreview = 5//20

    private fun updateFlowlayout(words: List<String>) {
        val tm = SystemClock.elapsedRealtime()
        val lvMarginPadding = getLvMarginPadding()
        val maxFlowWidth = ivDisplay.realImgShowWidth - lvMarginPadding
        //        if (minWidth < maxFlowWidth - offset) {
        //            para.width = minWidth + offset
        //        } else {
        //            para.width = maxFlowWidth - offset
        //        }
        val pain = getTextView().paint!!

        val flowTextMarginPadding = getFlowTextMarginPadding()
        val dataTry = getFlowlayoutContent(pain, words, maxFlowWidth, flowTextMarginPadding)
        val fullWidth = dataTry.size > 3
        llRstPara.width = if (fullWidth) frameLayout.width else {
            getProperFlowlayoutWidth(pain, words, frameLayout.width, flowTextMarginPadding) + lvMarginPadding
        }

        val data = if (fullWidth) dataTry else getFlowlayoutContent(pain, words, llRstPara.width - lvMarginPadding, flowTextMarginPadding)

        val lvParams = lv.layoutParams
        lvParams.width = llRstPara.width

        val maxAllowLlRstHeight = frameLayout.height - previewHeight - marginPreview
        val maxLvHeight = minOf(maxAllowLlRstHeight, data.size * getFlowTextHeight())

        var bShowArrow = true

        if (maxLvHeight == maxAllowLlRstHeight) {
            //para.height = ivDisplay.height - 250
            llRstPara.height = maxAllowLlRstHeight
            lvParams.height = llRstPara.height
            fullOcr = true
            bShowArrow = false
        } else {
            llRstPara.height = maxLvHeight + arrowHeight.toInt()//ViewGroup.LayoutParams.WRAP_CONTENT
            if (llRstPara.height >= maxAllowLlRstHeight) {
                llRstPara.height = maxAllowLlRstHeight
                bShowArrow = false
            }
            lvParams.height = maxLvHeight
        }

        if (!fullOcr) {
            val arrow: SharpView
            if (ivDisplay.selectedRect.top + frameLayout.y < llRstPara.height) {//提示框在选择内容下面
                arrowUp.visibility = View.VISIBLE
                arrowDown.visibility = View.GONE
                arrow = arrowUp
                llRst.y = frameLayout.y + ivDisplay.selectedRect.bottom
            } else {//提示框在选择内容上面
                arrowUp.visibility = View.GONE
                arrowDown.visibility = View.VISIBLE
                arrow = arrowDown
                llRst.y = frameLayout.y + ivDisplay.selectedRect.top - llRstPara.height
            }
            arrow.arrowWidth = arrowHeight//arrow.height.toFloat()
            arrow.arrowHeight = arrowHeight
            //arrow.invalidate()
            val ptx = frameLayout.x + ivDisplay.selectedRect.centerX()
            val pty = frameLayout.y + ivDisplay.selectedRect.top
            val half = llRstPara.width / 2

            llRst.x = ptx - half//默认中间对中间
            arrow.offset = half.toFloat()
            val diffLeft = frameLayout.x - llRst.x
            val diffRight = llRst.x + llRstPara.width - (frameLayout.x + frameLayout.width)
            if (diffLeft > 0) {
                llRst.x += diffLeft
                arrow.offset -= diffLeft
            } else if (diffRight > 0) {
                llRst.x -= diffRight
                arrow.offset += diffRight
            }

            val diff = llRst.y + llRstPara.height - (frameLayout.y + frameLayout.height - previewHeight)
            if (diff > 0) {//挡住了预览界面
                llRst.y -= diff
            }
            val rectYRange = (llRst.y + 1)..(llRst.y + llRstPara.height - 1)
            if ((pty + ivDisplay.selectedRect.height() - 2) in rectYRange || pty in rectYRange) {
                arrow.visibility = View.GONE
            }

            arrow.postDelayed({
                Log.i(TAG, "ptx:$ptx, llRstPara.width:${llRstPara.width} llRstPara.height:${llRstPara.height} " +
                        "flowLayout.width:${lv.width} flowLayout.height:${maxLvHeight} llRst.xy:${llRst.x},${llRst.y}" +
                        " ivDisplay.selectedRect:${ivDisplay.selectedRect}")
            }, 50)

        } else {
            arrowUp.visibility = View.GONE
            arrowDown.visibility = View.GONE
            llRst.x = frameLayout.x //+ (ivDisplay.width - llRstPara.width.toFloat()) / 2
            llRst.y = frameLayout.y //+ 20
        }


        lvAdapter.updateData(data)
        showSegmentWords(lvAdapter.count > 0)

        Log.d(TAG, "updateFlowlayout: waste time:${SystemClock.elapsedRealtime() - tm}")
    }

    private fun getLvMarginPadding() = lvParams.leftMargin + lvParams.rightMargin + lv.paddingLeft + lv.paddingRight

    private fun getFlowTextMarginPadding(): Int {
        return (2 * (resources.getDimension(R.dimen.margin_hori_haici_item) +
                resources.getDimension(R.dimen.padding_hori_haici_item))).toInt()
    }

    private fun getFlowTextHeight(): Int {
        return (2 * (resources.getDimension(R.dimen.margin_vert_haici_item) +
                resources.getDimension(R.dimen.padding_vert_haici_item)
                ) + resources.getDimension(R.dimen.height_haici_item)).toInt()
    }

    private fun tryShowAgainDlg() {
        mAgain.show()
    }


    private fun tryCloseExitDlg() {
        mAgain.cancel()
        mMsgDlg.cancel()
    }

    private fun printLog(translateRst: String): HaiciBean {
        val haiCiReturn: HaiciBean
        val gson = Gson()
        haiCiReturn = gson.fromJson(translateRst, HaiciBean::class.java)
        val beanSrc = gson.toJson(haiCiReturn)
        val src = translateRst.replace("\\}".toRegex(), "}\n")
        val bean = beanSrc.replace("\\}".toRegex(), "}\n")
        Log.i(TAG, "getHaiciBean: translateRst\n$src\n\n\n\n\n\n  haiCiBean:\n$bean")
        return haiCiReturn
    }

    private fun stopScan() {
        //mScan.setVisibility(View.INVISIBLE);
        //tvScan.setVisibility(View.INVISIBLE);
        mScan.animate().alpha(0f).setDuration(500).start()
        //tvScan.animate().alpha(0f).setDuration(400).start()
        mScanDlg.cancel()
        mAni.cancel()
    }

    private fun startScan() {
        mScan.setAlpha(0f)
        mScan.translationZ = 2f
        mScan.setVisibility(View.VISIBLE)
        mScan.animate().alpha(0.9f).setDuration(800).start()
        val size = mDisplay.updateImgDisplaySize()
        mScan.layoutParams.width = size.width.toInt() - 20
        tvScan.alpha = 0f
        tvScan.visibility = View.VISIBLE
        tvScan.translationZ = 2f
        //tvScan.animate().alpha(0.9f).setDuration(200).start()
        mScanDlg.show()
        val height = (mScan.parent as View).height
        val offset = 20f + (mDisplay.height - size.height) / 2
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

    fun getTextView(): TextView {
        var tv = getNewTextView()
        val f: (TextView) -> Unit = {
            val tag = it.tag as? Pair<Int, Int>
            if (tag != null) {
                lvAdapter.select = tag
                lvAdapter.selectedView = it
            }
            it.background = getDrawableSelected()
            it.setTextColor(Color.WHITE)
            val str = it.text.toString()
            val data = mMapReqData[str]//?:updateHaiciData(str)
            var ret = false
            if (data != null) {
                ret = mSimplePreview.showUpdatePreview(data)
            } else if (mReqError.contains(str)) {
                mMsgDlg.show("没有查找到“${it.text}”相关信息！", TIME_SHOW)
            } else {
                mMsgDlg.show("正在查询，请稍等...", time = 5000)
                thread {
                    val retData = updateHaiciData(str)

                    ivDisplay.postDelayed(
                            {
                                if (lvAdapter.selectedView?.text == it.text) {
                                    if (retData != null) {
                                        ret = mSimplePreview.showUpdatePreview(retData)
                                        mMsgDlg.cancel()
                                    }
                                    if (!ret || retData == null) {
                                        mMsgDlg.show("没有查找到“${it.text}”相关信息！", time = 2000)
                                    }
                                }
                            }, 1000L)
                }
            }
            //if (!ret) {
            //    mMsgDlg.show("没有查找到“${it.text}”相关信息！")
            //    //Toast.makeText(this@MainActivity, "没有查找到“${it.text}”相关信息！",
            //    //        Toast.LENGTH_SHORT).show()
            //}
        }
        tv.setOnClickListener {
            try {
                f(it as TextView)
            } catch(e: Exception) {
                Log.e(TAG, "getTextView setOnClickListener:$e")
            }
        }

        return tv
    }

    private fun getNewTextView(): TextView {
        val first1 = LayoutInflater.from(this).inflate(R.layout.haici_item, null).findViewById(R.id.tv) as TextView
        (first1.parent as? ViewGroup)?.removeView(first1)
        return first1
    }

    fun format(str: String) = str.replace(symbolDataRegex, "") // abcd35

    fun isAlphbet(c: Char) = c in 'a'..'z' || c in 'A'..'Z'

    private fun updateHaiciData(str: String): HaiciBean? {
        return mMapReqData[str] ?: { getHaiciRequest(str);mMapReqData[str] }()
    }

    private val mMapReqData: MutableMap<String, HaiciBean> = HashMap()
    private val mReqError: MutableSet<String> = HashSet()
    private val mMapReq: MutableMap<String, String> = HashMap()

    private fun getHaiciRequest(str: String): String? {//一般花费500~800ms
        val tm = SystemClock.elapsedRealtime()
        val s = mMapReq[str] ?: WebUtils.doPost(str)
                ?.also {
                    mMapReq.put(str, it)
                    val bean = getHaiciBean(it)
                    bean?.let { mMapReqData.put(str, it) }
                    if (bean == null && !it.isNullOrBlank()) {
                        mReqError.add(str)
                    }
                }
        Log.d(TAG, "getHaiciRequest waste time ${SystemClock.elapsedRealtime() - tm} str:$str")
        return s
    }

    fun getHaiciBean(translateRst: String): HaiciBean? {
        if (translateRst.length < 70 && translateRst.contains("\"http_status_code\":201")) {
            return null
        }
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

    private fun getDrawableSelected(): Drawable? {
        return resources.getDrawable(R.drawable.handle_pressed)
    }

    private fun getDrawableNormal(): Drawable? {
        return resources.getDrawable(R.drawable.handle_normal)
    }

    override fun onDestroy() {
        super.onDestroy()
        tryCloseExitDlg()
        clearExtraFiles()
        if (BuildConfig.DEBUG) {
            val tmp = ArrayList<Pair<String, String?>>()
            mReqError.forEach {
                tmp.add(it to mMapReq[it])
                Unit
            }
            print("mReqError:$tmp")
        }
    }

    private fun clearExtraFiles() {
        if (mDeletePic && !mSelectManual) {
            var ret: Boolean = false
            val parentFile = File(mSelectedPath).parentFile
            try {
                if (parentFile.name == "Pai") {//删除整个文件夹
                    deleteDirFile(parentFile.absolutePath, mSelectedPath)//deleteImage(mSelectedPath)
                    ret = true
                } else {//删除单个文件
                    ret = deleteImage(mSelectedPath)
                }
            } catch(e: Exception) {
                Log.e(TAG, "clearExtraFiles:$e")
            }
            Log.d(TAG, "delete result:$ret pic path $mSelectedPath")
        }
    }

    fun isOcrScanning() = mAni.isRunning

    override fun onBackPressed() {
        if (isOcrScanning()) {
            btnRet.performClick()
        } else if (!isConnectedToNet()) {
            super.onBackPressed()
        } else {
            tryShowAgainDlg()
        }
    }

    fun showReturnBtn(show: Boolean) {
        if (show) {
            ll2btn.visibility = View.INVISIBLE
            btnRet.visibility = View.VISIBLE
        } else {
            ll2btn.visibility = View.VISIBLE
            btnRet.visibility = View.INVISIBLE
        }
    }
}

abstract class OcrThread() : Thread("OcrThread") {
    var isCancel = false
    abstract override fun run()
}
