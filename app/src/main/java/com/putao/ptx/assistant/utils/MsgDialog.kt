package com.putao.ptx.assistant.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.putao.ptx.assistant.R

val TIME_SHOW = 3_000_000L

class MsgDialog(context: Context,
                var text: String = "网络错误！",
                var picId: Int = R.mipmap.no_word,
                var showTime: Long = TIME_SHOW,
                val mCancelListener: DialogInterface.OnCancelListener? = null,
                width: Int = context.resources.getDimension(R.dimen.toast_width).toInt(),
                height: Int = context.resources.getDimension(R.dimen.toast_height).toInt(),
                var layout: View = LayoutInflater.from(context).inflate(R.layout.pic_toast, null),
                style: Int = R.style.pano_dialog) : Dialog(context, style) {

    init {
        setContentView(layout)
        val window = window
        window!!.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        val params = window.attributes
        params.gravity = Gravity.CENTER
        params.width = width
        params.height = height
        window.attributes = params
        if (mCancelListener != null) {
            setOnCancelListener(mCancelListener)
        }
    }

    val function = Runnable { cancel() }

    fun show(text: String = this.text, time: Long = TIME_SHOW, pic: Int = R.mipmap.no_word, cancelListener: DialogInterface.OnCancelListener? = null) {
        super.show()
        this.picId = pic
        this.text = text
        this.showTime = time
        (layout.findViewById(R.id.toastImg) as ImageView).setImageResource(picId)
        (layout.findViewById(R.id.textToast) as TextView).text = text
        layout.removeCallbacks(function)
        layout.postDelayed(function, showTime)
        if (cancelListener != null) {
            setOnCancelListener(cancelListener)
        } else if (mCancelListener != null) {
            setOnCancelListener(mCancelListener)
        }
    }

    override fun show() {
        this.show(pic = this.picId)
    }

    fun showToastNoNet() = show(pic = R.mipmap.net_error, text = "网络异常！", time = 2000L)
    fun showToastNoTranslate() = show(pic = R.mipmap.no_word, text = "无对应翻译！", time = 1500L)
    fun showToastNoWord() = show(pic = R.mipmap.no_word, text = "没有识别到文字！", time = 1500L)

    override fun cancel() {
        super.cancel()
        layout.removeCallbacks(function)
    }
}