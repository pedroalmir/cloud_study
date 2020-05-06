/**
 * 
 */
package com.pedroalmir.ssnetwork.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author Pedro Almir
 */
public class DateUtil {
	/**
	 * @return new date in São Paulo
	 */
	public static Date newDateInSaoPaulo() {
		TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
		TimeZone.setDefault(tz);
		Calendar ca = GregorianCalendar.getInstance(tz);
		return ca.getTime();
	}
	
	/**
	 * @return
	 */
	public static SimpleDateFormat getDefaultFormatter() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
		return formatter;
	}
}
