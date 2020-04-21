/**
 * 
 */
package com.pedroalmir.ssnetwork.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Pedro Almir
 *
 */
public class AppEngineUtils {
	
	/**
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getStorageConfig() throws IOException{
		LinkedHashMap<String, String> config = new LinkedHashMap<>();
		Properties configFile = new Properties();
		configFile.load(AppEngineUtils.class.getClassLoader().getResourceAsStream("META-INF/keys.cfg"));
		config.put("projectId", configFile.getProperty("PROJECT_ID").trim());
		config.put("bucketName", configFile.getProperty("BUCKET_NAME").trim());
		return config;
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	public static String getBucketBaseURL() throws IOException {
		Properties configFile = new Properties();
		configFile.load(AppEngineUtils.class.getClassLoader().getResourceAsStream("META-INF/keys.cfg"));
		return configFile.getProperty("BUCKET_BASE_URL").trim();
	}
}
