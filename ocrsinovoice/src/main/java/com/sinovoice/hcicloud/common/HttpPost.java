/**  
 * Copyright © 2016捷通. All rights reserved.
 * @Title: HttpPost.java
 * @Prject: jtts_http
 * @Package: com.sinovoice.hcicloud.common
 * @date: 2016年10月25日 下午1:54:59
 * @version: V1.0  
 */
package com.sinovoice.hcicloud.common;

import android.util.Log;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.time.StopWatch;

//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
//import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.RequestEntity;
//import org.apache.commons.httpclient.params.HttpMethodParams;
//import org.apache.commons.lang3.time.StopWatch;

/**
 * Copyright © 2016捷通. All rights reserved.
 * 
 * @Title: HttpPost.java
 * @ClassName: HttpPost
 * @date: 2016年8月9日 下午1:54:59
 */
@SuppressWarnings("deprecation")
public class HttpPost {
	public static MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();

	static {
		multiThreadedHttpConnectionManager.setMaxTotalConnections(400);
		multiThreadedHttpConnectionManager.setMaxConnectionsPerHost(200);
		multiThreadedHttpConnectionManager.getParams().setConnectionTimeout(10000);
	}

	private static final String TAG = "HttpPost";

	public byte[] execute(HttpHead httpHead, byte[] body) throws Exception {

		HttpClient client = new HttpClient();

		PostMethod method = new PostMethod(httpHead.getUrl());
		method.setRequestHeader("x-app-key",     httpHead.getAppkey());
		method.setRequestHeader("x-sdk-version", httpHead.getSdkVersion());
		method.setRequestHeader("x-request-date",httpHead.getRequestDate());
		method.setRequestHeader("x-task-config", httpHead.getTaskConfig());
		method.setRequestHeader("x-session-key", httpHead.getSessionKey());
		method.setRequestHeader("Connection",    "close");
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);

		try {
			RequestEntity requestEntity = new ByteArrayRequestEntity(body, "UTF-8");
			method.setRequestEntity(requestEntity);

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			long tm = System.currentTimeMillis();
			int stateCode = client.executeMethod(method);
			long tmInterval = System.currentTimeMillis()-tm;
			System.out.println("  waste time:"+tmInterval);
			stopWatch.stop();
			if (stateCode == HttpStatus.SC_OK) {
				byte[] body1 = method.getResponseBody();
				System.out.println("  ok ");
				return body1;
			}
			else {
				System.out.println("  err!  stateCode:"+stateCode);
				//throw new Exception();
				byte[] body1 = method.getResponseBody();

				Log.d(TAG, "execute: ResponseBodyAsString:"+method.getResponseBodyAsString());
				return body1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception:  "+e);
			throw e;
		} 
	}
}
