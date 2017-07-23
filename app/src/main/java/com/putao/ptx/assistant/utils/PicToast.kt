package com.putao.ptx.assistant.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.putao.ptx.assistant.R

/**
 *
 *
 * <br></br>Description  :
 * <br></br>
 * <br></br>Author       : Victor<liuhe556></liuhe556>@126.com>
 * <br></br>
 * <br></br>Created date : 2017-05-04
 */
object PicToast {
    private var toast: Toast? = null
    private var view: View? = null
    private var mAnimate: ViewPropertyAnimator? = null
    var isShowing: Boolean = false
        private set

    /**
     * 显示
     */
    fun toastShow(context: Context, resId: Int, str: String): Toast {
        toastCancel()
        view = LayoutInflater.from(context).inflate(R.layout.pic_toast, null)
        val text = view!!.findViewById(R.id.textToast) as TextView
        val img = view!!.findViewById(R.id.toastImg) as ImageView
        img.setImageResource(resId)
        text.text = str // 设置显示文字
        var local = Toast(context)
        local.setGravity(Gravity.CENTER, 0, 0) // Toast显示的位置
        local.duration = Toast.LENGTH_SHORT // Toast显示的时间
        local.view = view
        local.show()
        isShowing = true
        text.postDelayed({
            toastCancel()
            isShowing = false
        }, 2000)
        toast = local
        return local
    }


    fun showToastNoNet(ctx: Context): Toast {
        val tmp = toastShow(ctx, R.mipmap.net_error, "网络错误！")
        toast = tmp
        return tmp
    }


    fun showToastNoWord(ctx: Context, msg: String): Toast {
        val tmp = toastShow(ctx, R.mipmap.no_word, if (TextUtils.isEmpty(msg)) "无对应翻译！" else msg)
        toast = tmp
        return tmp
    }

    fun toastOcrShow(context: Context, resId: Int, str: String) {
        toastShow(context, resId, str)
        view!!.findViewById(R.id.textToast).setBackgroundResource(R.drawable.shape_pic_toast_white)
        val img = view!!.findViewById(R.id.toastImg)
        mAnimate = img.animate()
        mAnimate!!.rotation(360f).setDuration(500)
                .setInterpolator(AccelerateInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        img.rotation = 0f
                        mAnimate!!.start()
                    }
                })
        mAnimate!!.start()
    }

    fun toastCancel() {
        mAnimate?.cancel()
        mAnimate = null
        toast?.cancel()
        toast = null
    }
}
