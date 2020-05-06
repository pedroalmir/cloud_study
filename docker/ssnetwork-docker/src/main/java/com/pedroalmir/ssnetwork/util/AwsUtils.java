/**
 * 
 */
package com.pedroalmir.ssnetwork.util;

import java.io.IOException;
import java.util.Properties;

import com.amazonaws.auth.BasicAWSCredentials;

/**
 * @author Pedro Almir
 *
 */
public class AwsUtils {
	/**
	 * @return
	 * @throws IOException
	 */
	public static BasicAWSCredentials getCredentials() throws IOException {
		Properties configFile = new Properties();
		configFile.load(AwsUtils.class.getClassLoader().getResourceAsStream("META-INF/keys.cfg"));
		
		String ACCESS_KEY_ID = configFile.getProperty("ACCESS_KEY_ID");
		String SECRET_ACCESS_KEY = configFile.getProperty("SECRET_ACCESS_KEY");
		
		return new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
	}
	
}
