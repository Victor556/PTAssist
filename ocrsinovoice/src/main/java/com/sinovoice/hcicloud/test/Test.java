/**
 * Copyright © 2016捷通. All rights reserved.
 *
 * @Title: Test.java
 * @Prject: ocr_http
 * @Package: com.sinovoice.hcicloud.test
 * @date: 2016年10月25日 下午3:09:42
 * @version: V1.0
 */
package com.sinovoice.hcicloud.test;

import com.sinovoice.hcicloud.analyze.Ocr;

import java.io.File;

/**
 * Copyright © 2016捷通. All rights reserved.
 *
 * @Title: Test.java
 * @ClassName: Test
 * @Description: 接口测试类
 * @date: 2016年10月25日 下午3:09:42
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("===> ocr test  <===");
        Ocr oz = new Ocr();
//        Scanner scanner = new Scanner(System.in);
//        do {
//            System.out.println("输入具体的能力去体验，或者\"quit\"退出");
//            System.out.println(" cn_text, en_text, bizcard, bankcard, idcard0, idcard1, vat, dlcard, vlcard, pid, pcn, bl");
//            System.out.println(" quit ");
//            String line = scanner.nextLine();
//            line.trim();
//            line.toLowerCase();
//            if (line.equals("quit")) {
//                break;
//            }
        String line = "en_text";
        switch (line) {
            case "cn_text":
                String str = "C:\\Users\\admin\\Desktop\\temp\\jieping2.jpg";
                File f = new File(str);
                if (f.exists()) {
                    System.out.println(str);
                }
                oz.recogChineseText(/*"中文文本识别图片路径+文件名"*/str);
                break;
            case "en_text":
                oz.recogEnglishText("英文文本识别图片路径+文件名");
                break;
            case "bizcard":
                oz.recogBizcard("名片识别图片路径+文件名");
                break;
            case "bankcard":
                oz.recogBankcard("银行卡识别图片路径+文件名");
                break;
            case "idcard0":
                oz.recogIDCardP("身份证正面识别图片路径+文件名");
                break;
            case "idcard1":
                oz.recogIDCardG("身份证反面识别图片路径+文件名");
                break;
            case "vat":
                oz.recogVAT("票据识别图片路径+文件名");
                break;
            case "dlcard":
                oz.recogDLCard("驾驶证识别图片路径+文件名");
                break;
            case "vlcard":
                oz.recogVLCard("行驶证识别图片路径+文件名");
                break;
            case "bl":
                oz.recogBl("营业执照识别图片路径+文件名");
                break;
            case "pcn":
                oz.recogPcn("中国护照识别图片路径+文件名");
                break;
            case "pid":
                oz.recogPid("旅行证件识别图片路径+文件名");
                break;
            default:
                System.out.println(" quit ");
        }
//
//    } while(true);
//
//        scanner.close();
        System.out.println("===> end test  <===");
    }
}
