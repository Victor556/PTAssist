package com.putao.ptx.assistant.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.putao.ptx.assistant.R
import com.putao.ptx.assistant.haicientity.HaiciBean
import com.putao.ptx.assistant.haicientity.ISimplePreview

/**
 * <p>
 * <br/>Description  :
 * <br/>
 * <br/>Author       : Victor<liuhe556@126.com>
 * <br/>
 * <br/>Created date : 2017-04-27</p> */
class ViewSimplePreview constructor(
        val rlRoot: RelativeLayout, tranz: Float = 0f,
        val funPara: (RelativeLayout.LayoutParams?) -> RelativeLayout.LayoutParams) {

    val TAG = "ViewSimplePreview "

    val layoutPara: RelativeLayout.LayoutParams = funPara(null)
    val mViewSimplePreview: View = LayoutInflater.from(rlRoot.context).inflate(R.layout.word_preview, null)
    val tvGoToDetail: TextView = mViewSimplePreview.findViewById(R.id.tvGoToDetail) as TextView
    val tvWordTitle: TextView = mViewSimplePreview.findViewById(R.id.tvWord) as TextView
    val tvExplain: TextView = mViewSimplePreview.findViewById(R.id.tvExplain) as TextView
    val tvClosePreview: View = mViewSimplePreview.findViewById(R.id.vClose)

    var tranz: Float = 0f
        set(value) {
            field = value
            mViewSimplePreview?.translationZ = value
        }

    init {
        initPreviewView()
        this.tranz = tranz
    }


    private fun initPreviewView() {
        mViewSimplePreview.translationZ = tranz
        rlRoot.addView(mViewSimplePreview, /*rl*/layoutPara)
        mViewSimplePreview.translationZ = 2f
        tvGoToDetail.setOnClickListener {
            if (!mViewSimplePreview.context.isConnectedToNet()) {
                MsgDialog(mViewSimplePreview.context).showToastNoNet()
                return@setOnClickListener
            }
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
            try {
                (rlRoot.context as Activity).startActivity(intent)
            } catch(e: Exception) {
                Log.d(TAG, "initPreviewView $e")
            }
        }
        tvClosePreview.setOnClickListener { closePreview() }
        mViewSimplePreview.invalidate()
        //closePreview()
        mViewSimplePreview.visibility = View.INVISIBLE
        Log.d(TAG, "mViewSimplePreview width:${mViewSimplePreview.width}" +
                " height:${mViewSimplePreview.height} visibility:${mViewSimplePreview.visibility}")

    }

    fun showPreview() {
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


    fun showPreviewInstant() {
        mViewSimplePreview.animate().cancel()
        mViewSimplePreview.alpha = 1f
        mViewSimplePreview.visibility = View.VISIBLE
        funPara(layoutPara)
    }

    fun closePreview() {
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

    fun closePreviewInstant() {
        mViewSimplePreview.animate().cancel()
        mViewSimplePreview.alpha = 0f
        mViewSimplePreview.visibility = View.INVISIBLE
    }


    @JvmOverloads
    fun showUpdatePreview(data: HaiciBean, showNotice: Boolean = false): Boolean {
        var m = data
        if (m == null) {
            Log.d(TAG, "showUpdatePreview  mMapReqData[text]=${data}")
            closePreview()
            return false
        } else {
            val ci = m.data?.ci
            val zi = m.data?.zi
            val ec = m.data?.ec
            val ce = m.data?.ce

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
        return true
    }
}