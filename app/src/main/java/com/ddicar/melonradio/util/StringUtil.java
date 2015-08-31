package com.ddicar.melonradio.util;

import java.text.DecimalFormat;

public class StringUtil {

	public static boolean isNullOrEmpty(String text) {
		return text == null || text.equals("");
	}

	public static String filterNull(Double value) {
		if (value == null) {
			return "0";
		}

		DecimalFormat df = new DecimalFormat("#.##");

		return df.format(value);
	}

	public static String formatPhone(String phone) {
		
		if (phone.length() >= 11) {
		
			String prefix = phone.substring(0, 3);
			String suffix = phone.substring(7, 11);
			
			return prefix + "****" + suffix;
		}else  {
			return phone;
		}
	}

}
