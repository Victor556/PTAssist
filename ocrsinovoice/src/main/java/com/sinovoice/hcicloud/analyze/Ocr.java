/**
 * Copyright © 2017捷通. All rights reserved.
 *
 * @Title: ocr.java
 * @Prject: jtts_http
 * @Package: com.sinovoice.hcicloud.analyze
 * @date: 2016年8月9日 下午1:27:23
 * @version: V1.0
 */

package com.sinovoice.hcicloud.analyze;

import android.util.Log;

import com.google.gson.Gson;
import com.sinovoice.hcicloud.common.HttpHead;
import com.sinovoice.hcicloud.common.HttpPost;
import com.sinovoice.hcicloud.common.MD5;
import com.sinovoice.hcicloud.model.OcrResult;
import com.sinovoice.hcicloud.model.TaskConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ocr {

    private int indexOf(byte[] source, byte[] tag) {
        for (int i = 0; i < (source.length - tag.length) + 1; ++i) {
            int j = 0;
            for (j = 0; j < tag.length; ++j) {
                if (source[i + j] != tag[j]) {
                    break;
                }
            }
            if (j == tag.length) {
                return i;
            }
        }

        return -1;
    }

    private byte[] readFromFile(String filename) {
        try {
            File file = new File(filename);
            FileInputStream fin = new FileInputStream(file);
            byte[] buff = new byte[(int) file.length()];
            int read = fin.read(buff);
            if (read < file.length()) {
            }
            fin.close();
            return buff;
        } catch (IOException ex) {
        }
        return null;
    }

    static final String app_key = "b05d54d3";//"开发者appkey"; // 填写图像云注册的开发者appkey
    static final String developer_Key = "2b5b6497aa2ffc1e0c2388dddd6f4195";//"开发者developer_Key"; // 填写图像云开发者分配的developer_Key
    static final String urladd = "ocr.aicloud.com:8541";//"serverURL:port"; // 填写网站api说明页面中提供的请求地址

    protected OcrResult recognise(String cu, String file, String config) {

        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format_date = formater.format(now);
        String session_key = MD5.getMD5Code(format_date + developer_Key);

        HttpHead httpHead = new HttpHead();
        httpHead.setUrl("http://" + urladd + cu);
        httpHead.setAppkey(app_key);
        httpHead.setSessionKey(session_key);
        httpHead.setRequestDate(format_date);
        httpHead.setSdkVersion("5.2");
        httpHead.setTaskConfig(config);

        String log = "";
        try {
            HttpPost httpPost = new HttpPost();

            // 读取二进制数据，包体内容发送出去
            byte[] body = readFromFile(file);
            // 响应包内容
            byte[] context = httpPost.execute(httpHead, body);
            String tagStr = "</ResponseInfo>";
            byte[] tag = tagStr.getBytes();
            int taglen = tagStr.length();
            // 未考虑失败
            String jsonResponse = new String(context, "UTF-8");
            Log.d(TAG, "recognise: UTF-8:" + jsonResponse);
            log = jsonResponse;
            Gson gson = new Gson();
            OcrResult rst = gson.fromJson(jsonResponse, OcrResult.class);
            return rst;
        } catch (Exception e) {
            System.out.println(e.toString() + "err log:" + log);
            return null;
        }//
    }

    private static final String TAG = "Ocr";

    /**
     * 中文文本识别
     *
     * @param file
     */
    public OcrResult recogChineseText(String file) {
        TaskConfig task_config = new TaskConfig();
        task_config.Set("capkey", "ocr.cloud");
        task_config.Set("lang", "chinese_cn");
        return recognise("/ocr", file, task_config.toString());
    }

    /**
     * 英文文本识别
     *
     * @param file
     */
    public OcrResult recogEnglishText(String file) {
        TaskConfig task_config = new TaskConfig();
        task_config.Set("capkey", "ocr.cloud");
        task_config.Set("lang", "english");
        return recognise("/ocr", file, task_config.toString());
    }

    /**
     * 银行卡识别
     *
     * @param file
     */
    public void recogBankcard(String file) {
        TaskConfig task_config = new TaskConfig();
        task_config.Set("capkey", "ocr.cloud.bankcard");
        recognise("/ocr", file, task_config.toString());
    }

    /**
     * 名片识别
     *
     * @param file
     */
    public void recogBizcard(String file) {
        TaskConfig task_config = new TaskConfig();
        task_config.Set("capkey", "ocr.cloud.bizcard");
        recognise("/ocr", file, task_config.toString());
    }

    /**
     * 驾驶证识别
     *
     * @param file
     */
    public void recogDLCard(String file) {
        TaskConfig task_config = new TaskConfig();
        task_config.Set("capkey", "ocr.cloud.template");
        task_config.Set("templateIndex", "0");
        task_config.Set("templatePageIndex", "0");
        task_config.Set("property", "dlcard");
        recognise("/ocr", file, task_config.toString());
        return;
    }

    /**
     * 行驶证识别
     *
     * @param file
     */
    public void recogVLCard(String file) {
        TaskConfig task_config = new TaskConfig();
        task_config.Set("capkey", "ocr.cloud.template");
        task_config.Set("templateIndex", "0");
        task_config.Set("templatePageIndex", "0");
        task_config.Set("property", "vlcard");
        recognise("/ocr", file, task_config.toString());
        return;
    }

    /**
     * 身份证头像面识别
     *
     * @param file
     */
    public void recogIDCardP(String file) {
        TaskConfig task_config = new TaskConfig();
        task_config.Set("capkey", "ocr.cloud.template");
        task_config.Set("templateIndex", "0");
        task_config.Set("templatePageIndex", "0");
        task_config.Set("exportImage", "yes");
        task_config.Set("property", "idcard");
        recognise("/ocr", file, task_config.toString());
        return;
    }

    /**
     * 身份证签证机关面识别
     *
     * @param file
     */
    public void recogIDCardG(String file) {
        TaskConfig task_config = new TaskConfig();
        task_config.Set("capkey", "ocr.cloud.template");
        task_config.Set("templateIndex", "0");
        task_config.Set("templatePageIndex", "0");
        task_config.Set("property", "idcard");
        recognise("/ocr", file, task_config.toString());
        return;
    }

    /**
     * 票据识别
     *
     * @param file
     */
    public void recogVAT(String file) {
        TaskConfig task_config = new TaskConfig();
        task_config.Set("capkey", "ocr.cloud.template");
        task_config.Set("templateIndex", "0");
        task_config.Set("templatePageIndex", "0");
        task_config.Set("property", "vat");
        recognise("/ocr", file, task_config.toString());
        return;
    }

    /**
     * 旅行证件识别
     *
     * @param file
     */
    public void recogPid(String file) {
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.Set("capkey", "ocr.cloud.template");
        taskConfig.Set("templateIndex", "0");
        taskConfig.Set("templatePageIndex", "0");
        taskConfig.Set("property", "pid");
        recognise("/ocr", file, taskConfig.toString());
        return;
    }

    /**
     * 中国护照识别
     *
     * @param file
     */
    public void recogPcn(String file) {
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.Set("capkey", "ocr.cloud.template");
        taskConfig.Set("templateIndex", "0");
        taskConfig.Set("templatePageIndex", "0");
        taskConfig.Set("property", "pcn");
        recognise("/ocr", file, taskConfig.toString());
        return;
    }

    /**
     * 营业执照识别
     *
     * @param file
     */
    public void recogBl(String file) {
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.Set("capkey", "ocr.cloud.template");
        taskConfig.Set("templateIndex", "0");
        taskConfig.Set("templatePageIndex", "0");
        taskConfig.Set("property", "bl");
        recognise("/ocr", file, taskConfig.toString());
        return;
    }
}
