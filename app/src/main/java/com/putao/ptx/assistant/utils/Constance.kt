package com.putao.ptx.assistant.utils

import android.os.Environment
import java.io.File

/**
 * <p>
 * <br/>Description  :
 * <br/>
 * <br/>Author       : Victor<liuhe556@126.com>
 * <br/>
 * <br/>Created date : 2017-04-07</p>
 */

object Constance {
    @JvmField
    val KEY_IMG = "SRC"
    @JvmField
    val KEY_SELECT = "SELECT"
    /**
     * 截屏小图片的保存路径
     */
    @JvmField
    val OCR_PATH = "/sdcard/ocr.jpg"
    @JvmField val KEY_DETAIL = "key_detai"
    @JvmField val KEY_DETAIL_REQ = "key_detai_req"

    val IS_MANUAL = "is_manual"
}

@JvmField val BUGLY_ID = "0a982d4858"
@JvmField val XunFeiAPPID = "56f39a2e"//正式 56f39a2e 测试 56f0e757 jack:5721c970
@JvmField val APP_NAME = "PIAssist"
@JvmField val EXTERNAL_DIR = Environment.getExternalStorageDirectory().absolutePath + File.separator + APP_NAME
@JvmField val CRASH_LOG_DIR = EXTERNAL_DIR + File.separator + "log"
