package com.putao.ptx.assistant.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.TextView
import com.putao.ptx.assistant.views.FlowLayout

/**
 * <p>
 * <br/>Description  :
 * <br/>
 * <br/>Author       : Victor<liuhe556@126.com>
 * <br/>
 * <br/>Created date : 2017-05-12</p>
 */
class AdapterFlowView(val context: Context,
                      val funcGetTextView: () -> TextView,
                      val funSelect: (v: TextView, select: Boolean) -> Unit,
                      val funFlowPara: () -> Pair<Int, Int>,
                      val funcShowItems: (List<String>?) -> Unit) : BaseAdapter() {
    val TAG = "AdapterFlowView"

    private var mData = ArrayList<List<String>>()
    var select = -1 to -1//pos to index
        set(value) {
            if (selectedView != null) {
                funSelect(selectedView!!, selectedView?.tag == value)
            }
            Log.d(TAG, "set select $value")
            field = value
        }

    var selectedView: TextView? = null


    fun clearData() {
        mData.clear()
        select = -1 to -1
        selectedView = null
        notifyDataSetChanged()
    }

    fun updateData(data: ArrayList<List<String>>) {
        select = -1 to -1
        selectedView = null
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
        Log.d(TAG, "updateData  Select:$select")
    }

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View {
        var tmp = ((convertView as? ViewGroup) ?: (FlowLayout(context, null)).also {
            val widthHeight = funFlowPara()
            if (it.layoutParams == null) {
                it.layoutParams = AbsListView.LayoutParams(widthHeight.first, widthHeight.second)
            } else {
                it.layoutParams.width = widthHeight.first
                it.layoutParams.height = widthHeight.second
            }
        })
        var data = mData[pos]
        if (data.size > tmp.childCount) {
            while (data.size > tmp.childCount) {
                tmp.addView(funcGetTextView())
            }
        } else if (data.size < tmp.childCount) {
            tmp.removeViews(data.size, tmp.childCount - data.size)
        }

        data.forEachIndexed { index, s ->
            val textView = tmp.getChildAt(index) as TextView
            textView.text = s
            val pair = pos to index
            textView.tag = pair
            val b = (pair.first == select.first && pair.second == select.second)
            if (b) {
                selectedView = textView
            }
            funSelect(textView, b)
        }
        funcShowItems(data)
        return tmp
    }

    override fun getItem(position: Int) = mData[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = mData.size
}