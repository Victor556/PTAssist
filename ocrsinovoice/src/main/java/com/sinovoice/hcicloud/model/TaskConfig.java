package com.sinovoice.hcicloud.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TaskConfig {
	/**
	 * 任务参数信息 必选，为name=value形式，多个参数以逗号隔开
	 */
	private Map<String, String> taskConfig;
	
	public TaskConfig() {
		taskConfig = new HashMap<String, String>();
	}
	
	public void Set(String key, String value) {
		taskConfig.put(key, value);
	}
	
	public String toString() {
		if (taskConfig == null || taskConfig.isEmpty()) {
			return null;
		}

		String x_task_config = "";
		Iterator<Entry<String, String>> iter = taskConfig.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> e = iter.next();
			String key = e.getKey();
			String value = e.getValue();
			x_task_config += key + "=" + value + ",";
		}
		x_task_config = x_task_config.substring(0, x_task_config.length() - 1);
		return x_task_config;
	}
}
