/**  
 * Copyright © 2016捷通. All rights reserved.
 * @Title: HttpHead.java
 * @Prject: jtts_http
 * @Package: com.sinovoice.hcicloud.common
 * @date: 2016年8月9日 上午11:48:02
 * @version: V1.0  
 */
package com.sinovoice.hcicloud.common;

import java.io.Serializable;

/**
 * Copyright © 2016捷通. All rights reserved.
 * @Title: HttpHead.java
 * @ClassName: HttpHead
 * @Description: Http报文头部
 * @date: 2016年10月25日 上午11:48:02
 */
public class HttpHead implements Serializable{
	private static final long serialVersionUID = -8583967184164520907L;
	/**
	 * 请求url
	 */
	private String url;
	/**
	 * 应用标识
	 */
	private String appkey;
	/**
	 * sdk版本号
	 */
	private String sdkVersion;
	/**
	 * 请求时间,2016-8-09 10:10:11
	 */
	private String requestDate;
	/**
	 * 任务参数信息
	 * 必选，为name=value形式，多个参数以逗号隔开
	 */
	private String taskConfig;
	/**
	 * 必选
	 *  x-session-key生成算法说明：
	 *  x-session-key = md5(x-request-date + devkey)
	 *  devkey是应用开发者密钥，在注册开发者用户时由开发者网站统一分配，用作请求数据签名；devkey在私有云能力平台中目前固定为developer_key
	 */
	private String sessionKey;
	/**
	 * 设备标识
	 * 如使用设备取设备标识号，如不使用设备设置为例子中的默认值
	 */
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getSdkVersion() {
		return sdkVersion;
	}
	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getTaskConfig() {
		return taskConfig;
	}
	public void setTaskConfig(String taskConfig) {
		this.taskConfig = taskConfig;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	
}
