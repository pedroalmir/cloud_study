/**
 * 
 */
package com.pedroalmir.ssnetwork.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.amazonaws.auth.BasicAWSCredentials;

/**
 * @author Pedro Almir
 *
 */
public class AppEngineUtils {
	/**
	 * @return
	 * @throws IOException
	 */
	public static BasicAWSCredentials getCredentials() throws IOException {
		Properties configFile = new Properties();
		configFile.load(AppEngineUtils.class.getClassLoader().getResourceAsStream("META-INF/keys.cfg"));
		
		String ACCESS_KEY_ID = configFile.getProperty("ACCESS_KEY_ID");
		String SECRET_ACCESS_KEY = configFile.getProperty("SECRET_ACCESS_KEY");
		
		return new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getStorageConfig() throws IOException{
		LinkedHashMap<String, String> config = new LinkedHashMap<>();
		Properties configFile = new Properties();
		configFile.load(AppEngineUtils.class.getClassLoader().getResourceAsStream("META-INF/keys.cfg"));
		config.put("projectId", configFile.getProperty("PROJECT_ID"));
		config.put("bucketName", configFile.getProperty("BUCKET_NAME"));
		return config;
	}
}
